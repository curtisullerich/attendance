package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.model.User.Section;
import edu.iastate.music.marching.attendance.util.PageBuilder;

public class StudentServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6636386568039228284L;
	
	private static final Logger LOG = Logger.getLogger(StudentServlet.class.getName());

	private static final String SERVLET_PATH = "student";

	public static final String INDEX_URL = pageToUrl(Page.index, SERVLET_PATH);

	private enum Page {
		index, attendance, forms, messages, info;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl(req));
			return;
		} else if (!isLoggedIn(req, resp, User.Type.Student, User.Type.TA)) {
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
			case attendance:
				showAttendance(req, resp);
				break;
			case forms:
				break;
			case messages:
				showIndex(req, resp);
				break;
			case info:
				showInfo(req, resp, null, null);
				break;
			}
	}

	private void showAttendance(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.attendance, SERVLET_PATH);

		User currentUser = train.getAuthController().getCurrentUser(req.getSession());

		page.setPageTitle("Attendance");

		List<Absence> a = train.getAbsenceController().get(currentUser);

		page.setAttribute("user", currentUser);
		page.setAttribute("forms", train.getFormsController().get(currentUser));
		page.setAttribute("absences", a);

		page.passOffToJsp(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl());
			return;
		} else if (!isLoggedIn(req, resp, User.Type.TA, User.Type.Student)) {
			resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
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
		
		DataTrain train = DataTrain.getAndStartTrain();
		
		List<String> errors = new ArrayList<String>();
		String success = "";

		String firstName, lastName, major;
		int year = -1;
		User.Section section = null;
		Email secondEmail;

		// Grab all the data from the fields
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");
		major = req.getParameter("Major");
		secondEmail = new Email(req.getParameter("SecondEmail"));

		String sectionString = req.getParameter("Section");
		for (Section s : User.Section.values()) {
			if (sectionString.equals(s.name())) {
				section = s;
			}
		}
		
		User localUser = train.getAuthController().getCurrentUser(req.getSession());
		
		try {
			year = Integer.parseInt(req.getParameter("Year"));
			localUser.setYear(year);
		} catch (NumberFormatException nfe) {
			LOG.severe(nfe.getStackTrace().toString());
			LOG.severe(nfe.getMessage());
			errors.add("Unable to save year.");
		}

		UserController uc = train.getUsersController();

		localUser.setMajor(major);
		localUser.setSection(section);
		localUser.setFirstName(firstName);
		localUser.setLastName(lastName);
		localUser.setSecondaryEmail(secondEmail);

		//May throw validation exceptions
		try {
			uc.update(localUser);
			success = (errors.size() == 0) ? "Successfully saved all data." : 
				"Successfully saved all data but the year.";
		}
		catch (IllegalArgumentException e) {
			errors.add("Unable to save "+ ((errors.size() == 0) ? "data that wasn't the year." : "any data."));
		}
		AuthController.updateCurrentUser(localUser, req.getSession());

		showInfo(req, resp, errors, success);

	}

	private void showInfo(HttpServletRequest req, HttpServletResponse resp, List<String> errors, String succex)
			throws IOException, ServletException {

		PageBuilder page = new PageBuilder(Page.info, SERVLET_PATH);

		page.setAttribute("user",
				DataTrain.getAndStartTrain().getAuthController().getCurrentUser(req.getSession()));

		page.setAttribute("sections", User.Section.values());
		
		page.setAttribute("error_messages", errors);
		
		page.setAttribute("success_message", succex);

		page.passOffToJsp(req, resp);
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PageBuilder page = new PageBuilder(Page.index, SERVLET_PATH);
		DataTrain train = DataTrain.getAndStartTrain();
		User currentUser = train.getAuthController().getCurrentUser(req.getSession());

		page.setAttribute("user", currentUser);
		page.setPageTitle("Student");
		page.setAttribute("StatusMessage", DataTrain.getAndStartTrain().getAppDataController().get().getStatusMessage());

		page.passOffToJsp(req, resp);
	}

}
