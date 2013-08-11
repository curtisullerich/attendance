package edu.iastate.music.marching.attendance.test.util;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.test.TestConfig;

public class Users {

	public static final User createDefaultStudent(UserManager uc) {
		return createStudent(uc, "defaultstudent", "123456789", "John", "Doe", 2,
				"major", User.Section.AltoSax);
	}
	
	public static final User createDefaultTA(UserManager uc) {
		return createTA(uc, "defaultta", "987654321", "Johnny", "Doe", 2,
				"major", User.Section.AltoSax);
	}

	public static final User createStudent(UserManager uc,
			String email_firstpart, String univID, String firstName,
			String lastName, int year, String major, User.Section section) {

		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + TestConfig.getEmailDomain(),
				"gmail.com");

		return uc.createStudent(google_user, univID, firstName, lastName, year,
				major, section, new Email(""));
	}

	public static final User createTA(UserManager uc, String email_firstpart,
			String univID, String firstName, String lastName, int year,
			String major, User.Section section) {

		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + TestConfig.getEmailDomain(),
				"gmail.com");

		User u = uc.createStudent(google_user, univID, firstName, lastName,
				year, major, section, new Email(""));
		u.setType(User.Type.TA);
		uc.update(u);
		return u;
	}

	public static final User createDirector(UserManager uc,
			String email_firstpart, String firstName, String lastName) {
		return uc.createDirector(
				email_firstpart + "@" + TestConfig.getEmailDomain(),
				email_firstpart + ".secondemail" + "@"
						+ TestConfig.getEmailDomain(), firstName, lastName);
	}

	public static User createDefaultDirector(UserManager uc) {
		return Users.createDirector(uc, "director", "I am", "The Director");
	}
}
