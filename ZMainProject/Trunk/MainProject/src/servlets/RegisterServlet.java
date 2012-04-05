package servlets;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import people.Person;
import people.Student;
import people.User;

import serverLogic.DatabaseUtil;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet
{

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//Reads in the stuff from NetID, Password, and Re-Enter Password then makes a person from there
		String buttonPressed = req.getParameter("Register");
		String netID, password, reEnter, firstName, lastName, univID;
		if (buttonPressed != null)
		{
			//Grab all the data from the fields
			netID = req.getParameter("NetID");
			password = req.getParameter("Hashed Password");
			firstName = req.getParameter("FirstName");
			lastName = req.getParameter("LastName");
			univID = req.getParameter("UniversityID");
			
			if (netID != null && password != null && firstName != null && lastName != null && univID != null)
			{
				//Need to do various other checks and stuff. Like whether this account already exists
				//Create a new person with this partial information and put it in the datastore
				User guy = new User(netID, password, firstName, lastName, univID, "Student");
				DatabaseUtil.addUser(guy);
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
