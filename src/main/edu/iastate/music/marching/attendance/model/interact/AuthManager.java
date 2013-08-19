package edu.iastate.music.marching.attendance.model.interact;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.util.GoogleAccountException;
import edu.iastate.music.marching.attendance.util.Util;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class AuthManager {

	private static final String SESSION_USER_ATTRIBUTE = "authenticated_user";

	private static final Logger LOG = Logger.getLogger(AuthManager.class
			.getName());

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

	public static com.google.appengine.api.users.User getGoogleUser() {
		UserService userService = UserServiceFactory.getUserService();

		return userService.getCurrentUser();
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

	public static boolean isAdminLoggedIn() {
		try {
			UserService userService = UserServiceFactory.getUserService();
			return userService.isUserAdmin();
		} catch (IllegalStateException e) {
			return false;
		}
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

	public static void logout(HttpSession session) {
		session.removeAttribute(SESSION_USER_ATTRIBUTE);
	}

	private static void putUserInSession(User u, HttpSession session) {
		if (session != null)
			session.setAttribute(SESSION_USER_ATTRIBUTE, u);
	}

	public void updateCurrentUser(User user, HttpSession session) {
		putUserInSession(user, session);
	}

	private DataTrain train;

	private User currentUser = null;

	public AuthManager(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public User getCurrentUser(HttpSession session) {
		if (this.currentUser == null) {
			this.currentUser = getUserFromSession(session);
			train.getDataStore().activate(this.currentUser);
		}

		if (this.currentUser != null
				&& !train.getDataStore().isAssociated(this.currentUser)) {
			try {
				train.getDataStore().associate(this.currentUser);
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Error associating current user: "
						+ this.currentUser.getId(), e);
			}
		}

		if (null != this.currentUser
				&& !train.getDataStore().isActivated(this.currentUser)) {
			try {
				train.getDataStore().activate(this.currentUser);
			} catch (Exception e) {
				LOG.log(Level.WARNING, "Error activating current user: "
						+ this.currentUser.getId(), e);
			}
		}

		return currentUser;
	}

	public boolean login(HttpSession session) {

		com.google.appengine.api.users.User google_user = getGoogleUser();

		if (google_user == null) {
			// No google user logged in at all
			throw new GoogleAccountException("No google user logged in at all",
					GoogleAccountException.Type.None);
		}

		Email google_users_email = Util.makeEmail(google_user.getEmail());

		User matchedUser = null;

		// Some kind of google user logged in, check it against the
		// primary email's of all users
		if (ValidationUtil.isValidPrimaryEmail(google_users_email, this.train)) {

			// Check if there is a user in the system already for this
			// google user
			matchedUser = train.users().get(google_users_email);

		} else if (ValidationUtil.isValidSecondaryEmail(google_users_email,
				this.train)) {
			// Maybe the secondary email will match a user in the database

			// Check if there is a user in the system already for this
			// google user
			matchedUser = train.users().getSecondary(google_users_email);
		} else {
			throw new GoogleAccountException("Not a valid google account",
					GoogleAccountException.Type.Invalid);
		}

		if (matchedUser == null) {
			// Still need to register
			return false;
		} else {
			// Did successful login
			updateCurrentUser(matchedUser, session);
			return true;
		}
	}

}
