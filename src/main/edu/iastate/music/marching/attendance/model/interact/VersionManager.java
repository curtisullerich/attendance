package edu.iastate.music.marching.attendance.model.interact;

import java.util.List;

import edu.iastate.music.marching.attendance.model.store.DatastoreVersion;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;

public class VersionManager {

	private DataTrain datatrain;

	public VersionManager(DataTrain dataTrain) {
		this.datatrain = dataTrain;
	}

	public DatastoreVersion getCurrent() {

		int id = DatastoreVersion.CURRENT + 1;

		// Try cache first
		DatastoreVersion version = this.datatrain.loadFromCache(
				DatastoreVersion.class, id);

		if (version != null
				&& null == this.datatrain.getDataStore().associatedKey(version)) {
			this.datatrain.getDataStore().associate(version);
		}

		if (version == null) {
			version = this.datatrain.getDataStore().load(
					DatastoreVersion.class, DatastoreVersion.CURRENT + 1);
			this.datatrain.updateCache(id, version);
		}

		if (version == null) {
			version = ModelFactory
					.newDatastoreVersion(DatastoreVersion.CURRENT);

			datatrain.getDataStore().store(version);
			this.datatrain.updateCache(id, version);
		}

		return version;
	}

	public List<DatastoreVersion> getAll() {
		return this.datatrain.getDataStore().find()
				.type(DatastoreVersion.class).returnAll().now();
	}
}