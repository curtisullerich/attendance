package edu.iastate.music.marching.attendance.controllers;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.InputUtil;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;

public class AuthController {

	private static final String HASH_ALGRO = "SHA-512";
	private static final Charset HASH_CHARSET = Charset.forName("UTF-8");
	private static final int SALT_SIZE = 64;
	private static final String SESSION_USER_ATTRIBUTE = "authenticated_user";

	public static User createStudent(String netID, int univID,
			String firstName, String lastName, List<String> errors) {

		// Sanitize inputs and check they are valid
		// TODO
		String sanitizedFirstName = InputUtil.sanitize(firstName);
		String sanitizedLastName = InputUtil.sanitize(lastName);

		UserController uc = DataTrain.getAndStartTrain().getUsersController();

		User u = uc.createStudent(netID, univID, sanitizedFirstName,
				sanitizedLastName);

		return u;

	}

	private static boolean checkPasswordStrength(String santizedPassword) {
		// TODO Auto-generated method stub
		return true;
	}

	private static byte[] hashPassword(String password, byte[] salt) {

		if (salt == null || password == null)
			return null;

		MessageDigest hasher;
		try {
			hasher = MessageDigest.getInstance(HASH_ALGRO);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
		// First just get the hash of the password
		byte[] password_hash = hasher.digest(password.getBytes(HASH_CHARSET));

		// Then add in the salt and hash again
		byte[] hash_and_salt = new byte[salt.length + password_hash.length];

		System.arraycopy(password_hash, 0, hash_and_salt, 0,
				password_hash.length);
		System.arraycopy(salt, 0, hash_and_salt, password_hash.length,
				salt.length);

		return hasher.digest(hash_and_salt);
	}

	private static String sanitizePassword(String password) {
		return InputUtil.sanitize(password);
	}

	public static void logout(HttpSession session) {
		session.removeAttribute(SESSION_USER_ATTRIBUTE);
	}

	private static void putUserInSession(User u, HttpSession session) {
		if (session != null)
			session.setAttribute(SESSION_USER_ATTRIBUTE, u);
	}

	private static User getUserFromSession(HttpSession session) {
		if (session == null)
			return null;

		Object u = session.getAttribute(SESSION_USER_ATTRIBUTE);

		if (!(u instanceof User))
			return null;
		else
			return (User) u;
	}

	public static boolean isLoggedIn(HttpSession session,
			User.Type... allowed_types) {
		User u = getUserFromSession(session);

		if (u == null)
			return false;

		for (User.Type type : allowed_types) {
			if (u.getType() == type)
				return true;
		}

		return false;
	}

	public static User getCurrentUser(HttpSession session) {
		return getUserFromSession(session);
	}

	public static void updateCurrentUser(User user, HttpSession session) {
		// TODO
		// if(user.getKey().equals(getUserFromSession(session).getKey()))
		// putUserInSession(user, session);
	}

	public static boolean google_login(HttpSession session) {

		UserService userService = UserServiceFactory.getUserService();

		if (!userService.isUserLoggedIn())
			return false;

		String email = userService.getCurrentUser().getEmail();

		// Hack
		User u = DataTrain.getAndStartTrain().getUsersController()
				.get(email.split("@")[0]);

		if (u != null && "iastate.edu".equals(email.split("@")[1])) {
			putUserInSession(u, session);
			return true;
		}

		// TODO Auto-generated method stub
		return false;
	}

	public static User createStudent(String netID, int univID,
			String firstName, String lastName, String major, int year)
			throws ValidationExceptions {
		// TODO Auto-generated method stub
		return null;
	}

}
