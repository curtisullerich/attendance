package edu.iastate.music.marching.attendance.util;

import java.util.List;

import org.joda.time.DateTime;

import edu.iastate.music.marching.attendance.model.store.Event;

public class UploadEvent {

	public List<UploadAbsence> absences;
	public Event.Type type;
	public DateTime startDateTime;
	public DateTime endDateTime;
}
