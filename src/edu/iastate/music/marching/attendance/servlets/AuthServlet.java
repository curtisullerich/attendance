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
import edu.iastate.music.marching.attendance.controllers.Controllers;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.InputUtil;

public class AuthServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4587683490944456397L;

	private static final String PATH = "auth";

	public static final String URL_ON_GOOGLE_LOGIN = Util.url(
			Page.on_google_login, PATH);

	public static final String URL_ON_GOOGLE_LOGOUT = Util.url(
			Page.on_google_logout, PATH);

	public static final String URL_LOGIN = Util.url(Page.login, PATH);

	private enum Page implements IPathEnum {
		index, login, register, logout, on_google_login, on_google_logout;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Page page = parsePathInfo(req, resp);

		switch (page) {
		case index:
			resp.sendRedirect(urlFromPage(Page.login));
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

		buildPage(Page.login, req, resp).setAttribute("errors", errors).show();
	}

	private void showRegistration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		PageBuilder page = buildPage(Page.register, req, resp);

		page.setAttribute("sections", User.Section.values());

		page.setPageTitle("Register");

		// If no director
		if (!App.isDirectorRegistered())
			page.setAttribute("error_message", Lang.ERROR_MESSAGE_NO_DIRECTOR);

		page.show();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Page page = parsePathInfo(req, resp);

		switch (page) {
		case index:
			resp.sendRedirect(urlFromPage(Page.login));
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
			Util.do404(req, resp);
		}

	}

	private void doRegistration(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String netID, password, firstName, lastName, major;
		int univID = -1, year = -1;
		User.Section section = null;
		User new_user = null;
		List<String> errors = new LinkedList<String>();

		// Grab all the data from the fields
		netID = req.getParameter("NetID");
		password = req.getParameter("Password");
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

		// Validate data not going to be given to the Auth module (which does
		// validation itself)
		String sanitizedMajor = InputUtil.sanitize(major);
		String santizedFirstName = InputUtil.sanitize(firstName);
		String santizedLastName = InputUtil.sanitize(lastName);

		if (sanitizedMajor == null) {
			errors.add("Invalid major entered");
		}
		if (santizedFirstName == null) {
			errors.add("Invalid first name");

		}

		if (santizedLastName == null) {
			errors.add("Invalid last name");

		}

		if (year < 1 | year > 10) {
			errors.add("Invalid number of years in band, must be in the range of 1-10 inclusive");
		}

		// Only continue creating if we have had no errors
		if (errors.size() == 0)
			new_user = AuthController.createUser(User.Type.Student, netID,
					password, univID, errors);

		if (new_user == null) {
			// Failed to create a new user
			errors.add(0, "Registration was unsuccessful!");

			// Render registration page again
			PageBuilder forward = buildPage(Page.register, req, resp);

			forward.setAttribute("FirstName", santizedFirstName);
			forward.setAttribute("LastName", santizedLastName);
			forward.setAttribute("NetID", netID);
			forward.setAttribute("Major", sanitizedMajor);
			forward.setAttribute("UniversityID", univID);
			forward.setAttribute("Year", year);
			forward.setAttribute("Section",
					(section == null) ? null : section.getValue());

			forward.setAttribute("sections", User.Section.values());

			forward.setAttribute("errors", errors);
			forward.setPageTitle("Failed Registration");

			forward.show();

		} else {
			// Did create a new user!
			errors.add(
					0,
					new_user.getName()
							+ " successfully added to the system. Please feel free to login now.");

			// Save additional info to them
			new_user.setYear(year);
			new_user.setMajor(sanitizedMajor);
			new_user.setSection(section);

			Controllers.users().update(new_user);

			PageBuilder forward = buildPage(Page.login, req, resp);

			// Auto-fill username
			forward.setAttribute("NetID", netID);

			forward.setAttribute("errors", errors);
			forward.setPageTitle("Successful Registration");

			forward.show();

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
			User user = AuthController.login(req.getParameter("NetID"),
					req.getParameter("Password"), req.getSession());

			if (user == null) {
				showLogin(req, resp,
						new String[] { "Invaild netID or password." });
				return;
			}

			// Successful login, redirect as appropriate
			redirectLogin(req, resp);

		}

		// OnFail
		PageBuilder forward = buildPage(Page.login, req, resp);
		forward.setAttribute("errors",
				new String[] { "Invalid username or password" });
		forward.show();
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

	@Override
	protected Class<? extends Enum<?>> getPageEnumType() {
		return Page.class;
	}

	@Override
	protected String getJspPath() {
		return PATH;
	}
}
