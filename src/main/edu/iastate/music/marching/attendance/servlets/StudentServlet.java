package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.InputUtil;

public class StudentServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6636386568039228284L;

	private static final String SERVLET_PATH = "student";

	public static final String INDEX_URL = pageToUrl(Page.index, SERVLET_PATH);

	private enum Page {
		index, attendance, forms, messages, info;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp, User.Type.Student)) {
			resp.sendRedirect(AuthServlet.URL_LOGIN);
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case index:
				showIndex(req, resp);
				break;
			case attendance:
				showIndex(req, resp);
				break;
			case forms:
				// TODO
				break;
			case messages:
				showIndex(req, resp);
				break;
			case info:
				showInfo(req, resp);
				break;
			}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp, User.Type.Student)) {
			resp.sendRedirect(AuthServlet.URL_LOGIN);
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			show404(req, resp);
		else
			switch (page) {
			case info:
				postInfo(req, resp);
				break;
			default:
				show404(req, resp);
			}

	}

	private void postInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String firstName, lastName, major;
		int year = -1;
		User.Section section = null;

		// Grab all the data from the fields
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");
		major = req.getParameter("Major");

		// Validate data not going to be given to the Auth module (which does
		// validation itself)
		String sanitizedMajor = InputUtil.sanitize(major);
		String santizedFirstName = InputUtil.sanitize(firstName);
		String santizedLastName = InputUtil.sanitize(lastName);

		User u = AuthController.getCurrentUser(req.getSession());

		u.setYear(year);
		u.setMajor(sanitizedMajor);
		u.setSection(section);
		u.setFirstName(santizedFirstName);
		u.setLastName(santizedLastName);

		DataTrain.getAndStartTrain().getUsersController().update(u);
		AuthController.updateCurrentUser(u, req.getSession());

		showInfo(req, resp);

	}

	private void showInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		PageBuilder page = new PageBuilder(Page.info, SERVLET_PATH);

		page.setAttribute("user",
				AuthController.getCurrentUser(req.getSession()));

		page.setAttribute("sections", User.Section.values());

		page.passOffToJsp(req, resp);
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PageBuilder page = new PageBuilder(Page.index, SERVLET_PATH);

		page.setPageTitle("Student");

		page.passOffToJsp(req, resp);
	}

}
