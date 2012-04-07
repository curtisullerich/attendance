package servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import attendance.AttendanceReport;

import people.User;

import serverLogic.DatabaseUtil;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet
{

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//Reads in the stuff from NetID, Password, and Re-Enter Password then makes a person from there
		String buttonPressed = req.getParameter("Register");
		String netID, password, firstName, lastName, univID, major, section;
		int year;
		if (buttonPressed != null)
		{
			//Grab all the data from the fields
			netID = req.getParameter("NetID");
			password = req.getParameter("Hashed Password");
			firstName = req.getParameter("FirstName");
			lastName = req.getParameter("LastName");
			univID = req.getParameter("UniversityID");
			year = Integer.parseInt(req.getParameter("Year"));
			major = req.getParameter("Major");
			section = req.getParameter("Section");
			
//			if (netID != null && password != null && firstName != null && lastName != null && univID != null && year > 0 && major != null && section != null)
//			{
				//Need to do various other checks and stuff. Like whether this account already exists
				
				//check for already registered users
				if(!DatabaseUtil.userExists(netID))
				{	
					//Create a new person with this partial information and put it in the datastore
					User guy = new User(netID, password, firstName,
							lastName, univID, "Student", major,
							section, year);	
					DatabaseUtil.addUser(guy);
					DatabaseUtil.addAttendanceReport(new AttendanceReport(netID));
					//Send them back to the login page?
					resp.sendRedirect("/JSPPages/loginPage.jsp?successfulAdd=true&user=" + netID);
				}
				
				else
				{
					resp.sendRedirect("/JSPPages/register.jsp?userExists=true&user=" + netID);
				}
//			}
//			else
//			{
//				//Throw an alert that they didn't add a field
//				System.out.println("some fields were invalid");
//			}
		}
	}
}
