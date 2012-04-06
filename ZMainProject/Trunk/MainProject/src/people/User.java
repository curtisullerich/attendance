package people;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

import javax.persistence.*;

import serverLogic.DatabaseUtil;

import attendance.*;

@Entity
public class User 
{
	@Id
	private Long id;
	
	//Field to tell what they are
	private String type;  //Student, Director, or TA
	
	//common fields
	private String netID;
	private String firstName;
	private String lastName;
	private String univID;
	private String hashedPassword;
	
	//student fields
	private String major;
	private String instrument;
	private String position;
	private String rank;
	private double grade;
	private String[] stringArray = {"this","is","a","string","array"};
	
	
	public String toHTML()
	{
		String ret = "";
		ret += "<p>Name: " + firstName + " " + lastName + "</p>\n";
		ret += "<p>NetID: " + netID + "</p>\n";
		return ret;
	}
	
	public User(String netID, String password, String firstName, String lastName, String univID, String type)
	{
		this.netID = netID;
		this.hashedPassword = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.univID = univID;
		this.type = type;
		this.id = hash(netID);
		
		
		//each student has a corresponding attendance report with field "netID" = the student's netID
		
		if (type.equalsIgnoreCase("Student"))
		{
			rank = "|";
			grade = 100.00;
			DatabaseUtil.addAttendanceReport(new AttendanceReport(netID));
		}
		else
		{
			rank = null;
			major = null;
			instrument = null;
			position = null;
		}
	}
	
	public User(String netID, String firstName, String lastName,
			String password, String univID, String major, String instrument, String type) 
	{
		//Common fields
		this.netID = netID;
		this.hashedPassword = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.univID = univID;
		this.type = type;
		this.id = hash(netID);
		
		//Student only fields
		if (type.equalsIgnoreCase("Student"))
		{
			this.major = major;
			this.instrument = instrument;
			this.grade = 100.00;
			this.rank = "|";
			DatabaseUtil.addAttendanceReport(new AttendanceReport(netID));
		}
		else
		{
			this.major = null;
			this.instrument = null;
			this.rank = null;
			
		}
	}
	
	public String toString()
	{
		return netID + " " + firstName + " " + lastName + " " + univID 
				+ " " + major + " " + instrument + " " + position + " " + rank + " " + grade;
	}
	
	public void addTardy(Tardy newTardy) {
		//get student's attendance report, add the tardy, and re-store it in the database
		AttendanceReport report = getAttendanceReport();
		report.addTardy(newTardy);
		DatabaseUtil.addAttendanceReport(report);
	}
	
	public void addAbsence(Absence newAbsence) {
		//get student's attendance report, add the absence, and re-store it in the database
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

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
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
	
	public String getLetterGrade() {
		String letterGrade;
		if(grade >= 90.00)
			letterGrade = "A";
		else if(grade >= 80.00)
			letterGrade = "B";
		else if(grade >= 70.00)
			letterGrade = "C";
		else if(grade >= 60.00)
			letterGrade = "D";
		else
			letterGrade = "F";
		
		if(grade%10 > 6.66 && grade < 90.00)
			letterGrade += "+";
		else if(grade%10 < 3.33)
			letterGrade += "-";
		
		return letterGrade;
	}	
	
	public void calculateGrade() {
		//big crazy function to calculate grade based off absences, tardies, and excuses.
	}

	public long hash(String netID)
	{
		try {
			MessageDigest cript = MessageDigest.getInstance("SHA-1");
			cript.reset();
			cript.update(netID.getBytes("utf8"));
			BigInteger bigot =  new BigInteger(cript.digest());
			//Something about things
			return bigot.longValue();
			
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
