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

	private static final String SERVLET_PAGE = "form";
	private static final int SUCCESS_FORMA = 0;
	private static final String URL_INDEX = null;

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

		//
		// //Form( netID, reason, startTime, endTime, type)
		// Form myform= new
		// Form(""+req.getSession().getAttribute("user"),reason,startTime,endTime,
		// "FormA" );
		//
		// DatabaseUtil.addForm(myform);
		//
		// resp.sendRedirect("/JSPPages/Student_Page.jsp?formSubmitted='true'");
		// return;
		// }
		// else
		// {
		// resp.sendRedirect("/JSPPages/Student_Form_A_Performance_Absence_Request.jsp?error='nullFields'");
		// return;
		// }

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

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(parseStartDate(req));
			date = calendar.getTime();
		}

		if (validForm) {
			// Create our form
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
			PageBuilder page = new PageBuilder(Page.forma, SERVLET_PAGE);

			page.setPageTitle("Form A");

			page.setAttribute("cutoff", train.getAppDataController().get()
					.getFormSubmissionCutoff());

			page.passOffToJsp(req, resp);
		}
	}

	private boolean formSubmitted(HttpServletRequest req) {
		return "POST".equals(req.getMethod())
				&& req.getParameter("Submit") != null;
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

	private void handleFormB(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		new PageBuilder(Page.formb, SERVLET_PAGE).passOffToJsp(req, resp);
	}

	private void handleFormC(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		new PageBuilder(Page.formc, SERVLET_PAGE).passOffToJsp(req, resp);
	}

	private void handleFormD(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		new PageBuilder(Page.formd, SERVLET_PAGE).passOffToJsp(req, resp);
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();
		FormController fc = train.getFormsController();

		PageBuilder page = new PageBuilder(Page.index, SERVLET_PAGE);

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
