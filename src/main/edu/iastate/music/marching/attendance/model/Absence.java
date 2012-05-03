package edu.iastate.music.marching.attendance.model;

import com.google.code.twig.annotation.Parent;

public class Absence {
	/**
	 * Create absence through AbsenceController (DataModel.absence().create(...)
	 */
	Absence() {

	}

	@Parent
	private Event event;

	private User student;
}
