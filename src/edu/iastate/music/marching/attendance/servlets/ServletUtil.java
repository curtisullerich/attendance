package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.Auth;
import edu.iastate.music.marching.attendance.model.User;

public class ServletUtil {

	private static final String URL_404 = "/error.html";

	public static void do404(HttpServletResponse resp) throws IOException {
		resp.sendRedirect(URL_404);
	}
	
	public static void requireLogin(HttpServletRequest req, HttpServletResponse resp, User.Type... allowed_types) throws IOException
	{
		if(!Auth.isLoggedIn(req.getSession(), allowed_types))
		{
			// User of correct type is not logged in
			resp.sendRedirect(AuthServlet.URL_LOGIN);
		}
	}

	/**
	 * 
	 * @param jsp_path Relative to /war/WEB-INF/
	 * @param req
	 * @param resp
	 */
	public static PageBuilder buildPage(String jsp_path,
			HttpServletRequest req,
			HttpServletResponse resp) {
		return new PageBuilder(jsp_path, req, resp);
	}

}
