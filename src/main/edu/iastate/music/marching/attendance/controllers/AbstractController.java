package edu.iastate.music.marching.attendance.controllers;

import javax.jdo.PersistenceManager;

import edu.iastate.music.marching.attendance.model.ModelFactory;


public abstract class AbstractController {

	private PersistenceManager mPersistenceManager = ModelFactory
			.getPersistenceManager();

	protected AbstractController() {
		mPersistenceManager = null;
	}

	protected AbstractController(PersistenceManager pm) {
		mPersistenceManager = pm;
	}

	public AbstractController(DataTrain dataTrain) {
		// TODO Auto-generated constructor stub
	}

	protected PersistenceManager getPersistenceManager() {
		return (mPersistenceManager != null) ? mPersistenceManager : ModelFactory
				.getPersistenceManager();
	}

	protected void closePersistenceManager(PersistenceManager pm) {
		if (mPersistenceManager != pm && pm != null)
			pm.close();
	}
}
