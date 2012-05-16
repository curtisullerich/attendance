package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class AuthServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4587683490944456397L;

	private static final String SERVLET_PATH = "auth";

	public static final String URL_LOGOUT = pageToUrl(Page.logout, SERVLET_PATH);

	public static final String URL_LOGIN = pageToUrl(Page.login, SERVLET_PATH);

	private static final String URL_REGISTER = pageToUrl(Page.register,
			SERVLET_PATH);

	private enum Page {
		index, login, logout, register_pre, register, register_post, login_fail;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case index:
				resp.sendRedirect(URL_LOGIN);
				break;
			case login:
				doLogin(req, resp);
				break;
			case logout:
				doLogout(req, resp);
				break;
			case register:
				handleRegistration(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case register:
				doRegistrationPost(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private void doLogin(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		com.google.appengine.api.users.User google_user = AuthController
				.getGoogleUser();

		if (google_user == null) {
			// No google user logged in at all, try to login
			resp.sendRedirect(AuthController.getGoogleLoginURL(pageToUrl(
					Page.login, SERVLET_PATH)));
		} else {
			// Some kind of google user logged in, check it is a valid one
			if (ValidationUtil.validGoogleUser(google_user)) {

				// Check if there is a user in the system already for this
				// google user
				DataTrain train = DataTrain.getAndStartTrain();

				User u = train.getUsersController().get(google_user);

				if (u == null) {
					// Still need to register
					resp.sendRedirect(URL_REGISTER);
				} else {
					// Did successful login
					AuthController.updateCurrentUser(u, req.getSession());
					redirectPostLogin(req, resp);
				}
			} else {
				new PageBuilder(Page.login_fail, SERVLET_PATH).setAttribute(
						"error_message", "Invalid google account")
						.passOffToJsp(req, resp);
			}
		}
	}

	private void handleRegistration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		if (AuthController.getGoogleUser() != null) {
			// Have a valid google login
			// Check if current user is already registered
			User u = AuthController.getCurrentUser(req.getSession());
			if (u == null) {
				// Not yet registered
				showRegistration(req, resp);
			} else {
				// Already registered
				redirectPostLogin(req, resp);
			}
		} else {
			// No valid google login, show a welcome page prompting them to
			// login
			new PageBuilder(Page.register_pre, SERVLET_PATH).passOffToJsp(req,
					resp);
		}
	}

	private void showRegistration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		PageBuilder page = new PageBuilder(Page.register, SERVLET_PATH);

		page.setAttribute("NetID", AuthController.getGoogleUser().getEmail());

		page.setAttribute("sections", User.Section.values());

		page.setPageTitle("Register");

		// If no director
		if (!App.isDirectorRegistered())
			page.setAttribute("error_message",
					"There is no director registered. You cannot register for an account yet.");

		page.passOffToJsp(req, resp);
	}

	private void doRegistrationPost(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		String firstName;
		String lastName;
		String major;
		int univID = -1;
		int year = -1;
		User.Section section = null;
		User new_user = null;
		List<String> errors = new LinkedList<String>();

		com.google.appengine.api.users.User google_user = AuthController
				.getGoogleUser();

		// Grab all the data from the form fields
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

		try {
			new_user = train.getUsersController().createStudent(google_user,
					univID, firstName, lastName, year, major, section);

		} catch (IllegalArgumentException e) {
			// Save validation errors
			errors.add(e.getMessage());

		}

		if (new_user == null) {
			// Render registration page again
			PageBuilder page = new PageBuilder(Page.register, SERVLET_PATH);

			page.setAttribute("error_message", "Registration was unsuccessful");

			page.setAttribute("FirstName", firstName);
			page.setAttribute("LastName", lastName);
			page.setAttribute("NetID", google_user.getEmail());
			page.setAttribute("Major", major);
			page.setAttribute("UniversityID", (univID > 0) ? univID : "");
			page.setAttribute("Year", year);
			page.setAttribute("Section",
					(section == null) ? null : section.getValue());

			page.setAttribute("sections", User.Section.values());

			page.setAttribute("errors", errors);
			page.setPageTitle("Failed Registration");

			page.passOffToJsp(req, resp);
		} else {
			// Did create a new user!
			
			// Log them in
			resp.sendRedirect(URL_LOGIN);
		}

	}

	private void doLogout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Force logout from app itself
		if (AuthController.isLoggedIn(req.getSession())) {
			AuthController.logout(req.getSession());
		}

		// Also log out of google for this app
		// Does not log user compelely out of google
		if (AuthController.getGoogleUser() != null) {
			// Logout from page and redirect back to this same page
			resp.sendRedirect(AuthController.getGoogleLogoutURL(pageToUrl(
					Page.logout, SERVLET_PATH)));
		} else {
			new PageBuilder(Page.logout, SERVLET_PATH).passOffToJsp(req, resp);
		}
	}

	private void redirectPostLogin(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {

		User user = AuthController.getCurrentUser(req.getSession());

		if (user == null)
			resp.sendRedirect(URL_REGISTER);
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
}
