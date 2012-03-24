package people;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import attendance.Absence;
import attendance.AttendanceReport;
import attendance.Tardy;

/**
 * 
 * @author Brandon AND ONLY BRANDON!
 * 
 */
@Entity(name = "Student")
@DiscriminatorValue("Student")
public class Student extends Person {
	// Need a way to store the absences -> actual dates
	// AttendanceRecord class?

	// This is where absences and tardies are stored
	private AttendanceReport report;

	// This field can be used to determine the grade without having to
	// iterate thru all the absences and count them
	private String major;
	private String instrument;
	private String position;
	private String rank;

	public Student(String netID, String firstName, String lastName,
			String password, String major, String instrument) {
		super(netID, firstName, lastName, password);

		this.major = major;
		this.instrument = instrument;
		this.rank = "|";
		this.report = new AttendanceReport();
		this.id = hash(netID);
	}

	public Student(String netID, String password, String firstName,
			String lastName) {
		super(netID, password, firstName, lastName);
		rank = "|";
		report = new AttendanceReport();
		this.id = hash(netID);
	}

	public String toString() {
		return super.toString() + " " + major + " " + instrument + " " + position
				+ " " + rank;
	}

	public String toHTML() {
		return "";
	}
	
	public void addTardy(Tardy newTardy) {
		report.addTardy(newTardy);
	}

	public void addAbsence(Absence newAbsence) {
		report.addAbsence(newAbsence);
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public void getInstrument(String instrument) {
		this.instrument = instrument;
	}
	
	public String setInstrument() {
		return this.instrument;
	}
	
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

}
