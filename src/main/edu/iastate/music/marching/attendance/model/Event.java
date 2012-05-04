package edu.iastate.music.marching.attendance.model;

import java.util.Date;
import java.util.Set;

import com.google.code.twig.annotation.Child;

public class Event {

	/**
	 * No-args constructor for datastore
	 */
	Event() {

	}

	/**
	 * Create events through UserController (DataModel.events().create(...)
	 */
	Event(Event.Type type, Date start, Date end) {
		this.type = type;
		this.setStart(start);
		this.setEnd(end);
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public enum Type {
		Rehearsal, Performance;
	}

	public void setType(Event.Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	private Date start;

	private Date end;

	private Type type;

	/**
	 * Absent students
	 * 
	 */
	@Child
	private Set<Absence> absences;
}
