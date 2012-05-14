package edu.iastate.music.marching.attendance.controllers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;

import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class UserController extends AbstractController {

	DataTrain datatrain;

	UserController(DataTrain dataTrain) {
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

		List<User> users = this.datatrain.getDataStore().find()
				.type(User.class)
				.addFilter(User.FIELD_NETID, FilterOperator.EQUAL, netid)
				.returnAll().now();

		if (users.size() == 1)
			return users.get(0);
		else if (users.size() > 1)
			throw new IllegalStateException(
					"Found more than one user with same netid");
		else
			return null;
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

		this.datatrain.getDataStore().update(u);
	}
}
