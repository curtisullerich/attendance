package edu.iastate.music.marching.attendance.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Util {


	public static Date parseDate(String sMonth, String sDay, String sYear, String hour, String AMPM, String minute, TimeZone timeZone) {
		int year = 0, month = 0, day = 0;
		Calendar calendar = Calendar.getInstance(timeZone);

		// Do validate first and store any problems to this exception
		ValidationExceptions exp = new ValidationExceptions();
		try {
			year = Integer.parseInt(sYear);
		} catch (NumberFormatException e) {
			exp.getErrors().add("Invalid year, not a number.");
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
			calendar.set(Calendar.MONTH, month-1);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid month given:" + e.getMessage() + '.');
		}
		try {
			calendar.set(Calendar.DATE, day);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid day given:" + e.getMessage() + '.');
		}

		try
		{
			calendar.set(Calendar.HOUR, Integer.parseInt(hour));
		}
		catch (NumberFormatException e)
		{
			exp.getErrors().add("Invalid hour, not a number");
		}
		
		try
		{
			calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
		}
		catch (NumberFormatException e)
		{
			exp.getErrors().add("Invalid minute, not a number");
		}
		int timeOfDay = 0;
		if ("AM".equals(AMPM.toUpperCase()))
			timeOfDay = Calendar.AM;
		else if ("PM".equals(AMPM.toUpperCase()))
			timeOfDay = Calendar.PM;
		else
		{
			exp.getErrors().add("Invalid input for AM/PM");
		}
		
		try {
			calendar.set(Calendar.AM_PM, timeOfDay);
		} catch (ArrayIndexOutOfBoundsException e) {
			exp.getErrors().add("Invalid time of day given:" + e.getMessage());
		}
		
		

		if (exp.getErrors().size() > 0)
			throw exp;

		return calendar.getTime();
	}
}
