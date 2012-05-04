package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import edu.iastate.music.marching.attendance.model.User.Type;

public class ModelFactory {

	public static User getUser() {
		return new User();
	}


	public static Event newEvent(Event.Type type, Date start, Date end) {
		return new Event(type, start, end);
	}

	public static User newUser(Type type, String netID, int univID) {
		// TODO Auto-generated method stub
		return null;
	}
}
