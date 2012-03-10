package serverLogic;

import java.util.Scanner;

import classes.*;

public class Parser 
{	
	private static final String absentPrependPerformance = "absentStudentPerformance";
	private static final String absentPrependRehearsal = "absentStudentRehearsal";
	private static final String rehearsalPrepend = "storedRehearsal";
	private static final String performancePrepend = "storedPerformance";
	private static final String tardyPrepend = "tardyStudent";
	private static final String studentPrepend = "studentRecord";
	
	
	public static void splat(String add)
	{
		//Splats the massive string into an array of strings for each person
		String[] people = add.split(" ");
		System.out.println("The string is: " + add);
		//Need to make a new Event and persist it
		//For every element go thru this
		for (String e: people)
		{
			//The person splat is in the form
			//prepend, firstName, lastName, netID, date, startTime, endTime, rank
			String[] personalInfo = e.split(",");
			String prepend = personalInfo[0];
			String netID = personalInfo[3];
			String date = personalInfo[4];
			String startTime = personalInfo[5];
			String endTime = personalInfo[6];
			
			//Now get the person that this info goes to
			Person person = DatabaseUtil.getPerson(netID);
			Date useDate = parseDate(date);
			Time start = parseTime(startTime);
			Time end = parseTime(endTime);
			//Now I need to find out what type of absence or tardy it is
			updateStudent(person, prepend, useDate, start, end);
		}
		//Parse the string and then add whatever it is
	}
	
	private static Date parseDate(String date)
	{
		//Dates in the form year-month-day
		Scanner parser = new Scanner(date).useDelimiter("-");
		return new Date(Integer.parseInt(parser.next()), Integer.parseInt(parser.next()), Integer.parseInt(parser.next()));	
	}
	
	private static Time parseTime(String time)
	{
		int hour = Integer.parseInt(time.substring(0, 2));
		int minute = Integer.parseInt(time.substring(2, 4));
		return new Time(hour, minute);
	}
	
	private static void updateStudent(Person p, String prepend, Date eventDate, Time start, Time end)
	{
		if (prepend.equalsIgnoreCase(absentPrependPerformance))
		{
			p.addAbsence(new PerformanceAbsence(eventDate, start, end));
		}
		else if (prepend.equalsIgnoreCase(absentPrependRehearsal))
		{
			p.addAbsence(new RehearsalAbsence(eventDate, start, end));
		}
		//Need to do something to figure out what type of event this is or something
		else if(prepend.equalsIgnoreCase(tardyPrepend))
		{
			p.addTardy(new Tardy(eventDate, start, end));
		}	
	}

}
