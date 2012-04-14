package servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.servlet.http.*;

import forms.Form;

import people.User;

import serverLogic.DatabaseUtil;
import time.Date;
import time.Time;

public class FormD extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		if (req.getParameter("Submit") != null)
		{
			String Email = req.getParameter("Email");
			String AmountWorked = req.getParameter("AmountWorked");
			String Details = req.getParameter("Details");
			
			if (Email != null && AmountWorked != null && Details != null && Email != "" && AmountWorked != "" && Details != "" 
					&& req.getParameter("StartDay") != null && req.getParameter("StartMonth") != null && req.getParameter("StartYear") != null   
					&& req.getParameter("StartDay") != "" && req.getParameter("StartMonth") != "" && req.getParameter("StartYear") != "" )
			{
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
				Time time = new Time(0, 0, date);
				
				User guy = DatabaseUtil.getUser(""+req.getSession().getAttribute("user"));
				Form form = new Form(guy.getNetID(), Details, time, time, Email, Integer.parseInt(AmountWorked), "FormD");
				DatabaseUtil.addForm(form);
				
				Properties props = new Properties();
				Session session = Session.getDefaultInstance(props, null);
				String msgBody = "...";
				try
				{
					Message msg = new MimeMessage(session);
					msg.setFrom(new InternetAddress("bmaxwell921@gmail.com"));
					msg.addRecipient(Message.RecipientType.TO, new InternetAddress(Email));
					msg.setSubject(guy.getFirstName() + " " + guy.getLastName() + " requests approval of work time.");
					msg.setText((msgBody));
					Transport.send(msg);
					
				}
				catch (AddressException e)
				{
					e.printStackTrace();					
				}
				catch (MessagingException e)
				{
					e.printStackTrace();
				}
				
				resp.sendRedirect("/JSPPages/Student_Page.jsp?formSubmitted='true'");
				return;
			}
			else
			{
				resp.sendRedirect("/JSPPages/Student_Form_D_TimeWorked.jsp?error='nullFields'");
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
