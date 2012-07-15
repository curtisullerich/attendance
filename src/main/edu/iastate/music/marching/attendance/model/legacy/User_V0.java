package edu.iastate.music.marching.attendance.model.legacy;

import java.io.ObjectInputStream;
import java.io.Serializable;

import com.google.appengine.api.users.User;
import com.google.code.twig.annotation.Activate;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Version;

@Activate
@Version(0)
@Entity(kind = "edu.iastate.music.marching.attendance.model.User", allocateIdsBy = 10)
public class User_V0 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1421557192976557705L;

	public static final String FIELD_TYPE = "type";

	public static final String FIELD_ID = "id";

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

	public enum Grade {
		A, Aminus("A-"), Bplus("B+"), B, Bminus("B-"), Cplus("C+"), C, Cminus(
				"C-"), Dplus("D+"), D, Dminus("D-"), F;
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
	User_V0() {

	}

	@Index
	private com.google.appengine.api.users.User google_user;

	@Index
	private String netID;

	private Type type;

	private Grade grade;

	@Id
	private String id;

	private int universityID;

	// @Activate(0)
	// private List<Absence> absences;
	//
	// @Activate(0)
	// private List<Form> forms;

	private Section section;

	private String firstName;

	private String lastName;

	private int year;

	private String major;

	private String rank;

	private boolean showApproved;

	private int minutesAvailable;

	public int getMinutesAvailable() {
		return minutesAvailable;
	}

	public void setMinutesAvailable(int minutesAvailable) {
		this.minutesAvailable = minutesAvailable;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public boolean isShowApproved() {
		return this.showApproved;
	}

	public void setShowApproved(boolean show) {
		this.showApproved = show;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	public Grade getGrade() {

		return this.grade;
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

	public String getId() {
		return this.id;
	}

	public String getNetID() {
		return this.netID;
	}

	public User getGoogleUser() {
		return this.google_user;
	}

//	private void writeObject(ObjectOutputStream oos) throws IOException {
//		// default serialization
//		oos.defaultWriteObject();
//		// write the object
//		List loc = new ArrayList();
//		loc.add(location.x);
//		loc.add(location.y);
//		loc.add(location.z);
//		loc.add(location.uid);
//		oos.writeObject(loc);
//	}
//
//	private void readObject(ObjectInputStream ois)
//			throws ClassNotFoundException, IOException {
//		// default deserialization
//		ois.
//		ois.defaultReadObject();
//		List loc = (List) ois.readObject(); // Replace with real deserialization
//		location = new Location(loc.get(0), loc.get(1), loc.get(2), loc.get(3));
//		// ... more code
//
//	}
}
