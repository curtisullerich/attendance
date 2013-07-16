package edu.iastate.music.marching.attendance.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.AppData;

public class Util {
	private static final String dateString = "MM/dd/yyyy";
	private static final String timeString = "h:mm aa";
	public static final SimpleDateFormat datetimeFormat = new SimpleDateFormat(
			dateString + " " + timeString);
	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			dateString);
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat(
			timeString);

	public static Date parseDate(String sMonth, String sDay, String sYear,
			String hour, String AMPM, String minute, TimeZone timeZone) {
		int year = 0, month = 0, day = 0;
		Calendar calendar = Calendar.getInstance(timeZone);

		// Do validate first and store any problems to this exception
		ValidationExceptions exp = new ValidationExceptions();
		try {
			year = Integer.parseInt(sYear);
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid year, not a number.");
		}
		if (year < 1000) {
			exp.getErrors().add("Invalid year, must be four digits.");
		}

		try {
			month = Integer.parseInt(sMonth);
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid month, not a number.");
		}
		try {
			day = Integer.parseInt(sDay);
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid day, not a number.");
		}

		calendar.setTimeInMillis(0);
		calendar.setLenient(false);

		try {
			calendar.set(Calendar.YEAR, year);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid year given:" + e.getMessage() + '.');
		}
		try {
			calendar.set(Calendar.MONTH, month - 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid month given:" + e.getMessage() + '.');
		}
		try {
			calendar.set(Calendar.DATE, day);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid day given:" + e.getMessage() + '.');
		}

		try {
			int intHour = Integer.parseInt(hour);

			// Allow for midnight / noon to be specified as
			if (intHour == 12) {
				intHour = 0;
			}

			calendar.set(Calendar.HOUR, intHour);
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid hour, not a number");
		}

		try {
			calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid minute, not a number");
		}
		int timeOfDay = 0;
		if ("AM".equals(AMPM.toUpperCase()))
			timeOfDay = Calendar.AM;
		else if ("PM".equals(AMPM.toUpperCase()))
			timeOfDay = Calendar.PM;
		else {
			exp.getErrors().add("Invalid input for AM/PM");
		}

		try {
			calendar.set(Calendar.AM_PM, timeOfDay);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid time of day given:" + e.getMessage());
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}

		return calendar.getTime();
	}

	public static String formatDateTime(Date datetime, TimeZone timeZone) {
		AppData data = DataTrain.getAndStartTrain().getAppDataManager().get();
		datetimeFormat.setTimeZone(data.getTimeZone());
		return datetimeFormat.format(data.getFormSubmissionCutoff());
	}

	public static String formatTime(Date datetime, TimeZone timeZone) {
		AppData data = DataTrain.getAndStartTrain().getAppDataManager().get();
		timeFormat.setTimeZone(data.getTimeZone());
		return timeFormat.format(data.getFormSubmissionCutoff());
	}

	public static String formatDate(Date datetime, TimeZone timeZone) {
		AppData data = DataTrain.getAndStartTrain().getAppDataManager().get();
		dateFormat.setTimeZone(data.getTimeZone());
		return dateFormat.format(data.getFormSubmissionCutoff());
	}

	public static Date parseDateTime(String datetime, TimeZone timeZone) {
		Calendar calendar = Calendar.getInstance(timeZone);
		Date d;
		datetimeFormat.setTimeZone(timeZone);

		ValidationExceptions exp = new ValidationExceptions();
		try {
			d = datetimeFormat.parse(datetime);
			calendar.setTime(d);
		} catch (ParseException e) {
			exp.getErrors().add("Invalid date.");
		}

		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setLenient(false);

		Calendar cutoff = Calendar.getInstance(timeZone);
		cutoff.set(Calendar.YEAR, 1900);
		cutoff.set(Calendar.MONTH, 1);
		cutoff.set(Calendar.DAY_OF_MONTH, 1);
		cutoff.set(Calendar.HOUR, 0);
		cutoff.set(Calendar.MINUTE, 0);
		cutoff.set(Calendar.SECOND, 0);
		cutoff.set(Calendar.MILLISECOND, 0);

		if (calendar.before(cutoff)) {
			exp.getErrors().add("Date was before 1900.");
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}
		Date ret = calendar.getTime();
		return ret;
	}

	public static Date parseTime(String time, TimeZone timeZone) {
		Calendar calendar = Calendar.getInstance(timeZone);
		Date d;
		timeFormat.setTimeZone(timeZone);

		ValidationExceptions exp = new ValidationExceptions();
		try {
			d = timeFormat.parse(time);
			calendar.setTime(d);
		} catch (ParseException e) {
			exp.getErrors().add("Invalid time.");
		}

//		calendar.set(Calendar.YEAR, 0);
//		calendar.set(Calendar.MONTH, 0);
//		calendar.set(Calendar.DAY_OF_MONTH, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setLenient(false);

		if (exp.getErrors().size() > 0) {
			throw exp;
		}
		Date ret = calendar.getTime();
		return ret;
	}

	public static Date parseNewDate(String date, TimeZone timeZone) {
		Calendar calendar = Calendar.getInstance(timeZone);
		Date d;
		dateFormat.setTimeZone(timeZone);

		ValidationExceptions exp = new ValidationExceptions();
		try {
			d = dateFormat.parse(date);
			calendar.setTime(d);
		} catch (ParseException e) {
			exp.getErrors().add("Invalid date.");
		}
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.setLenient(false);

		Calendar cutoff = Calendar.getInstance(timeZone);
		cutoff.set(Calendar.YEAR, 1900);
		cutoff.set(Calendar.MONTH, 1);
		cutoff.set(Calendar.DAY_OF_MONTH, 1);
//		cutoff.set(Calendar.HOUR, 0);
//		cutoff.set(Calendar.MINUTE, 0);
//		cutoff.set(Calendar.SECOND, 0);
//		cutoff.set(Calendar.MILLISECOND, 0);

		if (calendar.before(cutoff)) {
			exp.getErrors().add("Date was before 1900.");
		}

		if (exp.getErrors().size() > 0) {
			throw exp;
		}
		Date ret = calendar.getTime();
		return ret;
	}
}
