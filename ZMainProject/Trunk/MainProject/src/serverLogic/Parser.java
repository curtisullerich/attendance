package serverLogic;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Scanner;

import people.*;
import time.*;
import attendance.*;

public class Parser {
	private static final String absentPrependPerformance = "absentStudentPerformance";
	private static final String absentPrependRehearsal = "absentStudentRehearsal";
	private static final String rehearsalPrepend = "storedRehearsal";
	private static final String performancePrepend = "storedPerformance";
	private static final String tardyPrepend = "tardyStudent";
	private static final String studentPrepend = "studentRecord";

	//Remember the people absent and if they were tardy then just remove the absence.
	public static void splat(String add) {
		// Splats the massive string into an array of strings for each person
		String[] people = add.split(",");
		
		HashMap<User, Absence> absences = new HashMap<User, Absence>();
		HashMap<User, Tardy> tardies = new HashMap<User, Tardy>();
		List<Event> events = new LinkedList<Event>();

		//Puts everything into HashMaps!
		for (String e : people) {
			// The person splat is in the form
			// prepend, firstName, lastName, netID, date, startTime, endTime,
			// rank
			String[] personalInfo = e.split(" ");
			String prepend = personalInfo[0];
			if (prepend.equalsIgnoreCase(studentPrepend))
				continue;
			else if (prepend.equalsIgnoreCase(rehearsalPrepend) || prepend.equalsIgnoreCase(performancePrepend))
			{
				//Store the Event
				String date = personalInfo[4];
				String startTime = personalInfo[5];
				String endTime = personalInfo[6];
				Date useDate = parseDate(date);
				Time start = parseTime(startTime, useDate);
				Time end = parseTime(endTime, useDate);
				Event newEvent = new Event(start, end, prepend.substring(0, 6));
				events.add(newEvent);
				DatabaseUtil.addEvent(newEvent);
			}
			else if (prepend.equalsIgnoreCase(absentPrependPerformance) || prepend.equalsIgnoreCase(absentPrependRehearsal)
					|| prepend.equalsIgnoreCase(tardyPrepend))
			{
				String netID = personalInfo[3];
				String date = personalInfo[4];
				String startTime = personalInfo[5];
				String endTime = personalInfo[6];
				
				User person = DatabaseUtil.getUser(netID);
				Date useDate = parseDate(date);
				Time start = parseTime(startTime, useDate);
				Time end = parseTime(endTime, useDate);
				updateMaps(person, prepend, useDate, start, end, absences, tardies);
			}
		}
		//TODO add all the stuff in the hashmaps
		updateALLTheThings(absences, tardies, events);	
	}

	private static Date parseDate(String date) {
		// Dates in the form year-month-day
		Scanner parser = new Scanner(date).useDelimiter("-");
		return new Date(Integer.parseInt(parser.next()),
				Integer.parseInt(parser.next()),
				Integer.parseInt(parser.next()));
	}

	private static Time parseTime(String time, Date date) {
		if (time.length() >= 4)
		{
			int hour = Integer.parseInt(time.substring(0, 2));
			int minute = Integer.parseInt(time.substring(2, 4));
			return new Time(hour, minute, date);
		}
		else //Return an empty date. This should only happen for Tardy endtimes cause it's just a |
			return new Time(0,0, date);
	}

	private static void updateMaps(User guy, String prepend, Date eventDate, Time start, Time end, HashMap<User, Absence> absences, HashMap<User, Tardy> tardies) {
		if ( guy!= null && (guy.getType().equalsIgnoreCase("Student")))
		{
			if (prepend.equalsIgnoreCase(absentPrependPerformance)) 
			{
				Absence toAdd = new Absence(guy.getNetID(), start, end, "Performance");
				//guy.addAbsence(new Absence(start, end, "Performance"));
				if (!absences.containsKey(guy))
					absences.put(guy, toAdd);
				else
				{
					if (timeLaterThan(absences.get(guy).getStartTime(), toAdd.getStartTime()))
					{
						absences.put(guy, toAdd);
					}
				}
			} else if (prepend.equalsIgnoreCase(absentPrependRehearsal)) 
			{
				//guy.addAbsence(new Absence(start, end, "Rehearsal"));
				Absence toAdd = new Absence(guy.getNetID(), start, end, "Rehearsal");
				if (!absences.containsKey(guy))
					absences.put(guy, toAdd);
				else
				{
					if (timeLaterThan(absences.get(guy).getStartTime(), toAdd.getStartTime()))
					{
						absences.put(guy, toAdd);
					}
				}
			}
			else if (prepend.equalsIgnoreCase(tardyPrepend)) 
			{
				//guy.addTardy(new Tardy(start, "unknown"));
				Tardy toAdd = new Tardy(guy.getNetID(), start, "unknown");
				if (!tardies.containsKey(guy))
					tardies.put(guy, toAdd);
				else
				{
					if (timeLaterThan(tardies.get(guy).getTime(), toAdd.getTime()));
					{
						tardies.put(guy, toAdd);
					}
				}
			}
		}
	}
	
	//Returns true if second time is later  than first time
	private static boolean timeLaterThan(Time firstTime, Time secondTime)
	{
		return secondTime.compareTo(firstTime) > 0;
	}
	
	private static void updateALLTheThings(HashMap<User, Absence> absences, HashMap<User, Tardy> tardies, List<Event> events)
	{
		//If they are in the TardyMap then take them out of the AbsentMap
		//Update the tardies to what type of event they are: rehearsal, performance
		for (User u: tardies.keySet())
		{
			if (absences.containsKey(u))
			{
				absences.remove(u);
			}
			for (int i = 0; i < events.size(); i++)
			{
				Event e = events.get(i);
				if (e.getStartTime().compareTo(tardies.get(u).getTime()) <= 0 
						&& e.getEndTime().compareTo(tardies.get(u).getTime()) >= 0)
				{
					tardies.get(u).setType(e.getType());
				}
			}
		}
		
		for (User u: absences.keySet())
		{
			DatabaseUtil.addAbsence(absences.get(u));
		}
		
		for (User u: tardies.keySet())
		{
			DatabaseUtil.addTardy(tardies.get(u));
		}
		
	}

}
