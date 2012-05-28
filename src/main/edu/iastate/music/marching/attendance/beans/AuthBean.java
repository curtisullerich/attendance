package edu.iastate.music.marching.attendance.beans;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.User;

public class AuthBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6300883251650797582L;

	private static final String ATTRIBUTE_NAME = "auth";
	HttpSession session;

	private DataTrain train;

	private AuthBean(HttpSession session, DataTrain train) {
		this.session = session;
		this.train = train;
	}

	public User getUser() {
		return this.train.getAuthController().getCurrentUser(this.session);
	}

	public com.google.appengine.api.users.User getGoogleUser() {
		UserService userService = UserServiceFactory.getUserService();

		return userService.getCurrentUser();
	}

	public boolean isGoogleLogin() {
		com.google.appengine.api.users.User google_user = getGoogleUser();

		return google_user != null;
	}
	
	public boolean isAdmin() {
		UserService userService = UserServiceFactory.getUserService();
		
		return userService.isUserLoggedIn() && userService.isUserAdmin();
	}

	public void apply(ServletRequest request) {
		if (request != null)
			request.setAttribute(ATTRIBUTE_NAME, this);
	}

	public static AuthBean getBean(HttpSession session, DataTrain train) {
		return new AuthBean(session, train);
	}
}
