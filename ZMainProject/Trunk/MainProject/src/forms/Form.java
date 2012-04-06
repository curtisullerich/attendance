package forms;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import people.User;
import time.Date;
import time.Time;

/**
 * 
 * @author Yifei Zhu, Todd Wegter
 *
 */

@Entity
public class Form {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //creates id for entry
	private Long id;
	
	
	private String netID;
	private boolean approved;
	private String reason;
	//private Time startTime;
	//private Time endTime;
	
	//private List<DayOfWeek> listDayOfWeek;
	
	private String type;
	
	public Form(String netID) {
		//super(); TODO 
		this.person = person;
		this.startTime = startTime;
		this.endTime = endTime;
		this.reason = reason;
		approved = false;
	}
	
	
	public List<DayOfWeek> getListDayOfWeek() {
		return listDayOfWeek;
	}


	public void setListDayOfWeek(List<DayOfWeek> listDayOfWeek) {
		this.listDayOfWeek = listDayOfWeek;
	}


	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Time getStartTime() {
		return startTime;
	}
	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}
	public Time getEndTime() {
		return endTime;
	}
	public void setEndTime(Time endTime) {
		this.endTime = endTime;
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
	
	
}
