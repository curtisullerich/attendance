package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class ModelFactory {

	public static User getUser() {
		return new User();
	}

	public static PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

	static final class PMF {
		private static final PersistenceManagerFactory pmfInstance = JDOHelper
				.getPersistenceManagerFactory("transactions-optional");

		private PMF() {
		}

		public static PersistenceManagerFactory get() {
			return pmfInstance;
		}
	}

	public static Event newEvent(Event.Type type, Date start, Date end) {
		return new Event(type, start, end);
	}
}
