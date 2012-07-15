package edu.iastate.music.marching.attendance.controllers;

import java.util.List;

import edu.iastate.music.marching.attendance.model.DatastoreVersion;
import edu.iastate.music.marching.attendance.model.ModelFactory;

public class VersionController {

	private DataTrain datatrain;

	public VersionController(DataTrain dataTrain) {
		this.datatrain = dataTrain;
	}

	public DatastoreVersion getCurrent() {
		DatastoreVersion version = this.datatrain.getDataStore().load(DatastoreVersion.class,
				DatastoreVersion.CURRENT + 1);
		
		if(version == null)
		{
			version = ModelFactory.newDatastoreVersion(DatastoreVersion.CURRENT);
			
			datatrain.getDataStore().store(version);
		}
		
		return version;
	}

	public List<DatastoreVersion> getAll() {
		return this.datatrain.getDataStore().find()
				.type(DatastoreVersion.class).returnAll().now();
	}
}
