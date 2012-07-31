package edu.iastate.music.marching.attendance.controllers;

import java.util.Calendar;
import java.util.TimeZone;

import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.AttendanceDatastore;
import edu.iastate.music.marching.attendance.model.DatastoreVersion;
import edu.iastate.music.marching.attendance.model.ModelFactory;

public class AppDataController extends AbstractController {

	private DataTrain dataTrain;

	public AppDataController(DataTrain dataTrain) {
		this.dataTrain = dataTrain;
	}

	/**
	 * Always returns an instance of AppData, a default one if none is in the
	 * datastore
	 * 
	 * @return
	 */
	public AppData get() {

		int id = this.dataTrain.getVersionController().getCurrent()
				.getVersion();

		// Try cache first
		AppData appData = this.dataTrain.loadFromCache(AppData.class, id);

		if (appData == null) {
			appData = dataTrain.getDataStore().find().type(AppData.class)
					.returnUnique().now();
			this.dataTrain.updateCache(id, appData);
		}

		if (appData == null) {
			// No AppData in the data store, so make a new default one
			appData = ModelFactory.newAppData();

			//
			// Defaults
			//
			appData.setDatastoreVersion(AttendanceDatastore.VERSION);

			appData.setTitle("Band Attendance");

			appData.setTimeZone(TimeZone.getDefault());

			// Default form cutoff is the end of august
			Calendar calendar = Calendar.getInstance(appData.getTimeZone());
			calendar.set(Calendar.MONTH, Calendar.AUGUST);
			calendar.set(Calendar.DATE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 16);
			calendar.set(Calendar.MINUTE, 35);
			calendar.set(Calendar.MILLISECOND, 0);

			calendar.roll(Calendar.MONTH, true);
			calendar.roll(Calendar.MILLISECOND, false);
			appData.setFormSubmissionCutoff(calendar.getTime());

			// Defaults to "password"
			appData.setHashedMobilePassword("5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8");

			this.dataTrain.getDataStore().store(appData);
			this.dataTrain.updateCache(id, appData);
		}

		// We have some app data from the store
		return appData;
	}

	public void save(AppData appData) {
		this.dataTrain.updateCache(appData.getDatastoreVersion(), appData);
		dataTrain.getDataStore().update(appData);
	}
}
