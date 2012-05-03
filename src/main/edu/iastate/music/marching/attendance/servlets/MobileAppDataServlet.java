package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.model.User;

public class MobileAppDataServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3138258973922548889L;

	private static final String JSPATH = "mobiledata";

	public enum Page implements IPathEnum {
		index;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if(!requireLogin(req, resp, User.Type.TA, User.Type.Director))
			return;

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		requireLogin(req, resp, User.Type.TA, User.Type.Director);

	}

	@Override
	public String getJspPath() {
		return JSPATH;
	}

}
