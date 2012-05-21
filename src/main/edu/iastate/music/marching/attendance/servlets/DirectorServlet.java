package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.User;

public class DirectorServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6100206975846317440L;

	public enum Page {
		index, appinfo, attendance, export, forms, unanchored, users, user, stats;
	}

	private static final String SERVLET_JSP_PATH = "director";

	public static final String INDEX_URL = pageToUrl(
			DirectorServlet.Page.index, SERVLET_JSP_PATH);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp, User.Type.Director)) {
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
				showAttendance(req, resp);
				break;
			case appinfo:
				showAppInfo(req, resp);
				break;
			case unanchored:
				showUnanchored(req, resp);
				break;
			case users:
				showUsers(req, resp);
				break;
			case user:
				showUserInfo(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Admin login required by web.xml entry

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case appinfo:
				postAppInfo(req, resp);
				break;
			case users:
				postUserInfo(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private void postAppInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.appinfo, SERVLET_JSP_PATH);

		page.setAttribute("appinfo", train.getAppDataController().get());

		page.setPageTitle("Application Info");

		page.passOffToJsp(req, resp);
	}

	private void showAppInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.appinfo, SERVLET_JSP_PATH);

		page.setAttribute("appinfo", train.getAppDataController().get());

		page.setPageTitle("Application Info");

		page.passOffToJsp(req, resp);
	}
	
	private void showAttendance(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.attendance, SERVLET_JSP_PATH);

		page.setAttribute("absences", train.getAbscencesController().getAll());

		page.setPageTitle("Attendance");

		page.passOffToJsp(req, resp);
	}
	
	private void showUnanchored(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.unanchored, SERVLET_JSP_PATH);

		page.setAttribute("absences", train.getAbscencesController().getUnanchored());

		page.setPageTitle("Unanchored");

		page.passOffToJsp(req, resp);
	}

	private void postUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String netID, strType, firstName, lastName;

		// Grab all the data from the fields
		netID = req.getParameter("NetID");
		strType = req.getParameter("Type");
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");

		User.Type type = User.Type.valueOf(strType);

		UserController uc = DataTrain.getAndStartTrain().getUsersController();

		User localUser = uc.get(netID);

		localUser.setType(type);
		localUser.setFirstName(firstName);
		localUser.setLastName(lastName);

		// TODO May throw validation exceptions
		uc.update(localUser);

		showUsers(req, resp);

	}

	private void showUsers(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.users, SERVLET_JSP_PATH);

		page.setAttribute("users", train.getUsersController().getAll());

		page.passOffToJsp(req, resp);

	}

	private void showUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.user, SERVLET_JSP_PATH);

		// Grab the second part of the url as the user's netid
		String[] parts = req.getPathInfo().split("/");
		User u = null;

		if (parts.length >= 3) {
			String netid = parts[2];
			u = train.getUsersController().get(netid);
		}

		if (u == null)
			page.setAttribute("error_message", "No such user");

		page.setAttribute("user", u);

		page.setPageTitle("User Info");

		page.setAttribute("sections", User.Section.values());

		page.setAttribute("types", User.Type.values());

		page.passOffToJsp(req, resp);
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PageBuilder page = new PageBuilder(Page.index, SERVLET_JSP_PATH);

		page.setPageTitle("Director");

		page.passOffToJsp(req, resp);
	}

}
