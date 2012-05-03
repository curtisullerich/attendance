package edu.iastate.music.marching.attendance.controllers;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;


public class DataTrain {
	
	private ObjectDatastore datastore;

	public static DataTrain getAndStartTrain()
	{
		return new DataTrain();
	}

	/** Never need to create an instance of this, only call static methods */
	private DataTrain() {
		datastore = new AnnotationObjectDatastore(false);
	}
	
	public AbsenceController getAbscencesController() {
		return new AbsenceController(this);
	}
	
	public EventController getEventsController() {
		return new EventController(this);
	}
	
	public FormController getFormsController() {
		return new FormController(this);
	}
	
	public UserController getUsersController() {
		return new UserController(this);
	}

	ObjectDatastore getDataStore() {
		// TODO Auto-generated method stub
		return null;
	}

	public MobileDataController getMobileDataController() {
		return new MobileDataController(this);
	}

}





