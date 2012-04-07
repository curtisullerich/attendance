package people;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import serverLogic.DatabaseUtil;
import attendance.Absence;
import attendance.AttendanceReport;
import attendance.Tardy;

@Entity
public class User {
	@Id
	private Long id;

	// Field to tell what they are
	private String type; // Student, Director, or TA

	// common fields
	private String netID;
	private String firstName;
	private String lastName;
	private String univID;
	private String hashedPassword;

	// student fields
	private String major;
	private String section;
	private int year;
	private String rank;
	private double grade;

	public String toHTML() {
		String ret = "";
		ret += "<p>Name: " + firstName + " " + lastName + "</p>\n";
		ret += "<p>NetID: " + netID + "</p>\n";
		return ret;
	}

	public User(String netID, String password, String firstName,
			String lastName, String univID, String type, String major,
			String section, int year) {
		this.netID = netID;
		this.hashedPassword = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.univID = univID;
		this.type = type;
		this.id = hash(netID);
		this.major = major;
		this.section = section;
		this.year = year;

		// each student has a corresponding attendance report with field "netID"
		// = the student's netID

		if (type.equalsIgnoreCase("Student")) {
			rank = "|";
			grade = 100.00;
			DatabaseUtil.addAttendanceReport(new AttendanceReport(netID));
		} else {
			rank = null;
			major = null;
			section = null;
		}
	}

	public String toString() {
		return netID + " " + firstName + " " + lastName + " " + univID + " "
				+ major + " " + section + " " + year + " " + rank + " " + grade;
	}

	public void addTardy(Tardy newTardy) {
		// get student's attendance report, add the tardy, and re-store it in
		// the database
		AttendanceReport report = getAttendanceReport();
		report.addTardy(newTardy);
		DatabaseUtil.addAttendanceReport(report);
	}

	public void addAbsence(Absence newAbsence) {
		// get student's attendance report, add the absence, and re-store it in
		// the database
		AttendanceReport report = getAttendanceReport();
		report.addAbsence(newAbsence);
		DatabaseUtil.addAttendanceReport(report);
	}

	public AttendanceReport getAttendanceReport() {
		return DatabaseUtil.getAttendanceReport(netID);
	}

	public List<Absence> getAbsences() {
		return getAttendanceReport().getAbsences();
	}

	public List<Tardy> getTardies() {
		return getAttendanceReport().getTardies();
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNetID() {
		return netID;
	}

	public void setNetID(String netID) {
		this.netID = netID;
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

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUnivID() {
		return univID;
	}

	public void setUnivID(String univID) {
		this.univID = univID;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public double getGrade() {
		return grade;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getLetterGrade() {
		String letterGrade;
		if (grade >= 90.00)
			letterGrade = "A";
		else if (grade >= 80.00)
			letterGrade = "B";
		else if (grade >= 70.00)
			letterGrade = "C";
		else if (grade >= 60.00)
			letterGrade = "D";
		else
			letterGrade = "F";

		if (grade % 10 > (20.0 / 3.0) && grade < 90.00)
			letterGrade += "+";
		else if (grade % 10 < (10.0 / 3.0) && grade < 100.00)
			letterGrade += "-";

		return letterGrade;
	}

	public void calculateGrade() {
		// big crazy function to calculate grade based off absences, tardies,
		// and excuses.
	}

	public long hash(String netID) {
		try {
			MessageDigest cript = MessageDigest.getInstance("SHA-1");
			cript.reset();
			cript.update(netID.getBytes("utf8"));
			BigInteger bigot = new BigInteger(cript.digest());
			// Something about things
			return bigot.longValue();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
