package edu.iastate.music.marching.attendance.tasks;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;

public class Import {

	private static final Logger LOG = Logger.getLogger(Import.class.getName());

	public static void performImport(InputStream jsonDump) {

		DataTrain.getAndStartTrain().getDataManager()
				.importJSONDatabaseDump(new InputStreamReader(jsonDump));
	}
}
