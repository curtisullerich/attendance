package edu.iastate.music.marching.attendance.controllers;

import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;

import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

public class AppDataController extends AbstractController {

	private DataTrain dataTrain;

	public AppDataController(DataTrain dataTrain) {
		this.dataTrain = dataTrain;
	}

	/**
	 * Always returns an instance of AppData, a default one if none is in the datastore
	 * @return
	 */
	public AppData get() {
		// TODO Caching
		AppData appData = dataTrain.getDataStore().find().type(AppData.class).returnUnique().now();
		
		if(appData == null)
		{
			// No AppData in the data store, so make a new default one
			appData = ModelFactory.newAppData();
			appData = save(appData);
		}
		
		// We have some app data from the store
		return appData;
	}

	private AppData save(AppData appData) {
		dataTrain.getDataStore().storeOrUpdate(appData);
		return appData;
	}
	
	

}
