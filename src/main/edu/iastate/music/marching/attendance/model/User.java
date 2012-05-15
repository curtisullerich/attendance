package edu.iastate.music.marching.attendance.model;

import java.io.Serializable;
import java.util.Set;

import com.google.code.twig.annotation.Activate;
import com.google.code.twig.annotation.Child;
import com.google.code.twig.annotation.Index;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1421557192976557705L;

	public static final String FIELD_TYPE = "type";

	public static final String FIELD_NETID = "netID";

	public static final String FIELD_GOOGLEUSER = "google_user";

	public enum Type {
		Student, TA, Director;

		public boolean isStudent() {
			return this.equals(Student);
		}

		public boolean isTa() {
			return this.equals(TA);
		}

		public boolean isDirector() {
			return this.equals(Director);
		}
	}

	public enum Section {
		Piccolo, Clarinet, AltoSax("Alto Sax"), TenorSax("Tenor Sax"), Trumpet, Trombone, Mellophone, Baritone, Sousaphone, Guard, DrumMajor(
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

	/**
	 * Create users through UserController (DataModel.users().create(...)
	 */
	User() {

	}

	@Index
	private com.google.appengine.api.users.User google_user;
	
	private Type type;
	
	@Index
	private String netID;

	private int universityID;

	@Child
	@Activate(0)
	private Set<Absence> absences;

	@Child
	@Activate(0)
	private Set<Form> forms;

	private Section section;

	private String firstName;

	private String lastName;

	private int year;

	private String major;
	
	private String rank;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	public String getNetID() {
		return this.netID;
	}

	public void setNetID(String netID) {
		this.netID = netID;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public void setUniversityID(int new_universityID) {
		this.universityID = new_universityID;
	}

	public int getUniversityID() {
		return universityID;
	}

	public String getRank() {
		return this.rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public void setGoogleUser(com.google.appengine.api.users.User google_user) {
		this.google_user = google_user;
	}
	
	public com.google.appengine.api.users.User getGoogleUser() {
		return this.google_user;
	}

}
