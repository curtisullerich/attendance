package edu.iastate.music.marching.attendance.model.migration;

public class MigrationConfiguration extends AbstractMigrationConfiguration {

	@Override
	protected void configure() {
		register(Migrate_0to1_EmailsOnUsers.class);
	}

}
