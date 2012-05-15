package edu.iastate.music.marching.attendance.controllers;

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
		
		find.addFilter(Form.FIELD_STUDENT, FilterOperator.EQUAL, user);

		return find.returnAll().now();
	}

}
