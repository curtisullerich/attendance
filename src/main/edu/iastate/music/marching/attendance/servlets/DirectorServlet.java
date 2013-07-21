package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.model.interact.AbsenceManager;
import edu.iastate.music.marching.attendance.model.interact.AppDataManager;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.EventManager;
import edu.iastate.music.marching.attendance.model.interact.FormManager;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.AppData;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.model.store.User.Section;
import edu.iastate.music.marching.attendance.util.GradeExport;
import edu.iastate.music.marching.attendance.util.PageBuilder;
import edu.iastate.music.marching.attendance.util.Util;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class DirectorServlet extends AbstractBaseServlet {

	private static final long serialVersionUID = 6100206975846317440L;

	private static final Logger LOG = Logger.getLogger(DirectorServlet.class
			.getName());

	private enum Page {
		index, appinfo, attendance, export, forms, unanchored, users, user, info, viewabsence, student, makeevent, deletestudent, studentinfo, viewevent, deleteevent, postdelete;
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
				viewAbsence(req, resp, new ArrayList<String>(), "");
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
				break;
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
				Event event = train.getEventManager().get(Long.parseLong(sid));
				page.setAttribute("event", event);
				AbsenceManager ac = train.getAbsenceManager();
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
		DateTime today = DateTime.now();

		page.setAttribute("arst", today.getYear() + 1900);
		page.setAttribute("today", today);
		page.setAttribute("types", Event.Type.values());
		page.setPageTitle("Make Event");
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
		boolean bRemoveAnchored;
		EventManager ec = train.getEventManager();
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
				if (sremoveAnchored != null && sremoveAnchored.equals("true")) {
					bRemoveAnchored = true;
				} else {
					bRemoveAnchored = false;
				}
				ec.delete(event, bRemoveAnchored);

				// this is set just in case we go back to the page with errors.
				page.setAttribute("event", event);
			}

		} else {
			errors.add("Invalid event id");
			// page.setAttribute("errors", errors);
		}

		// Don't redirect to attendance if in a new window
		if ("true".equals(req.getParameter("newindow"))) {
			new PageBuilder(Page.postdelete, SERVLET_PATH)
					.setAttribute(
							"success_message",
							success
									+ ", please close this window to return to the previous page.")
					.passOffToJsp(req, resp);
		} else {
			showStudent(req, resp, errors, success);
		}
	}

	private void postDirectorInfo(HttpServletRequest req,
			HttpServletResponse resp) throws IOException, ServletException {
		DataTrain train = DataTrain.getAndStartTrain();
		String first = req.getParameter("FirstName");
		String last = req.getParameter("LastName");
		String secondEmail = req.getParameter("SecondEmail");
		String success = null;
		User director = train.getAuthManager().getCurrentUser(req.getSession());
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
			train.getUsersManager().update(director);
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

		UserManager uc = train.getUsersManager();

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
			UserManager uc = DataTrain.getAndStartTrain().getUsersManager();
			User todie = uc.get(sid);
			uc.delete(todie);
			success = "Student successfully deleted.";
		} else {
			errors.add("Unable to delete student.");
		}

		// add a success or error message
		showStudent(req, resp, errors, success);
	}

	private void postEvent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		EventManager ec = train.getEventManager();
		List<String> errors = new LinkedList<String>();
		String success = "";

		try {
			DateTime start = Util.parseDateTime(
					req.getParameter("startdatetime"), train
							.getAppDataManager().get().getTimeZone());
			DateTime end = Util.parseDateTime(req.getParameter("enddatetime"),
					train.getAppDataManager().get().getTimeZone());

			if (!start.toDateMidnight().equals(end.toDateMidnight())) {
				errors.add("The event must start and end on the same day.");
			}

			Event.Type type = req.getParameter("Type").equals(
					Event.Type.Rehearsal.getDisplayName()) ? Event.Type.Rehearsal
					: Event.Type.Performance;
			ec.createOrUpdate(type, new Interval(start, end));
			success = "Event created.";
		} catch (ValidationExceptions e) {
			errors.add("Invalid Input: The input DateTime was invalid.");
		} catch (IllegalArgumentException e) {
			errors.add("Invalid Input: The input DateTime is invalid.");
		}
		// show success message?
		resp.sendRedirect("/director/unanchored");
		showUnanchored(req, resp, errors, success);
	}

	private void postUnanchored(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		AbsenceManager ac = train.getAbsenceManager();
		EventManager ec = train.getEventManager();
		int count = -1;

		try {
			count = Integer.parseInt(req.getParameter("UnanchoredCount"));
		} catch (NumberFormatException nfe) {
			LOG.severe("Unanchored count wasn't a number. This is a hidden field, so something's wrong.");
		}
		List<String> errors = new ArrayList<String>();
		int numLinked = 0;
		for (int i = 0; i <= count; i++) {
			String eventID = req.getParameter("EventID" + i);
			String absenceID = req.getParameter("AbsenceID" + i);
			if (eventID != null && !eventID.equals("")) {
				if (absenceID != null && !absenceID.equals("")) {
					// retrieve the event and link it up
					Event e = ec.get(Long.parseLong(eventID));
					Absence a = ac.get(Long.parseLong(absenceID));
					if (Util.overlapDays(a.getInterval(), e.getInterval())) {
						errors.add("Absence was not the same day as the event.");
					} else {
						a.setEvent(e);
						ac.updateAbsence(a);
						++numLinked;
					}
				}
			}
		}
		// show success message and add error messages?
		if (numLinked == 0) {
			showUnanchored(req, resp, errors, null);
		} else {
			showUnanchored(req, resp, errors, numLinked
					+ ((numLinked == 1) ? " absence " : " absences ")
					+ "linked.");
		}
	}

	private void postAbsenceInfo(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		AbsenceManager ac = train.getAbsenceManager();

		DateTimeZone zone = train.getAppDataManager().get().getTimeZone();

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
					event = train.getEventManager().get(eid);
				} catch (NumberFormatException nfe) {
					errors.add("No event to add. Can't create absence.");
				}
				if (absid.equals("new")) {
					String sid = req.getParameter("studentid");
					User student = train.getUsersManager().get(sid);
					Absence.Type atype = Absence.Type.valueOf(type);
					if (student != null) {
						// TODO might want to split this up and the combine date
						// and time with Calendar methods
						DateTime time = Util.parseDateTime(
								req.getParameter("date") + " "
										+ req.getParameter("time"), train
										.getAppDataManager().get()
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
						// TODO same concern as above
						LocalTime d = Util.parseTimeOnly(
								req.getParameter("time"), train
										.getAppDataManager().get()
										.getTimeZone());

						switch (toUpdate.getType()) {
						case EarlyCheckOut:
							toUpdate.setCheckout(d.toDateTime(toUpdate
									.getCheckout().withZone(zone)
									.toDateMidnight()));
							break;
						case Tardy:
							toUpdate.setCheckin(d.toDateTime(toUpdate
									.getCheckin().withZone(zone)
									.toDateMidnight()));
							break;
						default:
							throw new IllegalStateException();
						}
						toUpdate.setType(Absence.Type.valueOf(type));
						toUpdate.setStatus(Absence.Status.valueOf(status));
					} catch (ValidationExceptions e) {
						validForm = false;
						errors.add("Invalid Input: The input DateTime was invalid.");
					} catch (IllegalArgumentException e) {
						validForm = false;
						errors.add("Invalid Input: The input DateTime is invalid.");
					} catch (IllegalStateException e) {
						validForm = false;
						errors.add("Invalid Input: Cannot set time for absence");
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
				// How about upDateTime the absence huh?
				ac.updateAbsence(toUpdate);

				// Do not redirect if in a new window, instead show a success
				// message
				// if ("true".equals(req.getParameter("newindow"))) {
				viewAbsence(
						req,
						resp,
						errors,
						"Successfully updated absence, close this window to return to the previous page");
				// } else {
				// resp.sendRedirect(pageToUrl(Page.attendance, SERVLET_PATH));
				// }
			} else {
				viewAbsence(req, resp, errors, "");
			}
		}
	}

	private void deleteAbsence(String absid, AbsenceManager ac,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<String> errors = new ArrayList<String>();
		try {
			Absence a = ac.get(Integer.parseInt(absid));
			ac.remove(a);

			// Do not redirect if in a new window, instead show a success
			// message
			// if ("true".equals(req.getParameter("newindow"))) {
			new PageBuilder(Page.postdelete, SERVLET_PATH)
					.setAttribute(
							"success_message",
							"Successfully deleted absence, close this window to return to the previous page.")
					.passOffToJsp(req, resp);
			// } else {
			// // Redirect
			// resp.sendRedirect(pageToUrl(Page.attendance, SERVLET_PATH));
			// showStudent(req, resp, errors,
			// "Successfully deleted absence.");
			// }
		} catch (NumberFormatException e) {
			LOG.severe("Unable to find absence to delete.");
			errors.add("Internal error, unable to delete absence.");
			viewAbsence(req, resp, errors, "");
		}

	}

	private void postAppInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		AppDataManager appDataController = train.getAppDataManager();
		AppData data = appDataController.get();
		// get the train

		// check if there was a form submitted
		// no->//rebuild and return the page with errors
		// yes->//parse and validate
		// get the item from the controller
		// upDateTime the item and save it
		// return the jsp
		boolean validForm = true;
		boolean cronExportEnabled;
		List<String> errors = new LinkedList<String>();
		String success = null;
		String timezone = null;
		String title = null;
		String statusMessage = null;
		if (!ValidationUtil.isPost(req)) {
			validForm = false;
		} else {
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
			try {
				DateTimeZone storedZone = train.getAppDataManager().get()
						.getTimeZone();
				DateTime performanceAbsenceDatetime = Util.parseDateTime(
						req.getParameter("performanceAbsenceDatetime"),
						storedZone);
				data.setPerformanceAbsenceFormCutoff(performanceAbsenceDatetime);

				DateTime classConflictDatetime = Util.parseDateTime(
						req.getParameter("classConflictDatetime"), storedZone);
				data.setClassConflictFormCutoff(classConflictDatetime);

				DateTime timeWorkedDatetime = Util.parseDateTime(
						req.getParameter("timeWorkedDatetime"), storedZone);
				data.setTimeWorkedFormCutoff(timeWorkedDatetime);

			} catch (ValidationExceptions e) {
				validForm = false;
				errors.add(e.getMessage());
			}
			// Thrown by Calendar.getTime() if we don't have a valid time
			catch (IllegalArgumentException e) {
				validForm = false;
				errors.add("Invalid Input: The input DateTime is invalid.");
			}

			title = req.getParameter("Title");
			if (title == null || title.equals("")) {
				errors.add("Must supply a title.");

			} else {
				data.setTitle(title);
			}

			cronExportEnabled = null != req.getParameter("CronExportEnabled");
			data.setCronExportEnabled(cronExportEnabled);

			statusMessage = req.getParameter("StatusMessage");
			// if (statusMessage == null || statusMessage.equals("") ) {
			// errors.add("Status message was empty.");
			// } else {
			data.setStatusMessage(statusMessage);
			// }

		}
		if (validForm) {
			appDataController.save(data);
		} else {
			errors.add("There was a problem.");
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

		AppData data = train.getAppDataManager().get();

		page.setAttribute("appinfo", data);

		page.setAttribute("timezone", data.getTimeZone());

		if (data.getPerformanceAbsenceFormCutoff() != null) {
			page.setAttribute("performanceAbsenceDatetime", Util
					.formatDateTime(data.getPerformanceAbsenceFormCutoff(),
							data.getTimeZone()));
		}

		if (data.getClassConflictFormCutoff() != null) {
			page.setAttribute(
					"classConflictDatetime",
					Util.formatDateTime(data.getClassConflictFormCutoff(),
							data.getTimeZone()));
		}

		if (data.getTimeWorkedFormCutoff() != null) {
			page.setAttribute(
					"timeWorkedDatetime",
					Util.formatDateTime(data.getTimeWorkedFormCutoff(),
							data.getTimeZone()));
		}

		page.setAttribute("timezones", data.getTimezoneOptions());

		page.setAttribute("StatusMessage", data.getStatusMessage());

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

		List<User> students = train.getUsersManager().get(User.Type.Student);
		Map<User, List<Absence>> absenceList = new HashMap<User, List<Absence>>();

		for (User s : students) {
			// TODO may be more efficient to just get them all and then split
			// here
			List<Absence> a = train.getAbsenceManager().get(s);
			absenceList.put(s, a);
		}

		page.setAttribute("students", students);
		page.setAttribute("absenceList", absenceList);
		page.setPageTitle("Attendance");
		page.setAttribute("error_messages", errors);
		page.setAttribute("success_message", success);
		// so if the page arrives with ?=showApproved=true then we display them
		User me = train.getAuthManager().getCurrentUser(req.getSession());
		if (ValidationUtil.isPost(req)) {
			String show = req.getParameter("approved");
			boolean showb = Boolean.parseBoolean(show);
			me.setShowApproved(showb);
			train.getUsersManager().update(me);
		}

		page.setAttribute("showApproved", me.isShowApproved());
		page.passOffToJsp(req, resp);
	}

	private void showUnanchored(HttpServletRequest req,
			HttpServletResponse resp, List<String> errors, String success)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.unanchored, SERVLET_PATH);
		List<Event> events = train.getEventManager().readAll();
		page.setAttribute("events", events);

		List<Absence> unanchored = train.getAbsenceManager().getAll();
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

		page.setAttribute("user", DataTrain.getAndStartTrain().getAuthManager()
				.getCurrentUser(req.getSession()));

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

		UserManager uc = DataTrain.getAndStartTrain().getUsersManager();

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

		page.setAttribute("users", train.getUsersManager().getAll());

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
			u = train.getUsersManager().get(netid);
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
		page.setAttribute("StatusMessage", DataTrain.getAndStartTrain()
				.getAppDataManager().get().getStatusMessage());

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
			List<String> incomingErrors, String success_message)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		AbsenceManager ac = train.getAbsenceManager();

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
					e = train.getEventManager().get(Long.parseLong(seventid));
					student = train.getUsersManager().get(sstudentid);
				}
				if (e != null && student != null) {
					// have to create the absence without storing it, or there
					// will automatically be an absence created without clicking
					// submit
					Absence absence = ModelFactory.newAbsence(
							Absence.Type.Absence, student);
					absence.setStatus(Absence.Status.Pending);

					if (e != null) {
						absence.setEvent(e);
						absence.setInterval(e.getInterval());
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

		// if (validInput) {
		page.setPageTitle("View Absence");
		page.setAttribute("absence", checkedAbsence);
		page.setAttribute("types", Absence.Type.values());
		page.setAttribute("status", Absence.Status.values());
		page.setAttribute("error_messages", incomingErrors);
		page.setAttribute("success_message", success_message);

		DateTime setDateTime;
		switch (checkedAbsence.getType()) {
		default:
		case Absence:
			setDateTime = null;
			break;
		case EarlyCheckOut:
			setDateTime = checkedAbsence.getCheckout();
			break;
		case Tardy:
			setDateTime = checkedAbsence.getCheckin();
			break;
		}

		DateTimeZone timeZone = train.getAppDataManager().get().getTimeZone();
		page.setAttribute("date", Util.formatDateOnly(setDateTime, timeZone));
		page.setAttribute("time", Util.formatTimeOnly(setDateTime, timeZone));

		page.passOffToJsp(req, resp);
		// } else {
		// showAttendance(req, resp, errors, success_message);
		// }
	}

	private void showStudent(HttpServletRequest req, HttpServletResponse resp,
			List<String> errors, String success) throws ServletException,
			IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.student, SERVLET_PATH);

		FormManager fc = train.getFormsManager();
		String netid = req.getParameter("id");

		if (netid == null || netid.equals("")) {
			errors.add("The netID was missing.");
		} else {
			User student = train.getUsersManager().get(netid);

			page.setPageTitle("Attendance");

			List<Absence> absences = train.getAbsenceManager().get(student);

			page.setAttribute("user", student);
			page.setAttribute("forms", fc.get(student));
			page.setAttribute("absences", absences);
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
