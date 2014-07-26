package edu.iastate.music.marching.attendance.util;

import java.util.List;

import org.joda.time.DateTime;

import edu.iastate.music.marching.attendance.model.store.Event;

public class UploadEvent {

	// TODO(curtis) use annotations to define shorter serialized names. same for
	// UploadAbsence.
	public List<UploadAbsence> absences;
	public Event.Type type;
	public DateTime startDateTime;
	public DateTime endDateTime;
}
