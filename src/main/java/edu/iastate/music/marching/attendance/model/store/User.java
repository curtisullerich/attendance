package edu.iastate.music.marching.attendance.model.store;

import java.io.Serializable;

import com.google.appengine.api.datastore.Email;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;

@Entity(kind = "User", allocateIdsBy = 0)
public class User implements Serializable {

	public enum Grade {
		A, B, C, D, F;
		private String mDisplayString;

		private Grade() {
			mDisplayString = this.toString();
		}

		private Grade(String display_string) {
			mDisplayString = display_string;
		}

		public String getDisplayName() {
			return mDisplayString;
		}

		public String getValue() {
			return name();
		}
	}

	public enum Section {
		Piccolo, Clarinet, AltoSax("Alto Sax"), TenorSax("Tenor Sax"), Trumpet, Trombone, Bass_Trombone(
				"Bass trombone"), Mellophone, Baritone, Sousaphone, Guard, DrumMajor(
				"Drum Major"), Staff, Drumline_Cymbals("Drumline: Cymbals"), Drumline_Tenors(
				"Drumline: Tenors"), Drumline_Snare("Drumline: Snare"), Drumline_Bass(
				"Drumline: Bass"), Twirler;

		private String mDisplayString;

		private Section() {
			mDisplayString = this.toString();
		}

		private Section(String display_string) {
			mDisplayString = display_string;
		}

		public String getDisplayName() {
			return mDisplayString;
		}

		public String getValue() {
			return name();
		}
	}

	public enum Type {
		Student, TA, Director;

		public boolean isDirector() {
			return this.equals(Director);
		}

		public boolean isStudent() {
			return this.equals(Student);
		}

		public boolean isTa() {
			return this.equals(TA);
		}
	}

	private static final long serialVersionUID = 1421557192976557704L;

	public static final String FIELD_TYPE = "type";

	public static final String FIELD_ID = "id";

	public static final String FIELD_PRIMARY_EMAIL = "email";

	public static final String FIELD_SECONDARY_EMAIL = "secondEmail";

	public static final String FIELD_UNIVERSITY_ID = "universityID";

	private Type type;

	private Grade grade;

	@Id
	private String id;

	private Email email;

	private Email secondEmail;

	private String universityID;

	private Section section;

	private String firstName;

	private String lastName;

	private int year;

	private String major;

	private String rank;

	private boolean showApproved;

	// number of minutes available from submitted TimeWorked forms
	private int minutesAvailable;

	// number of unexcused minutes absent so far this semester
	private int minutesMissed;

	/**
	 * Create users through UserController (DataModel.users().create(...)
	 */
	User() {

	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;

		// emails are unique, so compare based on them
		if (o instanceof User) {
			User u = (User) o;

			if (this.email == null || this.email.getEmail() == null
					|| u.email == null || u.email.getEmail() == null) {
				if (this.email == null && u.email == null) {
					return true;
				} else if (this.email.getEmail() == null
						&& u.email.getEmail() == null) {
					return true;
				} else {
					return false;
				}
			}

			if (this.email.equals(u.email))
				return true;
		}
		return false;
	}

	public String getFirstName() {
		return firstName;
	}

	public Grade getGrade() {

		return this.grade;
	}

	public String getId() {
		return this.id;
	}

	public String getLastName() {
		return lastName;
	}

	public String getMajor() {
		return major;
	}

	public int getMinutesAvailable() {
		return minutesAvailable;
	}

	public String getName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	/**
	 * Iowa State specific, instead use getId()
	 * 
	 * @return net id of the user
	 */
	@Deprecated
	public String getNetID() {
		return this.id;
	}

	public Email getPrimaryEmail() {
		return this.email;
	}

	public String getRank() {
		return this.rank;
	}

	public Email getSecondaryEmail() {
		return this.secondEmail;
	}

	public Section getSection() {
		return section;
	}

	public Type getType() {
		return this.type;
	}

	public String getUniversityID() {
		return universityID;
	}

	public int getYear() {
		return year;
	}

	@Override
	public int hashCode() {
		if (this.email == null || this.email.getEmail() == null)
			return 0;
		else
			return this.email.hashCode();
	}

	public boolean isShowApproved() {
		return this.showApproved;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public void setMinutesAvailable(int minutesAvailable) {
		this.minutesAvailable = minutesAvailable;
	}

	public void setPrimaryEmail(Email email) {
		this.email = email;
		// TODO https://github.com/curtisullerich/attendance/issues/170
		// This shouldn't be in the model, it should happen in the controllers
		// somehow
		if (email.getEmail().indexOf('%') == -1) {
			this.id = email.getEmail().substring(0,
					email.getEmail().indexOf('@'));
		} else {
			this.id = email.getEmail().substring(0,
					email.getEmail().indexOf('%'));
		}
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public void setSecondaryEmail(Email email) {
		this.secondEmail = email;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public void setShowApproved(boolean show) {
		this.showApproved = show;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setUniversityID(String new_universityID) {
		this.universityID = new_universityID;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public void setMinutesMissed(int minutes) {
		this.minutesMissed = minutes;
	}

	public int getMinutesMissed() {
		return this.minutesMissed;
	}

	@Override
	public String toString() {
		return getName();
	}

}
