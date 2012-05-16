package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.FormController;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.User;

public class FormsServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4738485557840953303L;

	private enum Page {
		forma, formb, formc, formd, index, view, remove, messages;
	}

	private static final String SERVLET_PATH = "form";

	private static final String URL_INDEX = pageToUrl(Page.index, SERVLET_PATH);

	private static final String SUCCESS_FORMA = "Submitted new Form A";
	private static final String SUCCESS_FORMB = "Submitted new Form B";
	private static final String SUCCESS_FORMC = "Submitted new Form C";
	private static final String SUCCESS_FORMD = "Submitted new Form D";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!isLoggedIn(req, resp, getServletUserType())) {
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
		case remove:
			removeForm(req, resp);
			break;
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
		if (!isLoggedIn(req, resp, getServletUserType())) {
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

		if (!formSubmitted(req)) {
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
				errors.add(e.getMessage());
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
			String url = URLEncoder.encode(URL_INDEX + "?success_message="
					+ SUCCESS_FORMA, "UTF-8");
			url = resp.encodeRedirectURL(url);
			resp.sendRedirect(url);
		} else {
			// Show form
			PageBuilder page = new PageBuilder(Page.forma, SERVLET_PATH);

			page.setPageTitle("Form A");
			
			page.setAttribute("error_messages", errors);

			page.setAttribute("cutoff", train.getAppDataController().get()
					.getFormSubmissionCutoff());
			
			page.setAttribute("Reason", reason);

			page.passOffToJsp(req, resp);
		}
	}

	private boolean formSubmitted(HttpServletRequest req) {
		return "POST".equals(req.getMethod());
	}

	private Date parseStartDate(HttpServletRequest req) {
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

		if (!formSubmitted(req)) {
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
				errors.add(e.getMessage());
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
			String url = URLEncoder.encode(URL_INDEX + "?success_message="
					+ SUCCESS_FORMB, "UTF-8");
			url = resp.encodeRedirectURL(url);
			resp.sendRedirect(url);
		} else {
			// Show form
			PageBuilder page = new PageBuilder(Page.formb, SERVLET_PATH);

			page.setPageTitle("Form B");
			
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

		if (!formSubmitted(req)) {
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
				errors.add(e.getMessage());
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
			String url = URLEncoder.encode(URL_INDEX + "?success_message="
					+ SUCCESS_FORMC, "UTF-8");
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

		if (!formSubmitted(req)) {
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
				errors.add(e.getMessage());
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = AuthController.getCurrentUser(req.getSession());

			Form form = null;
			try {
				form = train.getFormsController().createFormD(student, date,
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
			String url = URLEncoder.encode(URL_INDEX + "?success_message="
					+ SUCCESS_FORMD, "UTF-8");
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
		// TODO Auto-generated method stub

	}

	private void setStartDate(Date date, PageBuilder page) {
		// TODO Auto-generated method stub

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

		// Handle students and director differently
		List<Form> forms = null;
		if (getServletUserType() == User.Type.Student)
			forms = fc.get(AuthController.getCurrentUser(req.getSession()));
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

}
