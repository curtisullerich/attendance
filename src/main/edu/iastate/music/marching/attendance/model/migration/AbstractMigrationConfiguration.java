package edu.iastate.music.marching.attendance.model.migration;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMigrationConfiguration {

	private List<Class<IMigrationStrategy>> migrationStrategies;

	public IMigrationStrategy getMigrationStrategy(int fromVersion,
			int toVersion) throws MigrationException {
		List<Class<IMigrationStrategy>> strategies = getStrategies();

		for (Class<IMigrationStrategy> strategyClass : strategies) {
			IMigrationStrategy strategy;
			try {
				strategy = strategyClass.newInstance();

				if (strategy.getFromVersion() == fromVersion
						&& strategy.getToVersion() == toVersion) {
					return strategy;
				}

			} catch (InstantiationException e) {
				throw new MigrationException(
						"Could not instantiate migration strategy", e);
			} catch (IllegalAccessException e) {
				throw new MigrationException(
						"Could not instantiate migration strategy", e);
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	protected <T extends IMigrationStrategy> void register(
			Class<T> migrationStrategy) {

		if (this.migrationStrategies == null)
			this.migrationStrategies = new ArrayList<Class<IMigrationStrategy>>();

		migrationStrategies.add((Class<IMigrationStrategy>) migrationStrategy);
	}

	protected abstract void configure();

	private List<Class<IMigrationStrategy>> getStrategies() {
		if (this.migrationStrategies != null)
			return this.migrationStrategies;

		configure();

		return this.migrationStrategies;
	}

}
