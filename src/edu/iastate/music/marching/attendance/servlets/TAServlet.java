package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.model.User;

public class TAServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9160409137196393008L;
	
	public static final String BASE_URL = "/ta";
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		ServletUtil.requireLogin(req, resp, User.Type.Director);
		
	}

}
