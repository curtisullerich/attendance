package edu.iastate.music.marching.attendance.controllers;

import javax.transaction.NotSupportedException;

import edu.iastate.music.marching.attendance.model.AttendanceDatastore;
import edu.iastate.music.marching.attendance.model.migration.AbstractMigrationConfiguration;
import edu.iastate.music.marching.attendance.model.migration.IMigrationStrategy;
import edu.iastate.music.marching.attendance.model.migration.MigrationConfiguration;
import edu.iastate.music.marching.attendance.model.migration.MigrationException;

public class MigrationController {
	
	private DataTrain train;

	public MigrationController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public void upgrade(int fromVersion) throws MigrationException {
		AbstractMigrationConfiguration configuration = new MigrationConfiguration();

		IMigrationStrategy strategy = configuration.getMigrationStrategy(
				fromVersion, AttendanceDatastore.VERSION);

		if (strategy == null) {
			throw new MigrationException(
					"No migration strategy found from version " + fromVersion
							+ " to current version",
					new NotSupportedException());
		}

		strategy.doUpgrade(this.train.getDataStore());
	}
}
