package people;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.persistence.*;

import attendance.*;

@Entity
public class User 
{
	@Id
	private Long id;
	
	//Fields from the Person class
	private String netID;
	
	private String firstName;
	private String lastName;
	private String hashedUnivID;
	
	private String hashedPassword;
	
	//Fields from the Student class
	private String absenceInfo;
	private String tardyInfo;
	
	private String major;
	private String instrument;
	private String position;
	private String rank;
	
	//Field to tell what they are
	private String type;
	
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
		this.hashedUnivID = univID;
		this.type = type;
		id = hash(netID);
		
		if (type.equalsIgnoreCase("Student"))
		{
			rank = "|";
			this.absenceInfo = "";
			this.tardyInfo = "";
		}
		else
		{
			rank = null;
			absenceInfo = null;
			tardyInfo = null;
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
		this.hashedUnivID = univID;
		this.type = type;
		this.id = hash(netID);
		
		//Student only fields
		if (type.equalsIgnoreCase("Student"))
		{
			this.major = major;
			this.instrument = instrument;
			this.rank = "|";
			this.absenceInfo = "";
			this.tardyInfo = "";
		}
		else
		{
			this.major = null;
			this.instrument = null;
			this.rank = null;
			this.absenceInfo = null;
			this.tardyInfo = null;
		}
	}
	
	public String toString()
	{
		return netID + " " + firstName + " " + lastName + " " + hashedUnivID 
				+ " " + major + " " + instrument + " " + position + " " + rank;
	}
	
	public void addTardy(Tardy newTardy) {
		//toString is in the form: "Date checkInTime" the student should know everything else it needs
		//Delimited by * for easy parsablitity
		//split by * to get each tardy, split that by " " to get each part of the tardy
		tardyInfo += newTardy.toString() + "*";
	}
	
	public void addAbsence(Absence newAbsence) {
		//Same idea as addTardy with a form of:
		//"Date startTime endTime" again, delimited by parsability
		absenceInfo += newAbsence.toString() + "*";
	}
	
	public AttendanceReport toAbsenceObject()
	{
		AttendanceReport myReport = new AttendanceReport();
		//Each absence or tardy is delimited from the others by the *
		String[] absences = absenceInfo.split("*");
		String[] tardies = tardyInfo.split("*");
		for (int i = 0; i < absences.length; i++)
		{
			myReport.addAbsence(new Absence(absences[i]));
		}
		for (int i = 0; i < tardies.length; i++)
		{
			myReport.addTardy(new Tardy(tardies[i]));
		}
		return myReport;
		
	}
	
	public String getAbsenceInfo() {
		return absenceInfo;
	}

	public String getTardyInfo() {
		return tardyInfo;
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

		public String getHashedUnivID() {
			return hashedUnivID;
		}

		public void setHashedUnivID(String hashedUnivID) {
			this.hashedUnivID = hashedUnivID;
		}

		public String getHashedPassword() {
			return hashedPassword;
		}

		public void setHashedPassword(String hashedPassword) {
			this.hashedPassword = hashedPassword;
		}
	

}
