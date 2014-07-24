package edu.iastate.music.marching.attendance.testlib;

import org.junit.Ignore;

import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.util.Util;

@Ignore
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
				email_firstpart + "@" + Config.getEmailDomain(),
				"gmail.com");

		return uc.createStudent(google_user, univID, firstName, lastName, year,
				major, section, Util.makeEmail(""));
	}

	public static final User createTA(UserManager uc, String email_firstpart,
			String univID, String firstName, String lastName, int year,
			String major, User.Section section) {

		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + Config.getEmailDomain(),
				"gmail.com");

		User u = uc.createStudent(google_user, univID, firstName, lastName,
				year, major, section, Util.makeEmail(""));
		u.setType(User.Type.TA);
		uc.update(u);
		return u;
	}

	public static final User createDirector(UserManager uc,
			String email_firstpart, String firstName, String lastName) {
		return uc.createDirector(
				email_firstpart + "@" + Config.getEmailDomain(),
				email_firstpart + ".secondemail" + "@"
						+ Config.getEmailDomain(), firstName, lastName);
	}

	public static User createDefaultDirector(UserManager uc) {
		return Users.createDirector(uc, "director", "I am", "The Director");
	}
}
