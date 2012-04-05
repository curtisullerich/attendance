package attendance;

import javax.persistence.*;
import time.Date;

import time.Time;
/**
 * 
 * @author Todd Wegter, Yifei Zhu
 *
 */

//get and set messages with key field with corresponding times and type
//create message key format and creator method

@Entity
public class Event 
{
	//Id: take the date and start and end times and smash them together
	@Id
	private Long id;
	
	//private Time startTime;
	//private Time endTime;
	
	//Needs to be stored as Strings so they can go in the database
	private String startTime;
	private String endTime;
	private String type; //denotes type of event: "rehearsal" or "performance"
	
	public Event(Time start, Time end, String type)
	{
		startTime = start.toString(24);
		endTime = end.toString(24);
		id = toId(start, end);
		setType(type);
	}

	public String toString()
	{
		//                                        start Time              End Time
		//                                             |                      |        
		//Should be in the form "year-month-day hour:minute:second hour:minute:second type"
		return getStartTime().getDate().toString() + " " + getStartTime().get24Format() 
				+ " " + getEndTime().get24Format() + " " + type;
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
	
	public Date getDate(){
		return new Time(startTime).getDate();
	}
	
	public void setDate(Date date){
		Time start = getStartTime();
		Time end = getEndTime();
		this.startTime = new Time(start.getHour(), start.getMinute(), date).toString(24);
		this.endTime = new Time(end.getHour(), end.getMinute(), date).toString(24);
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
	
	public Long getId(){
		return id;
	}
	
	public Long toId(Time start, Time end)
	{
		//All the numbers will be "date StartTime EndTime" with no spaces and just numbers
		String allTheNumbers = "";
		allTheNumbers += new Time(startTime).getDate().toString();
		allTheNumbers += getStartTime().get24Format();
		allTheNumbers += getEndTime().get24Format();;
		allTheNumbers = allTheNumbers.replaceAll(":","");
		allTheNumbers = allTheNumbers.replaceAll("-","");
		return new Long(allTheNumbers);
	}
	
	
}
