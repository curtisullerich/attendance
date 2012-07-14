package edu.iastate.music.marching.attendance.model;

import com.google.code.twig.annotation.Id;

public class DatastoreVersion {

	public static final int CURRENT = AttendanceDatastore.VERSION;

	DatastoreVersion() {
	}

	@Id
	private int version;

	public int getVersion() {
		return this.version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
}
