package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.Lang;
import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.InputUtil;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;

public class AuthServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4587683490944456397L;

	private static final String SERVLET_PATH = "auth";

	public static final String URL_ON_GOOGLE_LOGIN = pageToUrl(
			Page.on_google_login, SERVLET_PATH);

	public static final String URL_ON_GOOGLE_LOGOUT = pageToUrl(
			Page.on_google_logout, SERVLET_PATH);

	public static final String URL_LOGIN = pageToUrl(Page.login, SERVLET_PATH);

	private enum Page {
		index, login, register, logout, on_google_login, on_google_logout;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			show404(req, resp);
		else
			switch (page) {
			case index:
				resp.sendRedirect(urlFromPage(Page.login, SERVLET_PATH));
				break;
			case login:
				showLogin(req, resp, null);
				break;
			case logout:
				doLogout(req, resp);
				break;
			case register:
				showRegistration(req, resp);
				break;
			case on_google_login:
				didGoogleLogin(req, resp);
				break;
			}

	}

	private Page pathInfoToPage(String pathInfo, Class<Page> class1) {
		// TODO Auto-generated method stub
		return null;
	}

	private void didGoogleLogin(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		if (!AuthController.google_login(req.getSession())) {
			// Failed google attempt
			showLogin(req, resp, new String[] { "Google login failed." });
		} else {
			redirectLogin(req, resp);
		}
	}

	private void showLogin(HttpServletRequest req, HttpServletResponse resp,
			String[] errors) throws ServletException, IOException {

		new PageBuilder(Page.login, SERVLET_PATH)
				.setAttribute("errors", errors).passOffToJsp(req, resp);
	}

	private void showRegistration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		PageBuilder page = new PageBuilder(Page.register, SERVLET_PATH);

		page.setAttribute("sections", User.Section.values());

		page.setPageTitle("Register");

		// If no director
		if (!App.isDirectorRegistered())
			page.setAttribute("error_message", Lang.ERROR_MESSAGE_NO_DIRECTOR);

		page.passOffToJsp(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			show404(req, resp);
		else
			switch (page) {
			case index:
				resp.sendRedirect(urlFromPage(Page.login, SERVLET_PATH));
				break;
			case login:
				doLogin(req, resp);
				break;
			case logout:
				doLogout(req, resp);
				break;
			case register:
				doRegistration(req, resp);
				break;
			default:
				show404(req, resp);
			}

	}

	private void doRegistration(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String netID, firstName, lastName, major;
		int univID = -1, year = -1;
		User.Section section = null;
		User new_user = null;
		List<String> errors = new LinkedList<String>();

		// Grab all the data from the form fields
		netID = req.getParameter("NetID");
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");
		major = req.getParameter("Major");

		try {
			univID = Integer.parseInt(req.getParameter("UniversityID"));
		} catch (NumberFormatException e) {
			errors.add("University ID entered was not a number");
		}
		try {
			year = Integer.parseInt(req.getParameter("Year"));
		} catch (NumberFormatException e) {
			errors.add("Invalid number of years in band entered, was not a number");
		}

		try {
			section = User.Section.valueOf(req.getParameter("Section"));
		} catch (IllegalArgumentException e) {
			errors.add("Invalid section");
		} catch (NullPointerException e) {
			errors.add("Invalid section");
		}

		// Only continue creating if we have had no errors
		try {
			new_user = AuthController.createStudent(netID, univID, firstName,
					lastName, major, year);

		} catch (ValidationExceptions e) {
			// TODO

			// Failed to create a new user
			errors.add(0, "Registration was unsuccessful!");

			// Render registration page again
			PageBuilder page = new PageBuilder(Page.register, SERVLET_PATH);

			page.setAttribute("FirstName", firstName);
			page.setAttribute("LastName", lastName);
			page.setAttribute("NetID", netID);
			page.setAttribute("Major", major);
			page.setAttribute("UniversityID", univID);
			page.setAttribute("Year", year);
			page.setAttribute("Section",
					(section == null) ? null : section.getValue());

			page.setAttribute("sections", User.Section.values());

			page.setAttribute("errors", errors);
			page.setPageTitle("Failed Registration");

			page.passOffToJsp(req, resp);
		}

		if (new_user == null) {

		} else {
			// Did create a new user!
			errors.add(
					0,
					new_user.getName()
							+ " successfully added to the system. Please feel free to login now.");

			PageBuilder forward = new PageBuilder(Page.login, SERVLET_PATH);

			// Auto-fill username
			forward.setAttribute("NetID", netID);

			forward.setAttribute("errors", errors);
			forward.setPageTitle("Successful Registration");

			forward.passOffToJsp(req, resp);

		}

	}

	private void doLogout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		AuthController.logout(req.getSession());

		showLogin(req, resp, new String[] { "Successfully logged out" });
	}

	private void doLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (req.getParameter("Register") != null)
			showRegistration(req, resp);
		else {
			// TODO
			// User user = AuthController.login(req.getParameter("NetID"),
			// req.getParameter("Password"), req.getSession());

			// if (user == null) {
			// showLogin(req, resp,
			// new String[] { "Invaild netID or password." });
			// return;
			// }

			// Successful login, redirect as appropriate
			redirectLogin(req, resp);

		}

		// OnFail
		PageBuilder forward = new PageBuilder(Page.login, SERVLET_PATH);
		forward.setAttribute("errors",
				new String[] { "Invalid username or password" });
		forward.passOffToJsp(req, resp);
	}

	private void redirectLogin(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		User user = AuthController.getCurrentUser(req.getSession());

		if (user == null)
			throw new IllegalStateException(
					"Invaild login redirection, no user logged in");

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
}
