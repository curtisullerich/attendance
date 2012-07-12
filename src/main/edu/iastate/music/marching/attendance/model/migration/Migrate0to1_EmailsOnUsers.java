package edu.iastate.music.marching.attendance.model.migration;

public class Migrate0to1_EmailsOnUsers extends DefaultMigrationStrategy {
	
	public static final int FROM_VERSION = 0;
	
	public static final int TO_VERSION = 1;

	public Migrate0to1_EmailsOnUsers() {
		super(FROM_VERSION, TO_VERSION);
	}

}
