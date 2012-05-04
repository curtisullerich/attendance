package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import edu.iastate.music.marching.attendance.model.User.Type;

public class ModelFactory {

	public static Event newEvent(Event.Type type, Date start, Date end) {
		Event e = new Event();
		e.setType(type);
		e.setStart(start);
		e.setEnd(end);
		return e;
	}

	public static User newUser(Type type, String netID, int univID) {
		User u = new User();
		u.setType(type);
		u.setNetID(netID);
		u.setUniversityID(univID);
		return u;
	}
}
