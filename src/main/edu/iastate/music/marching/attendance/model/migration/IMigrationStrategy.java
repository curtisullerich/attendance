package edu.iastate.music.marching.attendance.model.migration;

import com.google.code.twig.standard.StandardObjectDatastore;

public interface IMigrationStrategy {
	
	int getFromVersion();
	
	int getToVersion();

	void doUpgrade(StandardObjectDatastore datastore) throws MigrationException;
	
	void doDowngrade() throws MigrationException;
}
