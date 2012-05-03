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

	private static final String PATH = "error";

	public static final String URL = "/" + PATH;

	public enum Page implements IPathEnum {
		index;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.getOutputStream().println(req.getPathInfo());
	}

	public static void forwardError(HttpServletRequest req,
			HttpServletResponse resp, int i) throws ServletException,
			IOException {
		AbstractBaseServlet
				.buildPage(new ErrorServlet(), Page.index, req, resp).show();
	}

	@Override
	protected String getJspPath() {
		return PATH;
	}

}
