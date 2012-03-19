package servlets;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import classes.Student;

import serverLogic.DatabaseUtil;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet
{

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//Reads in the stuff from NetID, Password, and Re-Enter Password then makes a person from there
		String buttonPressed = req.getParameter("Register");
		String netID, password, reEnter, firstName, lastName;
		if (buttonPressed != null)
		{
			//Grab all the data from the fields
			netID = req.getParameter("NetID");
			password = req.getParameter("Password");
			reEnter = req.getParameter("Re-Enter Password");
			firstName = req.getParameter("FirstName");
			lastName = req.getParameter("LastName");
			
			if (netID != null && password != null && reEnter != null && firstName != null && lastName != null)
			{
				//Need to do various other checks and stuff. Like whether this account already exists
				//Create a new person with this partial information and put it in the datastore
				Student s = new Student(netID, password, firstName, lastName);
				DatabaseUtil.addStudent(s);
				//Send them back to the login page?
				resp.sendRedirect("/JSPPages/loginPage.jsp");
			}
			else
			{
				//Throw an alert that they didn't add a field
			}
		}
		
		//Will need to check if they pressed the back button
		
	}
}
