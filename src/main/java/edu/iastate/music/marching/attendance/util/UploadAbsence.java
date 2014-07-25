package edu.iastate.music.marching.attendance.util;

import org.joda.time.DateTime;

import edu.iastate.music.marching.attendance.model.store.Absence;

public class UploadAbsence {

	public Absence.Type type;
	public DateTime time;
	public String netid;
}
