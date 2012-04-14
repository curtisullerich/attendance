package forms;

import java.io.File;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import serverLogic.DatabaseUtil;
import time.Time;

/**
 * 
 * @author Yifei Zhu
 * @Fixed it Todd Wegter
 * 
 */

@Entity
public class Form {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// creates id for entry
	private Long id;

	private String netID;
	private String status;
	private String reason;
	private String startTime;
	private String endTime;
	//private String additionalInfo;
	
	//Strings to be used by Form B
	private String dept;
	private String course;
	private String section;
	private String building;
	
	//String to be used by Form D
	private String emailTo;
	private int hoursWorked;
	
	//What are my options chief?
	//FormA, FormB, FormC, FormD
	private String type; 

	//This constructor is called by Form A and C
	public Form(String netID, String reason, Time startTime, Time endTime,
			String type) {
		this.netID = netID;
		this.startTime = startTime.toString(24);
		this.endTime = endTime.toString(24);
		this.reason = reason;
		this.type = type;
		this.status = "pending";
		this.dept = "";
		this.course = "";
		this.section = "";
		this.building = "";
	}
	//This constructor is called by Form D
	public Form(String netID, String reason, Time startTime, Time endTime, String emailTo, int hoursWorked,
			String type) {
		this.netID = netID;
		this.startTime = startTime.toString(24);
		this.endTime = endTime.toString(24);
		this.reason = reason;
		this.type = type;
		this.status = "pending";
		this.dept = "";
		this.course = "";
		this.section = "";
		this.building = "";
		this.emailTo = emailTo;
		this.hoursWorked = hoursWorked;
	}
	//This constructor is only for Form B
	public Form(String netID, String reason, Time startTime, Time endTime,
			String dept, String course, String section, String building, String type) {
		this.netID = netID;
		this.startTime = startTime.toString(24);
		this.endTime = endTime.toString(24);
		this.reason = reason;
		this.type = type;
		this.status = "pending";
		this.dept = dept;
		this.course = course;
		this.section = section;
		this.building = building;
	}
	public Form(String netID, String reason, Time startTime, Time endTime,
			String type, File attachedFile) {
		this.netID = netID;
		this.startTime = startTime.toString(24);
		this.endTime = endTime.toString(24);
		this.reason = reason;
		this.type = type;
		//setFile(attachedFile);
		this.status="pending";
	}

	
	public Long getID()
	{
		return this.id;
	}
	public String getNetID() {
		return netID;
	}

	public void setNetID(String netID) {
		this.netID = netID;
	}

	public Time getStartTime() {
		return new Time(startTime);
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime.toString(24);
	}

	public Time getEndTime() {
		return new Time(endTime);
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime.toString(24);
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String toString()
	{
		return "ID:"+this.netID+" start time:"+this.startTime+" end time:"+this.endTime
				+" approved?:"+this.status;
	}
	
	public String durationToString()
	{
		String[] start = startTime.split(" ");
		String[] end = endTime.split(" ");
		if (start[1].equals("00:01") && !end[1].equals("23:59"))
		{
			String hour = end[1].substring(0, 2);
			String[] hourMin = end[1].split(":");
			int hourNum = Integer.parseInt(hourMin[0]);
			if (hourNum > 12)
			{
				hourMin[0] = "" + (hourNum - 12);
				return "Until " + hourMin[0] + ":" + hourMin[1] + "PM"; 
			}
			else
			{
				return "Until " + ((hourMin[0].length() == 1) ? hourMin[0] : hourMin[0].charAt(1)) + ":" + hourMin[1]+ "AM";
			}
		}
		else if (!start[1].equals("00:01") && end[1].equals("23:59"))
		{
			String hour = start[1].substring(0, 2);
			String[] hourMin = start[1].split(":");
			int hourNum = Integer.parseInt(hour);
			if (hourNum > 12)
			{
				hourMin[0] = "" + (hourNum - 12);
				return "Starting At " + hourMin[0] + ":" + hourMin[1] + "PM"; 
			}
			else
			{
				return "Starting At " + ((start[1].length() == 1) ? start[1] : start[1].charAt(1)) +":" + hourMin[1] + "AM";
			}
			//return "Starting At " + start[1];
		}
		else
		{
			return "Completely Miss";
		}	
	}

	public String getDeptartment() {return dept;}
	public String getCourse() {return course;}
	public String getSection() {return section;}
	public String getBuilding() {return building;}
	public String getEmail() {return emailTo;}
	public int getHours() {return hoursWorked;}
//	public File getFile() {
//		return DatabaseUtil.getFile(attachedFile); // get file with this key
//													// from database
//	}
//
//	public void setFile(File attachedFile) {
//		this.attachedFile = DatabaseUtil.addFile(attachedFile); // return key
//																// for file
//	}
}