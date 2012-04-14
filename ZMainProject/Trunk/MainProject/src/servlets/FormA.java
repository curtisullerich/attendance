package servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.*;

import forms.Form;

import serverLogic.DatabaseUtil;
import time.Date;
import time.Time;
import people.User;
/**
 * 
 * @author Yifei Zhu
 * 
 * This is Form A - Performance Absence Request 
 *
 */
@SuppressWarnings("serial")
public class FormA extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		
		String buttonPressed = req.getParameter("Submit");
		String directTo="/JSPPages/Student_Page.jsp?formSubmitted='true'";
		
		if (buttonPressed != null)
		{
			String reason = req.getParameter("Reason"); 
			if(reason == null || reason == "") {
				resp.sendRedirect("/JSPPages/Student_Form_A_Performance_Absence_Request.jsp?error='noReason'");
				return;
			}
			
			else if (req.getParameter("StartDay") != null && req.getParameter("StartMonth") != null && req.getParameter("StartYear") != null   
				  && req.getParameter("StartDay") != ""   && req.getParameter("StartMonth") != ""   && req.getParameter("StartYear") != "" ) {
				
				int year = Integer.parseInt(req.getParameter("StartYear"));
				int month = Integer.parseInt(req.getParameter("StartMonth"));
				int day = Integer.parseInt(req.getParameter("StartDay"));
				if(!isValidateDate(month, day, year)) {
					resp.sendRedirect("/JSPPages/Student_Form_A_Performance_Absence_Request.jsp?error='invalidDate'");
					return;
				}
				//public Date(int year, int month, int day)
				Date date = new Date(year, month, day);
				
				//(int hour, int minute, Date date)
				Time startTime = new Time(0,0,date);
				Time endTime= new Time(23,59,date);
				
				//Form( netID,  reason,  startTime,  endTime, type)		
				Form myform= new Form(""+req.getSession().getAttribute("user"),reason,startTime,endTime, "FormA" );
	
				DatabaseUtil.addForm(myform);
				
				resp.sendRedirect("/JSPPages/Student_Page.jsp?formSubmitted='true'");
				return;
			}
			else
			{
				resp.sendRedirect("/JSPPages/Student_Form_A_Performance_Absence_Request.jsp?error='nullFields'");
				return;
			}
		}
	}


	private boolean isValidateDate(int month, int day, int year) {
		int monthDays[] = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (month <= 0 || month > 12)
			return false;
		int thisYear = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago")).get(Calendar.YEAR);
		if (year > thisYear + 1 || year < thisYear)
			return false;
		if (day > monthDays[month])
			return false;
		return true;
	}
	
}
