package attendance;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import time.*;
/**
 * 
 * @author Yifei Zhu, Todd Wegter
 *
 */

//get and set messages with key field with corresponding times and type

@Entity
public class Absence{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //creates id for entry
	private Long id;
	
	//The netID for the student this Absence belongs to
	private String netID;
	private String startTime;
	private String endTime;
	private boolean isApproved;
	//Either Rehearsal or Performance
	private String type;
	
	public Absence(String netID, Time startTime, Time endTime, String type) {
		this.netID = netID;
		this.startTime = startTime.toString(24);
		this.endTime = endTime.toString(24);
		this.isApproved = false;
		setType(type);
	}

//	public Absence(String dbAbsence)
//	{	
//		String[] info = dbAbsence.split(" ");
//		Time start = new Time(info[0], info[1]);
//		Time end = new Time (info[0], info[2]);
//		this.setStartTime(start);
//		this.setEndTime(end);
//		this.type = info[3];
//		this.isApproved = Boolean.parseBoolean(info[4]);
//	}
	
	public String toString()
	{
		//                                        start Time              End Time
		//                                             |                      |        
		//Should be in the form "year-month-day hour:minute:second hour:minute:second type isApproved"
		return getStartTime().getDate().toString() + " " + getStartTime().get24Format() 
				+ " " + getEndTime().get24Format() + " " + type + " " + isApproved;
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

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		if(type.equalsIgnoreCase("performance"))
			this.type = "performance";
		else
			this.type = "rehearsal";
	}
	
	public boolean isRehearsal(){
		return type.equalsIgnoreCase("rehearsal");
	}
	
	public boolean isPerformance(){
		return type.equalsIgnoreCase("performance");
	}
	
	public void setID(long id){
		this.id = id;
	}
	
	public long getID(){
		return id;
	}

}
