package edu.iastate.music.marching.attendance.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import edu.iastate.music.marching.attendance.servlets.TaskServlet;

public class Tasks {

	public static void exportData() {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl(TaskServlet.EXPORT_DATA_URL));
	}

	public static void importData(InputStream stream) throws IOException {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl(TaskServlet.IMPORT_DATA_URL).payload(
				IOUtils.toByteArray(stream)));
	}

}
