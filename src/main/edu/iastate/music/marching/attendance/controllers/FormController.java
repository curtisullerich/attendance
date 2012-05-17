package edu.iastate.music.marching.attendance.controllers;

import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;

import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.User;

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
		return null;
		// TODO Auto-generated method stub
		
		
		// Check date is after now and before january 9 of the next year
//		calendar.after(Calendar.getInstance());
//		calendar.before(train.getAppDataController().get());
//		// TODO
//
//		// Parsed date starts at beginning of day
//		Date start = calendar.getTime();
//
//		// End exactly one time unit before the next day starts
//		calendar.roll(Calendar.DATE, true);
//		calendar.roll(Calendar.MILLISECOND, false);
//		Date end = calendar.getTime();
	}
	
	private Form createForm(Form.Type type, User student, Date startDate, Date endDate)
	{
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
