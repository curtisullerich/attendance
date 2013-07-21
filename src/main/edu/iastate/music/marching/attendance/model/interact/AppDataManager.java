package edu.iastate.music.marching.attendance.model.interact;

import java.util.Calendar;
import java.util.TimeZone;

import edu.iastate.music.marching.attendance.model.store.AppData;
import edu.iastate.music.marching.attendance.model.store.AttendanceDatastore;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;

public class AppDataManager extends AbstractManager {

	private DataTrain dataTrain;

	public AppDataManager(DataTrain dataTrain) {
		this.dataTrain = dataTrain;
	}

	/**
	 * Always returns an instance of AppData, a default one if none is in the
	 * datastore
	 * 
	 * @return
	 */
	public AppData get() {
		// Try cache first
		AppData appData = this.dataTrain.loadFromCache(AppData.class, AttendanceDatastore.VERSION);

		if (appData == null) {
			appData = dataTrain.getDataStore().find().type(AppData.class)
					.returnUnique().now();
			this.dataTrain.updateCache(AttendanceDatastore.VERSION, appData);
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

			//TODO if this is supposed to be roll(), then revert 
			calendar.add(Calendar.MONTH, 1);
			calendar.add(Calendar.MILLISECOND, -1);
			appData.setPerformanceAbsenceFormCutoff(calendar.getTime());

			this.dataTrain.getDataStore().store(appData);
			this.dataTrain.updateCache(AttendanceDatastore.VERSION, appData);
		}

		// We have some app data from the store
		return appData;
	}

	public void save(AppData appData) {
		this.dataTrain.updateCache(appData.getDatastoreVersion(), appData);
		dataTrain.getDataStore().storeOrUpdate(appData);
	}
}
