package edu.iastate.music.marching.attendance;

import org.joda.time.DateTimeConstants;

public class App {

	public enum WeekDay {
		Sunday(DateTimeConstants.SUNDAY), Monday(DateTimeConstants.MONDAY), Tuesday(
				DateTimeConstants.TUESDAY), Wednesday(
				DateTimeConstants.WEDNESDAY), Thursday(
				DateTimeConstants.THURSDAY), Friday(DateTimeConstants.FRIDAY), Saturday(
				DateTimeConstants.SATURDAY);
		public final int DayOfWeek;

		WeekDay(int jodaDayOfWeek) {
			this.DayOfWeek = jodaDayOfWeek;
		}

		public static WeekDay valueOf(int day) {
			for (WeekDay d : WeekDay.values()) {
				if (d.DayOfWeek == day) {
					return d;
				}
			}

			return null;
		}
	}
}
