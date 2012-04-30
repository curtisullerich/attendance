package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.model.User;

public class MobileAppDataServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3138258973922548889L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ServletUtil.requireLogin(req, resp, User.Type.TA, User.Type.Director);
		
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ServletUtil.requireLogin(req, resp, User.Type.TA, User.Type.Director);
		
	}

}
