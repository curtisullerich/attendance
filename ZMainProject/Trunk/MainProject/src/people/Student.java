package people;

import java.util.*;
import javax.persistence.*;
import forms.*;
import attendance.*;
import javax.persistence.*;


/**
 * 
 * @author Brandon AND ONLY BRANDON!
 * 
 */
@Entity(name = "Student")
@DiscriminatorValue("Student")
public class Student extends Person
{
	//Need a way to store the absences -> actual dates
	//AttendanceRecord class?
	
	//This is where absences and tardies are stored
	//private AttendanceReport report;
	
	//Since we can't store large objects in the database we will store the attendance the same 
	//way it's stored in the local app
	private String absenceInfo;
	private String tardyInfo;
	
	//This field can be used to determine the grade without having to
	//iterate thru all the absences and count them
	private int numAbsences = 0;
	private String major;
	private String instrument;
	private String position;
	private String rank;

	public Student(String netID, String firstName, String lastName,
			String password, String univID, String major, String instrument) {
		super(netID, firstName, lastName, password, univID);

		this.major = major;
		this.instrument = instrument;
		this.rank = "|";
		this.absenceInfo = "";
		this.tardyInfo = "";
		this.id = hash(netID);
	}

	public Student(String netID, String password, String firstName,
			String lastName, String univID) {
		super(netID, password, firstName, lastName, univID);
		rank = "|";
		this.absenceInfo = "";
		this.tardyInfo = "";
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
		//toString is in the form: "Date checkInTime" the student should know everything else it needs
		//Extra space at the end so we can add more
		tardyInfo += newTardy.toString() + " ";
	}

	public void addAbsence(Absence newAbsence) {
		//Same idea as addTardy with a form of:
		//"Date startTime endTime"
		absenceInfo += newAbsence.toString() + " ";
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
