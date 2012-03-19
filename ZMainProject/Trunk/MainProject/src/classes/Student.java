package classes;

import javax.persistence.Entity;
import javax.persistence.*;

/**
 * 
 * @author Brandon AND ONLY BRANDON!
 *
 */
@Entity(name="Student")
@DiscriminatorValue("Student")
public class Student extends Person
{
	//Need a way to store the absences -> actual dates
	//AttendanceRecord class?
	
	//This is where absences and tardies are stored
	private AttendanceReport report;	
	
	//This field can be used to determine the grade without having to
	//iterate thru all the absences and count them
	private int numAbsences = 0;
	private String major;
	private String advisor;
	private String position;
	private String rank;

	public Student(String netID, String firstName, String lastName,
			String password, String major, String advisor, String position) {
		super(netID, firstName, lastName, password, major, advisor, position);

		this.major = major;
		this.advisor = advisor;
		this.position = position;
		rank = "|";
		report = new AttendanceReport();
		this.id = hash(netID);
	}
	
	public Student(String netID, String password, String firstName, String lastName)
	{
		super(netID, password, firstName, lastName);
		rank = "|";
		report = new AttendanceReport();
		this.id = hash(netID);
	}
	
	public String toString()
	{
		return super.toString() + " " + major + " " + advisor + " " + position + " " + rank;
	}
	
	public int getNumAbsences() {
		return numAbsences;
	}

	public void setNumAbsences(int numAbsences) {
		this.numAbsences = numAbsences;
	}
	
	public void addNumAbsences()
	{
		this.numAbsences++;
	}
	
	public void addTardy(Tardy newTardy)
	{
		report.addTardy(newTardy);
	}
	
	public void addAbsence(Absence newAbsence)
	{
		report.addAbsence(newAbsence);
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getAdvisor() {
		return advisor;
	}

	public void setAdvisor(String advisor) {
		this.advisor = advisor;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

}
