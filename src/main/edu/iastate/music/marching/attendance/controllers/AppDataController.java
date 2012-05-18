package edu.iastate.music.marching.attendance.controllers;

import java.util.Calendar;
import java.util.List;

import com.google.appengine.api.datastore.QueryResultIterator;

import edu.iastate.music.marching.attendance.App;
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
			
			//
			// Defaults
			//
			appData.setDirectorRegistered(false);
			
			appData.setTitle("Band Attendance");
			
			// Default form cutoff is the end of august
			Calendar calendar = Calendar.getInstance(App.getTimeZone());
			calendar.set(Calendar.MONTH, Calendar.AUGUST);
			calendar.set(Calendar.DATE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 16);
			calendar.set(Calendar.MINUTE, 35);
			calendar.set(Calendar.MILLISECOND, 0);
			
			calendar.roll(Calendar.MONTH, true);
			calendar.roll(Calendar.MILLISECOND, false);
			appData.setFormSubmissionCutoff(calendar.getTime());
			
			appData.setHashedMobilePassword("5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8");
			
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
