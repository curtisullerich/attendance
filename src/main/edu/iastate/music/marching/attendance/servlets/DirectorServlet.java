package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.controllers.AbsenceController;
import edu.iastate.music.marching.attendance.controllers.AppDataController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.EventController;
import edu.iastate.music.marching.attendance.controllers.FormController;
import edu.iastate.music.marching.attendance.controllers.MessagingController;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.model.User.Section;
import edu.iastate.music.marching.attendance.util.GradeExport;
import edu.iastate.music.marching.attendance.util.PageBuilder;
import edu.iastate.music.marching.attendance.util.Util;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class DirectorServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6100206975846317440L;

	private static final Logger LOG = Logger.getLogger(DirectorServlet.class
			.getName());

	public enum Page {
		index, appinfo, attendance, export, forms, unanchored, users, user, stats, info, viewabsence, student, makeevent, deletestudent, studentinfo, viewevent, deleteevent;
	}

	private static final String SERVLET_PATH = "director";

	public static final String INDEX_URL = pageToUrl(
			DirectorServlet.Page.index, SERVLET_PATH);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl(req));
			return;
		} else if (!isLoggedIn(req, resp, User.Type.Director)) {
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
			case viewabsence:
				viewAbsence(req, resp, new ArrayList<String>());
				break;
			case attendance:
				showAttendance(req, resp, null, null);
				break;
			case appinfo:
				showAppInfo(req, resp, new ArrayList<String>(), null);
				break;
			case unanchored:
				showUnanchored(req, resp, null, null);
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
				showInfo(req, resp, new ArrayList<String>(), null);
				break;
			case student:
				showStudent(req, resp, new ArrayList<String>(), "");
				break;
			case makeevent:
				showMakeEvent(req, resp);
				break;
			case viewevent:
				viewEvent(req, resp);
				break;
			case export:
				showGradeExport(req, resp);
			default:
				ErrorServlet.showError(req, resp, 404);
			}
	}

	private void viewEvent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		String sid = req.getParameter("id");

		List<String> errors = new ArrayList<String>();
		PageBuilder page = new PageBuilder(Page.viewevent, SERVLET_PATH);
		if (sid != null && !sid.equals("")) {
			try {
				Event event = train.getEventController().get(
						Long.parseLong(sid));
				page.setAttribute("event", event);
				AbsenceController ac = train.getAbsenceController();
				List<Absence> absences = ac.getAll(event);
				int absent = 0;
				int tardy = 0;
				int earlyout = 0;
				for (Absence a : absences) {
					switch (a.getType()) {
					case Absence:
						absent++;
						break;
					case Tardy:
						tardy++;
						break;
					case EarlyCheckOut:
						earlyout++;
						break;
					default:
						break;
					}
				}
				page.setAttribute("absent", absent);
				page.setAttribute("tardy", tardy);
				page.setAttribute("earlyout", earlyout);
			} catch (NumberFormatException nfe) {
				errors.add("Invalid event id");
			}
		} else {
			errors.add("Invalid event id");
			page.setAttribute("errors", errors);
		}
		page.setPageTitle("View Event");
		page.passOffToJsp(req, resp);
	}

	private void showMakeEvent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PageBuilder page = new PageBuilder(Page.makeevent, SERVLET_PATH);
		Date today = new Date();

		page.setAttribute("arst", today.getYear() + 1900);
		page.setAttribute("today", today);
		page.setAttribute("types", Event.Type.values());
		page.setPageTitle("Make Event");
		page.passOffToJsp(req, resp);
	}

	private void showStats(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.stats, SERVLET_PATH);

		Date date = new Date();

		int numAbsences = train.getAbsenceController()
				.get(Absence.Type.Absence).size();
		int numTardy = train.getAbsenceController()
				.getCount(Absence.Type.Tardy);
		int numLeaveEarly = train.getAbsenceController().getCount(
				Absence.Type.EarlyCheckOut);
		int numStudents = train.getUsersController()
				.getCount(User.Type.Student);
		int numEvents = train.getEventController().getCount();
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
		// page.setAttribute("avgGrade",
		// train.getUsersController().averageGrade());

		page.setPageTitle("Statistics");

		page.passOffToJsp(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl());
			return;
		} else if (!isLoggedIn(req, resp, User.Type.Director)) {
			resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
			return;
		}

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
			case attendance:
				showAttendance(req, resp, null, null);
				break;
			case unanchored:
				postUnanchored(req, resp);
				break;
			case makeevent:
				postEvent(req, resp);
				break;
			case deletestudent:
				deleteStudent(req, resp);
				break;
			case studentinfo:
				postStudentInfo(req, resp);
				break;
			case info:
				postDirectorInfo(req, resp);
				break;
			case deleteevent:
				deleteEvent(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private void deleteEvent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String sid = req.getParameter("id");
		List<String> errors = new ArrayList<String>();
		String success = "";
		DataTrain train = DataTrain.getAndStartTrain();
		PageBuilder page = new PageBuilder(Page.attendance, SERVLET_PATH);
		String sremoveAnchored = req.getParameter("RemoveAnchored");
		EventController ec = train.getEventController();
		if (sid != null && !sid.equals("")) {
			Event event = null;
			try {
				event = ec.get(Long.parseLong(sid));
			} catch (NumberFormatException nfe) {
				errors.add("Invalid event id");
			}
			if (event != null) {
				// page.setAttribute("success_message", "Event deleted");
				success = "Event deleted";
				if (sremoveAnchored != null) {
					if (sremoveAnchored.equals("true")) {
						AbsenceController ac = train.getAbsenceController();
						List<Absence> todie = ac.getAll(event);
						ac.remove(todie);
						// page.setAttribute("success_message","Event and associated absences deleted.");
						success = "Event and associated absences deleted.";
					}
				}
				ec.delete(event);

				// this is set just in case we go back to the page with errors.
				page.setAttribute("event", event);
			}

		} else {
			errors.add("Invalid event id");
			// page.setAttribute("errors", errors);
		}

		showAttendance(req, resp, errors, success);
	}

	private void postDirectorInfo(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		DataTrain train = DataTrain.getAndStartTrain();
		String first = req.getParameter("FirstName");
		String last = req.getParameter("LastName");
		String secondEmail = req.getParameter("SecondEmail");
		String success = null;
		User director = train.getAuthController().getCurrentUser(
				req.getSession());
		List<String> errors = new ArrayList<String>();
		if (first == null || first.equals("")) {
			errors.add("Please supply a first name.");
		}
		if (last == null || last.equals("")) {
			errors.add("Please supply a last name.");
		}
		if (secondEmail != null && !secondEmail.equals("")) {
			director.setSecondaryEmail(new Email(secondEmail));
		}
		if (errors.size() > 0) {
			req.setAttribute("errors", errors);
		} else {
			director.setFirstName(first);
			director.setLastName(last);
			train.getUsersController().update(director);
			// req.setAttribute("success_message", "Info saved successfully.");
			success = "Info saved.";
		}
		showInfo(req, resp, errors, success);
	}

	private void postStudentInfo(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		List<String> errors = new ArrayList<String>();
		String success = "";

		String firstName, lastName, major, netid, rank;
		int year = -1;
		int minutesAvailable = -1;
		User.Section section = null;

		// Grab all the data from the fields
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");
		major = req.getParameter("Major");
		netid = req.getParameter("NetID");
		rank = req.getParameter("Rank");
		String sectionString = req.getParameter("Section");
		for (Section s : User.Section.values()) {
			if (sectionString.equals(s.name())) {
				section = s;
			}
		}

		try {
			year = Integer.parseInt(req.getParameter("Year"));
			minutesAvailable = Integer.parseInt(req
					.getParameter("MinutesAvailable"));
		} catch (NumberFormatException nfe) {
			LOG.severe(nfe.getStackTrace().toString());
			LOG.severe(nfe.getMessage());
			errors.add("Unable to save minutes available.");
		}

		UserController uc = train.getUsersController();

		User user = uc.get(netid);

		if (minutesAvailable > -1) {
			user.setMinutesAvailable(minutesAvailable);
		}
		user.setYear(year);
		user.setMajor(major);
		user.setSection(section);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setRank(rank);
		// TODO https://github.com/curtisullerich/attendance/issues/118
		// May throw validation exceptions
		try {
			uc.update(user);
			success = "All user information "
					+ ((errors.size() != 0) ? "except minutes available " : "")
					+ "saved.";
		} catch (IllegalArgumentException e) {
			errors.add("Unable to save student information: " + e.getMessage());
		}

		// so the user can get it
		req.setAttribute("id", netid.toString());
		// if (success) {
		// req.setAttribute("success_message", "Info successfully updated.");
		// }
		// else {
		// req.setAttribute("error_messages",
		// Arrays.asList("Info couldn't be saved"));
		// }
		showStudent(req, resp, errors, success);
	}

	private void deleteStudent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String sid = req.getParameter("deleteid");
		String success = "";
		List<String> errors = new ArrayList<String>();
		if (sid != null) {
			UserController uc = DataTrain.getAndStartTrain()
					.getUsersController();
			User todie = uc.get(sid);
			uc.delete(todie);
			success = "Student successfully deleted.";
		} else {
			errors.add("Unable to delete student.");
		}

		// add a success or error message
		showAttendance(req, resp, errors, success);
	}

	private void postEvent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		EventController ec = train.getEventController();
		List<String> errors = new LinkedList<String>();
		String success = "";

		try {
			Date start = Util.parseDate(req.getParameter("Month"),
					req.getParameter("Day"), req.getParameter("Year"),
					req.getParameter("StartHour"),
					req.getParameter("StartAMPM"),
					req.getParameter("StartMinute"), train
							.getAppDataController().get().getTimeZone());
			Date end = Util.parseDate(req.getParameter("Month"),
					req.getParameter("Day"), req.getParameter("Year"),
					req.getParameter("EndHour"), req.getParameter("EndAMPM"),
					req.getParameter("EndMinute"), train.getAppDataController()
							.get().getTimeZone());
			Event.Type type = req.getParameter("Type").equals(
					Event.Type.Rehearsal.getDisplayName()) ? Event.Type.Rehearsal
					: Event.Type.Performance;
			ec.createOrUpdate(type, start, end);
			success = "Event created.";
		} catch (ValidationExceptions e) {
			errors.add("Invalid Input: The input date was invalid.");
		} catch (IllegalArgumentException e) {
			errors.add("Invalid Input: The input date is invalid.");
		}
		// show success message?
		resp.sendRedirect("/director/unanchored");
		showUnanchored(req, resp, errors, success);
	}

	private void postUnanchored(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		AbsenceController ac = train.getAbsenceController();
		EventController ec = train.getEventController();
		int count = Integer.parseInt(req.getParameter("UnanchoredCount"));

		int numLinked = 0;
		for (int i = 0; i <= count; i++) {
			String eventID = req.getParameter("EventID" + i);
			String absenceID = req.getParameter("AbsenceID" + i);
			if (eventID != null && !eventID.equals("")) {
				if (absenceID != null && !absenceID.equals("")) {
					// retrieve the event and link it up
					Event e = ec.get(Long.parseLong(eventID));
					Absence a = ac.get(Long.parseLong(absenceID));
					a.setEvent(e);
					ac.updateAbsence(a);
					++numLinked;
				}
			}
		}
		// show success message and add error messages?
		showUnanchored(req, resp, null, numLinked
				+ ((numLinked == 1) ? " absence " : " absences ") + "linked.");
	}

	private void postAbsenceInfo(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		AbsenceController ac = train.getAbsenceController();

		boolean validForm = true;

		List<String> errors = new LinkedList<String>();

		if (!ValidationUtil.isPost(req)) {
			validForm = false;
		} else {
			String absid = req.getParameter("absenceid");
			if (req.getParameter("delete") != null) {
				deleteAbsence(absid, ac, req, resp);
				return;
			}
			long aid;
			Absence toUpdate = null;
			if (absid != null) {
				String type = req.getParameter("Type");
				String status = req.getParameter("Status");
				String eventid = req.getParameter("eventid");
				Event event = null;
				try {
					long eid = Long.parseLong(eventid);
					event = train.getEventController().get(eid);
				} catch (NumberFormatException nfe) {
					errors.add("No event to add. Can't create absence.");
				}
				if (absid.equals("new")) {
					String sid = req.getParameter("studentid");
					User student = train.getUsersController().get(sid);
					Absence.Type atype = Absence.Type.valueOf(type);
					if (student != null) {
						Date time = Util.parseDate(
								req.getParameter("StartMonth"),
								req.getParameter("StartDay"),
								req.getParameter("StartYear"),
								req.getParameter("StartHour"),
								req.getParameter("StartAMPM"),
								req.getParameter("StartMinute"), train
										.getAppDataController().get()
										.getTimeZone());
						switch (atype) {
						case Absence:
							toUpdate = ac.createOrUpdateAbsence(student, event);
							break;
						case Tardy:
							toUpdate = ac.createOrUpdateTardy(student, time);
							break;
						case EarlyCheckOut:
							toUpdate = ac.createOrUpdateEarlyCheckout(student,
									time);
							break;
						}
					}
				} else {

					aid = Long.parseLong(absid);
					toUpdate = ac.get(aid);
				}
				if (toUpdate != null) {

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
				resp.sendRedirect(pageToUrl(Page.attendance, SERVLET_PATH));
			} else {
				viewAbsence(req, resp, errors);
			}
		}
	}

	private void deleteAbsence(String absid, AbsenceController ac, 
			HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		List<String> errors = new ArrayList<String>();
		try {
			Absence a = ac.get(Integer.parseInt(absid));
			ac.remove(a);
			showAttendance(req, resp, errors, "Successfully deleted absence.");
		}
		catch (NumberFormatException e) {
			LOG.severe("Unable to find absence to delete.");
			errors.add("Internal error, unable to delete absence.");
			viewAbsence(req, resp, errors);
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
		String success = null;
		String newPassConf = null;
		String timezone = null;
		String title = null;
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
			timezone = req.getParameter("Timezone");

			if (timezone == null || timezone.equals("")) {
				errors.add("Invalid Input: Timezone can't be empty.");
			}

			TimeZone zone = TimeZone.getTimeZone(timezone);
			if (zone == null) {
				errors.add("Not a valid timezone.");
			} else {
				data.setTimeZone(zone);
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
			newPassConf = req.getParameter("hashedPassConf");
			if (newPass != null && !newPass.equals("")) {
				if (newPassConf == null || newPassConf.equals("")) {
					validForm = false;
					errors.add("Password and Confirm Password didn't match.");
				} else if (!newPass.equals(newPassConf)) {
					validForm = false;
					errors.add("Password and Confirm Password didn't match");
				} else {
					data.setHashedMobilePassword(newPass);
				}
			}

			title = req.getParameter("Title");
			if (title == null || title.equals("")) {
				errors.add("Must supply a title.");

			} else {
				data.setTitle(title);
			}
		}
		if (validForm) {
			appDataController.save(data);
		}

		if (errors.size() == 0) {
			success = "App info updated.";
		}
		showAppInfo(req, resp, errors, success);
	}

	private void showAppInfo(HttpServletRequest req, HttpServletResponse resp,
			List<String> errors, String success) throws ServletException,
			IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.appinfo, SERVLET_PATH);

		AppData data = train.getAppDataController().get();

		Calendar cutoffDate = Calendar.getInstance(data.getTimeZone());
		cutoffDate.setTime(data.getFormSubmissionCutoff());

		page.setAttribute("appinfo", data);
		
		page.setAttribute("timezone", data.getTimeZone());
		
		page.setAttribute("emails", data.getTimeWorkedEmails());

		page.setAttribute("Month", cutoffDate.get(Calendar.MONTH) + 1);

		page.setAttribute("Day", cutoffDate.get(Calendar.DATE));

		page.setAttribute("timezones", data.getTimezoneOptions());

		page.setAttribute("ToHour", (cutoffDate.get(Calendar.HOUR) == 0) ? 12
				: cutoffDate.get(Calendar.HOUR));

		page.setAttribute("ToMinute", cutoffDate.get(Calendar.MINUTE));

		page.setAttribute("ToAMPM",
				(cutoffDate.get(Calendar.AM_PM) == Calendar.AM) ? "AM" : "PM");

		// page.setAttribute("cutoffTime", data.getFormSubmissionCutoff());
		page.setAttribute("error_messages", errors);
		page.setAttribute("success_message", success);
		page.setPageTitle("Application Info");

		page.passOffToJsp(req, resp);
	}

	private void showAttendance(HttpServletRequest req,
			HttpServletResponse resp, List<String> errors, String success)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.attendance, SERVLET_PATH);

		List<User> students = train.getUsersController().get(User.Type.Student);
		List<Event> events = train.getEventController().readAll();

		Comparator<User> studentComparator = new Comparator<User>() {
			public int compare(User a, User b) {
				if (a == null) {
					return -1;
				} else if (b == null) {
					return 1;
				} else {
					return a.getLastName().compareToIgnoreCase(b.getLastName());
				}
			}
		};

		Comparator<Event> eventComparator = new Comparator<Event>() {
			public int compare(Event a, Event b) {
				if (a == null) {
					return -1;
				} else if (b == null) {
					return 1;
				} else {
					if (a.getStart().before(b.getStart())) {
						return -1;
					} else if (b.getStart().before(a.getStart())) {
						return 1;
					} else {
						return 0;
					}
				}
			}
		};
		// TODO https://github.com/curtisullerich/attendance/issues/117
		// is there better way to do this? Note that otherwise, events
		// print in the order of creation, NOT date order.
		Collections.sort(students, studentComparator);
		Collections.sort(events, eventComparator);
		AbsenceController ac = train.getAbsenceController();
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
		page.setAttribute("absences", train.getAbsenceController().getAll());
		page.setAttribute("events", events);
		page.setPageTitle("Attendance");
		page.setAttribute("error_messages", errors);
		page.setAttribute("success_message", success);
		// so if the page arrives with ?=showApproved=true then we display them
		User me = train.getAuthController().getCurrentUser(req.getSession());
		if (ValidationUtil.isPost(req)) {
			String show = req.getParameter("approved");
			boolean showb = Boolean.parseBoolean(show);
			me.setShowApproved(showb);
			train.getUsersController().update(me);
		}

		page.setAttribute("showApproved", me.isShowApproved());
		page.passOffToJsp(req, resp);
	}

	private void showUnanchored(HttpServletRequest req,
			HttpServletResponse resp, List<String> errors, String success)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.unanchored, SERVLET_PATH);
		List<Event> events = train.getEventController().readAll();
		page.setAttribute("events", events);

		List<Absence> unanchored = train.getAbsenceController().getAll();
		Set<Absence> set = new HashSet<Absence>();
		for (Absence a : unanchored) {
			if (a.getEvent() != null) {
				set.add(a);
			}
		}
		unanchored.removeAll(set);

		page.setAttribute("absences", unanchored);
		page.setPageTitle("Unanchored");
		page.setAttribute("error_messages", errors);
		page.setAttribute("success_message", success);

		page.passOffToJsp(req, resp);
	}

	private void showInfo(HttpServletRequest req, HttpServletResponse resp,
			List<String> errors, String success) throws IOException,
			ServletException {

		PageBuilder page = new PageBuilder(Page.info, SERVLET_PATH);

		page.setAttribute("user", DataTrain.getAndStartTrain()
				.getAuthController().getCurrentUser(req.getSession()));

		page.setAttribute("errors", errors);
		page.setAttribute("success_message", success);
		page.passOffToJsp(req, resp);
	}

	private void postUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String netID, strType, firstName, lastName;
		Email secondEmail;

		// Grab all the data from the fields
		netID = req.getParameter("NetID");
		strType = req.getParameter("Type");
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");
		secondEmail = new Email(req.getParameter("SecondEmail"));

		User.Type type = User.Type.valueOf(strType);

		UserController uc = DataTrain.getAndStartTrain().getUsersController();

		User localUser = uc.get(netID);

		localUser.setType(type);
		localUser.setFirstName(firstName);
		localUser.setLastName(lastName);
		localUser.setSecondaryEmail(secondEmail);

		// TODO https://github.com/curtisullerich/attendance/issues/115
		// May throw validation exceptions
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

	private void showGradeExport(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		String suggestedBaseFileName = "grades-"
				+ new SimpleDateFormat("yyyy.MM.dd").format(new Date());

		if ("csv".equals(req.getParameter("type"))) {

			resp.addHeader("Content-Disposition", "attachment; filename="
					+ suggestedBaseFileName + ".csv");

			resp.setContentType(GradeExport.CONTENT_TYPE_CSV);
			GradeExport.exportCSV(DataTrain.getAndStartTrain(),
					resp.getOutputStream());
		} else {
			new PageBuilder(Page.export, SERVLET_PATH).setPageTitle(
					"Grade Export").passOffToJsp(req, resp);
		}
	}

	private void viewAbsence(HttpServletRequest req, HttpServletResponse resp,
			List<String> incomingErrors) throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		AbsenceController ac = train.getAbsenceController();

		boolean validInput = true;

		List<String> errors = new LinkedList<String>();

		String sabsenceid = req.getParameter("absenceid");

		Absence checkedAbsence = null;

		long absenceid;
		PageBuilder page = new PageBuilder(Page.viewabsence, SERVLET_PATH,
				train);

		if (sabsenceid != null) {

			if (sabsenceid.equals("new")) {

				// get the event id and make a new absence. the director will
				// have to submit at the next page for it to be stored
				String seventid = req.getParameter("eventid");
				String sstudentid = req.getParameter("studentid");
				Event e = null;
				User student = null;
				if (seventid != null && !seventid.equals("")
						&& sstudentid != null && !sstudentid.equals("")) {
					e = train.getEventController()
							.get(Long.parseLong(seventid));
					student = train.getUsersController().get(sstudentid);
				}
				if (e != null && student != null) {
					// have to create the absence without storing it, or there
					// will automatically be an absence created without clicking
					// submit
					// checkedAbsence = train.getAbsenceController()
					// .createOrUpdateAbsence(student, e);
					Absence absence = ModelFactory.newAbsence(
							Absence.Type.Absence, student);
					absence.setStatus(Absence.Status.Pending);

					if (e != null && e.getStart() != null && e.getEnd() != null) {
						absence.setEvent(e);
						absence.setStart(e.getStart());
						absence.setEnd(e.getEnd());
						// associated with this event for this student
					} else {
						// the absence is orphaned if there's no event for it
					}
					checkedAbsence = absence;
					page.setAttribute("new", true);
				} else {
					validInput = false;
				}
			} else {
				absenceid = Long.parseLong(sabsenceid);
				checkedAbsence = ac.get(absenceid);
			}
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
			page.setPageTitle("View Absence");
			page.setAttribute("absence", checkedAbsence);
			page.setAttribute("types", Absence.Type.values());
			page.setAttribute("status", Absence.Status.values());
			page.setAttribute("error_messages", incomingErrors);
			page.passOffToJsp(req, resp);
		} else {
			showAttendance(req, resp, errors, null);
		}
	}

	private void showStudent(HttpServletRequest req, HttpServletResponse resp,
			List<String> errors, String success) throws ServletException,
			IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.student, SERVLET_PATH);

		FormController fc = train.getFormsController();
		String netid = req.getParameter("id");

		if (netid == null || netid.equals("")) {
			errors.add("The netID was missing.");
		} else {
			User student = train.getUsersController().get(netid);

			List<MessageThread> messageThreads = train.getMessagingController()
					.get(student);

			List<MessageThread> messageThreadsNonEmpty = new ArrayList<MessageThread>();
			for (MessageThread mt : messageThreads) {
				if (mt.getMessages() != null && !mt.getMessages().isEmpty()) {
					messageThreadsNonEmpty.add(mt);
				}
			}

			page.setPageTitle("Attendance");

			List<Absence> absences = train.getAbsenceController().get(student);

			page.setAttribute("user", student);
			page.setAttribute("forms", fc.get(student));
			page.setAttribute("absences", absences);
			page.setAttribute("threads", messageThreadsNonEmpty);
			page.setAttribute("sections", User.Section.values());

			// Pass through any success message in the url parameters sent from
			// a new form being created or deleted
			page.setAttribute("success_message", success);
		}
		String removeid = req.getParameter("removeid");
		if (removeid != null && removeid != "") {
			long id = Long.parseLong(removeid);
			if (fc.removeForm(id)) {
				page.setAttribute("success_message",
						"Form successfully deleted");
			} else {
				errors.add("Form not deleted. If the form was already approved then you can't delete it.");
			}
		}
		page.setAttribute("error_messages", errors);

		page.passOffToJsp(req, resp);
	}

}
