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

public class AuthServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4587683490944456397L;

	public static final String BASE_URL = "/auth";

	private static final String REL_PATH_LOGIN = "/login";

	private static final String REL_PATH_REGISTER = "/register";

	private static final String REL_PATH_LOGOUT = "/logout";

	private static final String REL_PATH_REGISTER_STAFF = "/register_staff";

	public static final String URL_LOGIN = BASE_URL + REL_PATH_LOGIN;

	public static final String URL_LOGOUT = BASE_URL + REL_PATH_LOGOUT;

	public static final String URL_REGISTER = BASE_URL + REL_PATH_REGISTER;

	public static final String URL_REGISTER_STAFF = BASE_URL
			+ REL_PATH_REGISTER_STAFF;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String path = req.getPathInfo();

		if (path == null || "/".equals(path))
			resp.sendRedirect(URL_LOGIN);

		else if (path.equals(REL_PATH_LOGIN))
			showLogin(req, resp, null);

		else if (path.equals(REL_PATH_LOGOUT))
			doLogout(req, resp);

		else if (path.equals(REL_PATH_REGISTER))
			showRegistration(req, resp);

		else if (path.equals(REL_PATH_REGISTER_STAFF))
			showStaffRegistration(req, resp);

		else
			ServletUtil.do404(resp);

	}

	private void showLogin(HttpServletRequest req, HttpServletResponse resp,
			String[] errors) throws ServletException, IOException {

		ServletUtil.buildPage(URL_LOGIN, req, resp)
				.setAttribute("errors", errors).showPage();
	}

	private void showRegistration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		PageBuilder page = ServletUtil.buildPage(URL_REGISTER, req, resp);

		page.setAttribute("sections", User.Section.values());
		
		page.setPageTitle("Register");

		// If no director
		// TODO
		page.setAttribute(
				"errors",
				new String[] { "There is no director registered. You cannot register for an account yet." });

		page.showPage();
	}

	private void showStaffRegistration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub

		PageBuilder forward = ServletUtil.buildPage(URL_REGISTER_STAFF, req,
				resp);
		// If no director
		forward.setAttribute(
				"errors",
				new String[] { "There is no director registered. You cannot register for an account yet." });
		forward.showPage();

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String path = req.getPathInfo();

		if (path == null)
			resp.sendRedirect(URL_LOGIN);

		else if (path.equals(REL_PATH_LOGIN))
			doLogin(req, resp);

		else if (path.equals(REL_PATH_LOGOUT))
			doLogout(req, resp);

		else if (path.equals(REL_PATH_REGISTER))
			doRegistration(req, resp);

		else if (path.equals(REL_PATH_REGISTER_STAFF))
			doStaffRegistration(req, resp);

		else
			ServletUtil.do404(resp);

	}

	private void doStaffRegistration(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

	private void doRegistration(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String netID, password, password_confirm, firstName, lastName, major;
		int univID = -1, year = -1;
		User.Section section = null;
		User new_user = null;
		List<String> errors = new LinkedList<String>();

		// Grab all the data from the fields
		netID = req.getParameter("NetID");
		password = req.getParameter("Password");
		password_confirm = req.getParameter("PasswordConfirm");
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
			new_user = Auth.createUser(User.Type.Student, netID, password,
					univID, errors);

		if (new_user == null) {
			// Failed to create a new user
			errors.add(0, "Registration was unsuccessful!");

			// Render registration page again
			PageBuilder forward = ServletUtil
					.buildPage(URL_REGISTER, req, resp);

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

			forward.showPage();

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

			DataModel.users().update(new_user);

			PageBuilder forward = ServletUtil.buildPage(URL_LOGIN, req, resp);

			// Auto-fill username
			forward.setAttribute("NetID", netID);

			forward.setAttribute("errors", errors);
			forward.setPageTitle("Successful Registration");

			forward.showPage();

		}

	}

	private void doLogout(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Auth.logout(req.getSession());

		showLogin(req, resp, new String[] { "Successfully logged out" });
	}

	private void doLogin(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (req.getParameter("Register") != null)
			showRegistration(req, resp);
		else {
			User user = Auth.login(req.getParameter("NetID"),
					req.getParameter("Password"), req.getSession());

			if (user == null) {
				showLogin(req, resp,
						new String[] { "Invaild netID or password." });
				return;
			}

			// Successful login, redirect as appropriate
			switch(user.getType()) {
			case Student:
				resp.sendRedirect(StudentServlet.BASE_URL);
				break;
			case Director:
				resp.sendRedirect(DirectorServlet.BASE_URL);
				break;
			case TA:
				resp.sendRedirect(TAServlet.BASE_URL);
				break;
			default:
				throw new IllegalStateException("Unsupported user type");
			}
			
			
		}

		// OnFail
		PageBuilder forward = ServletUtil.buildPage(URL_LOGIN, req, resp);
		forward.setAttribute("errors",
				new String[] { "Invalid username or password" });
		forward.showPage();
	}
}
