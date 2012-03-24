package forms;

import people.Person;
import time.Date;
/**
 * 
 * @author Yifei Zhu
 *
 */
public abstract class Form {
	private Person person;
	private Date startDate;
	private Date endDate;
	private String reason;
	private boolean Isapproved;
	
	
	public Form(Person person, Date startDate, Date endDate, String reason,
			boolean isapproved) {
		//super(); TODO 
		this.person = person;
		this.startDate = startDate;
		this.endDate = endDate;
		this.reason = reason;
		Isapproved = false;
	}
	
	
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public boolean isIsapproved() {
		return Isapproved;
	}
	public void setIsapproved(boolean isapproved) {
		Isapproved = isapproved;
	}
	
	
	
	
}
