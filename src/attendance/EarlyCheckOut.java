package attendance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import time.Time;
/**
 * 
 * @author Yifei Zhu, Todd Wegter
 *
 */

//get and set messages with key field with corresponding times and type

@Entity
public class EarlyCheckOut{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //creates id for entry
	private Long id;

	private String netID;
	private String checkOutTime;
	private String status;
	private String type;
	
	public EarlyCheckOut(Time checkOutTime){
		this.checkOutTime = checkOutTime.toString(24);
		this.status = "pending";
	}

	public String toString()
	{      
		//Should be in the form "year-month-day hour:minute:second type isApproved"
		return getTime().getDate().toString() + " " + getTime().get24Format() + " " + type + " " + status;
	}
	
	public Time getTime() {
		return new Time(checkOutTime);
	}

	public void setTime(Time time) {
		this.checkOutTime = time.toString(24);
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	
	/**
	 * 
	 * Returns true if this EarlyCheckOut was during the given event.
	 * 
	 * @author Curtis Ullerich
	 * @date 4/9/12
	 * @param event
	 * @return
	 */
	public boolean isDuringEvent(Event event) {
		//TODO we should probably test this.
		return (this.getTime().compareTo(event.getStartTime()) >= 0 
				&& this.getTime().compareTo(event.getEndTime())<=0);
		
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

	public String getNetID() {
		return netID;
	}

	public void setNetID(String netID) {
		this.netID = netID;
	}
}
