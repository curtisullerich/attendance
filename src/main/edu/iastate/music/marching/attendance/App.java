package edu.iastate.music.marching.attendance;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTimeConstants;

public class App {

	public enum WeekDay {
		Sunday(DateTimeConstants.SUNDAY), Monday(DateTimeConstants.MONDAY), Tuesday(
				DateTimeConstants.TUESDAY), Wednesday(
				DateTimeConstants.WEDNESDAY), Thursday(
				DateTimeConstants.THURSDAY), Friday(DateTimeConstants.FRIDAY), Saturday(
				DateTimeConstants.SATURDAY);
		public static WeekDay valueOf(int day) {
			for (WeekDay d : WeekDay.values()) {
				if (d.DayOfWeek == day) {
					return d;
				}
			}

			return null;
		}

		public final int DayOfWeek;

		WeekDay(int jodaDayOfWeek) {
			this.DayOfWeek = jodaDayOfWeek;
		}
	}
	
	public static List<TimeZone> getTimezoneOptions() {
		List<TimeZone> opts = new ArrayList<TimeZone>();
		opts.add(TimeZone.getTimeZone("CST"));
		return opts;
	}
}
