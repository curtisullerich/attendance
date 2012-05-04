package edu.iastate.music.marching.attendance.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.datanucleus.sco.backed.LinkedList;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.User;

public class MobileDataController {

	private static final String NEWLINE = ",";
	private static final String SEPARATOR = "&split&";
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
				sb.append(next.getNetID());
				sb.append(SEPARATOR);
				sb.append(next.getFirstName());
				sb.append(SEPARATOR);
				sb.append(next.getLastName());
				sb.append(SEPARATOR);
				sb.append(App.getHashedMobilePassword());
				sb.append(SEPARATOR);
				sb.append(next.getRank());
			} else if (next.getType() == User.Type.Student) {
				sb.append("Student");
				sb.append(SEPARATOR);
				sb.append(next.getNetID());
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

	public boolean pushMobileData(String data) throws IllegalArgumentException {

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

		EventController ec = this.train.getEventsController();
		AbsenceController ac = this.train.getAbscencesController();
		UserController uc = this.train.getUsersController();

		// List<Event> localEvents = new LinkedList<Event>();

		// TODO do we get the data in the same format as we push it? i.e.,
		// SEPARATOR delimited?
		for (String s : eventLines) {
			// TODO, this is all really bullshit to mock it up.
			String[] event = s.split(SEPARATOR);
			String strType = event[1];
			String startTime = event[5];
			String strDate = event[4];
			String endTime = event[6];
			Date start = new Date();
			Date end = new Date();

			Event.Type type2 = Event.Type.Performance;
			Event newEvent = ec.createOrUpdate(type2, start, end);

			if (newEvent == null) {
				// do something TODO
			}
		}

		for (String s : otherLines) {

			String[] parts = s.split(SEPARATOR);

			if (s.contains("tardy")) {
				Date time = null;
				String netid = null;

				User student = uc.get(netid);

				ac.createOrUpdateTardy(student, time);
			} else if (s.contains("absent")) {

				String firstName = parts[1];
				String lastName = parts[2];
				String netid = parts[3];
				String strDate = parts[4];
				String strStartTime = parts[5];
				String strEndTime = parts[6];

				Date start = null;
				Date end = null;

				User student = uc.get(netid);
				
				// TODO check for valid student

				ac.createOrUpdateAbsence(student, start, end);
			} else if (s.toLowerCase().contains("earlycheckout")) {

				String netid = null;

				Date time = null;

				User student = uc.get(netid);

				ac.createOrUpdateEarlyCheckout(student, time);
			} else {
				// WE HAVE SOMETHING INCORRECT HERE, JIM.
			}
		}

		return true;
	}
}
