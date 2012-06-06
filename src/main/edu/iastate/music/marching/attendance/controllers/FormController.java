package edu.iastate.music.marching.attendance.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

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

		// Store
		dataTrain.getDataStore().store(form);

		// Update grade, it may have changed
		dataTrain.getUsersController().update(form.getStudent());

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

		// Check date is before cutoff but after today
		if (!calendar.after(Calendar.getInstance())) {
			exp.getErrors().add("Invalid date, must be after today.");
		}

		if (!calendar.before(dataTrain.getAppDataController().get()
				.getFormSubmissionCutoff())) {
			exp.getErrors().add(
					"Invalid date, must before form submission cutoff.");
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}

		return formACHelper(student, date, reason, Form.Type.A);
	}

	public Form createFormB(User student, String department, String course,
			String section, String building, Date startDate, Date endDate,
			int day, Date startTime, Date endTime, String details) {
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
		
		//TODO: This was a quick fix just to get the startTime and endTime to actually do something
		
		startDate.setHours(startTime.getHours());
		startDate.setMinutes(startTime.getMinutes());
		
		endDate.setHours(endTime.getHours());
		endDate.setMinutes(endTime.getMinutes());
		
		form.setStart(startDate);
		form.setEnd(endDate);
		// TODO form.set(All the other things)
		if (ValidationUtil.isValidText(department, false))
		{
			form.setDept(department);
		}
		else
		{
			exp.getErrors().add("Invalid department.");
		}
		
		if (ValidationUtil.isValidText(course, false))
		{
			form.setCourse(course);
		}
		else
		{
			exp.getErrors().add("Invalid department.");
		}
		
		if (ValidationUtil.isValidText(section, false))
		{
			form.setSection(section);
		}
		else
		{
			exp.getErrors().add("Invalid department.");
		}
		
		if (ValidationUtil.isValidText(building, false))
		{
			form.setBuilding(building);
		}
		else
		{
			exp.getErrors().add("Invalid department.");
		}
		form.setDay(day);
		// Set remaining fields
		form.setDetails(details);
		form.setStatus(Form.Status.Pending);

		// Perform store
		storeForm(form);

		return form;
	}

	// TODO perform form c-specific validation
	public Form createFormC(User student, Date date, String reason) {

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

		return formACHelper(student, date, reason, Form.Type.C);
	}

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

		if (!ValidationUtil.isValidFormDEmail(email)) {
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

		// Perform store
		storeForm(form);

		return form;
	}

	/**
	 * Creates a form of type A or C because they have identical creation with
	 * separate time-based validation.
	 * 
	 * @param student
	 * @param date
	 * @param reason
	 * @param type
	 *            The Form.Type A or C
	 * @author curtisu
	 * @return
	 */
	private Form formACHelper(User student, Date date, String reason,
			Form.Type type) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

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

		Form form = ModelFactory.newForm(type, student);

		form.setStart(startDate);
		form.setEnd(endDate);

		// Set remaining fields
		form.setDetails(reason);
		form.setStatus(Form.Status.Pending);

		// Perform store
		storeForm(form);

		return form;
	}

	public Form get(long id) {
		ObjectDatastore od = this.dataTrain.getDataStore();
		Form form = od.load(Form.class, id);
		return form;
	}

	public boolean removeForm(Long id) {
		ObjectDatastore od = this.dataTrain.getDataStore();
		Form form = od.load(Form.class, id);
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

}
