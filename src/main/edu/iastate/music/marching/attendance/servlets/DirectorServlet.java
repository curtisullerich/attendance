package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
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

import edu.iastate.music.marching.attendance.controllers.AbsenceController;
import edu.iastate.music.marching.attendance.controllers.AppDataController;
import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.Util;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class DirectorServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6100206975846317440L;

	public enum Page {
		index, appinfo, attendance, export, forms, unanchored, users, user, stats, info, viewabsence;
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
			case viewabsence:
				viewAbsence(req, resp, new LinkedList<String>());
				break;
			case attendance:
				showAttendance(req, resp, new LinkedList<String>());
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

		// example value: train.getAppDataController().get();

		Date date = new Date();

		int numAbsences = train.getAbsencesController()
				.get(Absence.Type.Absence).size();
		int numTardy = train.getAbsencesController().get(Absence.Type.Tardy)
				.size();
		int numLeaveEarly = train.getAbsencesController()
				.get(Absence.Type.EarlyCheckOut).size();
		int numStudents = train.getUsersController().get(User.Type.Student)
				.size();
		int numEvents = train.getEventsController().readAll().size();
		String avgPresent = numEvents != 0 ? (numStudents - (numAbsences / numEvents))
				+ ""
				: "No Recorded Events";
		String avgTardy = numEvents != 0 ? (numTardy / numEvents) + ""
				: "No Recorded Events";
		String avgLeaveEarly = numEvents != 0 ? (numLeaveEarly / numEvents)
				+ "" : "No Recorded Events";
		String avgAbsent = numEvents != 0 ? (numAbsences / numEvents) + ""
				: "No Recorded Events";
		String avgPresentWR = numEvents != 0 ? (numStudents - (numAbsences
				+ numTardy + numLeaveEarly)
				/ numEvents)
				+ "" : "No Recorded Events";
		
		
		
		page.setAttribute("date", date);

		page.setAttribute("numStudents", numStudents);
		page.setAttribute("numEvents", numEvents);
		page.setAttribute("avgPresentStudents", avgPresent);
		page.setAttribute("avgTardyStudents", avgTardy);
		page.setAttribute("avgAbsentStudents", avgAbsent);
		page.setAttribute("avgPresentStudentsWR", avgPresentWR);
		page.setAttribute("avgLeaveEarly", avgLeaveEarly);
		page.setAttribute("avgGrade",train.getUsersController().averageGrade());

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
			case viewabsence:
				postAbsenceInfo(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private void postAbsenceInfo(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		AbsenceController ac = train.getAbsencesController();

		boolean validForm = true;

		List<String> errors = new LinkedList<String>();

		if (!ValidationUtil.isPost(req)) {
			validForm = false;
		} else {
			String absid = req.getParameter("absenceid");
			long id;
			Absence toUpdate = null;
			if (absid != null) {
				id = Long.parseLong(absid);
				toUpdate = ac.get(id);
				if (toUpdate != null) {
					String type = req.getParameter("Type");
					String status = req.getParameter("Status");

					try {
						toUpdate.setDatetime(Util.parseDate(
								req.getParameter("StartMonth"),
								req.getParameter("StartDay"),
								req.getParameter("StartYear"),
								req.getParameter("StartHour"),
								req.getParameter("StartAMPM"),
								req.getParameter("StartMinute"), train
										.getAppDataController().get()
										.getTimeZone()));
						toUpdate.setType(Absence.Type.valueOf(type));
						toUpdate.setStatus(Absence.Status.valueOf(status));
					} catch (ValidationExceptions e) {
						validForm = false;
						errors.add("Invalid Input: The input date was invalid.");
					} catch (IllegalArgumentException e) {
						validForm = false;
						errors.add("Invalid Input: The input date is invalid.");
					}
				} else {
					validForm = false;
					errors.add("Could not find the Absence to update");
				}
			} else {
				validForm = false;
				errors.add("Could not find the Absence to update");
			}

			if (validForm) {
				// How about update the absence huh?
				ac.updateAbsence(toUpdate);
				showAttendance(req, resp, null);
			} else {
				viewAbsence(req, resp, errors);
			}
		}
	}

	private void postAppInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		AppDataController appDataController = train.getAppDataController();
		AppData data = appDataController.get();
		// get the train

		// check if there was a form submitted
		// no->//rebuild and return the page with errors
		// yes->//parse and validate
		// get the item from the controller
		// update the item and save it
		// return the jsp
		boolean validForm = true;
		List<String> errors = new LinkedList<String>();
		String newPass = null;
		List<String> emailList = null;
		if (!ValidationUtil.isPost(req)) {
			validForm = false;
		} else {
			// All of this emailList checking should be abstracted out probably

			// Check and update the emails. Need to use the constructor of
			// LinkedList otherwise
			// Its runtime type is AbstractSequentialList...and basically no
			// methods are supported
			emailList = new LinkedList<String>(Arrays.asList(req.getParameter(
					"hiddenEmails").split("delimit")));
			// If they remove all the emails it adds an empty string to the
			// appdata so we need to remove them
			for (int i = 0; i < emailList.size(); ++i) {
				if (emailList.get(i).equals(""))
					emailList.remove(i--);
			}
			if (emailList == null || emailList.size() <= 0) {
				validForm = false;
				errors.add("Invalid Input: There must be at least one valid email. Emails will remain unchanged.");
			} else {
				data.setTimeWorkedEmails(emailList);
			}
			// Handle the thrown exception
			Date testDate = null;
			try {
				testDate = Util.parseDate(req.getParameter("Month"),
						req.getParameter("Day"), req.getParameter("Year"),
						req.getParameter("ToHour"), req.getParameter("ToAMPM"),
						req.getParameter("ToMinute"), train
								.getAppDataController().get().getTimeZone());
				data.setFormSubmissionCutoff(testDate);
			} catch (ValidationExceptions e) {
				validForm = false;
				errors.add(e.getMessage());
			}
			// Thrown by Calendar.getTime() if we don't have a valid time
			catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input date is invalid.");
			}

			// Check if they added a new password and then change it
			newPass = req.getParameter("hashedPass");
			if (newPass != null && !newPass.equals("")) {
				data.setHashedMobilePassword(newPass);
			}
		}
		if (validForm) {
			appDataController.save(data);
		}
		showAppInfo(req, resp, errors);
	}

	private void showAppInfo(HttpServletRequest req, HttpServletResponse resp,
			List<String> errors) throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.appinfo, SERVLET_PATH);

		AppData data = train.getAppDataController().get();

		Calendar cutoffDate = Calendar.getInstance(data.getTimeZone());
		cutoffDate.setTime(data.getFormSubmissionCutoff());

		page.setAttribute("appinfo", data);

		page.setAttribute("emails", data.getTimeWorkedEmails());

		page.setAttribute("Month", cutoffDate.get(Calendar.MONTH) + 1);

		page.setAttribute("Day", cutoffDate.get(Calendar.DATE));

		page.setAttribute("ToHour",
				cutoffDate.get((cutoffDate.get(Calendar.HOUR) == 0) ? 12 : cutoffDate.get(Calendar.HOUR)));

		page.setAttribute("ToMinute", cutoffDate.get(Calendar.MINUTE));

		page.setAttribute("ToAMPM",
				(cutoffDate.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM");

		// page.setAttribute("cutoffTime", data.getFormSubmissionCutoff());
		page.setAttribute("error_messages", errors);
		page.setPageTitle("Application Info");

		page.passOffToJsp(req, resp);
	}

	private void showAttendance(HttpServletRequest req,
			HttpServletResponse resp, List<String> errors)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.attendance, SERVLET_PATH);

		List<User> students = train.getUsersController().get(User.Type.Student);
		List<Event> events = train.getEventsController().readAll();

		AbsenceController ac = train.getAbsencesController();

		Map<User, Map<Event, List<Absence>>> absenceMap = new HashMap<User, Map<Event, List<Absence>>>();
		for (User s : students) {

			Map<Event, List<Absence>> eventAbsencesMap = new HashMap<Event, List<Absence>>();

			// for each event, create a Map that will contain a list of all
			// Absences for this student AND this event
			for (Event e : events) {
				// HashMap<Event, List<Absence>> eventAbsencesMap = new
				// HashMap<Event,List<Absence>>();

				List<Absence> currentEventAbsences = ac.getAll(e, s);

				eventAbsencesMap.put(e, currentEventAbsences);

			}

			absenceMap.put(s, eventAbsencesMap);

		}

		page.setAttribute("students", students);
		page.setAttribute("absenceMap", absenceMap);
		page.setAttribute("absences", train.getAbsencesController().getAll());
		page.setAttribute("events", events);
		page.setPageTitle("Attendance");
		page.setAttribute("error_messages", errors);
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

	private void viewAbsence(HttpServletRequest req, HttpServletResponse resp,
			List<String> incomingErrors) throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		AbsenceController ac = train.getAbsencesController();

		boolean validInput = true;

		List<String> errors = new LinkedList<String>();

		String urlParam = req.getParameter("absenceid");

		Absence checkedAbsence = null;

		long absenceid;

		if (urlParam != null) {
			absenceid = Long.parseLong(urlParam);
			checkedAbsence = ac.get(absenceid);
		} else {
			validInput = false;
			errors.add("No absence to look for");
		}

		// Shouldn't ever happen since the id is sent by us from the jsp
		if (checkedAbsence == null) {
			validInput = false;
			errors.add("Could not find the absence.");
		}

		if (validInput) {
			PageBuilder page = new PageBuilder(Page.viewabsence, SERVLET_PATH);
			page.setPageTitle("View Absence");
			page.setAttribute("absence", checkedAbsence);
			page.setAttribute("types", Absence.Type.values());
			page.setAttribute("status", Absence.Status.values());
			page.setAttribute("error_messages", incomingErrors);
			page.passOffToJsp(req, resp);
		} else {
			showAttendance(req, resp, errors);
		}
	}

}
