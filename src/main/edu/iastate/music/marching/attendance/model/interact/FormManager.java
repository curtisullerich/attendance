package edu.iastate.music.marching.attendance.model.interact;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.App.WeekDay;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class FormManager extends AbstractManager {

	private DataTrain train;

	public FormManager(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public void approve(Form form) {
		form.setStatus(Form.Status.Approved);
		this.update(form);
	}

	public Form createClassConflictForm(User student, String department,
			String course, String section, String building,
			Interval interval, LocalTime startTime,
			LocalTime endTime, WeekDay dayOfWeek, String details,
			int minutesToOrFrom, Absence.Type absenceType) {

		// TODO https://github.com/curtisullerich/attendance/issues/108
		// NEEDS MORE PARAMETERS and LOTS OF VALIDATION
		// do we need to do more date/time validation than just
		// before/after? I don't think it's wise to exclude date ranges outside
		// of this year, just because, but is there anything else to consider?

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (endTime.isBefore(startTime)) {
			exp.getErrors().add("End time was before start time.");
		}

		if (!ValidationUtil.isValidText(details, true)) {
			exp.getErrors().add("Invalid details.");
		}

		Form form = ModelFactory.newForm(Form.Type.ClassConflict, student);

		if (endTime.isBefore(startTime)) {
			exp.getErrors().add(
					"End DateTime time was before start DateTime time.");
		}

		form.setInterval(interval);
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
		form.setSubmissionTime(DateTime.now());
		form.setLate(form.getSubmissionDateTime().isAfter(
				DataTrain.depart().appData().get()
						.getPerformanceAbsenceFormCutoff()));

		form.setDayOfWeek(dayOfWeek);
		form.setDetails(details);
		form.setStatus(Form.Status.Pending);
		form.setMinutesToOrFrom(minutesToOrFrom);

		storeForm(form);

		return form;
	}

	public Form createPerformanceAbsenceForm(User student, LocalDate date,
			String reason) {

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(reason, true)) {
			exp.getErrors().add("Invalid reason");
		}

		DateTime cutoff = train.appData().get()
				.getPerformanceAbsenceFormCutoff();

		boolean late = cutoff.isBeforeNow();

		if (exp.getErrors().size() > 0) {
			throw exp;
		}

		Form form = ModelFactory.newForm(Form.Type.PerformanceAbsence, student);

		form.setInterval(date.toInterval(this.train.appData().get()
				.getTimeZone()));

		// current
		form.setSubmissionTime(DateTime.now());
		form.setLate(form.getSubmissionDateTime().isAfter(
				DataTrain.depart().appData().get()
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
	public Form createTimeWorkedForm(User student, LocalDate date, int minutes,
			String details) {
		DateTimeZone zone = this.train.appData().get().getTimeZone();

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

		Form form = ModelFactory.newForm(Form.Type.TimeWorked, student);

		form.setInterval(date.toInterval(zone));

		// current
		form.setSubmissionTime(DateTime.now());
		form.setLate(form.getSubmissionDateTime().isAfter(
				DataTrain.depart().appData().get()
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

	void delete(User user) {
		List<Form> forms = this.get(user);
		this.train.getDataStore().deleteAll(forms);
	}

	public Form get(long id) {
		ObjectDatastore od = this.train.getDataStore();
		Form form = od.load(this.train.getTie(Form.class, id));
		return form;
	}

	/**
	 * @param user
	 *            Assume user is associated in data store
	 * @return
	 */
	public List<Form> get(User user) {
		ObjectDatastore od = this.train.getDataStore();
		RootFindCommand<Form> find = od.find().type(Form.class);

		// Set the ancestor for this form, automatically limits results to be
		// forms of the user
		find = find.addFilter(Form.FIELD_STUDENT, FilterOperator.EQUAL, user);
		return find.returnAll().now();
	}

	public List<Form> getAll() {
		return this.train.getDataStore().find().type(Form.class).returnAll()
				.now();
	}

	public boolean removeForm(Long id) {
		ObjectDatastore od = this.train.getDataStore();
		Form form = od.load(this.train.getTie(Form.class, id));
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

	private void storeForm(Form form) {
		// TODO: https://github.com/curtisullerich/attendance/issues/114
		// Perform store of this new form and upDateTime of student's grade in a
		// transaction to prevent inconsistent states
		// Track transaction = dataTrain.switchTracksInternal();
		//
		// try {

		updateFormD(form);

		// Store
		train.getDataStore().store(form);

		// UpDateTime grade, it may have changed
		train.users().update(form.getStudent());

		// TODO https://github.com/curtisullerich/attendance/issues/106
		// what if the student is null?
		AbsenceManager ac = this.train.absences();
		for (Absence absence : ac.get(form.getStudent())) {
			// TODO https://github.com/curtisullerich/attendance/issues/106
			// I wrote a (private) method in Absence controller that could
			// do this more efficiently because it checks for a specific form
			// and specific absence. We /could/ expose it as protected, but
			// that may introduce bugs elsewhere because we're not forced to
			// call updateAbsence in order to perform checks and thus may forget
			// to upDateTime it or perform all necessary validation. This
			// applies to
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

	public void update(Form f) {
		updateFormD(f);
		this.train.getDataStore().update(f);

		AbsenceManager ac = this.train.absences();

		// TODO https://github.com/curtisullerich/attendance/issues/106
		// what if the student is null?
		for (Absence absence : ac.get(f.getStudent())) {
			ac.updateAbsence(absence);
		}

	}

	private void updateFormD(Form f) {
		User student = f.getStudent();
		if (f.getType() == Form.Type.TimeWorked && student != null) {
			if (!f.isApplied()) {
				if (f.getStatus() == Form.Status.Approved) {
					student.setMinutesAvailable(student.getMinutesAvailable()
							+ f.getMinutesWorked());
					this.train.users().update(student);
					f.setApplied(true);
				}
			}
		}
	}
}
