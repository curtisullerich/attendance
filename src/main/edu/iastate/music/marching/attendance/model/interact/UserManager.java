package edu.iastate.music.marching.attendance.model.interact;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Interval;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class UserManager extends AbstractManager {

	DataTrain datatrain;

	// a user may not be later than this without 'missing' the whole rehearsal
	private static final int MAXIMUM_LATENESS_MINUTES = 30;

	public UserManager(DataTrain dataTrain) {
		this.datatrain = dataTrain;
	}

	public User.Grade averageGrade() {
		int total = 0;
		int count = 0;
		List<User> students = this.get(User.Type.Student, User.Type.TA);
		for (User s : students) {
			// if(s.getGrade() != null)
			{
				total += s.getGrade().ordinal();
				count += 1;
			}
		}

		if (count == 0)
			return null;
		else
			return intToGrade(total / count);
	}

	public User createDirector(String schoolEmail, String loginEmail,
			String firstName, String lastName) throws IllegalArgumentException {

		// ValiDateTime email
		Email primaryEmail = new Email(schoolEmail);
		Email secondaryEmail = new Email(loginEmail);
		ValidationUtil.validPrimaryEmail(primaryEmail, this.datatrain);
		ValidationUtil.validSecondaryEmail(secondaryEmail, this.datatrain);
		ValidationUtil.isUniqueSecondaryEmail(secondaryEmail, primaryEmail,
				this.datatrain);

		// Check no duplicate users exist
		if (get(primaryEmail) != null)
			throw new IllegalArgumentException(
					"User already exists in the system");

		User user = ModelFactory.newUser(User.Type.Director, primaryEmail,
				"000000000");
		user.setFirstName(firstName);
		user.setLastName(lastName);
		if (secondaryEmail != null && secondaryEmail.getEmail() != "") {
			user.setSecondaryEmail(secondaryEmail);
		}

		validateUser(user);

		this.datatrain.getDataStore().store(user);

		return user;
	}

	public User createFakeStudent(String[] parts) {
		// first name, last name, primary email, secondary email, type,
		// section, uid, year, major, rank, minutesavailable
		String firstName = parts[0];
		String lastName = parts[1];
		Email primaryEmail = new Email(parts[2]);
		Email secondaryEmail = new Email(parts[3]);
		// String type = parts[4];// just in case we want it
		User.Section section = User.Section.valueOf(parts[5]);
		String univID = parts[6];
		int year = Integer.parseInt(parts[7]);
		String major = parts[8];

		String rank = parts[9];// just in case
		int minutesAvailable = Integer.parseInt(parts[10]);// just in case

		User user = ModelFactory.newUser(User.Type.Student, primaryEmail,
				univID);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setSecondaryEmail(secondaryEmail);
		user.setSection(section);
		user.setYear(year);
		user.setMajor(major);
		user.setRank(rank);
		user.setMinutesAvailable(minutesAvailable);
		user.setGrade(User.Grade.A);

		// com.google.appengine.api.users.User google_user = AuthController
		// .getGoogleUser();
		validateUser(user);

		this.datatrain.getDataStore().store(user);
		return user;
	}

	public User createStudent(com.google.appengine.api.users.User google_user,
			String univID, String firstName, String lastName, int year,
			String major, User.Section section, Email secondaryEmail)
			throws IllegalArgumentException {

		// Check google user
		if (google_user == null)
			throw new IllegalArgumentException("Must give a google user");

		// Extract email
		Email email = new Email(google_user.getEmail());

		return createStudent(email, univID, firstName, lastName, year, major,
				section, secondaryEmail);
	}

	public User createStudent(Email primaryEmail, String univID,
			String firstName, String lastName, int year, String major,
			User.Section section, Email secondaryEmail)
			throws IllegalArgumentException {

		// Check google user
		if (primaryEmail == null)
			throw new IllegalArgumentException("Must give a google user");

		// Check no duplicate users exist
		if (get(primaryEmail) != null)
			throw new IllegalArgumentException(
					"User already exists in the system");

		User user = ModelFactory.newUser(User.Type.Student, primaryEmail,
				univID);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setYear(year);
		user.setMajor(major);
		user.setSection(section);
		user.setSecondaryEmail(secondaryEmail);
		user.setGrade(User.Grade.A);

		validateUser(user);

		this.datatrain.getDataStore().store(user);
		updateUserGrade(user);
		return user;
	}

	/*
	 * Full cleanup of user happens without affecting other things in the
	 * datastore
	 * 
	 * This includes:
	 * 
	 * Absences, Forms, mobile data uploads
	 */
	public void delete(User user) {
		ObjectDatastore od = this.datatrain.getDataStore();

		// Absences
		this.datatrain.absences().delete(user);

		// Forms
		this.datatrain.forms().delete(user);

		// Remove any Mobile Data uploads
		this.datatrain.mobileData().scrubUploader(user);

		od.delete(user);
	}

	/**
	 * 
	 * @param email
	 *            Primary email of a user
	 * @return
	 */
	public User get(Email primaryEmail) {

		User u;

		if (primaryEmail == null)
			return null;

		if (primaryEmail.getEmail() == null)
			return null;

		Iterator<User> users = this.datatrain
				.find(User.class)
				.addFilter(User.FIELD_PRIMARY_EMAIL, FilterOperator.EQUAL,
						primaryEmail.getEmail()).fetchMaximum(2).now();

		if (users.hasNext())
			u = users.next();
		else
			return null;

		if (users.hasNext())
			throw new IllegalStateException(
					"Found more than one user corresponding to primary email "
							+ primaryEmail);

		return u;
	}

	/**
	 * 
	 * @param netid
	 * @return User object for the netid, or null if there is no such netid
	 */
	public User get(String netid) {

		return this.datatrain.getDataStore().load(
				this.datatrain.getTie(User.class, netid));
	}

	public List<User> get(User.Type... types) {

		RootFindCommand<User> find = this.datatrain.find(User.class);
		find.addFilter(User.FIELD_TYPE, FilterOperator.IN, Arrays.asList(types));

		return find.returnAll().now();
	}

	public List<User> getAll() {
		return this.datatrain.find(User.class).returnAll().now();
	}

	/**
	 * 
	 * @param email
	 *            Secondary email of a user
	 * @return
	 */
	public User getSecondary(Email secondaryEmail) {

		User u;

		if (secondaryEmail == null)
			return null;

		if (secondaryEmail.getEmail() == null)
			return null;

		Iterator<User> users = this.datatrain
				.find(User.class)
				.addFilter(User.FIELD_SECONDARY_EMAIL, FilterOperator.EQUAL,
						secondaryEmail.getEmail()).fetchMaximum(2).now();

		if (users.hasNext())
			u = users.next();
		else
			return null;

		if (users.hasNext())
			throw new IllegalStateException(
					"Found more than one user corresponding to secondary email "
							+ secondaryEmail);

		return u;
	}

	private User.Grade intToGrade(int count) {
		// users may miss up to 140 minutes for free
		int autoexcused = 140;
		count -= autoexcused;
		if (count <= 140) {
			return User.Grade.A;

		} else if (count <= 155) {
			return User.Grade.B;

		} else if (count <= 170) {
			return User.Grade.C;

		} else if (count <= 185) {
			return User.Grade.D;
		} else {
			return User.Grade.F;
		}
	}

	public boolean isUniqueId(String id, Email primary) {
		RootFindCommand<User> find = this.datatrain.find(User.class);
		find.addFilter(User.FIELD_UNIVERSITY_ID, FilterOperator.EQUAL, id);
		List<User> found = find.returnAll().now();
		// When we are registering we need this result to be zero because that
		// means
		// the person registering is the one who has it. But when we are just
		// validating stuff
		// this number can be 1, meaning the current person
		return found.size() == 0
				|| (found.size() == 1 && found.get(0).getPrimaryEmail()
						.equals(primary));
	}

	public boolean isUniqueSecondaryEmail(Email secondaryEmail, Email primary) {
		if (secondaryEmail == null || "".equals(secondaryEmail.getEmail()))
			return true;
		RootFindCommand<User> find = this.datatrain.find(User.class);
		find.addFilter(User.FIELD_SECONDARY_EMAIL, FilterOperator.EQUAL,
				secondaryEmail);
		List<User> found = find.returnAll().now();
		return found.size() == 0
				|| (found.size() == 1 && found.get(0).getPrimaryEmail()
						.equals(primary));
	}

	public void update(User u) {

		// Force user's secondary email to be lowercase
		if (u.getSecondaryEmail() != null
				&& u.getSecondaryEmail().getEmail() != null) {
			u.setSecondaryEmail(new Email(u.getSecondaryEmail().getEmail()
					.toLowerCase()));
		}

		validateUser(u);

		// TODO investigate why this is necessary
		// this.datatrain.getDataStore().activate(u);

		// I make this redundant call because the call chain in updateUserGrade
		// wipes the changes from the User
		this.datatrain.getDataStore().update(u);
		updateUserGrade(u);
		this.datatrain.getDataStore().update(u);
	}

	/**
	 * Always calculated based on the attendance stored in the database. There
	 * should never be a reason for a user to override a grade manually.
	 * 
	 * Note that this DOES NOT currently refresh the student in the database.
	 */
	public void updateUserGrade(User student) {
		AbsenceManager ac = this.datatrain.absences();
		int minutes = 0;
		List<Absence> absences = ac.get(student);

		Map<Event, List<Absence>> map = new HashMap<Event, List<Absence>>(
				absences.size());
		for (Absence a : absences) {
			Event e = a.getEvent();
			if (e == null) {
				// it's unlinked!
			} else {
				if (map.containsKey(e)) {
					map.get(e).add(a);
				} else {
					List<Absence> list = new ArrayList<Absence>();
					list.add(a);
					map.put(e, list);
				}
			}
		}

		for (Event e : map.keySet()) {
			List<Absence> l = map.get(e);
			if (l.size() < 1) {
				throw new IllegalStateException(
						"Somehow the event had no associated absences");
			}
			if (l.size() == 1) {
				minutes += simpleGrade(e, l);
			} else {
				minutes += generalGrade(e, l);
			}
		}
		student.setGrade(intToGrade(minutes));
	}

	private int generalGrade(Event e, List<Absence> l) {
		// TODO implement
		// TODO log that there were multiple absences for an event for a student
		class PresenceInterval {

			private boolean present;
			private Interval interval;

			public PresenceInterval(Interval interval, boolean present) {
				this.setInterval(interval);
				this.setPresent(present);
			}

			public boolean isPresent() {
				return present;
			}

			public void setPresent(boolean present) {
				this.present = present;
			}

			public Interval getInterval() {
				return interval;
			}

			public void setInterval(Interval interval) {
				this.interval = interval;
			}
		}

		List<PresenceInterval> ints = new ArrayList<PresenceInterval>();
		DateTimeZone zone = datatrain.appData().get().getTimeZone();
		ints.add(new PresenceInterval(e.getInterval(zone), true));
		for (Absence a : l) {
			if (a.getType() == Absence.Type.Absence) {
				throw new IllegalArgumentException(
						"there were multiple absences of type Absence for a single event!");
			}
			DateTime d = a.getType() == Absence.Type.Tardy ? a.getCheckin(zone)
					: a.getCheckout(zone);
			for (int i = 0; i < ints.size(); i++) {
				PresenceInterval p = ints.get(i);
				if (p.getInterval().contains(d)) {
					// found a match
					ints.remove(p);
					Interval first = new Interval(p.getInterval().getStart(), d);
					Interval second = new Interval(d, p.getInterval().getEnd());
					if (a.getType() == Absence.Type.Tardy) {
						// they were present AFTER the checkin
						ints.add(new PresenceInterval(first, false));
						ints.add(new PresenceInterval(second, true));
					} else {
						// they were present BEFORE the checkout
						ints.add(new PresenceInterval(first, true));
						ints.add(new PresenceInterval(second, false));
					}
					break;
					// move on to the next absence
				}
			}
		}
		int minutes = 0;
		for (PresenceInterval p : ints) {
			if (!p.isPresent()) {
				minutes += p.getInterval().toDuration().getStandardMinutes();
			}
		}
		return minutes;
	}

	private int simpleGrade(Event e, List<Absence> l) {
		if (l.size() != 1) {
			throw new IllegalArgumentException(
					"list of Absences must be of size 1");
		}
		int minutes = 0;
		DateTimeZone zone = datatrain.appData().get().getTimeZone();
		Absence a = l.get(0);
		if (a.getStatus() == Absence.Status.Approved) {
			// nothing to do, it's approved!
		} else if (a.getStatus() == Absence.Status.Pending
				|| a.getStatus() == Absence.Status.Denied) {

			// this means that absences with unanchored
			// events will have no grade penalty

			if (a.getEvent() != null) {
				if (a.getType() == Absence.Type.Absence) {
					minutes += a.getEvent().getInterval(zone).toDuration()
							.getStandardMinutes();

				} else {
					Duration d;
					if (a.getType() == Absence.Type.Tardy) {
						d = new Duration(a.getEvent().getInterval(zone)
								.getStart(), a.getCheckin(zone));
					} else if (a.getType() == Absence.Type.EarlyCheckOut) {
						d = new Duration(a.getCheckout(zone), a.getEvent()
								.getInterval(zone).getEnd());
					} else {
						d = null;
						throw new IllegalArgumentException(
								"invalid absence type");
					}
					if (d.getStandardMinutes() > MAXIMUM_LATENESS_MINUTES) {
						// too late!
						minutes += a.getEvent().getInterval(zone).toDuration()
								.getStandardMinutes();
					} else {
						minutes += d.getStandardMinutes();
					}
				}
			}
		}
		return minutes;
	}

	private void validateUser(User user) throws IllegalArgumentException {

		if (user == null)
			throw new IllegalArgumentException("Invalid user");

		// Check primary email
		if (!ValidationUtil.validPrimaryEmail(user.getPrimaryEmail(),
				this.datatrain))
			throw new IllegalArgumentException("Invalid primary email");

		// Check name
		if (!ValidationUtil.isValidName(user.getFirstName()))
			throw new IllegalArgumentException("Invalid first name");
		if (!ValidationUtil.isValidName(user.getLastName()))
			throw new IllegalArgumentException("Invalid last name");

		// Check id
		String uId = user.getUniversityID();
		if (!ValidationUtil.isValidUniversityID(uId)) {
			throw new IllegalArgumentException("Invalid university id");
		}

		if (user.getType() != User.Type.Director
				&& !ValidationUtil.isUniqueId(uId, user.getPrimaryEmail())) {
			throw new IllegalArgumentException("University id was not unique");
		}

		// Check secondary email
		if (!ValidationUtil.validSecondaryEmail(user.getSecondaryEmail(),
				this.datatrain))
			throw new IllegalArgumentException("Invalid secondary email");
		if (!ValidationUtil.isUniqueSecondaryEmail(user.getSecondaryEmail(),
				user.getPrimaryEmail(), datatrain)) {
			throw new IllegalArgumentException("Non-unique secondary email");
		}
		// Check student specific things
		if (user.getType() == User.Type.Student) {
			String major = user.getMajor();
			String rank = user.getRank();
			User.Section section = user.getSection();
			int year = user.getYear();

			if (!ValidationUtil.isValidMajor(major)) {
				throw new IllegalArgumentException("Invalid major");
			}
			if (!ValidationUtil.isValidRank(rank)) {
				throw new IllegalArgumentException("Invalid rank");
			}
			if (!ValidationUtil.isValidSection(section)) {
				throw new IllegalArgumentException("Invalid section");
			}
			if (!ValidationUtil.isValidYear(year)) {
				throw new IllegalArgumentException("Invalid year");
			}
		}
	}
}
