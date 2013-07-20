package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.FormManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.util.PageBuilder;
import edu.iastate.music.marching.attendance.util.Util;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class FormsServlet extends AbstractBaseServlet {

	private static final long serialVersionUID = -4738485557840953301L;

	private static final Logger log = Logger.getLogger(FormsServlet.class
			.getName());

	private enum Page {
		performanceabsence, classconflict, timeworked, index, view, remove;
	}

	private static final String SERVLET_PATH = "form";

	private static final String SUCCESS_PERFORMANCE_ABSENCE_FORM = "Submitted new Performance Absence Form";
	private static final String SUCCESS_CLASS_CONFLICT_FORM = "Submitted new Class Conflict Form";
	private static final String SUCCESS_TIME_WORKED_FORM = "Submitted new Time Worked Form";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl(req));
			return;
		} else if (!isLoggedIn(req, resp, getServletUserTypes())) {
			resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);
		if (page == null) {
			show404(req, resp);
			return;
		}

		switch (page) {
		case performanceabsence:
			handlePerformanceAbsenceForm(req, resp);
			break;
		case classconflict:
			handleClassConflictForm(req, resp);
			break;
		case timeworked:
			handleTimeWorkedForm(req, resp);
			break;
		case index:
			showIndex(req, resp);
			break;
		case view:
			viewForm(req, resp, new LinkedList<String>(), "");
			break;
		// case remove:
		// removeForm(req, resp);
		// break;
		default:
			ErrorServlet.showError(req, resp, 404);
		}
	}

	private void viewForm(HttpServletRequest req, HttpServletResponse resp,
			List<String> errors, String success_message)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		User currentUser = train.getAuthManager().getCurrentUser(
				req.getSession());
		Form form = null;
		try {
			long id = Long.parseLong(req.getParameter("id"));
			form = train.getFormsManager().get(id);

			PageBuilder page = new PageBuilder(Page.view, SERVLET_PATH);

			if (form == null) {
				log.warning("Could not find form number " + id + ".");
				errors.add("Could not find form number " + id + ".");
				page.setAttribute("error_messages", errors);
			} else {
				page.setPageTitle(form.getType().toString() + " Form");
				page.setAttribute("form", form);
				page.setAttribute("day", form.getDayAsString());
				page.setAttribute("isDirector", currentUser.getType()
						.isDirector());

				page.setAttribute("error_messages", errors);
				page.setAttribute("success_message", success_message);
			}
			page.passOffToJsp(req, resp);
		} catch (NumberFormatException nfe) {
			log.warning("Could not parse view form id: "
					+ req.getParameter("id"));
			ErrorServlet.showError(req, resp, 500);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl());
			return;
		} else if (!isLoggedIn(req, resp, getServletUserTypes())) {
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
			case performanceabsence:
				handlePerformanceAbsenceForm(req, resp);
				break;
			case classconflict:
				handleClassConflictForm(req, resp);
				break;
			case timeworked:
				handleTimeWorkedForm(req, resp);
				break;
			case view:
				updateStatus(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}
	}

	private void updateStatus(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		FormManager fc = train.getFormsManager();

		List<String> errors = new LinkedList<String>();
		String success_message = "";
		boolean validForm = true;

		Form f = null;
		Form.Status status = null;
		long id = 0;
		if (!ValidationUtil.isPost(req)) {
			validForm = false;
		}
		if (validForm) {
			try {
				id = Long.parseLong(req.getParameter("id"));
				f = fc.get(id);
				String strStat = req.getParameter("status");
				if (strStat.equalsIgnoreCase("Approved"))
					status = Form.Status.Approved;
				else if (strStat.equalsIgnoreCase("Pending"))
					status = Form.Status.Pending;
				else if (strStat.equalsIgnoreCase("Denied"))
					status = Form.Status.Denied;
				else {
					validForm = false;
					errors.add("Invalid form status.");
				}

			} catch (NumberFormatException e) {
				errors.add("Unable to find form.");
				validForm = false;
			} catch (NullPointerException e) {
				errors.add("Unable to find form.");
				validForm = false;
			}
		}

		if (validForm) {
			// Send them back to their Form page
			f.setStatus(status);
			fc.update(f);
			success_message = "Successfully updated form.";
		}

		viewForm(req, resp, errors, success_message);

	}

	private void handlePerformanceAbsenceForm(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
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
				date = Util.parseNewDate(req.getParameter("startdate"),
						train.getAppDataManager().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}

		if (validForm) {
			// Store our new form to the data store
			User student = train.getAuthManager().getCurrentUser(
					req.getSession());

			Form form = null;
			try {
				form = train.getFormsManager().createPerformanceAbsenceForm(
						student, date, reason);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add(e.getMessage());
			}

			if (form == null) {
				validForm = false;
				errors.add("--------");
				errors.add("If any of the errors above are fixable, please address them and try to resubmit. If you're still having issues, submit a bug report using the form at the bottom of the page.");
			}
		}

		TimeZone timezone = train.getAppDataManager().get().getTimeZone();

		Calendar cutoff = Calendar.getInstance(timezone);
		cutoff.setTime(train.getAppDataManager().get()
				.getFormSubmissionCutoff());

		if (validForm) {

			String success = SUCCESS_PERFORMANCE_ABSENCE_FORM;
			if (!Calendar.getInstance(timezone).before(cutoff)) {
				success = "PLEASE NOTE: This form was submitted after the deadline, so AttendBot 2.0 marked it as late and denied. The director will see this and choose to approve it or leave it as denied.";
			}

			String url = getIndexURL() + "?success_message="
					+ URLEncoder.encode(success, "UTF-8");
			url = resp.encodeRedirectURL(url);

			resp.sendRedirect(url);
		} else {
			// Show form
			PageBuilder page = new PageBuilder(Page.performanceabsence,
					SERVLET_PATH);

			String displayName = Form.Type.PerformanceAbsence.getDisplayName();
			page.setPageTitle(displayName);

			page.setAttribute("error_messages", errors);

			page.setAttribute("cutoff", train.getAppDataManager().get()
					.getFormSubmissionCutoff());

			page.setAttribute("Reason", reason);
			setStartDate(date, page, timezone);
			if (!Calendar.getInstance(timezone).before(cutoff)) {
				errors.add("PLEASE NOTE: The deadline for submitting "
						+ displayName
						+ " has passed. You can still submit one, but it will be marked as late by AttendBot 2.0. Be sure to explain your circumstances when you submit.");
			}

			page.passOffToJsp(req, resp);
		}
	}

	private void handleClassConflictForm(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String department = null;
		String course = null;
		String section = null;
		String building = null;
		Date startDate = null;
		Date endDate = null;
		Date fromTime = null;
		Date toTime = null;
		int day = -1;
		int minutesToOrFrom = 0;
		String comments = null;
		Absence.Type absenceType = null;

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
			comments = req.getParameter("Comments");

			String minutestmp = req.getParameter("MinutesToOrFrom");

			try {
				minutesToOrFrom = Integer.parseInt(minutestmp);
			} catch (NumberFormatException nfe) {
				errors.add("Minutes to or from must be a whole number.");
			}

			String stype = req.getParameter("Type");
			if (stype != null && !stype.equals("")) {
				if (stype.equals(Absence.Type.Absence.getValue())) {
					absenceType = Absence.Type.Absence;
				} else if (stype.equals(Absence.Type.Tardy.getValue())) {
					absenceType = Absence.Type.Tardy;
				} else if (stype.equals(Absence.Type.EarlyCheckOut.getValue())) {
					absenceType = Absence.Type.EarlyCheckOut;
				}
			} else {
				errors.add("Invalid type.");
			}

			// this is one-based! Starting on Sunday.

			try {
				day = Integer.parseInt(req.getParameter("DayOfWeek"));

			} catch (NumberFormatException nfe) {
				errors.add("Weekday was invalid.");
			}

			if (day < 1 || day > 7) {
				errors.add("Value of " + day + " for day was not valid.");
			}

			try {
				startDate = Util.parseNewDate(req.getParameter("startdate"),
						train.getAppDataManager().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("The start date is invalid.");
			}
			try {
				endDate = Util.parseNewDate(req.getParameter("enddate"), train
						.getAppDataManager().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("The end date is invalid.");
			}
			try {
				fromTime = Util.parseTime(req.getParameter("starttime"), train
						.getAppDataManager().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("The start time is invalid.");
			}
			try {
				toTime = Util.parseTime(req.getParameter("endtime"), train
						.getAppDataManager().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("The end time is invalid.");
			}

		}

		if (validForm) {
			// Store our new form to the datastore
			User student = train.getAuthManager().getCurrentUser(
					req.getSession());

			Form form = null;
			try {
				form = train.getFormsManager().createClassConflictForm(student,
						department, course, section, building, startDate,
						endDate, day, fromTime, toTime, comments,
						minutesToOrFrom, absenceType);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add(e.getMessage());
			}

			if (form == null) {
				validForm = false;
				errors.add("Fix any errors above and try to resubmit. If you're still having issues, submit a bug report using the form at the bottom of the page.");
			}
		}
		if (validForm) {
			String url = getIndexURL() + "?success_message="
					+ URLEncoder.encode(SUCCESS_CLASS_CONFLICT_FORM, "UTF-8");
			url = resp.encodeRedirectURL(url);
			resp.sendRedirect(url);
		} else {
			// Show form
			TimeZone timeZone = train.getAppDataManager().get().getTimeZone();
			PageBuilder page = new PageBuilder(Page.classconflict, SERVLET_PATH);

			page.setPageTitle(Form.Type.ClassConflict.getDisplayName());
			page.setAttribute("daysOfWeek", App.getDaysOfTheWeek());
			page.setAttribute("error_messages", errors);
			page.setAttribute("Department", department);
			page.setAttribute("Course", course);
			page.setAttribute("Section", section);
			page.setAttribute("Building", building);
			page.setAttribute("startdate", Util.formatDate(startDate, timeZone));
			page.setAttribute("enddate", Util.formatDate(endDate, timeZone));
			page.setAttribute("Type", Form.Type.ClassConflict);
			page.setAttribute("Comments", comments);
			page.setAttribute("MinutesToOrFrom", minutesToOrFrom);
			page.setAttribute("Type", absenceType);
			page.setAttribute("types", Absence.Type.values());

			if (fromTime != null) {
				page.setAttribute("starttime",
						Util.formatTime(fromTime, timeZone));
			}

			if (toTime != null) {
				page.setAttribute("endtime", Util.formatTime(toTime, timeZone));
			}

			page.passOffToJsp(req, resp);
		}
	}

	private void handleTimeWorkedForm(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
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
			details = req.getParameter("Details");

			try {
				minutes = Integer.parseInt(req.getParameter("AmountWorked"));
			} catch (NumberFormatException e) {
				validForm = false;
				errors.add("Invalid amount of time worked: " + e.getMessage());
			}

			try {
				date = Util.parseNewDate(req.getParameter("startdate"), train
						.getAppDataManager().get().getTimeZone());
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
		}
		User student = train.getAuthManager().getCurrentUser(req.getSession());

		if (validForm) {
			// Store our new form to the data store

			Form form = null;
			try {
				// hash the id for use in the accepting and declining forms
				form = train.getFormsManager().createTimeWorkedForm(student,
						date, minutes, details);
			} catch (IllegalArgumentException e) {
				validForm = false;
				errors.add(e.getMessage());
			}

			if (form == null) {
				validForm = false;
				errors.add("Fix any errors above and try to resubmit. If you're still having issues, submit a bug report using the form at the bottom of the page.");
			}
		}
		if (validForm) {
			String url = getIndexURL() + "?success_message="
					+ URLEncoder.encode(SUCCESS_TIME_WORKED_FORM, "UTF-8");
			url = resp.encodeRedirectURL(url);
			resp.sendRedirect(url);
		} else {
			// Show form
			PageBuilder page = new PageBuilder(Page.timeworked, SERVLET_PATH);

			page.setPageTitle(Form.Type.TimeWorked.getDisplayName());

			page.setAttribute("error_messages", errors);

			page.setAttribute("AmountWorked", minutes);
			page.setAttribute(
					"startdate",
					Util.formatDate(date, train.getAppDataManager().get()
							.getTimeZone()));
			page.setAttribute("Details", details);

			page.passOffToJsp(req, resp);
		}
	}

	// TODO remove this
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
		FormManager fc = train.getFormsManager();

		PageBuilder page = new PageBuilder(Page.index, SERVLET_PATH);

		// Pass through any success message in the url parameters sent from a
		// new form being created or deleted
		page.setAttribute("success_message",
				req.getParameter("success_message"));

		User currentUser = train.getAuthManager().getCurrentUser(
				req.getSession());

		String removeid = req.getParameter("removeid");
		if (removeid != null && removeid != "") {
			try {
				long id = Long.parseLong(removeid);
				if (currentUser.getType() == User.Type.Student
						&& fc.get(id).getStatus() != Form.Status.Pending) {
					page.setAttribute("error_messages",
							"Students cannot delete any non-pending form.");
				} else {
					if (fc.removeForm(id)) {
						page.setAttribute("success_message",
								"Form successfully deleted");
					} else {
						page.setAttribute("error_messages",
								"Form not deleted. If the form was already approved then you can't delete it.");
					}
				}
			} catch (NullPointerException e) {
				page.setAttribute("error_messages", "The form does not exist.");
			}
		}

		// Handle students and director differently
		List<Form> forms = null;
		if (getServletUserType() == User.Type.Student)
			forms = fc.get(currentUser);
		else if (getServletUserType() == User.Type.Director)
			forms = fc.getAll();
		page.setAttribute("forms", forms);

		page.passOffToJsp(req, resp);
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
