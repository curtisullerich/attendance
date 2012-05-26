package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.FormController;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;
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
			handleFormA(req, resp);
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
			// TODO
			break;
//		case remove:
//			removeForm(req, resp);
//			break;
		case messages:
			// TODO
			break;
		default:
			ErrorServlet.showError(req, resp, 404);
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
				handleFormA(req, resp);
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

	private void handleFormA(HttpServletRequest req, HttpServletResponse resp)
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
				date = parseStartDate(req);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = AuthController.getCurrentUser(req.getSession());

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
					.getFormSubmissionCutoff().getTime());

			page.setAttribute("Reason", reason);
			setStartDate(date, page);

			page.passOffToJsp(req, resp);
		}
	}

	private Date parseStartDate(HttpServletRequest req) {
		int year = 0, month = 0, day = 0;
		Calendar calendar = Calendar.getInstance(App.getTimeZone());

		// Do validate first and store any problems to this exception
		ValidationExceptions exp = new ValidationExceptions();

		try {
			year = Integer.parseInt(req.getParameter("StartYear"));
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid year, not a number.");
		}
		try {
			month = Integer.parseInt(req.getParameter("StartMonth"));
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid month, not a number.");
		}
		try {
			day = Integer.parseInt(req.getParameter("StartDay"));
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid day, not a number.");
		}

		calendar.setTimeInMillis(0);
		calendar.setLenient(false);

		try {
			calendar.set(Calendar.YEAR, year);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid year given:" + e.getMessage() + '.');
		}
		try {
			calendar.set(Calendar.MONTH, month-1);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid month given:" + e.getMessage() + '.');
		}
		try {
			calendar.set(Calendar.DATE, day);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid day given:" + e.getMessage() + '.');
		}

		if (exp.getErrors().size() > 0)
			throw exp;

		return calendar.getTime();
	}

	private Date parseStartDateTime(HttpServletRequest req, Date date) {
		int hour = 0, minute = 0, timeofday = 0;

		// Do validate first and store any problems to this exception
		ValidationExceptions exp = new ValidationExceptions();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseStartDate(req));

		try {
			hour = Integer.parseInt(req.getParameter("StartHour"));
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid hour, not a number");
		}
		try {
			minute = Integer.parseInt(req.getParameter("StartMinute"));
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid minute, not a number");
		}

		if (req.getParameter("StartPeriod") == null)
			exp.getErrors().add("Time of day (AM/PM) not specified");
		else if ("AM".equals(req.getParameter("StartPeriod").toUpperCase()))
			timeofday = Calendar.AM;
		else if ("PM".equals(req.getParameter("StartPeriod").toUpperCase()))
			timeofday = Calendar.PM;
		else
			exp.getErrors().add("Invalid time of day (AM/PM)");

		calendar.setLenient(false);

		try {
			calendar.set(Calendar.HOUR, hour);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid year given:" + e.getMessage());
		}
		try {
			calendar.set(Calendar.MINUTE, minute);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid month given:" + e.getMessage());
		}
		try {
			calendar.set(Calendar.AM_PM, timeofday);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid time of day given:" + e.getMessage());
		}

		if (exp.getErrors().size() > 0)
			throw exp;

		return calendar.getTime();
	}

	private Date parseEndDate(HttpServletRequest req) {
		// TODO Auto-generated method stub

		//
		// else if (req.getParameter("StartDay") != null &&
		// req.getParameter("StartMonth") != null &&
		// req.getParameter("StartYear") != null
		// && req.getParameter("StartDay") != "" &&
		// req.getParameter("StartMonth") != "" && req.getParameter("StartYear")
		// != "" ) {
		//
		// int year = Integer.parseInt(req.getParameter("StartYear"));
		// int month = Integer.parseInt(req.getParameter("StartMonth"));
		// int day = Integer.parseInt(req.getParameter("StartDay"));
		// if(!isValidateDate(month, day, year)) {
		// resp.sendRedirect("/JSPPages/Student_Form_A_Performance_Absence_Request.jsp?error='invalidDate'");
		// return;
		// }
		// //public Date(int year, int month, int day)
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTimeInMillis(0);
		// calendar.set(year, month, day);
		//
		// // Start at beginning of day
		// Date start = calendar.getTime();
		//
		// // End exactly one time unit before the next day starts
		// calendar.roll(Calendar.DATE, true)
		// calendar.roll(Calendar.MILLISECOND, false);
		// Date end = calendar.getTime();
		return null;
	}

	private void handleFormB(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String department = null;
		String course = null;
		String section = null;
		String building = null;
		Date startDate = null;
		Date endDate = null;
		String type = null;
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
			type = req.getParameter("Type");
			comments = req.getParameter("Comments");

			try {
				startDate = parseStartDate(req);
				endDate = parseEndDate(req);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = AuthController.getCurrentUser(req.getSession());

			Form form = null;
			try {
				// form = train.getFormsController().createFormA(student,
				// startDate,
				// comments);
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
			setStartDate(startDate, page);
			setEndDate(endDate, page);
			page.setAttribute("Type", type);
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
				date = parseStartDate(req);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = AuthController.getCurrentUser(req.getSession());

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

			setStartDate(date, page);
			page.setAttribute("Reason", reason);

			page.passOffToJsp(req, resp);
		}
	}

	private void handleFormD(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String email = null;
		int hours = 0;
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
				hours = Integer.parseInt(req.getParameter("AmountWorked"));
			} catch (NumberFormatException e) {
				validForm = false;
				errors.add("Invalid amount of hours worked: " + e.getMessage());
			}

			try {
				date = parseStartDate(req);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = AuthController.getCurrentUser(req.getSession());

			Form form = null;
			try {
				form = train.getFormsController().createFormD(student, email, date,
						hours, details);
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
			page.setAttribute("AmountWorked", hours);
			setStartDate(date, page);
			page.setAttribute("Details", details);

			page.passOffToJsp(req, resp);
		}
	}

	private void setEndDate(Date date, PageBuilder page) {
		if (date == null)
			return;

		Calendar c = Calendar.getInstance(App.getTimeZone());
		c.setTime(date);
		page.setAttribute("EndYear", c.get(Calendar.YEAR));
		page.setAttribute("EndMonth", c.get(Calendar.MONTH));
		page.setAttribute("EndDay", c.get(Calendar.DATE));
	}

	private void setStartDate(Date date, PageBuilder page) {
		if (date == null)
			return;

		Calendar c = Calendar.getInstance(App.getTimeZone());
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
				page.setAttribute("success_message","Form successfully deleted");
			} else {
				page.setAttribute("error_messages", "Form not deleted. If the form was already approved then you can't delete it.");
				//TODO you might be able to. Check with staub
			}
		}
		
		
		User currentUser = AuthController.getCurrentUser(req.getSession());

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
