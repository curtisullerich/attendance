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

	public List<Form> get(User user) {

		RootFindCommand<Form> find = this.dataTrain.getDataStore().find()
				.type(Form.class);

		// Set the ancestor for this form
		find = find.ancestor(user);

		find.addFilter(Form.FIELD_STUDENT, FilterOperator.EQUAL, user);

		return find.returnAll().now();
	}

	public Form createFormA(User student, Date date, String reason) {
		Calendar calendar = Calendar.getInstance(App.getTimeZone());
		calendar.setTime(date);

		// Simple validation first
		ValidationExceptions exp = new ValidationExceptions();

		if (!ValidationUtil.isValidText(reason, true)) {
			exp.getErrors().add("Invalid reason");
		}

		// Check date is before cutoff but after today
		if (!calendar.after(Calendar.getInstance(App.getTimeZone())))
			exp.getErrors().add("Invalid date, must be after today.");
		if (!calendar.before(dataTrain.getAppDataController().get()
				.getFormSubmissionCutoff()))
			exp.getErrors().add(
					"Invalid date, must before form submission cutoff.");

		if (exp.getErrors().size() > 0)
			throw exp;

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

		// Pass on remaining creation work
		Form form = createForm(Form.Type.A, student, startDate, endDate);

		// Set remaining fields
		form.setDetails(reason);

		// Perform store
		storeForm(form);

		return form;
	}

	private void storeForm(Form form) {
		// Perform store of this new form and update of student's grade in a
		// transaction to prevent inconsistent states
		Track transaction = dataTrain.switchTracksInternal();

		try {
			// Store
			dataTrain.getDataStore().store(form);

			// Update grade, it may have changed
			dataTrain.getUsersController().update(form.getStudent());

			// Commit
			transaction.bendIronBack();

		} catch (RuntimeException ex) {
			transaction.derail();
			throw ex;
		}
	}

	/**
	 * Does not store form, just creates and validates
	 * 
	 * @param type
	 * @param student
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	private Form createForm(Form.Type type, User student, Date startDate,
			Date endDate) {

		// if(date.before(Calendar.getInstance(App.getTimeZone())))
		// {
		// exp.getErrors().add("Date given was before today, no back-dating allowed");
		// }
		return null;
		// TODO
	}

	public Form createFormC(User student, Date date, String reason) {
		// TODO Auto-generated method stub
		return null;
	}

	public Form createFormD(User student, Date date, int hours, String details) {
		// TODO Auto-generated method stub
		return null;
	}

}
