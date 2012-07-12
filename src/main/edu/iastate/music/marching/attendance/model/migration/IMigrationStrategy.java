package edu.iastate.music.marching.attendance.model.migration;

public interface IMigrationStrategy {
	
	int getFromVersion();
	
	int getToVersion();

	void doUpgrade() throws MigrationException;
	
	void doDowngrade() throws MigrationException;
}
