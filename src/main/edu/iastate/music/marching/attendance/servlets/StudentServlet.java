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

	private static final String PATH = "student";

	public static final String INDEX_URL = pageToUrl(Page.index,
			PATH);

	private enum Page {
		index, attendance, forms, messages, info;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp, User.Type.Student))
			return;

		Page page = pathInfoToPage(req, resp, Page.class);

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

		isLoggedIn(req, resp, User.Type.Student);

		Page page = pathInfoToPage(req, resp, Page.class);

		switch (page) {
		case info:
			postInfo(req, resp);
			break;
		default:
			do404(req, resp);
		}

	}

	private void postInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String firstName, lastName, major;
		int univID = -1, year = -1;
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

		DataTrain.users().update(u);
		AuthController.updateCurrentUser(u, req.getSession());

		showInfo(req, resp);

	}

	private void showInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		PageBuilder page = buildPage(Page.info, req, resp);

		page.setAttribute("user",
				AuthController.getCurrentUser(req.getSession()));

		page.setAttribute("sections", User.Section.values());

		page.show();
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PageBuilder page = buildPage(Page.index, req, resp);

		page.setPageTitle("Student");

		page.show();
	}

	@Override
	public String getJspPath() {
		return PATH;
	}

}
