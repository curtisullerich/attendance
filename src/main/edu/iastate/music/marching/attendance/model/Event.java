package edu.iastate.music.marching.attendance.model;

import java.util.Date;
import java.util.Set;

import com.google.code.twig.annotation.Index;

public class Event {

	public static final String FIELD_START = "start";
	public static final String FIELD_END = "end";
	public static final String FIELD_TYPE = "type";
	public static final String FIELD_DATE = "date";

	/**
	 * No-args constructor for datastore
	 */
	Event() {

	}
	
	@Index
	private Date start;
	
	@Index
	private Date end;
	
	@Index
	private Date date;

	private Type type;

	/**
	 * Absent students
	 * 
	 */
	private Set<Absence> absences;
	
	public Date getDate() {
		return start;
	}

	public void setDate(Date date) {
		this.date = date;
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
}
