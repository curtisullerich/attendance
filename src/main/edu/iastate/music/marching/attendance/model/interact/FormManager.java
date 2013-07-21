package edu.iastate.music.marching.attendance.model.interact;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class FormManager extends AbstractManager {

	private DataTrain dataTrain;

	public FormManager(DataTrain dataTrain) {
		this.dataTrain = dataTrain;
	}

	public List<Form> getAll() {
		return this.dataTrain.getDataStore().find().type(Form.class)
				.returnAll().now();
	}

	/**
	 * 
	 * @param user
	 *            Assume user is associated in data store
	 * @return
	 */
	public List<Form> get(User user) {
		ObjectDatastore od = this.dataTrain.getDataStore();
		RootFindCommand<Form> find = od.find().type(Form.class);

		// Set the ancestor for this form, automatically limits results to be
		// forms of the user
		find = find.addFilter(Form.FIELD_STUDENT, FilterOperator.EQUAL, user);
		return find.returnAll().now();
	}

	private void updateFormD(Form f) {
		User student = f.getStudent();
		if (f.getType() == Form.Type.TimeWorked && student != null) {
			if (!f.isApplied()) {
				if (f.getStatus() == Form.Status.Approved) {
					student.setMinutesAvailable(student.getMinutesAvailable()
							+ f.getMinutesWorked());
					this.dataTrain.getUsersManager().update(student);
					f.setApplied(true);
				}
			}
		}
	}

	public void update(Form f) {
		updateFormD(f);
		this.dataTrain.getDataStore().update(f);

		AbsenceManager ac = this.dataTrain.getAbsenceManager();

		// TODO https://github.com/curtisullerich/attendance/issues/106
		// what if the student is null?
		for (Absence absence : ac.get(f.getStudent())) {
			ac.updateAbsence(absence);
		}

	}

	public Form getByHashedId(long id) {
		return this.dataTrain.getDataStore().find().type(Form.class)
				.addFilter(Form.HASHED_ID, FilterOperator.EQUAL, id)
				.returnUnique().now();
	}

	private void storeForm(Form form) {
		// TODO: https://github.com/curtisullerich/attendance/issues/114
		// Perform store of this new form and update of student's grade in a
		// transaction to prevent inconsistent states
		// Track transaction = dataTrain.switchTracksInternal();
		//
		// try {

		updateFormD(form);

		// Store
		dataTrain.getDataStore().store(form);

		// Update grade, it may have changed
		dataTrain.getUsersManager().update(form.getStudent());

		// TODO https://github.com/curtisullerich/attendance/issues/106
		// what if the student is null?
		AbsenceManager ac = this.dataTrain.getAbsenceManager();
		for (Absence absence : ac.get(form.getStudent())) {
			// TODO https://github.com/curtisullerich/attendance/issues/106
			// I wrote a (private) method in Absence controller that could
			// do this more efficiently because it checks for a specific form
			// and specific absence. We /could/ expose it as protected, but
			// that may introduce bugs elsewhere because we're not forced to
			// call updateAbsence in order to perform checks and thus may forget
			// to update it or perform all necessary validation. This applies to
			// all forms
			ac.updateAbsence(absence);
		}

		// Commit
		// transaction.bendIronBack();

		// } catch (RuntimeException ex) {
		// transaction.derail();
		// throw ex;
		// }
	}

	public Form createPerformanceAbsenceForm(User student, Date date,
			String reason) {

		TimeZone timezone = this.dataTrain.getAppDataManager().get()
				.getTimeZone();

		Calendar calendar = Calendar.getInstance(timezone);
		calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(reason, true)) {
			exp.getErrors().add("Invalid reason");
		}

		Calendar cutoff = Calendar.getInstance(timezone);
		cutoff.setTime(dataTrain.getAppDataManager().get()
				.getPerformanceAbsenceFormCutoff());

		boolean late = false;

		// TODO https://github.com/curtisullerich/attendance/issues/103
		// is timezone correct?
		if (!Calendar.getInstance(timezone).before(cutoff)) {
			late = true;
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}

		Calendar temp = Calendar.getInstance(timezone);
		temp.setTime(date);

		// Parsed date starts at beginning of day
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		Date startDate = temp.getTime();

		// TODO https://github.com/curtisullerich/attendance/issues/103
		// app engine stores this as midnight on August 9th. This should
		// never cause an error, but should we fix it? Probably. Maybe the
		// resolution of time on app engine doesn't go to milliseconds?
		// End exactly one time unit before the next day starts
		temp.add(Calendar.DATE, 1);
		temp.add(Calendar.MILLISECOND, -1);
		Date endDate = temp.getTime();

		Form form = ModelFactory.newForm(Form.Type.PerformanceAbsence, student);

		form.setStart(startDate);
		form.setEnd(endDate);

		// current
		form.setSubmissionTime(new Date());
		form.setLate(form.getSubmissionTime().after(
				DataTrain.getAndStartTrain().getAppDataManager().get()
						.getPerformanceAbsenceFormCutoff()));

		// Set remaining fields
		form.setDetails(reason);
		form.setStatus(Form.Status.Pending);

		if (late) {
			form.setStatus(Form.Status.Denied);
			// Perform store
			storeForm(form);
		} else {
			storeForm(form);
		}

		return form;
		// return formACHelper(student, date, reason, Form.Type.A);
	}

	public Form createClassConflictForm(User student, String department,
			String course, String section, String building, Date startDate,
			Date endDate, int day, Date startTime, Date endTime,
			String details, int minutesToOrFrom, Absence.Type absenceType) {

		TimeZone timezone = this.dataTrain.getAppDataManager().get()
				.getTimeZone();

		Calendar startDateTime = Calendar.getInstance(timezone);
		Calendar endDateTime = Calendar.getInstance(timezone);

		Calendar startTimeCalendar = Calendar.getInstance(timezone);
		Calendar endTimeCalendar = Calendar.getInstance(timezone);

		startTimeCalendar.setTime(startTime);
		endTimeCalendar.setTime(endTime);

		// TODO https://github.com/curtisullerich/attendance/issues/108
		// NEEDS MORE PARAMETERS and LOTS OF VALIDATION
		// do we need to do more date/time validation than just
		// before/after? I don't think it's wise to exclude date ranges outside
		// of this year, just because, but is there anything else to consider?

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (endTimeCalendar.before(startTimeCalendar)) {
			exp.getErrors().add("End time was before start time.");
		}

		if (!ValidationUtil.isValidText(details, true)) {
			exp.getErrors().add("Invalid details.");
		}

		if (day < 1 || day > 7) {
			exp.getErrors().add("Day with value of " + day + " is not valid.");
		}

		Form form = ModelFactory.newForm(Form.Type.ClassConflict, student);

		startDateTime.setTime(startDate);
		startDateTime.set(Calendar.HOUR_OF_DAY,
				startTimeCalendar.get(Calendar.HOUR_OF_DAY));
		startDateTime.set(Calendar.MINUTE,
				startTimeCalendar.get(Calendar.MINUTE));

		endDateTime.setTime(endDate);
		endDateTime.set(Calendar.HOUR_OF_DAY,
				endTimeCalendar.get(Calendar.HOUR_OF_DAY));
		endDateTime.set(Calendar.MINUTE, endTimeCalendar.get(Calendar.MINUTE));

		if (endDateTime.before(startDateTime)) {
			exp.getErrors().add("End date time was before start date time.");
		}

		form.setStart(startDateTime.getTime());
		form.setEnd(endDateTime.getTime());
		if (absenceType != null) {
			form.setAbsenceType(absenceType);
		} else {
			exp.getErrors().add("Absence type was null.");
		}

		if (ValidationUtil.isValidText(department, false)) {
			form.setDept(department);
		} else {
			exp.getErrors().add("Invalid department.");
		}

		if (ValidationUtil.isValidText(course, false)) {
			form.setCourse(course);
		} else {
			exp.getErrors().add("Invalid course.");
		}

		if (ValidationUtil.isValidText(section, false)) {
			form.setSection(section);
		} else {
			exp.getErrors().add("Invalid section.");
		}

		if (ValidationUtil.isValidText(building, false)) {
			form.setBuilding(building);
		} else {
			exp.getErrors().add("Invalid building.");
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}

		// current
		form.setSubmissionTime(new Date());
		form.setLate(form.getSubmissionTime().after(
				DataTrain.getAndStartTrain().getAppDataManager().get()
						.getPerformanceAbsenceFormCutoff()));

		form.setDay(day);
		form.setDetails(details);
		form.setStatus(Form.Status.Pending);
		form.setMinutesToOrFrom(minutesToOrFrom);

		storeForm(form);

		return form;
	}

	/**
	 * Note that FormD is never approved/verified upon creation, so we don't do
	 * auto-approval checks here.
	 * 
	 * @param student
	 * @param email
	 * @param date
	 * @param minutes
	 * @param details
	 * @return
	 */
	public Form createTimeWorkedForm(User student, Date date, int minutes,
			String details) {
		TimeZone timezone = this.dataTrain.getAppDataManager().get()
				.getTimeZone();

		Calendar calendar = Calendar.getInstance(timezone);
		calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(details, true)) {
			exp.getErrors().add("Invalid details");
		}

		if (minutes <= 0) {
			exp.getErrors().add("Time worked is not positive");
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}

		// Parsed date starts at beginning of day
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date startDate = calendar.getTime();

		// End exactly one time unit before the next day starts
		calendar.roll(Calendar.DATE, true);
		calendar.roll(Calendar.MILLISECOND, false);
		Date endDate = calendar.getTime();

		Form form = ModelFactory.newForm(Form.Type.TimeWorked, student);

		form.setStart(startDate);
		form.setEnd(endDate);

		// current
		form.setSubmissionTime(new Date());
		form.setLate(form.getSubmissionTime().after(
				DataTrain.getAndStartTrain().getAppDataManager().get()
						.getPerformanceAbsenceFormCutoff()));

		// Set remaining fields
		form.setDetails(details);
		form.setStatus(Form.Status.Pending);
		form.setMinutesWorked(minutes);
		// Perform store
		storeForm(form);
		// The id field isn't stored until the form is persisted so

		return form;
	}

	public Form get(long id) {
		ObjectDatastore od = this.dataTrain.getDataStore();
		Form form = od.load(this.dataTrain.getTie(Form.class, id));
		return form;
	}

	public boolean removeForm(Long id) {
		ObjectDatastore od = this.dataTrain.getDataStore();
		Form form = od.load(this.dataTrain.getTie(Form.class, id));
		if (form.getStatus() == Form.Status.Approved) {
			// Only Directors can remove forms
			return false;
		} else {
			User student = form.getStudent();
			od.delete(form);
			od.refresh(student);
			return true;
		}
	}

	public void approve(Form form) {
		form.setStatus(Form.Status.Approved);
		this.update(form);
	}

	void delete(User user) {
		List<Form> forms = this.get(user);
		this.dataTrain.getDataStore().deleteAll(forms);
	}
}
