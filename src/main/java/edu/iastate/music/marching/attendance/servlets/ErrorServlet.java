package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.util.PageBuilder;

public class ErrorServlet extends AbstractBaseServlet {

	public enum Page {
		index, unauthorized;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -285523290212068266L;

	private static final String SERVLET_PATH = "error";

	public static final String URL = "/" + SERVLET_PATH;

	public static String getLoginFailedUrl(HttpServletRequest req) {
		return pageToUrl(Page.unauthorized, SERVLET_PATH);
	}

	public static void showError(HttpServletRequest req,
			HttpServletResponse resp, int i) throws ServletException,
			IOException {

		showError(req, resp, new Integer(i).toString() + " Error");
	}

	public static void showError(HttpServletRequest req,
			HttpServletResponse resp, String message) throws ServletException,
			IOException {
		new PageBuilder(Page.index, SERVLET_PATH).setPageTitle(message).passOffToJsp(req, resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Page page = parsePathInfo(req.getPathInfo(), Page.class);
		if (page == null) {
			showError(req, resp, 404);
			return;
		}

		switch (page) {
		case unauthorized:
			showUnauthorized(req, resp);
			break;
		default:
			showError(req, resp, 404);
		}
	}

	private void showUnauthorized(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		new PageBuilder(Page.unauthorized, SERVLET_PATH).setPageTitle(
				"Unauthorized").passOffToJsp(req, resp);
	}

}
