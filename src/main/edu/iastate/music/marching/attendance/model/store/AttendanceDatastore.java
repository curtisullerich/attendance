package edu.iastate.music.marching.attendance.model.store;

import com.google.code.twig.LoadCommand.CacheMode;
import com.google.code.twig.Settings;
import com.google.code.twig.annotation.AnnotationConfiguration;
import com.google.code.twig.standard.StandardObjectDatastore;

public class AttendanceDatastore extends StandardObjectDatastore {

	public static final int VERSION = 1;
	public static final String MODEL_OBJECTS_PACKAGE = "edu.iastate.music.marching.attendance.model.store";

	static final AnnotationConfiguration CONFIGURATION;

	static {
		AnnotationConfiguration a = new AnnotationConfiguration();
		a.register(Absence.class);
		a.register(AppData.class);
		a.register(Event.class);
		a.register(Form.class);
		a.register(ImportData.class);
		a.register(MobileDataUpload.class);
		a.register(User.class);
		CONFIGURATION = a;

		AttendanceDatastore.registerCachedKind(a.typeToKind(User.class), 6000,
				1000, true, true);
		AttendanceDatastore.registerCachedKind(a.typeToKind(Event.class), 6000,
				1000, true, true);
	}

	public AttendanceDatastore() {
		super(Settings.builder().build(), CONFIGURATION, 0, true);
	}
}
