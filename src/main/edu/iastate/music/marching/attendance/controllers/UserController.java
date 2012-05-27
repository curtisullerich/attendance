package edu.iastate.music.marching.attendance.controllers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class UserController extends AbstractController {

	DataTrain datatrain;

	public UserController(DataTrain dataTrain) {
		super(dataTrain);
		this.datatrain = dataTrain;
	}

	public List<User> getAll() {
		return this.datatrain.getDataStore().find().type(User.class)
				.returnAll().now();
	}

	public List<User> get(User.Type... types) {

		RootFindCommand<User> find = this.datatrain.getDataStore().find()
				.type(User.class);
		find.addFilter(User.FIELD_TYPE, FilterOperator.IN, Arrays.asList(types));

		return find.returnAll().now();
	}

	public User createStudent(com.google.appengine.api.users.User google_user,
			int univID, String firstName, String lastName, int year,
			String major, User.Section section) throws IllegalArgumentException {

		// Check google user
		if (google_user == null)
			throw new IllegalArgumentException("Must give a google user");

		// Extract net id
		String netID = google_user.getEmail().split("@")[0];

		// Check no duplicate users exist
		if (get(google_user) != null)
			throw new IllegalArgumentException(
					"User already exists in the system");

		User user = ModelFactory.newUser(User.Type.Student, google_user, netID,
				univID);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setYear(year);
		user.setMajor(major);
		user.setSection(section);

		validateUser(user);

		this.datatrain.getDataStore().store(user);

		return user;
	}

	private void validateUser(User user) throws IllegalArgumentException {

		if (user == null)
			throw new IllegalArgumentException("Invalid user");

		// Check google user
		if (!ValidationUtil.validGoogleUser(user.getGoogleUser()))
			throw new IllegalArgumentException("Invalid google user");

		// Check name
		if (!ValidationUtil.isValidName(user.getFirstName()))
			throw new IllegalArgumentException("Invalid first name");
		if (!ValidationUtil.isValidName(user.getLastName()))
			throw new IllegalArgumentException("Invalid last name");

		// Check university id
		// TODO
		user.getUniversityID();

		// Check student specific things
		if (user.getType() == User.Type.Student) {
			// TODO validation
			user.getMajor();
			user.getRank();
			user.getSection();
			user.getYear();
		}

	}

	public User createDirector(com.google.appengine.api.users.User google_user,
			int univID, String firstName, String lastName)
			throws IllegalArgumentException {

		// Check google user
		if (google_user == null)
			throw new IllegalArgumentException("Must give a google user");

		// Extract net id
		String netID = google_user.getEmail().split("@")[0];

		// Check no duplicate users exist
		if (get(google_user) != null)
			throw new IllegalArgumentException(
					"User already exists in the system");

		User user = ModelFactory.newUser(User.Type.Director, google_user,
				netID, univID);
		user.setFirstName(firstName);
		user.setLastName(lastName);

		validateUser(user);

		this.datatrain.getDataStore().store(user);

		return user;
	}

	/**
	 * 
	 * @param netid
	 * @return User object for the netid, or null if there is no such netid
	 */
	public User get(String netid) {

		return this.datatrain.getDataStore().load(User.class, netid);
	}

	/**
	 * 
	 * @param googleUser
	 *            Google user object from AuthController
	 * @return
	 */
	public User get(com.google.appengine.api.users.User googleUser) {

		User u;

		if (googleUser == null)
			return null;

		Iterator<User> users = this.datatrain
				.getDataStore()
				.find()
				.type(User.class)
				.addFilter(User.FIELD_GOOGLEUSER, FilterOperator.EQUAL,
						googleUser).now();

		if (users.hasNext())
			u = users.next();
		else
			return null;

		if (users.hasNext())
			throw new IllegalStateException(
					"Found more than one user corresponding to a google user");

		return u;
	}

	public void update(User u) {
		validateUser(u);
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
		AbsenceController ac = this.datatrain.getAbsencesController();
		int count = 0;
		List<Absence> absences = ac.get(student);
		for (Absence a : absences) {

			if (a.getStatus() == Absence.Status.Approved) {
				// nothing to do, it's approved!
			} else if (a.getStatus() == Absence.Status.Pending
					|| a.getStatus() == Absence.Status.Denied) {

				// this means that absences with unanchored
				// events will have no grade penalty
				if (a.getEvent() != null) {
					switch (a.getEvent().getType()) {
					case Performance:
						switch (a.getType()) {
						case Absence:
							count += 6;
							break;
						case Tardy:
							count += 2;
							break;
						case EarlyCheckOut:
							// no penalty? TODO
							break;
						}
						break;
					case Rehearsal:
						switch (a.getType()) {
						case Absence:
							count += 3;
							break;
						case Tardy:
							count += 1;
							break;
						case EarlyCheckOut:
							// no penalty? TODO
							break;
						}
						break;
					}
				}
			}
		}
		student.setGrade(intToGrade(count));
	}

	private User.Grade intToGrade(int count) {
		switch (count) {
		case 0:
			return User.Grade.A;
		case 1:
			return User.Grade.Aminus;
		case 2:
			return User.Grade.Bplus;
		case 3:
			return User.Grade.B;
		case 4:
			return User.Grade.Bminus;
		case 5:
			return User.Grade.Cplus;
		case 6:
			return User.Grade.C;
		case 7:
			return User.Grade.Cminus;
		case 8:
			return User.Grade.Dplus;
		case 9:
			return User.Grade.D;
		case 10:
			return User.Grade.Dminus;
		default:
			return User.Grade.F;
		}
	}

	public User.Grade averageGrade() {
//		int count = 0;
//		List<User> students = this.get(User.Type.Student);
//		for (User s : students) {
//			count += s.getGrade().ordinal();
//		}
//		return intToGrade((int) count / students.size());
		//TODO
		return User.Grade.F;
	}
}
