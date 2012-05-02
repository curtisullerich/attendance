package edu.iastate.music.marching.attendance.controllers;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import edu.iastate.music.marching.attendance.model.ModelException;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.model.User.Type;
import edu.iastate.music.marching.attendance.util.InputUtil;

public class AuthController {

	private static final String HASH_ALGRO = "SHA-512";
	private static final Charset HASH_CHARSET = Charset.forName("UTF-8");
	private static final int SALT_SIZE = 64;
	private static final String SESSION_USER_ATTRIBUTE = "authenticated_user";

	public static User createUser(Type type, String netID, String password,
			int univID, List<String> errors) {

		// Sanitize inputs and check they are valid
		String santizedNetID = InputUtil.sanitize(netID);
		String santizedPassword = sanitizePassword(password);

		if (santizedNetID == null) {
			errors.add("Invaild netid");
			return null;
		}

		if (santizedPassword == null) {
			errors.add("Invalid password, try using less special characters");
			return null;
		}

		if (!checkPasswordStrength(santizedPassword)) {
			errors.add("Password is not strong enough");
			return null;
		}

		// Hash password
		byte[] user_salt = generateSalt();

		if (user_salt == null) {
			errors.add("Internal error trying to hash password");
			return null;
		}

		byte[] hashed_password = hashPassword(santizedPassword, user_salt);

		if (hashed_password == null) {
			errors.add("Internal error trying to hash password");
			return null;
		}

		PersistenceManager pm = ModelFactory.getPersistenceManager();

		try {

			UserController uc = Controllers.users(pm);

			try {

				User u = uc.create(type, netID, hashed_password, user_salt);

				if (u == null) {
					errors.add("Failed to create user");
					return null;
				}

				return u;

			} catch (ModelException e) {
				errors.add("Error: " + e.getMessage());
			}

		} finally {
			// pm.close();
		}

		return null;
	}

	private static byte[] generateSalt() {
		// Uses a secure Random not a simple Random
		SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e1) {
			return null;
		}
		byte[] salt = new byte[SALT_SIZE];

		random.nextBytes(salt);

		return salt;
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

	public static User login(String netid, String password, HttpSession session) {

		User u = Controllers.users().get(netid);

		if (u == null || u.getPasswordHash() == null
				|| u.getPasswordSalt() == null)
			return null;

		byte[] hashed_password = hashPassword(password, u.getPasswordSalt());

		if (Arrays.equals(hashed_password, u.getPasswordHash())) {
			// Successful login

			putUserInSession(u, session);

			return u;
		}

		return null;
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
		if(user.getKey().equals(getUserFromSession(session).getKey()))
			putUserInSession(user, session);
	}

	public static boolean google_login(HttpSession session) {
		
		UserService userService = UserServiceFactory.getUserService();
		
		if(!userService.isUserLoggedIn())
			return false;
		
		String email = userService.getCurrentUser().getEmail();
		
		// Hack
		User u = Controllers.users().get(email.split("@")[0]);
		
		if(u != null && "iastate.edu".equals(email.split("@")[1]))
		{
			putUserInSession(u, session);
			return true;
		}
		
		// TODO Auto-generated method stub
		return false;
	}

}
