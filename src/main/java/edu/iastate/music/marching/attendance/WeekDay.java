package edu.iastate.music.marching.attendance;

import org.joda.time.DateTimeConstants;

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
		
		throw new IllegalArgumentException("Bad value for day parameter of WeekDay.valueOf: " + day);
	}

	public final int DayOfWeek;

	WeekDay(int jodaDayOfWeek) {
		this.DayOfWeek = jodaDayOfWeek;
	}
}