package servlets;

import java.io.IOException;

import javax.servlet.http.*;

import forms.Form;

import serverLogic.DatabaseUtil;
import time.Date;
import time.Time;

/**
 * 
 * @author Yifei Zhu
 * 
 * This is Request for Excuse from Rehearsal | FORM C
 *
 */
@SuppressWarnings("serial")
public class FormC extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		
		String buttonPressed = req.getParameter("Submit");
		String directTo="/JSPPages/Student_Page.jsp?formSubmitted='true'";
		
		if (buttonPressed != null)
		{
			String reason = req.getParameter("Reason"); 
			
			//	public Date(int year, int month, int day)
			Date date = new Date(Integer.parseInt(req.getParameter("StartYear")),Integer.parseInt(req.getParameter("StartMonth")),Integer.parseInt(req.getParameter("StartDay")));
			//			(int hour, int minute, Date date)
			Time startTime = new Time(0,0,date);
			Time endTime= new Time(23,59,date);
			directTo= "/JSPPages/Student_Page.jsp";
			
			//Form( netID,  reason,  startTime,  endTime, type)		
			Form myform= new Form(""+req.getSession().getAttribute("user"),reason,startTime,endTime, "C" );

			DatabaseUtil.addForm(myform);
			
			resp.sendRedirect(directTo);
		
		}
		else
		{
			//Throw an alert that they didn't add a field
			System.out.println("some fields were invalid");
		}
		
		
	}

}
