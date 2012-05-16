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
		QueryResultIterator<AppData> iter = dataTrain.getDataStore().find().type(AppData.class).now();
		
		if(!iter.hasNext())
		{
			// No AppData in the data store, so make a new default one
			AppData appData = ModelFactory.newAppData();
			return save(appData);
		}
		else
			// We have some app data from the store
			return iter.next();
	}

	private AppData save(AppData appData) {
		dataTrain.getDataStore().storeOrUpdate(appData);
		return appData;
	}
	
	

}
