package edu.iastate.music.marching.attendance.model.interact;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.model.store.User.Type;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class UserManager extends AbstractManager {

	DataTrain datatrain;

	public UserManager(DataTrain dataTrain) {
		this.datatrain = dataTrain;
	}

	public List<User> getAll() {
		return this.datatrain.find(User.class).returnAll().now();
	}

	public List<User> get(User.Type... types) {

		RootFindCommand<User> find = this.datatrain.find(User.class);
		find.addFilter(User.FIELD_TYPE, FilterOperator.IN, Arrays.asList(types));

		return find.returnAll().now();
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

	public User createStudent(com.google.appengine.api.users.User google_user,
			String univID, String firstName, String lastName, int year,
			String major, User.Section section, Email secondaryEmail)
			throws IllegalArgumentException {

		// Check google user
		if (google_user == null)
			throw new IllegalArgumentException("Must give a google user");

		// Extract email
		Email email = new Email(google_user.getEmail());
		
		return createStudent(email,
				 univID,  firstName,  lastName,  year,
				 major,  section,  secondaryEmail);
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
		if(secondaryEmail != null && secondaryEmail.getEmail() != "")
		{
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
		String type = parts[4];// just in case we want it
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

	/**
	 * 
	 * @param netid
	 * @return User object for the netid, or null if there is no such netid
	 */
	public User get(String netid) {

		return this.datatrain.getDataStore().load(
				this.datatrain.getTie(User.class, netid));
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

	public void update(User u) {

		// Force user's secondary email to be lowercase
		if (u.getSecondaryEmail() != null
				&& u.getSecondaryEmail().getEmail() != null) {
			u.setSecondaryEmail(new Email(u.getSecondaryEmail().getEmail()
					.toLowerCase()));
		}

		validateUser(u);

		//TODO investigate why this is necessary
		this.datatrain.getDataStore().activate(u);
		
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
		AbsenceManager ac = this.datatrain.getAbsenceManager();
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
							count += 2;
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
							count += 1;
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
		case 3:
			return User.Grade.B;
		case 6:
			return User.Grade.C;
		case 9:
			return User.Grade.D;
		default:
			return User.Grade.F;
		}
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
			return intToGrade((int) total / count);
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
		this.datatrain.getAbsenceManager().delete(user);

		// Forms
		this.datatrain.getFormsManager().delete(user);

		// Remove any Mobile Data uploads
		this.datatrain.getMobileDataManager().scrubUploader(user);

		od.delete(user);
	}
}
