package servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.*;

public class FormB extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		if (req.getParameter("Submit") != null)
		{
			String dept, course, sect, building, comment;
			int startMonth, startDay, startYear, endMonth, endDay, endYear, startHour, startMinute;
			startMonth = startDay = startYear = endMonth = endDay = endYear = startHour = startMinute = 0;
			//netID = req.getParameter("NetID");
			dept = req.getParameter("dept");
			course = req.getParameter("course");
			sect = req.getParameter("sect");
			building = req.getParameter("building");
			//Doesn't have to be there
			comment = req.getParameter("comment");
			if (dept != null && course != null && sect != null && building != null && req.getParameter("startDay") != null
					&& req.getParameter("startMonth") != null && req.getParameter("startYear") != null
					&& req.getParameter("endDay") != null && req.getParameter("endMonth") != null &&
					req.getParameter("endYear") != null && req.getParameter("startHour") != null 
					&& req.getParameter("startMinute") != null) {
				try
				{
					startMonth = Integer.parseInt(req.getParameter("startMonth"));
					startDay = Integer.parseInt(req.getParameter("startDay"));
					startYear = Integer.parseInt(req.getParameter("startYear"));
					
					endMonth = Integer.parseInt(req.getParameter("endMonth"));
					endDay = Integer.parseInt(req.getParameter("endDay"));
					endYear = Integer.parseInt(req.getParameter("endYear"));
					
					startHour = Integer.parseInt(req.getParameter("startHour"));
					startMinute = Integer.parseInt(req.getParameter("startMinute"));
				}

				
				catch (NumberFormatException e)
				{
					resp.sendRedirect("/invalidInputs");
				}
				
				if (!isValidateDate(startMonth, startDay, startYear))
				{
					resp.sendRedirect("/invalidInputs");
				}
				if (!isValidateDate(endMonth, endDay, endYear))
				{
					resp.sendRedirect("/invalidInputs");
				}
				if (!isValidateTime(startHour, startMinute))
				{
					resp.sendRedirect("/invalidInputs");
				}
			}	
		}
	}
	
	private boolean isValidateDate(int month, int day, int year)
	{
		int monthDays[] = {0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
		if (month <= 0 || month > 12)
			return false;
		int thisYear = Calendar.getInstance().get(Calendar.YEAR) - 1900;
		if (year > thisYear + 1 || year < thisYear)
			return false;
		if (day > monthDays[month])
			return false;
		return true;
	}
	
	private boolean isValidateTime(int hour, int minute)
	{
		return hour <= 12 && minute <= 59;
	}

}
