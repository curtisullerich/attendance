package edu.iastate.music.marching.attendance.model;

import javax.jdo.PersistenceManager;

public abstract class AbstractController {

	private PersistenceManager mPersistenceManager = DataModel
			.getPersistenceManager();

	protected AbstractController() {
		mPersistenceManager = null;
	}

	protected AbstractController(PersistenceManager pm) {
		mPersistenceManager = pm;
	}

	protected PersistenceManager getPersistenceManager() {
		return (mPersistenceManager != null) ? mPersistenceManager : DataModel
				.getPersistenceManager();
	}

	protected void closePersistenceManager(PersistenceManager pm) {
		if (mPersistenceManager != pm && pm != null)
			pm.close();
	}
}
