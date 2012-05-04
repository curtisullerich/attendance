package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ErrorServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -285523290212068266L;

	private static final String SERVLET_PATH = "error";

	public static final String URL = "/" + SERVLET_PATH;

	public enum Page {
		index;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getOutputStream().println(req.getPathInfo());
	}

	public static void showError(HttpServletRequest req,
			HttpServletResponse resp, int i) throws ServletException,
			IOException {
		new PageBuilder(Page.index, SERVLET_PATH).passOffToJsp(req, resp);
	}

}
