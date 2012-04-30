package edu.iastate.music.marching.attendance.beans;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;

import edu.iastate.music.marching.attendance.controllers.Auth;
import edu.iastate.music.marching.attendance.model.User;

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
		return Auth.getCurrentUser(mSession);
	}

	public void apply(ServletRequest request) {
		if (request != null)
			request.setAttribute(ATTRIBUTE_NAME, this);
	}
}
