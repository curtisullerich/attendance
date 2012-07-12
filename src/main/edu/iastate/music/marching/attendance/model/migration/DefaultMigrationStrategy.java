package edu.iastate.music.marching.attendance.model.migration;

import javax.transaction.NotSupportedException;

/**
 * Copies all data on upgrade, throws on downgrade
 * @author stiner
 *
 */
public class DefaultMigrationStrategy implements IMigrationStrategy {
	
	int fromVersion;
	int toVersion;
	
	public DefaultMigrationStrategy(int fromVersion, int toVersion)
	{
		
	}

	@Override
	public int getFromVersion() {
		return fromVersion;
	}

	@Override
	public int getToVersion() {
		return toVersion;
	}

	@Override
	public void doUpgrade() throws MigrationException {
		// TODO Auto-generated method stub
		// Copy all data
	}

	@Override
	public void doDowngrade() throws MigrationException {
		throw new MigrationException("Not supported", new NotSupportedException());
	}

}
