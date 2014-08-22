package edu.iastate.music.marching.attendance;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class App {
	 
	public static boolean CachingEnabled = true;
	
	public static final String DOMAIN = "iastate.edu";

	public static class Emails {

		public static final String DATA_EXPORT_SENDER = "mbattendance@gmail.com";

		public static final String BUGREPORT_EMAIL_TO = "mbattendance@iastate.edu";
		public static final String BUGREPORT_EMAIL_FROM = "mbattendance@gmail.com";

	}

	public static List<TimeZone> getTimezoneOptions() {
		List<TimeZone> opts = new ArrayList<TimeZone>();
		opts.add(TimeZone.getTimeZone("CST"));
		return opts;
	}
}
