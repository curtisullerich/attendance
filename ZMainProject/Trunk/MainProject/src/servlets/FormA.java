package servlets;

import java.io.IOException;

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
			//	public Date(int year, int month, int day)
			
			if ( req.getParameter("startDay") != null && req.getParameter("startMonth") != null && req.getParameter("startYear") != null && reason != null 
					&& req.getParameter("startDay") != ""
					&& req.getParameter("startMonth") != "" && req.getParameter("startYear") != "" && req.getParameter("endDay") != ""
					&& req.getParameter("endMonth") != "" && req.getParameter("endYear") != "" && req.getParameter("startHour") != "" 
					&& req.getParameter("startMinute") != "" && startrdio1 != "" && type1 != "") {
				
			
			Date date = new Date(Integer.parseInt(req.getParameter("StartYear")),Integer.parseInt(req.getParameter("StartMonth")),Integer.parseInt(req.getParameter("StartDay")));
			//			(int hour, int minute, Date date)
			Time startTime = new Time(0,0,date);
			Time endTime= new Time(23,59,date);
			directTo= "/JSPPages/Student_Page.jsp";
			
			//Form( netID,  reason,  startTime,  endTime, type)		
			Form myform= new Form(""+req.getSession().getAttribute("user"),reason,startTime,endTime, "A" );

			DatabaseUtil.addForm(myform);
			
			resp.sendRedirect("/JSPPages/Student_Page.jsp?formSubmitted='true'");

		
		}
		else
		{
			resp.sendRedirect("/JSPPages/Student_Form_A_Performance_Absence_Request.jsp?error='nullFields'");
			return;
		}
		
		
	}

}
