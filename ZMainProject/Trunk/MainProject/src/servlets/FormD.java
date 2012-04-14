package servlets;

import java.io.IOException;
import java.util.Properties;

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
			
			if (Email != null && AmountWorked != null && Details != null)
			{
				Date date = new Date(Integer.parseInt(req.getParameter("StartYear")), Integer.parseInt(req.getParameter("StartMonth")),
						Integer.parseInt(req.getParameter("StartDay")));
				Time time = new Time(0, 0, date);
				User guy = DatabaseUtil.getUser(""+req.getSession().getAttribute("user"));
				Form form = new Form(guy.getNetID(), Details, time, time, "FormD");
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
				
				resp.sendRedirect("/JSPPages/Student_Page.jsp");
				
			}
			else
			{
				//TODO now you fucked up
			}
		}
		
	}
}
