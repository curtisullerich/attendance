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

	public static final String INDEX_URL = Util.url(TAServlet.Page.index, PATH);

	private enum Page implements IPathEnum {
		index;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Util.requireLogin(req, resp, User.Type.TA, User.Type.Director);

	}

	@Override
	public String getJspPath() {
		return PATH;
	}

	@Override
	protected Class<? extends Enum<?>> getPageEnumType() {
		return Page.class;
	}

}
