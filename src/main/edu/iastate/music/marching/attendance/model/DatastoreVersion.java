package edu.iastate.music.marching.attendance.model;

import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Version;

@Version(0)
@Entity(kind="DatastoreVersion", allocateIdsBy=10)
public class DatastoreVersion {

	public static final int CURRENT = AttendanceDatastore.VERSION;
	public static final String NAME = "edu.iastate.music.marching.attendance.model.DatastoreVersion";

	DatastoreVersion() {
	}

	@Id
	private int versionPlusOne;

	public int getVersion() {
		return this.versionPlusOne - 1;
	}

	public void setVersion(int version) {
		this.versionPlusOne = version + 1;
	}
}
