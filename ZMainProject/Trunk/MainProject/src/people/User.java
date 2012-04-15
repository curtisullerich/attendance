package people;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import comment.Message;
import forms.Form;

import serverLogic.DatabaseUtil;
import attendance.Absence;
import attendance.AttendanceReport;
import attendance.EarlyCheckOut;
import attendance.Event;
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
		this.id = (long) netID.hashCode();
		this.major = major;
		this.section = section;
		this.year = year;
		this.rank = "|";
	}

	public long getID() {
		
		return this.id;
	}
	
	public void copyAllFrom(User other) {
		this.type=other.type;
		this.netID=other.netID;
		this.firstName=other.firstName;
		this.lastName=other.lastName;
		this.univID=other.univID;
		this.hashedPassword=other.hashedPassword;
		this.major=other.major;
		this.section=other.section;
		this.year=other.year;
		this.rank=other.rank;
	}
	
	public String toString() {
		return netID + " " + firstName + " " + lastName + " " + univID + " "
				+ major + " " + section + " " + year + " " + rank + " " ;
	}
	
	public String toStringTA() {
		return "TA" + " " + netID + " " + firstName + " " + lastName + " " + hashedPassword + " " + rank;
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

	public List<EarlyCheckOut> getEarlyCheckOuts() {
		return getAttendanceReport().getEarlyCheckOuts();
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * Returns a string with the status of this user for the given event
	 * 
	 * @author Curtis Ullerich
	 * @date 4/9/12
	 * @return
	 */
	public String eventStatus(Event event) {
		
		if (this.type.equalsIgnoreCase("director")){
			return "present";
		}
		List<Tardy> tardies = this.getTardies();
		List<EarlyCheckOut> ecos = this.getEarlyCheckOuts();
		List<Absence> absences = this.getAbsences();

		String status = "present";
		for (Absence a : absences) {
			if (a.isDuringEvent(event)) {
				status = a.getStatus() + " " + "absent";
				break;
			}
		}

		for (EarlyCheckOut e : ecos) {
			if (e.isDuringEvent(event)) {
				status = e.getStatus() + " " + "early checkout";
				break;
			}
		}

		for (Tardy t : tardies) {
			if (t.isDuringEvent(event)) {
				status = t.getStatus() + " " + "tardy";
				break;
			}
		}
		return status;
	}
	
	
	/**
	 * Returns a string of the format "type,id" for this user's
	 * attendance item during the given event
	 * 
	 * @author Curtis Ullerich
	 * @date 4/9/12
	 * @return
	 */
	public String eventAttendanceItem(Event event) {
		
		if (this.type.equalsIgnoreCase("director")) {
			return "none,none";
		}
		List<Tardy> tardies = this.getTardies();
		List<EarlyCheckOut> ecos = this.getEarlyCheckOuts();
		List<Absence> absences = this.getAbsences();

		String type = "none";
		String id = "none";
		for (Absence a : absences) {
			if (a.isDuringEvent(event)) {
				type = "Absence";
				id = a.getID() + "";
				break;
			}
		}

		for (EarlyCheckOut e : ecos) {
			if (e.isDuringEvent(event)) {
				type = "EarlyCheckOut";
				id = e.getID() + "";
				break;
			}
		}

		for (Tardy t : tardies) {
			if (t.isDuringEvent(event)) {
				type = "Tardy";
				id = t.getID() + "";
				break;
			}
		}
		return type + "," + id;
	}
	
	
	
//
//	public long hash(String netID) {
//		try {
//			MessageDigest cript = MessageDigest.getInstance("SHA-1");
//			cript.reset();
//			cript.update(netID.getBytes("utf8"));
//			BigInteger bigot = new BigInteger(cript.digest());
//			// Something about things
//			return bigot.longValue();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
//	}

	@Override
	public boolean equals(Object other) {
		if (other == null)
			return false;
		if (other.getClass() != this.getClass())
			return false;
		User o = (User) other;
		return o.getNetID().equalsIgnoreCase(this.netID);
	}

	@Override
	public int hashCode() {
		return netID.hashCode();
	}

	public String getAttendanceHtml() {
		// TODO

		// loop through all events and print the information in the format below
		return "<tr><td>8/21</td><td>  </td><td>  </td><td>  </td><td>  </td><td><button onClick=\"sendMeToMyMessages();\">Messages</button></tr>";

	}
	
	/**
	 * @author Yifei Zhu
	 * reh.tardy=1
	 * per.tardy=2
	 * reh.absence =3
	 * per.absence = 6
	 * per.reh.absence`	=3
	 * 
	 * @return return the letter grade
	 */
	public String getGrade()
	{
		int count=0;
		List<Tardy> tardys=this.getTardies();
		for(Tardy t : tardys)
		{
			//rehearsal
			if(!t.getStatus().equals("excuse"))
			{
				if(t.getType().equals("rehearsal"))
				{
					count+=1;
				}
				else if(t.getType().equals("performance"))
				{
					count+=2;
				}
			}
		}
		List<Absence> absences = this.getAbsences();
		//absences
		for(Absence a : absences)
		{
			if(!a.getStatus().equals("excuse"))
			{
				if(a.getType().equals("rehearsal"))
				{
					count+=3;
				}
				if(a.getType().equals("performance"))
				{
					count+=6;
				}
			}
		}
		return getLetterGrade(count);
	}
	
	/**
	 * A  : 0
	 * A- : 1
	 * 
	 * 
	 * @author Yifei Zhu
	 * Base on the count, return letter grade 
	 * @param count  
	 * @return -- return letter grade
	 */
	String getLetterGrade(int count)
	{
		if(count==0)
			return "A";
		else if(count ==1)
			return "A-";
		else if(count==2)
			return "B+";
		else if(count==3)
			return "B";
		else if(count==4)
			return "B-";
		else if(count==5)
			return "C+";
		else if(count==6)
			return "C";
		else if(count==7)
			return "C-";
		else if(count==8)
			return "D+";
		else if(count==9)
			return "D";
		else if(count==10)
			return "D-";
		else 
			return "F";
	}
	/**
	 * Sort the given list of Users by the given Comparator (Section, LastName, etc.) in descending
	 * order
	 * 
	 * @param comp
	 *            - Comparator for comparing the forms
	 * @param users           
	 * 			  - list of users to be sorted
	 */
	public static void sortUsersDescending(Comparator<User> comp, List<User> users) {
		for (int i = 0; i < users.size() - 1; i++) {
			for (int j = i + 1; j < users.size(); j++) {
				if (comp.compare(users.get(i), users.get(j)) < 0) { // larger
																	// first
					User temp = users.get(i);
					users.set(i, users.get(j));
					users.set(j, temp);
				}
			}
		}
		return;
	}

	/**
	 * Sort the given list of Users by the given Comparator (Section, LastName, etc.) in ascending
	 * order
	 * 
	 * @param comp
	 *            - Comparator for comparing the forms
	 * @param users           
	 * 			  - list of users to be sorted
	 */
	public static void sortUsersAscending(final Comparator<User> comp, List<User> users) {
		sortUsersDescending(new Comparator<User>() {

			@Override
			public int compare(User o1, User o2) {
				return -comp.compare(o1, o2);
			}
			
		}, users);
	}
}
