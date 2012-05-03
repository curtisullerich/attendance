package edu.iastate.music.marching.attendance.controllers;

import com.google.code.twig.ObjectDatastore;


public class DataTrain {

	public static DataTrain getAndStartTrain()
	{
		return null;
	}
	
	public void endTrain()
	{
		
	}

	/** Never need to create an instance of this, only call static methods */
	private DataTrain() {
		
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

}
