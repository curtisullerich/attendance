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
public class RegisterStaffServlet extends HttpServlet
{

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//Reads in the stuff from NetID, Password, and Re-Enter Password then makes a person from there
		String buttonPressed = req.getParameter("Register");
		String directorNetID, directorPassword, studentStaffNetID, studentStaffPassword;
		boolean directorAdded = false, studentStaffAdded  = false;
		if (buttonPressed != null)
		{
			//Grab all the data from the fields
			directorNetID = req.getParameter("Director NetID");
			directorPassword = req.getParameter("Hashed Director Password");
			studentStaffNetID = req.getParameter("Student Staff NetID");
			studentStaffPassword = req.getParameter("Hashed Student Staff Password");			
			
			if (directorNetID != null && directorPassword != null && studentStaffNetID != null && studentStaffPassword != null &&
					directorNetID != "" && directorPassword != "" && studentStaffNetID != "" && studentStaffPassword != "")
			{
				//Need to do various other checks and stuff. Like whether this account already exists
				
				//create Director account if it doesn't exist
				if(!DatabaseUtil.directorExists())
				{	
					//Create a new person with this partial information and put it in the datastore
					User guy = new User(directorNetID, directorPassword, "Band",
							"Director", "000000000", "Director", "Music",
							"Director", -1);	
					DatabaseUtil.addUser(guy);
					directorAdded = true;
				}
				
				//create Student Staff account
				if(!DatabaseUtil.studentStaffExists())
				{	
					//Create a new person with this partial information and put it in the datastore
					User guy = new User(studentStaffNetID, studentStaffPassword, "Student",
							"Staff", "000000000", "TA", "Music",
							"TA", -1);	
					DatabaseUtil.addUser(guy);
					studentStaffAdded = true;
				}
				
				if(studentStaffAdded && directorAdded)
					resp.sendRedirect("/JSPPages/loginPage.jsp?successfulAdd=true&user=Both staff accounts");
				else if(!directorAdded && studentStaffAdded)
					resp.sendRedirect("/JSPPages/loginPage.jsp?successfulAdd=true&user=Director account already exists. Student Staff account");
				else if(directorAdded && !studentStaffAdded)
					resp.sendRedirect("/JSPPages/loginPage.jsp?successfulAdd=true&user=Student Staff account already exists. Director account");
				else
					resp.sendRedirect("/JSPPages/registerStaff.jsp?userExists=true&user=Both staff accounts");

			}
			else
			{
				//Throw an alert that they didn't add a field
				System.out.println("some fields were invalid");
				resp.sendRedirect("/JSPPages/registerStaff.jsp?error='true'");
			}
			
			
		}
	}
}
