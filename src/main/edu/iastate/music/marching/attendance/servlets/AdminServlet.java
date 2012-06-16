package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.PageBuilder;

public class AdminServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6636386568039228284L;

	private static final String SERVLET_PATH = "admin";

	public static final String INDEX_URL = pageToUrl(Page.index, SERVLET_PATH);

	private enum Page {
		index, users, user;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Admin login required by web.xml entry

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
			case users:
				postUserInfo(req, resp);
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
		
		// Update user in session if we just changed the currently logged in user
		if(localUser.equals(train.getAuthController().getCurrentUser(req.getSession())))
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

	/**
	 * Parses path info as: /key1/value1/key2/value2
	 * 
	 * @param pathInfo
	 * @return Map of keys to values
	 */
	private Map<String, String> parsePathInfoParameters(String pathInfo) {

		Map<String, String> map = new HashMap<String, String>();

		if (pathInfo == null)
			return map;

		String[] parts = pathInfo.split("/");

		for (int i = 2; i <= parts.length; i += 2) {
			String key = parts[i - 1];
			String value = parts[i];

			map.put(key, value);
		}

		// Could end up with an odd part that is not put in the map
		// map.put(null, parts.length-1)

		return map;
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PageBuilder page = new PageBuilder(Page.index, SERVLET_PATH);

		page.setPageTitle("Users");

		page.passOffToJsp(req, resp);
	}

}
