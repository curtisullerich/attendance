package edu.iastate.music.marching.attendance.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.ReadableInterval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Util {

	private static final DateTimeFormatter DATEFORMAT = DateTimeFormat
			.forPattern("MM/dd/yyyy");
	private static final DateTimeFormatter TIMEFORMAT = DateTimeFormat
			.forPattern("h:mm aa");
	private static final DateTimeFormatter DATETIMEFORMAT = DateTimeFormat
			.forPattern("MM/dd/yyyy h:mm aa");

	public static String formatDateTime(DateTime datetime, DateTimeZone zone) {
		return DATETIMEFORMAT.withZone(zone).print(datetime);
	}

	public static String formatDateOnly(DateTime datetime, DateTimeZone zone) {
		return DATEFORMAT.withZone(zone).print(datetime);
	}

	public static String formatDateOnly(LocalDate date) {
		return DATEFORMAT.print(date);
	}

	public static String formatTimeOnly(DateTime datetime, DateTimeZone zone) {
		return TIMEFORMAT.withZone(zone).print(datetime);
	}

	public static String formatTimeOnly(LocalTime datetime) {
		return TIMEFORMAT.print(datetime);
	}
	
	public static DateTime parseDateTime(String text, DateTimeZone zone) {
		return DATETIMEFORMAT.withZone(zone).parseDateTime(text);
	}

	public static LocalTime parseTimeOnly(String text, DateTimeZone zone) {
		return TIMEFORMAT.parseLocalTime(text);
	}

	public static LocalDate parseDateOnly(String text, DateTimeZone zone) {
		return DATEFORMAT.parseLocalDate(text);
	}

	public static boolean overlapDays(ReadableInterval interval1,
			ReadableInterval interval2) {
		// Expand one interval to be full days
		DateTime start = interval1.getStart().toDateMidnight().toDateTime();
		DateTime end = interval1.getEnd().toDateMidnight().toInterval()
				.getEnd();
		Interval interval1FullDays = new Interval(start, end);

		// Simply compare for any overlap
		return interval1FullDays.overlaps(interval2);
	}
}
