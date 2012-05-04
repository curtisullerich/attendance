package edu.iastate.music.marching.attendance.model;

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

	@Parent
	@Activate(0)
	private Event event;

	@Activate(0)
	private User student;

	public User getStudent() {
		return student;
	}

	public Event getEvent() {
		return event;
	}

}
