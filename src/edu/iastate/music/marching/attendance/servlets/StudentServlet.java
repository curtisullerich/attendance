package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.Auth;
import edu.iastate.music.marching.attendance.model.DataModel;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.InputUtil;

public class StudentServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6636386568039228284L;

	public static final String BASE_URL = "/student";

	public static final String INDEX_JSP_URL = BASE_URL + "/index";
	
	private static final String REL_PATH_ATTENDANCE = "/attendance";
	private static final String REL_PATH_FORMS = "/forms";
	private static final String REL_PATH_MESSAGES = "/messages";

	private static final String REL_PATH_INFO = "/info";

	public static final String URL_INFO = BASE_URL + REL_PATH_INFO;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String path = req.getPathInfo();

		ServletUtil.requireLogin(req, resp, User.Type.Student);

		if (path == null || "/".equals(path))
			showIndex(req, resp);

		else if (path.equals(REL_PATH_INFO))
			showInfo(req, resp);

		else if (path.equals(REL_PATH_ATTENDANCE))
			showIndex(req, resp);
		
		else if (path.equals(REL_PATH_FORMS))
			showIndex(req, resp);
		
		else if (path.equals(REL_PATH_MESSAGES))
			showIndex(req, resp);

		else
			ServletUtil.do404(resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ServletUtil.requireLogin(req, resp, User.Type.Student);

		String path = req.getPathInfo();

		if (path == null || "/".equals(path))
			resp.sendRedirect(BASE_URL);

		else if (path.equals(REL_PATH_INFO))
			postInfo(req, resp);

		else
			ServletUtil.do404(resp);

	}

	private void postInfo(HttpServletRequest req, HttpServletResponse resp) {

		String netID, password, password_confirm, firstName, lastName, major;
		int univID = -1, year = -1;
		User.Section section = null;

		// Grab all the data from the fields
		netID = req.getParameter("NetID");
		password = req.getParameter("Password");
		password_confirm = req.getParameter("PasswordConfirm");
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");
		major = req.getParameter("Major");

		// Validate data not going to be given to the Auth module (which does
		// validation itself)
		String sanitizedMajor = InputUtil.sanitize(major);
		String santizedFirstName = InputUtil.sanitize(firstName);
		String santizedLastName = InputUtil.sanitize(lastName);
		
		User u = Auth.getCurrentUser(req.getSession());
		
		u.setYear(year);
		u.setMajor(sanitizedMajor);
		u.setSection(section);
		u.setFirstName(santizedFirstName);
		u.setLastName(santizedLastName);

		DataModel.users().update(u);
		Auth.updateCurrentUser(u, req.getSession());

	}

	private void showInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		PageBuilder page = ServletUtil.buildPage(URL_INFO, req, resp);

		page.setAttribute("user", Auth.getCurrentUser(req.getSession()));

		page.setAttribute("sections", User.Section.values());

		page.showPage();
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ServletUtil.requireLogin(req, resp, User.Type.Student);

		PageBuilder page = ServletUtil.buildPage(INDEX_JSP_URL, req, resp);

		page.setPageTitle("Student");

		page.showPage();
	}

}
