package edu.iastate.music.marching.attendance.model;

import java.util.Date;
import java.util.Set;

import com.google.code.twig.annotation.Child;

public class Event {
	/**
	 * Create events through UserController (DataModel.events().create(...)
	 */
	Event() {

	}
	
	private Date start;
	
	private Date end;

	/**
	 * Absent students
	 * 
	 */
	@Child
	private Set<Absence> absences;
}
