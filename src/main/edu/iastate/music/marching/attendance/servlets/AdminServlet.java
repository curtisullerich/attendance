package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Lists;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.AttendanceDatastore;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.model.migration.MigrationException;
import edu.iastate.music.marching.attendance.util.PageBuilder;

public class AdminServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6636386568039228284L;

	private static final String SERVLET_PATH = "admin";

	public static final String INDEX_URL = pageToUrl(Page.index, SERVLET_PATH);

	private static final String CONTENT_TYPE_JSON = "application/json";

	private static final Logger log = Logger.getLogger(AdminServlet.class
			.getName());

	private enum Page {
		index, users, user, data, export, load, data_migrate, data_delete
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl(req));
			return;
		} else if (!(isLoggedIn(req, resp, User.Type.Director) || AuthController
				.isAdminLoggedIn())) {
			resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
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
			case users:
				showUsers(req, resp);
				break;
			case user:
				showUserInfo(req, resp);
				break;
			case export:
				downloadExportData(req, resp);
				break;
			case data:
				showDataPage(req, resp);
				break;
			case data_migrate:
				showDataMigratePage(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl(req));
			return;
		} else if (!(isLoggedIn(req, resp, User.Type.Director) || AuthController
				.isAdminLoggedIn())) {
			resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case users:
				postUserInfo(req, resp);
				break;
			case load:
				postImportData(req, resp);
				break;
			case data_migrate:
				doDataMigration(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private void postUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String netID, strType, firstName, lastName;

		DataTrain train = DataTrain.getAndStartTrain();

		// Grab all the data from the fields
		netID = req.getParameter("NetID");
		strType = req.getParameter("Type");
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");

		User.Type type = User.Type.valueOf(strType);

		UserController uc = train.getUsersController();

		User localUser = uc.get(netID);

		localUser.setType(type);
		localUser.setFirstName(firstName);
		localUser.setLastName(lastName);

		// TODO May throw validation exceptions
		uc.update(localUser);

		// Update user in session if we just changed the currently logged in
		// user
		if (localUser.equals(train.getAuthController().getCurrentUser(
				req.getSession())))
			AuthController.updateCurrentUser(localUser, req.getSession());

		showUsers(req, resp);

	}

	private void showUsers(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.users, SERVLET_PATH);

		page.setAttribute("users", train.getUsersController().getAll());

		page.passOffToJsp(req, resp);

	}

	private void showUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.user, SERVLET_PATH);

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

		PageBuilder page = new PageBuilder(Page.index, SERVLET_PATH);

		page.setPageTitle("Users");

		page.passOffToJsp(req, resp);
	}

	private void showDataPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.data, SERVLET_PATH);

		page.setPageTitle("Data Export/Import/Update");

		page.setAttribute("DatastoreVersions",
				Lists.reverse(train.getVersionController().getAll()));
		page.setAttribute("ObjectDatastoreVersion", AttendanceDatastore.VERSION);

		page.passOffToJsp(req, resp);
	}

	private void showDataMigratePage(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		PageBuilder page = new PageBuilder(Page.data_migrate, SERVLET_PATH);

		page.setPageTitle("Data Migration");

		page.setAttribute("version", req.getParameter("version"));

		page.setAttribute("ObjectDatastoreVersion", AttendanceDatastore.VERSION);

		page.passOffToJsp(req, resp);
	}

	private void doDataMigration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		int fromVersion = new Integer(req.getParameter("version"));

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.data_migrate, SERVLET_PATH);

		try {
			train.getMigrationController().upgrade(fromVersion);
			
			page.setAttribute("success_message",
					"Successfully migrated data from version " + fromVersion);
		} catch (MigrationException e) {
			log.log(Level.WARNING, "Could not migrate from version "
					+ fromVersion, e);
			page.setAttribute("error_messages", new String[] { e.toString() });
		}

		page.setPageTitle("Data Migration");

		page.setAttribute("version", req.getParameter("version"));

		page.passOffToJsp(req, resp);
	}

	private void downloadExportData(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType(CONTENT_TYPE_JSON);

		DataTrain train = DataTrain.getAndStartTrain();

		OutputStreamWriter osw = new OutputStreamWriter(resp.getOutputStream());

		train.getDataController().dumpDatabaseAsJSON(osw);

		osw.flush();
	}

	private void postImportData(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PageBuilder page = new PageBuilder(Page.data, SERVLET_PATH);

		page.setPageTitle("Data Export/Import");

		page.passOffToJsp(req, resp);
	}
}
