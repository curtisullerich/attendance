package edu.iastate.music.marching.attendance.testlib;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;

public class Dates {
	public static final LocalDate getDefaultLocalDate() {
		return new LocalDate(2012, 7, 7);
	}

	private static final LocalTime getDefaultStartTime() {
		LocalTime start = new LocalTime(16, 30, 0);
		return start;
	}

	public static final Interval getDefaultEventInterval(LocalDate date) {
		LocalTime end = new LocalTime(17, 50, 0);
		DateTimeZone zone = DataTrain.depart().appData().get().getTimeZone();
		Interval eventInterval = new Interval(date.toLocalDateTime(
				getDefaultStartTime()).toDateTime(zone), date.toLocalDateTime(
				end).toDateTime(zone));
		return eventInterval;
	}

	public static final DateTime getDefaultAdjustedStartTime() {
		DateTime d = getDefaultLocalDate().toLocalDateTime(getDefaultStartTime())
				.toDateTime(DataTrain.depart().appData().get().getTimeZone());
		return d.plusMinutes(20);
	}
}
