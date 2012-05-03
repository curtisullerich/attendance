package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.model.User;

public class TAServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9160409137196393008L;

	private static final String PATH = "ta";

	public static final String INDEX_URL = pageToUrl(Page.index, PATH);

	private enum Page implements IPathEnum {
		index;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if(!requireLogin(req, resp, User.Type.TA, User.Type.Director))
			return;

	}

	@Override
	public String getJspPath() {
		return PATH;
	}

}
