package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.controllers.AbsenceController;
import edu.iastate.music.marching.attendance.controllers.AppDataController;
import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class DirectorServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6100206975846317440L;

	public enum Page {
		index, appinfo, attendance, export, forms, unanchored, users, user, stats, info;
	}

	private static final String SERVLET_PATH = "director";

	public static final String INDEX_URL = pageToUrl(
			DirectorServlet.Page.index, SERVLET_PATH);

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
				showAppInfo(req, resp, new LinkedList<String>());
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
			case stats:
				showStats(req, resp);
				break;
			case info:
				showInfo(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private void showStats(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		DataTrain train = DataTrain.getAndStartTrain();
		
		PageBuilder page = new PageBuilder(Page.stats, SERVLET_PATH);

		//example value: train.getAppDataController().get();
		
		Date date = new Date();	
		
		int numAbsences = train.getAbsencesController().get(Absence.Type.Absence).size();
		int numTardy = train.getAbsencesController().get(Absence.Type.Tardy).size();
		int numLeaveEarly = train.getAbsencesController().get(Absence.Type.EarlyCheckOut).size();
		int numStudents = train.getUsersController().get(User.Type.Student).size();
		int numEvents = train.getEventsController().readAll().size(); 
		String avgPresent =  numEvents != 0 ? (numStudents-(numAbsences/numEvents))+"" : "No Recorded Events";
		String avgTardy = numEvents != 0 ? (numTardy/numEvents)+"" : "No Recorded Events";
		String avgLeaveEarly = numEvents != 0 ? (numLeaveEarly/numEvents)+"" : "No Recorded Events";		
		String avgAbsent = numEvents != 0 ? (numAbsences/numEvents)+"" : "No Recorded Events";
		String avgPresentWR = numEvents != 0 ? (numStudents-(numAbsences+numTardy+numLeaveEarly)/numEvents)+"" : "No Recorded Events";
		
		page.setAttribute("date", date);
		
		page.setAttribute("numStudents", numStudents);
		page.setAttribute("numEvents", numEvents);
		page.setAttribute("avgPresentStudents", avgPresent);
		page.setAttribute("avgTardyStudents", avgTardy);
		page.setAttribute("avgAbsentStudents", avgAbsent);
		page.setAttribute("avgPresentStudentsWR", avgPresentWR);
		page.setAttribute("avgLeaveEarly", avgLeaveEarly);

		page.setPageTitle("Statistics");
		
		page.passOffToJsp(req, resp);
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
		AppDataController appDataController = train.getAppDataController();
		AppData data = appDataController.get();
		//get the train
		
		//check if there was a form submitted
			//no->//rebuild and return the page with errors
			//yes->//parse and validate
				//get the item from the controller
				//update the item and save it
				//return the jsp
		boolean validForm = true;
		List<String> errors = new LinkedList<String>();
		String newPass = null;
		List<String> emailList = null;
		if (!ValidationUtil.isPost(req)) {
			validForm = false;
		}
		else
		{
			//All of this emailList checking should be abstracted out probably
			
			//Check and update the emails. Need to use the constructor of LinkedList otherwise
			//Its runtime type is AbstractSequentialList...and basically no methods are supported
			emailList = new LinkedList<String>(Arrays.asList(req.getParameter("hiddenEmails").split("delimit")));
			//If they remove all the emails it adds an empty string to the appdata so we need to remove them
			for (int i = 0; i < emailList.size(); ++i)
			{
				if (emailList.get(i).equals(""))
					emailList.remove(i--);
			}
			if (emailList == null || emailList.size() <= 0) {
				validForm = false;
				errors.add("Invalid Input: There must be at least one valid email. Emails will remain unchanged.");
			}
			else
			{
				data.setTimeWorkedEmails(emailList);
			}
			//Handle the thrown exception
			Date testDate = null;
			try
			{
				testDate = parseDate(req.getParameter("Month"), req.getParameter("Day"), 
						req.getParameter("Year"), req.getParameter("ToHour"), 
						req.getParameter("ToMinute"));
				data.setFormSubmissionCutoff(testDate);
			}
			catch (ValidationExceptions e)
			{
				validForm = false;
				errors.add(e.getMessage());
			}
			//Thrown by Calendar.getTime() if we don't have a valid time
			catch (IllegalArgumentException e)
			{
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}
			
			//Check if they added a new password and then change it
			newPass = req.getParameter("hashedPass");
			if (newPass != null && !newPass.equals(""))
			{
				data.setHashedMobilePassword(newPass);
			}
		}
		if (validForm)
		{
			appDataController.save(data);
		}
		showAppInfo(req, resp, errors);
	}

	private void showAppInfo(HttpServletRequest req, HttpServletResponse resp, List<String> errors)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.appinfo, SERVLET_PATH);
		
		AppData data = train.getAppDataController().get();
		
		Calendar cutoffDate = data.getFormSubmissionCutoff();

		page.setAttribute("appinfo", data);

		page.setAttribute("emails", data.getTimeWorkedEmails());
		
		page.setAttribute("Month", cutoffDate.get(Calendar.MONTH) + 1);
		
		page.setAttribute("Day", cutoffDate.get(Calendar.DATE));
		
		page.setAttribute("ToHour", cutoffDate.get((Calendar.HOUR == 0) ? 12: Calendar.HOUR));
		
		page.setAttribute("ToMinute", cutoffDate.get(Calendar.MINUTE));
		
		page.setAttribute("ToAMPM", (cutoffDate.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM");
		
		//page.setAttribute("cutoffTime", data.getFormSubmissionCutoff());
		page.setAttribute("error_messages", errors);
		page.setPageTitle("Application Info");

		page.passOffToJsp(req, resp);
	}
	
	private void showAttendance(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.attendance, SERVLET_PATH);

		List<User> students = train.getUsersController().get(User.Type.Student);
		List<Event> events = train.getEventsController().readAll();

		AbsenceController ac = train.getAbsencesController();
		
		Map<User, Map<Event, List<Absence>>> absenceMap = new HashMap<User, Map<Event, List<Absence>>>();
		for (User s : students) {
			
			Map<Event, List<Absence>> eventAbsencesMap = new HashMap<Event, List<Absence>>();

			//for each event, create a Map that will contain a list of all Absences for this student AND this event
			for (Event e : events) {
				//HashMap<Event, List<Absence>> eventAbsencesMap = new HashMap<Event,List<Absence>>();
				
				List<Absence> currentEventAbsences = ac.getAll(e,s);
				
				eventAbsencesMap.put(e, currentEventAbsences);
				
			}
			
			absenceMap.put(s, eventAbsencesMap);
			
			
		}

		page.setAttribute("students", students);
		page.setAttribute("absenceMap", absenceMap);
		page.setAttribute("absences", train.getAbsencesController().getAll());
		page.setAttribute("events",events);
		page.setPageTitle("Attendance");

		page.passOffToJsp(req, resp);
	}
	
	private void showUnanchored(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.unanchored, SERVLET_PATH);

		page.setAttribute("absences", train.getAbsencesController().getAll());

		page.setPageTitle("Unanchored");

		page.passOffToJsp(req, resp);
	}

	private void showInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		PageBuilder page = new PageBuilder(Page.info, SERVLET_PATH);

		page.setAttribute("user",
				AuthController.getCurrentUser(req.getSession()));

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

		page.setPageTitle("Director");

		page.passOffToJsp(req, resp);
	}
	
	private Date parseDate(String sMonth, String sDay, String sYear, String hour, String minute) {
		int year = 0, month = 0, day = 0;
		Calendar calendar = Calendar.getInstance(App.getTimeZone());

		// Do validate first and store any problems to this exception
		ValidationExceptions exp = new ValidationExceptions();
		try {
			year = Integer.parseInt(sYear);
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid year, not a number.");
		}
		try {
			month = Integer.parseInt(sMonth);
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid month, not a number.");
		}
		try {
			day = Integer.parseInt(sDay);
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

		
		//JSP only allows valid time numbers
		try
		{
			calendar.set(Calendar.HOUR, Integer.parseInt(hour));
		}
		catch (NumberFormatException e)
		{
			exp.getErrors().add("Invalid hour, not a number");
		}
		
		try
		{
			calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
		}
		catch (NumberFormatException e)
		{
			exp.getErrors().add("Invalid minute, not a number");
		}

		if (exp.getErrors().size() > 0)
			throw exp;

		return calendar.getTime();
	}

}
