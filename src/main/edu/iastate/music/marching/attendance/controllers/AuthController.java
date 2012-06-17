package edu.iastate.music.marching.attendance.controllers;

import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.GoogleAccountException;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class AuthController {

	private static final String SESSION_USER_ATTRIBUTE = "authenticated_user";

	private DataTrain train;

	private User currentUser = null;

	public AuthController(DataTrain dataTrain) {
		this.train = dataTrain;
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

		if (allowed_types == null || allowed_types.length == 0)
			return true;

		for (User.Type type : allowed_types) {
			if (u.getType() == type)
				return true;
		}

		return false;
	}

	public User getCurrentUser(HttpSession session) {

		if (this.currentUser == null) {
			this.currentUser = getUserFromSession(session);

			if (this.currentUser != null) {
				train.getDataStore().disassociate(this.currentUser);
				train.getDataStore().associate(this.currentUser);
			}
		}

		return currentUser;
	}

	public static com.google.appengine.api.users.User getGoogleUser() {
		UserService userService = UserServiceFactory.getUserService();

		return userService.getCurrentUser();
	}

	public static void updateCurrentUser(User user, HttpSession session) {
		// TODO
		// if(user.getKey().equals(getUserFromSession(session).getKey()))
		putUserInSession(user, session);
	}

	public static void logout(HttpSession session) {
		session.removeAttribute(SESSION_USER_ATTRIBUTE);
	}

	public boolean login(HttpSession session) {

		com.google.appengine.api.users.User google_user = getGoogleUser();

		if (google_user == null) {
			// No google user logged in at all
			throw new GoogleAccountException("No google user logged in at all",
					GoogleAccountException.Type.None);
		} else {
			// Some kind of google user logged in, check it is a valid one
			if (ValidationUtil.validGoogleUser(google_user, this.train)) {

				// Check if there is a user in the system already for this
				// google user
				User u = train.getUsersController().get(google_user);

				if (u == null) {
					// Still need to register
					return false;
				} else {
					// Did successful login
					AuthController.updateCurrentUser(u, session);
					return true;
				}
			} else {
				throw new GoogleAccountException("Not a valid google account",
						GoogleAccountException.Type.Invalid);
			}
		}
	}

	public static String getGoogleLoginURL(String redirect_url) {
		UserService userService = UserServiceFactory.getUserService();

		if (userService == null)
			return null;

		return userService.createLoginURL(redirect_url);
	}

	public static String getGoogleLogoutURL(String redirect_url) {
		UserService userService = UserServiceFactory.getUserService();

		if (userService == null)
			return null;

		return userService.createLogoutURL(redirect_url);
	}

	public static boolean validGoogleUser() {
		// TODO Auto-generated method stub
		return false;
	}

}
