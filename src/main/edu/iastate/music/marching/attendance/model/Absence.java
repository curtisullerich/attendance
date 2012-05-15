package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import com.google.code.twig.annotation.Activate;
import com.google.code.twig.annotation.Parent;

public class Absence {

	public enum Type {
		Absence, Tardy, EarlyCheckOut
	}

	/**
	 * Create absence through AbsenceController (DataModel.absence().create(...)
	 */
	Absence() {

	}
	
	private Type type;

	@Parent
	@Activate(0)
	private Event event;

	@Activate(0)
	private User student;

	private Date start;
	
	private Date end;

	public User getStudent() {
		return student;
	}

	public Event getEvent() {
		return event;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public Date getDatetime() {
		return this.start;
	}

	public void setDatetime(Date time) {
		this.start = time;
	}

	public void setStart(Date start) {
		this.start = start;
	}
	
	public void setEnd(Date end) {
		this.end = end;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setStudent(User student) {
		this.student = student;
	}

}
