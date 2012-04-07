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
 * @author Yifei Zhu, Todd Wegter
 * 
 */

@Entity
public class Form {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// creates id for entry
	private Long id;

	private String netID;
	private boolean approved;
	private String reason;
	private String startTime;
	private String endTime;
	private String attachedFile; // key for attached file
	private String additionalInfo;

	private String type; // rehearsal absence, performance absence, class
							// conflict

	public Form(String netID, String reason, Time startTime, Time endTime,
			String type) {
		this.netID = netID;
		this.startTime = startTime.toString(24);
		this.endTime = endTime.toString(24);
		this.reason = reason;
		this.type = type;
		approved = false;
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
		approved = false;
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

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

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