package edu.iastate.music.marching.attendance.test.util;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.TestConfig;

public class Users {

	public static final User createStudent(UserController uc,
			String email_firstpart, int univID, String firstName,
			String lastName, int year, String major, User.Section section) {
	
		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + TestConfig.getEmailDomain(),
				"gmail.com");
	
		return uc.createStudent(google_user, univID, firstName, lastName, year,
				major, section, new Email(""));
	}

	public static final User createTA(UserController uc,
			String email_firstpart, int univID, String firstName,
			String lastName, int year, String major, User.Section section) {
	
		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + TestConfig.getEmailDomain(),
				"gmail.com");
	
		User u = uc.createStudent(google_user, univID, firstName, lastName,
				year, major, section, new Email(""));
		u.setType(User.Type.TA);
		uc.update(u);
		return u;
	}

	public static final User createDirector(UserController uc,
			String email_firstpart, String firstName,
			String lastName) {
		return uc.createDirector(
				email_firstpart + "@" + TestConfig.getEmailDomain(),
				email_firstpart + ".secondemail" + "@"
						+ TestConfig.getEmailDomain(), firstName, lastName);
	}

}
