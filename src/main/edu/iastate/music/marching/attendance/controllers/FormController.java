package edu.iastate.music.marching.attendance.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class FormController extends AbstractController {

	private DataTrain dataTrain;

	public FormController(DataTrain dataTrain) {
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
		if (f.getType() == Form.Type.D && student != null) {
			if (!f.isApplied()) {
				if (f.getStatus() == Form.Status.Approved
						&& f.getEmailStatus() == Form.Status.Approved) {
					student.setMinutesAvailable(student.getMinutesAvailable()
							+ f.getMinutesWorked());
					this.dataTrain.getUsersController().update(student);
					f.setApplied(true);
				}
			}
		}
	}

	public void update(Form f) {
		updateFormD(f);
		this.dataTrain.getDataStore().update(f);

		AbsenceController ac = this.dataTrain.getAbsenceController();

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
		dataTrain.getUsersController().update(form.getStudent());

		// TODO https://github.com/curtisullerich/attendance/issues/106
		// what if the student is null?
		AbsenceController ac = this.dataTrain.getAbsenceController();
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

	// /**
	// * Does not store form, just creates and validates
	// *
	// * @param type
	// * @param student
	// * @param startDate
	// * @param endDate
	// * @return
	// */
	// private Form createForm(Form.Type type, User student, Date startDate,
	// Date endDate) {
	//
	// if (type == null)
	// throw new IllegalArgumentException("Null type given for form");
	//
	// if (student == null)
	// throw new IllegalArgumentException("Null student given for form");
	//
	// if (startDate == null)
	// throw new IllegalArgumentException("Null startDate given for form");
	//
	// if (endDate == null)
	// throw new IllegalArgumentException("Null endDate given for form");
	//
	// more validation of start/end dates
	// Form form = ModelFactory.newForm(type, student);
	//
	// form.setStart(startDate);
	// form.setEnd(endDate);
	//
	//
	// return form;
	// }

	public Form createFormA(User student, Date date, String reason) {

		TimeZone timezone = this.dataTrain.getAppDataController().get()
				.getTimeZone();

		Calendar calendar = Calendar.getInstance(timezone);
		calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(reason, true)) {
			exp.getErrors().add("Invalid reason");
		}

		Calendar cutoff = Calendar.getInstance(timezone);
		cutoff.setTime(dataTrain.getAppDataController().get()
				.getFormSubmissionCutoff());

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

		Form form = ModelFactory.newForm(Form.Type.A, student);

		form.setStart(startDate);
		form.setEnd(endDate);

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

	public Form createFormB(User student, String department, String course,
			String section, String building, Date startDate, Date endDate,
			int day, Date startTime, Date endTime, String details,
			int minutesToOrFrom, Absence.Type absenceType) {

		TimeZone timezone = this.dataTrain.getAppDataController().get()
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

		Form form = ModelFactory.newForm(Form.Type.B, student);

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

		form.setDay(day);
		form.setDetails(details);
		form.setStatus(Form.Status.Pending);
		form.setMinutesToOrFrom(minutesToOrFrom);

		storeForm(form);

		return form;
	}

	public Form createFormC(User student, Date date, Absence.Type type,
			String reason) {
		return createFormC(student, date, type, reason, true);
	}

	public Form createFormC(User student, Date date, Absence.Type type,
			String reason, boolean validateDate) {

		TimeZone timezone = this.dataTrain.getAppDataController().get()
				.getTimeZone();

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(reason, true)) {
			exp.getErrors().add("Invalid reason");
		}

		// Check date is before cutoff but after today
		boolean late = false;
		if (validateDate
				&& !ValidationUtil.canStillSubmitFormC(date, Calendar
						.getInstance(timezone).getTime(), timezone)) {
			late = true;
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}
		// ////////////////////////////////////////////////////////////////////////
		Calendar calendar = Calendar.getInstance(timezone);
		calendar.setTime(date);
		Date start = null;
		Date end = null;
		if (type == Absence.Type.Absence) {
			// start date is beginning of day
			// end date is end of day

			// Parsed date starts at beginning of day
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();

			// End exactly one time unit before the next day starts
			// TODO revert if roll was intended
			calendar.add(Calendar.DATE, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			end = calendar.getTime();

		} else if (type == Absence.Type.Tardy) {
			// start date is beginning of day
			// end date is given date

			// Parsed date starts at beginning of day
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			start = calendar.getTime();

			end = date;

		} else if (type == Absence.Type.EarlyCheckOut) {
			// start date is given date
			// end date is end of day

			start = date;

			// End exactly one time unit before the next day starts
			// TODO revert to roll() if that was intentional
			calendar.add(Calendar.DATE, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			end = calendar.getTime();

		} else {
			throw new IllegalArgumentException("Invalid Absence type");
		}

		Form form = ModelFactory.newForm(Form.Type.C, student);

		form.setStart(start);
		form.setEnd(end);

		// Set remaining fields
		form.setDetails(reason);
		form.setStatus(Form.Status.Pending);
		form.setAbsenceType(type);

		if (late) {
			form.setStatus(Form.Status.Denied);
			storeForm(form);
			exp.getErrors()
					.add("It's past the deadline for submitting this Form C. This form has been submitted, but marked as denied. If you believe you have a valid excuse for late submission, add a message to this form in order to bring it to the director's attention.");
		} else {
			// Perform store
			storeForm(form);
		}

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
	public Form createFormD(User student, String email, Date date, int minutes,
			String details) {
		TimeZone timezone = this.dataTrain.getAppDataController().get()
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

		if (!ValidationUtil.isValidFormDEmail(email, this.dataTrain
				.getAppDataController().get())) {
			exp.getErrors().add("Verification email is not valid");
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

		Form form = ModelFactory.newForm(Form.Type.D, student);

		form.setStart(startDate);
		form.setEnd(endDate);

		// Set remaining fields
		form.setDetails(details);
		form.setStatus(Form.Status.Pending);
		form.setEmailStatus(Form.Status.Pending);
		form.setEmailTo(email);
		form.setMinutesWorked(minutes);

		form.setHashedId(form.generateHashedId());
		sendEmail(form);
		// Perform store
		storeForm(form);
		// The id field isn't stored until the form is persisted so

		return form;
	}

	private boolean sendEmail(Form form) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		User student = form.getStudent();
		String msgBody = new StringBuilder()
				.append(student.getFirstName() + " " + student.getLastName())
				.append(" has requested that you approve their "
						+ form.getMinutesWorked() + " minutes of time worked ")
				.append("on "
						+ new SimpleDateFormat("MM/dd/yyyy").format(form
								.getStart()) + ".")
				.append("\r\n\r\n")
				.append("<br/><br/>")
				.append("The details provided in the message were: ")
				.append(form.getDetails())
				.append("\r\n\r\n")
				.append("<br/><br/>")
				.append("<a href='http://www.isucfvmb-attendance.appspot.com/public/verify?s=a&i="
						+ form.getHashedId() + "'>Click here to approve</a>.")
				.append("\r\n\r\n")
				.append("<br/><br/>")
				.append("<a href='http://www.isucfvmb-attendance.appspot.com/public/verify?s=d&i="
						+ form.getHashedId() + "'>Click here to deny</a>.")
				.append("\r\n\r\n")
				.append("<br/><br/>")
				.append("Thanks!")
				.append("\r\n\r\n")
				.append("<br/><br/>")
				.append("This email was automatically generated by the online attendance tracking system for the Iowa State University Cyclone Football 'Varsity' Marching Band. If this message was received in error or if there are any issues, please contact the developers at mbattendance@iastate.edu.")
				.toString();

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("mbattendance@gmail.com"));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(form.getEmailTo()));

			msg.setSubject(student.getFirstName() + " " + student.getLastName()
					+ " Requests Time Worked Approval");
			msg.setContent(msgBody, "text/html");

			Transport.send(msg);
			return true;
		} catch (AddressException e) {
			throw new IllegalArgumentException(
					"Internal Error: Could not send Email");
		} catch (MessagingException e) {
			throw new IllegalArgumentException(
					"Internal Error: Could not send Email");
		}

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
