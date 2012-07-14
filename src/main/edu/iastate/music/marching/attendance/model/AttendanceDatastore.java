package edu.iastate.music.marching.attendance.model;

import com.google.code.twig.annotation.AnnotationObjectDatastore;
import com.google.code.twig.conversion.CombinedConverter;
import com.google.code.twig.conversion.SpecificConverter;

public class AttendanceDatastore extends AnnotationObjectDatastore {

	public static final int VERSION = Absence.VERSION + AppData.VERSION
			+ Event.VERSION + Form.VERSION + MessageThread.VERSION
			+ User.VERSION;

	AttendanceDatastore() {
		super(true, VERSION);
	}
}
