package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.model.User;

public class DirectorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6100206975846317440L;
	
	public static final String BASE_URL = "/director";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ServletUtil.requireLogin(req, resp, User.Type.Director);
		
	}

}
