package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.FormController;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.Message;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class MessageServlet extends AbstractBaseServlet {

	/**
	 * TODO
	 */
//	private static final long serialVersionUID = 

	private enum Page {
		viewthread, inbox;
	}

	private static final String SERVLET_PATH = "message";

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
		case viewthread:
			handleViewThread(req, resp);
			break;
		case inbox:
			handleInbox(req, resp);
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
			case viewthread:
				handleViewThread(req,resp);
				break;
			case inbox:
				handleInbox(req,resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private void handleInbox(HttpServletRequest req, HttpServletResponse resp) {
		// TODO Auto-generated method stub
		
	}

	private void handleViewThread(HttpServletRequest req, HttpServletResponse resp) {

		String text = null;
		String resolved = null;
		DataTrain train = DataTrain.getAndStartTrain();
		boolean validForm = true;
		if (!ValidationUtil.isPost(req)) {
			// This is not a post request to create a new form, no need to
			// validate
			validForm = false;
		} else {
			// Extract all basic parameters
			text = req.getParameter("text");
			resolved = req.getParameter("resolved");
		}
		
		if (validForm) {
			User user = AuthController.getCurrentUser(req.getSession());
			Message message = null;
//			try {
//			train.getMessageThreadController().addMessage(threadid);
//			message = train.getMessageController().createMessage(text, U);
//			}
			//TODO
			//get the messagethread and add a new message to it.
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

		PageBuilder page = new PageBuilder(Page.inbox, SERVLET_PATH);

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
		return pageToUrl(Page.inbox, getInitParameter("path"));
	}

}
