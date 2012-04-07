package attendance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import time.Time;
/**
 *
 * @author Todd Wegter, Yifei Zhu
 *
 */

//get and set messages with key field with corresponding times and type

@Entity
public class Tardy {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //creates id for entry
	private Long id;

	private String checkInTime;
	private boolean approved;
	//Either rehearsal or performance or unknown if there was no Event
	private String type;
	
	public Tardy(Time time, String type)
	{
		this.checkInTime = time.toString();
		approved = false;
	}
	
//	public Tardy(String dbTardy)
//	{
//		String[] tardy = dbTardy.split(" ");
//		setTime(new Time(tardy[0], tardy[1]));
//		approved = false;
//	}
	
	public String toString()
	{
		//Should be in the form "year-month-day hour:minute:second type isApproved"
		return getTime().getDate().toString() + " " + getTime().get24Format() + " " + type + " " + approved;
	}
	
	public Time getTime() {
		return new Time(checkInTime);
	}

	public void setTime(Time time) {
		this.checkInTime = time.toString(24);
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
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
