package edu.iastate.music.marching.attendance.beans;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.servlets.AuthServlet;

public class AuthBean implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6300883251650797582L;
	
	private static final String ATTRIBUTE_NAME = "auth";
	HttpSession mSession;

	public AuthBean(HttpSession session) {
		mSession = session;
	}

	public User getUser() {
		return AuthController.getCurrentUser(mSession);
	}
	
	public com.google.appengine.api.users.User getGoogleuser() {
		UserService userService = UserServiceFactory.getUserService();
		
		return userService.getCurrentUser();
	}

	public String getGoogleLoginURL() {
		UserService userService = UserServiceFactory.getUserService();
		
		if(userService == null)
			return null;
		
		return userService.createLoginURL(AuthServlet.URL_ON_GOOGLE_LOGIN);
	}
	
	public String getGoogleLogoutURL() {
		UserService userService = UserServiceFactory.getUserService();
		
		if(userService == null)
			return null;
		
		return userService.createLogoutURL(AuthServlet.URL_ON_GOOGLE_LOGOUT);
	}
	
	public void apply(ServletRequest request) {
		if (request != null)
			request.setAttribute(ATTRIBUTE_NAME, this);
	}
}
