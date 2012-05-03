package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.model.User;

public class DirectorServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6100206975846317440L;

	public enum Page implements IPathEnum {
		index;
	}

	private static final String PATH = "director";

	public static final String INDEX_URL = pageToUrl(DirectorServlet.Page.index,
			PATH);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if(!isLoggedIn(req, resp, User.Type.Director))
			return;

	}

	@Override
	protected String getJspPath() {
		return PATH;
	}

}
