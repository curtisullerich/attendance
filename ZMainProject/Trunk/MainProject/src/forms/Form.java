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
	private String additionalInfo;
	
	//Strings to be used by Form B
	private String dept;
	private String course;
	private String section;
	private String building;
	//What are my options chief?
	//FormA, FormB, FormC, FormD
	private String type; 

	//This constructor is called by Form A, C, and D
	public Form(String netID, String reason, Time startTime, Time endTime,
			String type) {
		this.netID = netID;
		this.startTime = startTime.toString(24);
		this.endTime = endTime.toString(24);
		this.reason = reason;
		this.type = type;
		this.status = "pending";
		dept = "";
		course = "";
		section = "";
		building = "";
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
			String type, File attachedFile, String additionalInfo) {
		this.netID = netID;
		this.startTime = startTime.toString(24);
		this.endTime = endTime.toString(24);
		this.reason = reason;
		this.type = type;
		//setFile(attachedFile);
		this.additionalInfo = additionalInfo;
		this.status="pending";
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
	
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	public String toString()
	{
		return "ID:"+this.netID+" start time:"+this.startTime+" end time:"+this.endTime
				+" approved?:"+this.status;
	}

	public String getDeptartment() {return dept;}
	public String getCourse() {return course;}
	public String getSection() {return section;}
	public String getBuilding() {return building;}
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