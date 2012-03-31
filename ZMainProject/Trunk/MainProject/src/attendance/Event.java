package attendance;

import javax.persistence.*;
import time.Date;

import time.Time;
/**
 * 
 * @author Yifei Zhu
 *
 */
@Entity
public class Event 
{
	//private Time startTime;
	//private Time endTime;
	
	//Needs to be stored as Strings so they can go in the database
	private String startTime;
	private String endTime;
	private String date;
	
	
	//Id: take the date and start and end times and smash them together
	@Id
	private Long id;
	
	public Event(Time start, Time end)
	{
		startTime = start.get24Format();
		endTime = end.get24Format();
		date = start.getDate().toString();
		id = toId(start, end);
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Long getId(){return id;}
	public Long toId(Time start, Time end)
	{
		//All the numbers will be "date StartTime EndTime" with no spaces and just numbers
		String allTheNumbers = "";
		allTheNumbers += date;
		allTheNumbers += startTime;
		allTheNumbers += endTime;
		allTheNumbers = allTheNumbers.replaceAll(":","");
		allTheNumbers = allTheNumbers.replaceAll("-","");
		return new Long(allTheNumbers);
	}
	
	
}
