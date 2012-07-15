package edu.iastate.music.marching.attendance.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

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
import edu.iastate.music.marching.attendance.model.MessageThread;
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

		// TODO what if the student is null?
		for (Absence absence : ac.get(f.getStudent())) {
			// TODO I wrote a (private) method in Absence controller that could
			// do this more efficiently because it checks for a specific form
			// and specific absence. We /could/ expose it as protected, but
			// that may introduce bugs elsewhere because we're not forced to
			// call updateAbsence in order to perform checks and thus may forget
			// to update it or perform all necessary validation. This applies to
			// all forms.
			ac.updateAbsence(absence);
		}

	}

	public Form getByHashedId(long id) {
		return this.dataTrain.getDataStore().find().type(Form.class)
				.addFilter(Form.HASHED_ID, FilterOperator.EQUAL, id)
				.returnUnique().now();
	}

	private void storeForm(Form form) {
		// TODO: Reintroduce transactions

		// Perform store of this new form and update of student's grade in a
		// transaction to prevent inconsistent states
		// Track transaction = dataTrain.switchTracksInternal();
		//
		// try {
		// First build an empty message thread and store it
		MessageThread messages = dataTrain.getMessagingController()
				.createMessageThread(form.getStudent());
		form.setMessageThread(messages);

		updateFormD(form);

		// Store
		dataTrain.getDataStore().store(form);

		// Update grade, it may have changed
		dataTrain.getUsersController().update(form.getStudent());

		// TODO what if the student is null?
		AbsenceController ac = this.dataTrain.getAbsenceController();
		for (Absence absence : ac.get(form.getStudent())) {
			// TODO I wrote a (private) method in Absence controller that could
			// do this more efficiently because it checks for a specific form
			// and specific absence. We /could/ expose it as protected, but
			// that may introduce bugs elsewhere because we're not forced to
			// call updateAbsence in order to perform checks and thus may forget
			// to update it or perform all necessary validation. This applies to
			// all forms.
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
	// // TODO more validation of start/end dates
	// Form form = ModelFactory.newForm(type, student);
	//
	// form.setStart(startDate);
	// form.setEnd(endDate);
	//
	//
	// return form;
	// }

	// TODO perform form a-specific validation
	public Form createFormA(User student, Date date, String reason) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(reason, true)) {
			exp.getErrors().add("Invalid reason");
		}

		Calendar cutoff = Calendar.getInstance();
		cutoff.setTime(dataTrain.getAppDataController().get()
				.getFormSubmissionCutoff());

		// TODO is timezone correct?
		if (!Calendar.getInstance().before(cutoff)) {
			exp.getErrors().add(
					"Sorry, it's past the deadline for submitting Form A.");
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}

		Calendar temp = Calendar.getInstance();
		temp.setTime(date);

		// Parsed date starts at beginning of day
		temp.set(Calendar.HOUR_OF_DAY, 0);
		temp.set(Calendar.MINUTE, 0);
		temp.set(Calendar.SECOND, 0);
		temp.set(Calendar.MILLISECOND, 0);
		Date startDate = temp.getTime();

		// TODO app engine stores this as midnight on August 9th. This should
		// never cause an error, but should we fix it? Probably. Maybe the
		// resolution of time on app engine doesn't go to milliseconds?
		// End exactly one time unit before the next day starts
		temp.roll(Calendar.DATE, true);
		temp.roll(Calendar.MILLISECOND, false);
		Date endDate = temp.getTime();

		Form form = ModelFactory.newForm(Form.Type.A, student);

		form.setStart(startDate);
		form.setEnd(endDate);

		// Set remaining fields
		form.setDetails(reason);
		form.setStatus(Form.Status.Pending);

		// Perform store
		storeForm(form);

		return form;
		// return formACHelper(student, date, reason, Form.Type.A);
	}

	public Form createFormB(User student, String department, String course,
			String section, String building, Date startDate, Date endDate,
			int day, Date startTime, Date endTime, String details,
			int minutesToOrFrom) {
		// TODO NEEDS MORE PARAMETERS and LOTS OF VALIDATION
		Calendar calendar = Calendar.getInstance();
		// calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(details, true)) {
			exp.getErrors().add("Invalid details");
		}

		// TODO MORE VALIDATION
		if (exp.getErrors().size() > 0) {
			throw exp;
		}

		Form form = ModelFactory.newForm(Form.Type.B, student);

		// TODO: This was a quick fix just to get the startTime and endTime to
		// actually do something

		startDate.setHours(startTime.getHours());
		startDate.setMinutes(startTime.getMinutes());

		endDate.setHours(endTime.getHours());
		endDate.setMinutes(endTime.getMinutes());

		form.setStart(startDate);
		form.setEnd(endDate);
		// TODO form.set(All the other things)
		if (ValidationUtil.isValidText(department, false)) {
			form.setDept(department);
		} else {
			exp.getErrors().add("Invalid department.");
		}

		if (ValidationUtil.isValidText(course, false)) {
			form.setCourse(course);
		} else {
			exp.getErrors().add("Invalid department.");
		}

		if (ValidationUtil.isValidText(section, false)) {
			form.setSection(section);
		} else {
			exp.getErrors().add("Invalid department.");
		}

		if (ValidationUtil.isValidText(building, false)) {
			form.setBuilding(building);
		} else {
			exp.getErrors().add("Invalid department.");
		}
		form.setDay(day);
		// Set remaining fields
		form.setDetails(details);
		form.setStatus(Form.Status.Pending);
		form.setMinutesToOrFrom(minutesToOrFrom);
		// Perform store
		storeForm(form);

		return form;
	}

	// TODO perform form c-specific validation
	public Form createFormC(User student, Date date, Absence.Type type,
			String reason) {
		// TODO I think I might be breaking a paradigm by doing it this way (not
		// passing in explicit start and end dates)

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(reason, true)) {
			exp.getErrors().add("Invalid reason");
		}

		// Check date is before cutoff but after today
		if (!ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(date,
				this.dataTrain.getAppDataController().get().getTimeZone())) {
			exp.getErrors().add("Invalid date, submitted too late");
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}
		// ////////////////////////////////////////////////////////////////////////
		Calendar calendar = Calendar.getInstance();
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
			calendar.roll(Calendar.DATE, true);
			calendar.roll(Calendar.MILLISECOND, false);
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
			calendar.roll(Calendar.DATE, true);
			calendar.roll(Calendar.MILLISECOND, false);
			end = calendar.getTime();

		} else {

		}

		Form form = ModelFactory.newForm(Form.Type.C, student);

		// TODO what's the best way to indicate that something went wrong? Which
		// is what it means if either start or end are null at this point.
		form.setStart(start);
		form.setEnd(end);

		// Set remaining fields
		form.setDetails(reason);
		form.setStatus(Form.Status.Pending);

		// Perform store
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
	public Form createFormD(User student, String email, Date date, int minutes,
			String details) {
		Calendar calendar = Calendar.getInstance();
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
				.append("<a href=\"www.isucfvmb-attendance.appspot.com/public/verify?s=a&i="
						+ form.getHashedId() + "\">Click here to approve.</a>")
				.append("\r\n\r\n")
				.append("<br/><br/>")
				.append("<a href=\"www.isucfvmb-attendance.appspot.com/public/verify?s=d&i="
						+ form.getHashedId() + "\">Click here to deny.</a>")
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
			// you can't remove an approved form.
			// TODO you might be able to. check with staub
			return false;
		} else {
			User student = form.getStudent();
			MessageThread mt = form.getMessageThread();
			od.delete(mt); // the associated messages are embedded in the
							// MessageThread, so there's no need to delete them
							// separately
			od.delete(form);
			od.refresh(student);
			return true;
		}
	}

	public void approve(Form form) {
		// TODO Auto-generated method stub
		form.setStatus(Form.Status.Approved);
		this.update(form);
	}

}
