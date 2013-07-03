package edu.iastate.music.marching.attendance.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

import edu.iastate.music.marching.attendance.servlets.TaskServlet;

public class Tasks {

	public static void exportData() {
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withUrl(TaskServlet.EXPORT_DATA_URL));
	}

}
