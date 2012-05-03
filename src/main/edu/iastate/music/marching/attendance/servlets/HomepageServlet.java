package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.model.User;

public class HomepageServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -333131093361829044L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		User user = AuthController.getCurrentUser(req.getSession());

		if (user == null)
			resp.sendRedirect(AuthServlet.URL_LOGIN);
		else
			switch (user.getType()) {
			case Student:
				resp.sendRedirect(StudentServlet.INDEX_URL);
				break;
			case Director:
				resp.sendRedirect(DirectorServlet.INDEX_URL);
				break;
			case TA:
				resp.sendRedirect(TAServlet.INDEX_URL);
				break;
			default:
				throw new IllegalStateException("Unsupported user type");
			}
	}

	@Override
	protected String getJspPath() {
		return null;
	}

}
