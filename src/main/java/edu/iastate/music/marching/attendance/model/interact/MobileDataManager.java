package edu.iastate.music.marching.attendance.model.interact;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.google.appengine.api.datastore.Query.FilterOperator;

import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.MobileDataUpload;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;
import edu.iastate.music.marching.attendance.model.store.User;

public class MobileDataManager {

	private static final Logger log = Logger.getLogger(MobileDataManager.class
			.getName());

	private static final String NEWLINE = "&newline&";
	private static final String SEPARATOR = "&split&";
	private static final DateTimeFormatter MOBILE_DATETIME_FORMATTER = DateTimeFormat
			.forPattern("yyyy-MM-dd HHmm");

	private static DateTime ParseDateTime(String datetext, String timetext,
			DateTimeZone zone) {
		return MOBILE_DATETIME_FORMATTER.withZone(zone).parseDateTime(
				datetext + " " + timetext);
	}

	private DataTrain train;

	MobileDataManager(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public String getClassList() {

		// Get all students and TA's
		List<User> users = this.train.users().get(User.Type.Student,
				User.Type.TA);

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

	public List<MobileDataUpload> getUploads() {
		return this.train.find(MobileDataUpload.class).returnAll().now();
	}

	public List<MobileDataUpload> getUploads(User uploader) {
		return this.train
				.find(MobileDataUpload.class)
				.addFilter(MobileDataUpload.FIELD_UPLOADER,
						FilterOperator.EQUAL, uploader).returnAll().now();
	}

	public String pushMobileData(String data, User uploader)
			throws IllegalArgumentException {
		// First let's log what is being uploaded
		MobileDataUpload upload = ModelFactory.newMobileDataUpload(uploader,
				new DateTime(), data);
		this.train.getDataStore().store(upload);

		try {

			return uploadMobileDataImplementation(data, uploader);

		} catch (IllegalArgumentException e) {
			// Log exception
			log.log(Level.WARNING,
					"Parse exception for uploaded mobile app data", e);

			// Store exception message in upload object
			upload.setErrorMessage(e.toString());

			throw e;
		} catch (IllegalStateException e) {
			// Log exception
			log.log(Level.WARNING, "Exception while uploading mobile app data",
					e);

			// Store exception message in upload object
			upload.setErrorMessage(e.toString());

			throw e;
		}
	}

	public void scrubUploader(User uploader) {
		List<MobileDataUpload> uploads = getUploads(uploader);

		for (MobileDataUpload upload : uploads) {
			upload.setUploader(null);
			this.train.getDataStore().update(upload);
		}
	}

	private String uploadMobileDataImplementation(String data, User uploader)
			throws IllegalArgumentException {
		// Check we actually have something to work with
		if (data == null || "".equals(data.trim())) {
			log.log(Level.INFO, "Empty data upload by " + uploader.getId());
			throw new IllegalArgumentException("Empty data uploaded");
		}

		DateTimeZone zone = this.train.appData().get().getTimeZone();

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

		Set<User> updatedStudents = new HashSet<User>();

		EventManager ec = this.train.events();
		AbsenceManager ac = this.train.absences();
		UserManager uc = this.train.users();

		for (String s : eventLines) {
			// TODO: https://github.com/curtisullerich/attendance/issues/62
			// This is all really a mock-up, and should be re-written when
			// the mobile app is re-done
			String[] event = s.split(SEPARATOR);
			String strType = event[1].toLowerCase().trim();
			String strDateTime = event[4];
			String startTime = event[5];
			String endTime = event[6];

			DateTime start;
			try {
				start = ParseDateTime(strDateTime, startTime, zone);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Unable to parse event start datetime from: " + s, e);
			}

			DateTime end;
			try {
				end = ParseDateTime(strDateTime, endTime, zone);
			} catch (IllegalArgumentException e) {
				throw new IllegalArgumentException(
						"Unable to parse event end datetime from: " + s, e);
			}

			Event.Type type = Event.Type.valueOf(strType.substring(0, 1)
					.toUpperCase() + strType.substring(1));

			Event newEvent = ec.createOrUpdate(type, new Interval(start, end));

			if (newEvent == null) {
				errors.add("Insert of event failed: " + type.toString() + " "
						+ strDateTime + " from " + startTime + " to " + endTime);
			} else {
				successfulEvents++;
			}
		}

		for (String s : otherLines) {

			String[] parts = s.split(SEPARATOR);

			Absence a = null;

			if (s.contains("tardy")) {
				// String firstName = parts[1];
				// String lastName = parts[2];
				String netid = parts[3];
				String strDateTime = parts[4];
				String strTime = parts[5];

				DateTime time;
				try {
					time = ParseDateTime(strDateTime, strTime, zone);
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(
							"Unable to parse start datetime in:" + s, e);
				}

				User student = uc.get(netid);

				a = ac.createOrUpdateTardy(student, time);

				updatedStudents.add(student);

			} else if (s.contains("absent")) {

				// String firstName = parts[1];
				// String lastName = parts[2];
				String netid = parts[3];
				String strDateTime = parts[4];
				String strStartTime = parts[5];
				String strEndTime = parts[6];

				DateTime start;
				try {
					start = ParseDateTime(strDateTime, strStartTime, zone);
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(
							"Unable to parse start datetime in:" + s, e);
				}
				DateTime end;
				try {
					end = ParseDateTime(strDateTime, strEndTime, zone);
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(
							"Unable to parse end datetime in:" + s, e);
				}

				User student = uc.get(netid);

				a = ac.createOrUpdateAbsence(student, new Interval(start, end));

				updatedStudents.add(student);

			} else if (s.toLowerCase().contains("earlycheckout")) {
				// String firstName = parts[1];
				// String lastName = parts[2];
				String netid = parts[3];
				String strDateTime = parts[4];
				String strTime = parts[5];

				DateTime time;
				try {
					time = ParseDateTime(strDateTime, strTime, zone);
				} catch (IllegalArgumentException e) {
					throw new IllegalArgumentException(
							"Unable to parse start datetime in:" + s, e);
				}

				User student = uc.get(netid);

				a = ac.createOrUpdateEarlyCheckout(student, time);

				updatedStudents.add(student);

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

		StringBuilder errorStringB = new StringBuilder();
		for (String s : errors)
			errorStringB.append(s);

		return "Inserted " + successfulEvents + "/" + eventLines.size()
				+ " events." + "\n" + "Inserted " + successfulAbscenses + "/"
				+ otherLines.size() + " absences/tardies/early checkouts."
				+ "\n" + errorStringB.toString();
	}
}
