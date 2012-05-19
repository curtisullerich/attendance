package edu.iastate.music.marching.attendance.controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.controllers.DataTrain.Track;
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
	 * @param user Assume user is associated in data store
	 * @return
	 */
	public List<Form> get(User user) {
		ObjectDatastore od = this.dataTrain.getDataStore();
		RootFindCommand<Form> find = od.find().type(Form.class);

		// Set the ancestor for this form, automatically limits results to be
		// forms of the user
		find = find.ancestor(user);

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
		MessageThread messages = ModelFactory.newMessageThread();
		dataTrain.getDataStore().store(messages);
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

//	/**
//	 * Does not store form, just creates and validates
//	 * 
//	 * @param type
//	 * @param student
//	 * @param startDate
//	 * @param endDate
//	 * @return
//	 */
//	private Form createForm(Form.Type type, User student, Date startDate,
//			Date endDate) {
//
//		if (type == null)
//			throw new IllegalArgumentException("Null type given for form");
//
//		if (student == null)
//			throw new IllegalArgumentException("Null student given for form");
//
//		if (startDate == null)
//			throw new IllegalArgumentException("Null startDate given for form");
//
//		if (endDate == null)
//			throw new IllegalArgumentException("Null endDate given for form");
//
//		// TODO more validation of start/end dates
//		Form form = ModelFactory.newForm(type, student);
//
//		form.setStart(startDate);
//		form.setEnd(endDate);
//
//
//		return form;
//	}

	//TODO perform form a-specific validation
	public Form createFormA(User student, Date date, String reason) {
		Calendar calendar = Calendar.getInstance(App.getTimeZone());
		calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(reason, true)) {
			exp.getErrors().add("Invalid reason");
		}

		// Check date is before cutoff but after today
		if (!calendar.after(Calendar.getInstance(App.getTimeZone()))) {
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

		return formACHelper(student,date,reason,Form.Type.A);
	}

	public Form createFormB(User student, String department, String course, String section, String building, Date startDate, Date endDate, Enum day, Date startTime, Date endTime, String details) {
		// TODO NEEDS MORE PARAMETERS and LOTS OF VALIDATION
		Calendar calendar = Calendar.getInstance(App.getTimeZone());
//		calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(details, true)) {
			exp.getErrors().add("Invalid details");
		}

		//TODO MORE VALIDATION
		if (exp.getErrors().size() > 0) {
			throw exp;
		}
		
		Form form = ModelFactory.newForm(Form.Type.B, student);

		form.setStart(startDate);
		form.setEnd(endDate);
		//TODO form.set(All the other things)
		
		// Set remaining fields
		form.setDetails(details);

		// Perform store
		storeForm(form);

		return form;	
	}
	
	//TODO perform form c-specific validation
	public Form createFormC(User student, Date date, String reason) {
		Calendar calendar = Calendar.getInstance(App.getTimeZone());
		calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(reason, true)) {
			exp.getErrors().add("Invalid reason");
		}

		// Check date is before cutoff but after today
		if (!dateIsAtLeastThreeWeekdaysFresh(date)) {
			exp.getErrors().add("Invalid date, submitted too late");
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}
		
		return formACHelper(student,date,reason,Form.Type.C);
	}

	/**
	 * Validation check for form C submission date
	 * @param date
	 * @return
	 */
	private boolean dateIsAtLeastThreeWeekdaysFresh(Date date) {
		Calendar today = Calendar.getInstance(App.getTimeZone());
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE,0);
		today.set(Calendar.SECOND,0);
		today.set(Calendar.MILLISECOND,0);

		if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			today.roll(Calendar.DATE, 2);
		} else if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			today.roll(Calendar.DATE,1);
		}
		//now if it was a weekend, it's Monday for the comparison
		
		Calendar other = Calendar.getInstance(App.getTimeZone());
		other.setTime(date);
		other.set(Calendar.HOUR_OF_DAY, 0);
		other.set(Calendar.MINUTE,0);
		other.set(Calendar.SECOND,0);
		other.set(Calendar.MILLISECOND,0);
				
		other.roll(Calendar.DATE, 3);

		if (other.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			other.roll(Calendar.DATE, 2);
		} else if (other.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			other.roll(Calendar.DATE,1);
		}
		//now if it was a weekend, it's Monday for the comparison
		
		//now we should just be comparing weekend-agnostic DAYS.
		return !other.after(today);
	}
	
	
	public Form createFormD(User student, String email, Date date, int hours, String details) {
		Calendar calendar = Calendar.getInstance(App.getTimeZone());
		calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(details, true)) {
			exp.getErrors().add("Invalid details");
		}
		
		if (hours <= 0) {
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

		// Perform store
		storeForm(form);

		return form;
	}
	
	/**
	 * Creates a form of type A or C because they have identical creation
	 * with separate time-based validation.
	 * 
	 * @param student
	 * @param date
	 * @param reason
	 * @param type The Form.Type A or C
	 * @author curtisu
	 * @return
	 */
	private Form formACHelper(User student, Date date, String reason, Form.Type type) {
		Calendar calendar = Calendar.getInstance(App.getTimeZone());
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

		// Perform store
		storeForm(form);

		return form;
	}

}
