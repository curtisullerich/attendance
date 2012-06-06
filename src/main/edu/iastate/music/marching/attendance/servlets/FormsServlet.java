package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.FormController;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.Util;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class FormsServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4738485557840953303L;

	private enum Page {
		forma, formb, formc, formd, index, view, remove, messages;
	}

	private static final String SERVLET_PATH = "form";

	private static final String SUCCESS_FORMA = "Submitted new Form A";
	private static final String SUCCESS_FORMB = "Submitted new Form B";
	private static final String SUCCESS_FORMC = "Submitted new Form C";
	private static final String SUCCESS_FORMD = "Submitted new Form D";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!isLoggedIn(req, resp, getServletUserTypes())) {
			resp.sendRedirect(AuthServlet.URL_LOGIN);
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);
		if (page == null) {
			show404(req, resp);
			return;
		}

		switch (page) {
		case forma:
			postFormA(req, resp);
			break;
		case formb:
			handleFormB(req, resp);
			break;
		case formc:
			handleFormC(req, resp);
			break;
		case formd:
			handleFormD(req, resp);
			break;
		case index:
			showIndex(req, resp);
			break;
		case view:
			viewForm(req, resp);
			break;
		// case remove:
		// removeForm(req, resp);
		// break;
		case messages:
			// TODO
			break;
		default:
			ErrorServlet.showError(req, resp, 404);
		}
	}

	private void viewForm(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		Form form = null;
		try {
			long id = Long.parseLong(req.getParameter("formid"));
			form = train.getFormsController().get(id);
			PageBuilder page = new PageBuilder(Page.view, SERVLET_PATH);
			page.setPageTitle("Form " + form.getType());
			page.setAttribute("form", form);
			page.setAttribute("day", form.getDayAsString());
			page.passOffToJsp(req, resp);
		} catch (NumberFormatException nfe) {
			// TODO show an error?
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!isLoggedIn(req, resp, getServletUserTypes())) {
			resp.sendRedirect(AuthServlet.URL_LOGIN);
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case forma:
				postFormA(req, resp);
				break;
			case formb:
				handleFormB(req, resp);
				break;
			case formc:
				handleFormC(req, resp);
				break;
			case formd:
				handleFormD(req, resp);
				break;
			case messages:
				// TODO
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}
	}

	private void postFormA(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String reason = null;
		Date date = null;

		DataTrain train = DataTrain.getAndStartTrain();

		// Parse out all data from form and validate
		boolean validForm = true;
		List<String> errors = new LinkedList<String>();

		if (!ValidationUtil.isPost(req)) {
			// This is not a post request to create a new form, no need to
			// validate
			validForm = false;
		} else {
			// Extract all basic parameters
			reason = req.getParameter("Reason");

			try {
				date = Util.parseDate(req.getParameter("StartMonth"),
						req.getParameter("StartDay"),
						req.getParameter("StartYear"), "0", "AM", "0", train
								.getAppDataController().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = train.getAuthController().getCurrentUser(req.getSession());

			Form form = null;
			try {
				form = train.getFormsController().createFormA(student, date,
						reason);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add(e.getMessage());
			}

			if (form == null) {
				validForm = false;
				errors.add("Internal Error: Failed to create form and store in database");
			}
		}
		if (validForm) {
			String url = getIndexURL() + "?success_message="
					+ URLEncoder.encode(SUCCESS_FORMA, "UTF-8");
			// url = resp.encodeRedirectURL(url);
			resp.sendRedirect(url);
		} else {
			// Show form
			PageBuilder page = new PageBuilder(Page.forma, SERVLET_PATH);

			page.setPageTitle("Form A");

			page.setAttribute("error_messages", errors);

			page.setAttribute("cutoff", train.getAppDataController().get()
					.getFormSubmissionCutoff());

			page.setAttribute("Reason", reason);
			setStartDate(date, page, train.getAppDataController().get()
					.getTimeZone());

			page.passOffToJsp(req, resp);
		}
	}

	private void handleFormB(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String department = null;
		String course = null;
		String section = null;
		String building = null;
		Date startDate = null;
		Date endDate = null;
		Date fromTime = null;
		Date toTime = null;
		int day = -1;
//		String type = null;
		String comments = null;

		DataTrain train = DataTrain.getAndStartTrain();

		// Parse out all data from form and validate
		boolean validForm = true;
		List<String> errors = new LinkedList<String>();

		if (!ValidationUtil.isPost(req)) {
			// This is not a post request to create a new form, no need to
			// validate
			validForm = false;
		} else {
			// Extract all basic parameters
			department = req.getParameter("Department");
			course = req.getParameter("Course");
			section = req.getParameter("Section");
			building = req.getParameter("Building");
		//	type = req.getParameter("Type");
			comments = req.getParameter("Comments");

			// this is one-based! Starting on Sunday.
			day = Integer.parseInt(req.getParameter("DayOfWeek"));

			try {
				startDate = Util.parseDate(req.getParameter("StartMonth"),
						req.getParameter("StartDay"),
						req.getParameter("StartYear"), "0", "AM", "0", train
								.getAppDataController().get().getTimeZone());
				endDate = Util.parseDate(req.getParameter("EndMonth"),
						req.getParameter("EndDay"),
						req.getParameter("EndYear"), "0", "AM", "0", train
								.getAppDataController().get().getTimeZone());
				fromTime = Util.parseDate("1", "1", "1",
						req.getParameter("FromHour"),
						req.getParameter("FromAMPM"),
						req.getParameter("FromMinute"), train
								.getAppDataController().get().getTimeZone());
				toTime = Util.parseDate("1", "1", "1",
						req.getParameter("ToHour"), req.getParameter("ToAMPM"),
						req.getParameter("ToMinute"), train
								.getAppDataController().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = train.getAuthController().getCurrentUser(req.getSession());

			Form form = null;
			try {
				form = train.getFormsController().createFormB(student,
						department, course, section, building, startDate,
						endDate, day, fromTime, toTime, comments);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add(e.getMessage());
			}

			if (form == null) {
				validForm = false;
				errors.add("Internal Error: Failed to create form and store in database");
			}
		}
		if (validForm) {
			String url = getIndexURL() + "?success_message="
					+ URLEncoder.encode(SUCCESS_FORMB, "UTF-8");
			url = resp.encodeRedirectURL(url);
			resp.sendRedirect(url);
		} else {
			// Show form
			PageBuilder page = new PageBuilder(Page.formb, SERVLET_PATH);

			page.setPageTitle("Form B");
			page.setAttribute("daysOfWeek", App.getDaysOfTheWeek());
			page.setAttribute("error_messages", errors);
			page.setAttribute("Department", department);
			page.setAttribute("Course", course);
			page.setAttribute("Section", section);
			page.setAttribute("Building", building);
			setStartDate(startDate, page, train.getAppDataController().get()
					.getTimeZone());
			setEndDate(endDate, page, train.getAppDataController().get()
					.getTimeZone());
			page.setAttribute("Type", Form.Type.B);
			page.setAttribute("Comments", comments);

			page.passOffToJsp(req, resp);
		}
	}

	private void handleFormC(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Date date = null;
		String reason = null;

		DataTrain train = DataTrain.getAndStartTrain();

		// Parse out all data from form and validate
		boolean validForm = true;
		List<String> errors = new LinkedList<String>();

		if (!ValidationUtil.isPost(req)) {
			// This is not a post request to create a new form, no need to
			// validate
			validForm = false;
		} else {
			// Extract all basic parameters
			reason = req.getParameter("Reason");

			try {
				date = Util.parseDate(req.getParameter("StartMonth"),
						req.getParameter("StartDay"),
						req.getParameter("StartYear"), "0", "AM", "0", train
								.getAppDataController().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = train.getAuthController().getCurrentUser(req.getSession());

			Form form = null;
			try {
				form = train.getFormsController().createFormC(student, date,
						reason);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add(e.getMessage());
			}

			if (form == null) {
				validForm = false;
				errors.add("Internal Error: Failed to create form and store in database");
			}
		}
		if (validForm) {
			String url = getIndexURL() + "?success_message="
					+ URLEncoder.encode(SUCCESS_FORMC, "UTF-8");
			url = resp.encodeRedirectURL(url);
			resp.sendRedirect(url);
		} else {
			// Show form
			PageBuilder page = new PageBuilder(Page.formc, SERVLET_PATH);

			page.setPageTitle("Form C");

			page.setAttribute("error_messages", errors);

			setStartDate(date, page, train.getAppDataController().get()
					.getTimeZone());
			page.setAttribute("Reason", reason);

			page.passOffToJsp(req, resp);
		}
	}

	private void handleFormD(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = null;
		int minutes = 0;
		Date date = null;
		String details = null;

		DataTrain train = DataTrain.getAndStartTrain();

		// Parse out all data from form and validate
		boolean validForm = true;
		List<String> errors = new LinkedList<String>();

		if (!ValidationUtil.isPost(req)) {
			// This is not a post request to create a new form, no need to
			// validate
			validForm = false;
		} else {
			// Extract all basic parameters
			email = req.getParameter("Email");
			details = req.getParameter("Details");

			try {
				minutes = Integer.parseInt(req.getParameter("AmountWorked"));
			} catch (NumberFormatException e) {
				validForm = false;
				errors.add("Invalid amount of hours worked: " + e.getMessage());
			}

			try {
				date = Util.parseDate(req.getParameter("StartMonth"),
						req.getParameter("StartDay"),
						req.getParameter("StartYear"), "0", "AM", "0", train
								.getAppDataController().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = train.getAuthController().getCurrentUser(req.getSession());

			Form form = null;
			try {
				form = train.getFormsController().createFormD(student, email,
						date, minutes, details);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add(e.getMessage());
			}

			if (form == null) {
				validForm = false;
				errors.add("Internal Error: Failed to create form and store in database");
			}
		}
		if (validForm) {
			String url = getIndexURL() + "?success_message="
					+ URLEncoder.encode(SUCCESS_FORMD, "UTF-8");
			url = resp.encodeRedirectURL(url);
			resp.sendRedirect(url);
		} else {
			// Show form
			PageBuilder page = new PageBuilder(Page.formd, SERVLET_PATH);

			page.setPageTitle("Form D");

			page.setAttribute("error_messages", errors);

			page.setAttribute("verifiers", train.getAppDataController().get()
					.getTimeWorkedEmails());

			page.setAttribute("Email", email);
			page.setAttribute("AmountWorked", minutes);
			setStartDate(date, page, train.getAppDataController().get()
					.getTimeZone());
			page.setAttribute("Details", details);

			page.passOffToJsp(req, resp);
		}
	}

	private void setEndDate(Date date, PageBuilder page, TimeZone timezone) {
		if (date == null)
			return;

		Calendar c = Calendar.getInstance(timezone);
		c.setTime(date);
		page.setAttribute("EndYear", c.get(Calendar.YEAR));
		page.setAttribute("EndMonth", c.get(Calendar.MONTH));
		page.setAttribute("EndDay", c.get(Calendar.DATE));
	}

	private void setStartDate(Date date, PageBuilder page, TimeZone timezone) {
		if (date == null)
			return;

		Calendar c = Calendar.getInstance(timezone);
		c.setTime(date);
		page.setAttribute("StartYear", c.get(Calendar.YEAR));
		page.setAttribute("StartMonth", c.get(Calendar.MONTH));
		page.setAttribute("StartDay", c.get(Calendar.DATE));
		page.setAttribute("StartHour", c.get(Calendar.HOUR));
		page.setAttribute("StartMinute", c.get(Calendar.MINUTE));
		page.setAttribute("StartPeriod", c.get(Calendar.AM_PM));
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();
		FormController fc = train.getFormsController();

		PageBuilder page = new PageBuilder(Page.index, SERVLET_PATH);

		// Pass through any success message in the url parameters sent from a
		// new form being created or deleted
		page.setAttribute("success_message",
				req.getParameter("success_message"));

		String removeid = req.getParameter("removeid");
		if (removeid != null && removeid != "") {
			long id = Long.parseLong(removeid);
			if (fc.removeForm(id)) {
				page.setAttribute("success_message",
						"Form successfully deleted");
			} else {
				page.setAttribute("error_messages",
						"Form not deleted. If the form was already approved then you can't delete it.");
				// TODO you might be able to. Check with staub
			}
		}

		User currentUser = train.getAuthController().getCurrentUser(req.getSession());

		// HACK: @Daniel
		currentUser = train.getUsersController().get(currentUser.getNetID());

		// Handle students and director differently
		List<Form> forms = null;
		if (getServletUserType() == User.Type.Student)
			forms = fc.get(currentUser);
		else if (getServletUserType() == User.Type.Director)
			forms = fc.getAll();
		page.setAttribute("forms", forms);

		page.passOffToJsp(req, resp);
	}

	private void removeForm(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub

	}

	/**
	 * This servlet can be used for multiple user types, this grabs the type of
	 * user this specific servlet instance is for
	 * 
	 * @return
	 */
	private User.Type getServletUserType() {
		String value = getInitParameter("userType");

		return User.Type.valueOf(value);
	}

	/**
	 * This servlet can be used for multiple user types, this grabs the type of
	 * user this specific servlet instance can be accessed by
	 * 
	 * @return
	 */
	private User.Type[] getServletUserTypes() {
		User.Type userType = getServletUserType();

		// Since a TA is also a student, we also allow them access to their
		// student forms
		if (userType == User.Type.Student)
			return new User.Type[] { User.Type.Student, User.Type.TA };
		else
			return new User.Type[] { userType };
	}

	/**
	 * Have to use a method since this servlet can be mapped from different
	 * paths
	 */
	private String getIndexURL() {
		return pageToUrl(Page.index, getInitParameter("path"));
	}

}
