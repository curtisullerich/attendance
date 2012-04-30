package edu.iastate.music.marching.attendance.model;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class DataModel {

	private static final UserController usersInstance = new UserController();

	/** Never need to create an instance of this, only call static methods */
	private DataModel() {
	}

	public static UserController users() {
		return usersInstance;
	}
	
	public static UserController users(PersistenceManager pm) {
		return new UserController(pm);
	}
	
	public static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}
	
	static final class PMF {
	    private static final PersistenceManagerFactory pmfInstance =
	        JDOHelper.getPersistenceManagerFactory("transactions-optional");

	    private PMF() {}

	    public static PersistenceManagerFactory get() {
	        return pmfInstance;
	    }
	}

}
