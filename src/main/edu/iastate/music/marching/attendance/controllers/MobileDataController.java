package edu.iastate.music.marching.attendance.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.MobileDataUpload;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

public class MobileDataController {

	private static final String NEWLINE = "&newline&";
	private static final String SEPARATOR = "&split&";
	private static final SimpleDateFormat MOBILE_DATETIME_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HHmm");
	private static final Object EVENT_TYPE_REHERSAL = null;
	private static final Object EVENT_TYPE_PERFORMANCE = null;

	private DataTrain train;

	MobileDataController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public String getClassList() {

		// Get all students and TA's
		List<User> users = this.train.getUsersController().get(
				User.Type.Student, User.Type.TA);

		StringBuilder sb = new StringBuilder();

		for (User next : users) {
			if (next.getType() == User.Type.Director) {
				// do nothing
			} else if (next.getType() == User.Type.TA) {
				sb.append("TA");
				sb.append(SEPARATOR);
				sb.append(next.getId());
				sb.append(SEPARATOR);
				sb.append(next.getFirstName());
				sb.append(SEPARATOR);
				sb.append(next.getLastName());
				sb.append(SEPARATOR);
				sb.append(train.getAppDataController().get()
						.getHashedMobilePassword());
				sb.append(SEPARATOR);
				sb.append(next.getRank());
			} else if (next.getType() == User.Type.Student) {
				sb.append("Student");
				sb.append(SEPARATOR);
				sb.append(next.getId());
				sb.append(SEPARATOR);
				sb.append(next.getFirstName());
				sb.append(SEPARATOR);
				sb.append(next.getLastName());
				sb.append(SEPARATOR);
				sb.append(next.getUniversityID());
				sb.append(SEPARATOR);
				sb.append(next.getRank());
			}

			sb.append(NEWLINE);
		}

		return sb.toString();
	}

	public String pushMobileData(String data, User uploader)
			throws IllegalArgumentException {

		// First lets log what is being uploaded
		MobileDataUpload upload = ModelFactory.newMobileDataUpload(
				uploader,
				Calendar.getInstance(
						this.train.getAppDataController().get().getTimeZone())
						.getTime(), data);
		this.train.getDataStore().store(upload);

		// Check we actually have something to work with
		if (data == null || "".equals(data.trim()))
			throw new IllegalArgumentException("Empty data uploaded");

		String[] fullLines = data.split(NEWLINE);

		ArrayList<String> eventLines = new ArrayList<String>();
		ArrayList<String> otherLines = new ArrayList<String>();

		// for each line, we want to create an object of the appropriate type
		// using the controllers
		for (String s : fullLines) {
			if (s.contains("storedPerformance")
					|| s.contains("storedRehearsal")) {
				eventLines.add(s);
			} else {
				otherLines.add(s);
			}
		}

		int successfulAbscenses = 0;
		int successfulEvents = 0;
		List<String> errors = new ArrayList<String>();

		// Do everything in a transaction so entire upload goes as a single
		// unit, rolling back if anything fails
		// Track transaction = train.switchTracks();

		// try {

		EventController ec = this.train.getEventController();
		AbsenceController ac = this.train.getAbsenceController();
		UserController uc = this.train.getUsersController();

		// List<Event> localEvents = new LinkedList<Event>();

		for (String s : eventLines) {
			// TODO, this is all really a mock up.
			String[] event = s.split(SEPARATOR);
			String strType = event[1].toLowerCase().trim();
			String strDate = event[4];
			String startTime = event[5];
			String endTime = event[6];

			Date start;
			try {
				start = MOBILE_DATETIME_FORMAT.parse(strDate + " " + startTime);
			} catch (ParseException e) {
				throw new IllegalArgumentException(
						"Unable to parse event start datetime from: " + s, e);
			}

			Date end;
			try {
				end = MOBILE_DATETIME_FORMAT.parse(strDate + " " + endTime);
			} catch (ParseException e) {
				throw new IllegalArgumentException(
						"Unable to parse event end datetime from: " + s, e);
			}

			Event.Type type = Event.Type.valueOf(strType.substring(0, 1)
					.toUpperCase() + strType.substring(1));

			Event newEvent = ec.createOrUpdate(type, start, end);

			if (newEvent == null) {
				errors.add("Insert of event failed: " + type.toString() + " "
						+ strDate + " from " + startTime + " to " + endTime);
			} else {
				successfulEvents++;
			}
		}

		for (String s : otherLines) {

			String[] parts = s.split(SEPARATOR);

			Absence a = null;

			if (s.contains("tardy")) {
				String firstName = parts[1];
				String lastName = parts[2];
				String netid = parts[3];
				String strDate = parts[4];
				String strTime = parts[5];

				Date time;
				try {
					time = MOBILE_DATETIME_FORMAT
							.parse(strDate + " " + strTime);
				} catch (ParseException e) {
					throw new IllegalArgumentException(
							"Unable to parse start datetime in:" + s, e);
				}

				User student = uc.get(netid);

				a = ac.createOrUpdateTardy(student, time);
			} else if (s.contains("absent")) {

				String firstName = parts[1];
				String lastName = parts[2];
				String netid = parts[3];
				String strDate = parts[4];
				String strStartTime = parts[5];
				String strEndTime = parts[6];

				Date start;
				try {
					start = MOBILE_DATETIME_FORMAT.parse(strDate + " "
							+ strStartTime);
				} catch (ParseException e) {
					throw new IllegalArgumentException(
							"Unable to parse start datetime in:" + s, e);
				}
				Date end;
				try {
					end = MOBILE_DATETIME_FORMAT.parse(strDate + " "
							+ strEndTime);
				} catch (ParseException e) {
					throw new IllegalArgumentException(
							"Unable to parse end datetime in:" + s, e);
				}

				User student = uc.get(netid);

				a = ac.createOrUpdateAbsence(student, start, end);
			} else if (s.toLowerCase().contains("earlycheckout")) {
				String firstName = parts[1];
				String lastName = parts[2];
				String netid = parts[3];
				String strDate = parts[4];
				String strTime = parts[5];

				Date time;
				try {
					time = MOBILE_DATETIME_FORMAT
							.parse(strDate + " " + strTime);
				} catch (ParseException e) {
					throw new IllegalArgumentException(
							"Unable to parse start datetime in:" + s, e);
				}

				User student = uc.get(netid);

				a = ac.createOrUpdateEarlyCheckout(student, time);

			} else {
				// WE HAVE SOMETHING INCORRECT HERE, JIM.
				// The null check will catch it though
			}

			if (a == null) {
				errors.add("Insert of absence failed: " + s);
			} else {
				successfulAbscenses++;
			}
		}

		// } catch (RuntimeException ex) {
		// transaction.derail();
		// throw ex;
		// }
		//
		// // Must have all worked
		// transaction.bendIronBack();

		String errorString = "";
		for (String s : errors)
			errorString += s;

		return "Inserted " + successfulEvents + "/" + eventLines.size()
				+ " events." + "\n" + "Inserted " + successfulAbscenses + "/"
				+ otherLines.size() + " absences/tardies/early checkouts."
				+ "\n" + errorString;
	}

	List<MobileDataUpload> getAllUploads() {
		return this.train.find(MobileDataUpload.class).returnAll().now();
	}
}
