package edu.iastate.music.marching.attendance.controllers;

import javax.jdo.PersistenceManager;


public class Controllers {

	private static final UserController usersInstance = new UserController();

	/** Never need to create an instance of this, only call static methods */
	private Controllers() {
	}

	public static UserController users() {
		return usersInstance;
	}
	
	public static UserController users(PersistenceManager pm) {
		return new UserController(pm);
	}

}
