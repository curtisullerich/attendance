package edu.iastate.music.marching.attendance.tasks;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.ImportData;

public class Import {

	private static final Logger LOG = Logger.getLogger(Import.class.getName());

	public static void performImport(ImportData importData) {
		DataTrain.getAndStartTrain().getDataManager()
				.importJSONDatabaseDump(importData.getData());
	}
}
