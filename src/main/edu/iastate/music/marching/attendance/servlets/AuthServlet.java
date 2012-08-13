package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.GoogleAccountException;
import edu.iastate.music.marching.attendance.util.PageBuilder;
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
		index, login, login_callback, logout, register_pre, register, register_post, login_fail;
	}

	public static String getLoginUrl() {
		return pageToUrl(Page.login, SERVLET_PATH);
	}

	public static String getLoginUrl(String redirect_url) {
		String url = pageToUrl(Page.login, SERVLET_PATH);
		try {
			if (redirect_url != null && redirect_url != "")
				url += "?redirect="
						+ java.net.URLEncoder
								.encode(redirect_url, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
	}

	public static String getLoginUrl(HttpServletRequest request) {
		return getLoginUrl(request.getRequestURI() + '?'
				+ request.getQueryString());
	}

	private static String getLoginCallback(HttpServletRequest request) {
		String url = pageToUrl(Page.login_callback, SERVLET_PATH);
		try {
			if (null != request.getParameter(PageBuilder.PARAM_REDIRECT_URL))
				url += "?redirect="
						+ java.net.URLEncoder.encode(request
								.getParameter(PageBuilder.PARAM_REDIRECT_URL),
								"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return url;
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
				handleLogin(req, resp, true);
				break;
			case logout:
				doLogout(req, resp);
				break;
			case login_callback:
				handleLogin(req, resp, true);
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

	private void handleLogin(HttpServletRequest req, HttpServletResponse resp,
			boolean allow_redirect) throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		try {

			if (train.getAuthController().login(req.getSession())) {
				// Successful login
				redirectPostLogin(req, resp, allow_redirect);
			} else {
				// Still need to register
				resp.sendRedirect(URL_REGISTER);
			}
		} catch (GoogleAccountException e) {
			switch (e.getType()) {
			case None:
				// No google user logged in at all, try to login
				resp.sendRedirect(AuthController
						.getGoogleLoginURL(getLoginCallback(req)));
				break;
			case Invalid:
				new PageBuilder(Page.login_fail, SERVLET_PATH).setAttribute(
						"error_messages", new String[] { e.getMessage() })
						.passOffToJsp(req, resp);
				break;
			default:
				throw e;
			}
		}
	}

	private void handleRegistration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		if (AuthController.getGoogleUser() != null) {
			// Have a valid google login
			// Check if current user is already registered
			User u = train.getAuthController().getCurrentUser(req.getSession());
			if (u == null) {
				// Not yet registered
				showRegistration(req, resp);
			} else {
				// Already registered
				redirectPostLogin(req, resp, false);
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

		page.passOffToJsp(req, resp);
	}

	private void doRegistrationPost(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		String firstName;
		String lastName;
		String major;
		Email secondEmail;
		String univID;
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
		univID = req.getParameter("UniversityID");
		secondEmail = new Email(req.getParameter("SecondEmail"));

		if (!ValidationUtil.isValidUniversityID(univID)) {
			errors.add("University ID was not valid");
		}
		
		if(!ValidationUtil.isUniqueId(univID, new Email(google_user.getEmail()))) {
			errors.add("University ID was not unique");
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

		if (!ValidationUtil.validPrimaryEmail(
				new Email(google_user.getEmail()), train)) {
			errors.add("Invalid primary email, try logging out and then registering under your school email account");
		}

		if (!ValidationUtil.validSecondaryEmail(secondEmail, train)) {
			errors.add("Invalid secondary email");
		}
		if (!ValidationUtil.isUniqueSecondaryEmail(secondEmail, train)) {
			errors.add("Non-unique secondary email");
		}

		if (errors.size() == 0) {
			try {
				new_user = train.getUsersController().createStudent(
						google_user, univID, firstName, lastName, year, major,
						section, secondEmail);

			} catch (IllegalArgumentException e) {
				// Save validation errors
				errors.add(e.getMessage());

			}
		}

		if (new_user == null) {
			// Render registration page again
			PageBuilder page = new PageBuilder(Page.register, SERVLET_PATH);

			page.setAttribute("error_message", "Registration was unsuccessful");

			page.setAttribute("FirstName", firstName);
			page.setAttribute("LastName", lastName);
			page.setAttribute("NetID", google_user.getEmail());
			page.setAttribute("Major", major);
			page.setAttribute("UniversityID", univID);
			page.setAttribute("Year", year);
			page.setAttribute("SecondEmail", secondEmail.getEmail());
			page.setAttribute("Section",
					(section == null) ? null : section.getValue());

			page.setAttribute("sections", User.Section.values());

			page.setAttribute("error_messages", errors);
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
			HttpServletResponse resp, boolean redirect) throws IOException,
			ServletException {

		User user = DataTrain.getAndStartTrain().getAuthController()
				.getCurrentUser(req.getSession());

		if (user == null)
			resp.sendRedirect(URL_REGISTER);
		else if (null != req.getParameter(PageBuilder.PARAM_REDIRECT_URL)
				&& redirect)
			resp.sendRedirect(req.getParameter(PageBuilder.PARAM_REDIRECT_URL));
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
