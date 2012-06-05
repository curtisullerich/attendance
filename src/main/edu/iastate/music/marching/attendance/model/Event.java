package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import com.google.code.twig.annotation.Id;
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
	
	@Id
	private long id;
	
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
	// HACK: DANIEL
	//@Activate(0)
	//private List<Absence> absences;
	
	public long getId() {
		return this.id;
	}
	
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
