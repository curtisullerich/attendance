package edu.iastate.music.marching.attendance.tasks;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.ImportData;

public class Import {

	public static void performImport(ImportData importData) {
		DataTrain.depart().data().importJSONDatabaseDump(importData.getData());
	}
}
