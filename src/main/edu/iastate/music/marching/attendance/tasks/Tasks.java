package edu.iastate.music.marching.attendance.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.ImportData;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;
import edu.iastate.music.marching.attendance.servlets.TaskServlet;

public class Tasks {

	public static void exportData() {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl(TaskServlet.EXPORT_DATA_URL));
	}

	public static void importData(InputStream stream) throws IOException {

		ImportData iData = ModelFactory.newImportData();

		iData.setTimestamp(new DateTime());
		iData.setData(IOUtils.toString(stream));

		Key k = DataTrain.getAndStartTrain().getDataManager()
				.storeImportData(iData);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl(TaskServlet.IMPORT_DATA_URL)
				.param(TaskServlet.IMPORT_PARAM_STOREID, Long.toString(k.getId())));
	}

}
