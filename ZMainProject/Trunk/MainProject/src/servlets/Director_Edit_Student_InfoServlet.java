package servlets;

import java.io.IOException;

import javax.mail.Session;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import people.User;

import serverLogic.DatabaseUtil;

/**
 * 
 * @author Todd Wegter
 *
 */
@SuppressWarnings("serial")
public class Director_Edit_Student_InfoServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException 
			{
		//Reads in the stuff from NetID, Password, and Re-Enter Password then makes a person from there
		String buttonPressed = req.getParameter("SaveInfo");
		String  password, firstName, lastName, univID, major, section, netID;
		int year;
		
		if(buttonPressed!= null)
		{
			//Grab all the data from the fields
			netID = req.getParameter("Student NetID");
			password = req.getParameter("Hashed Password");
			firstName = req.getParameter("FirstName");
			lastName = req.getParameter("LastName");
			univID = req.getParameter("UniversityID");
			year = Integer.parseInt(req.getParameter("Year"));
			major = req.getParameter("Major");
			section = req.getParameter("Section");
			
			if (firstName != null && lastName != null && univID != null && year > 0 && major != null &&
					section != null && firstName != "" && lastName != "" && univID != "" && major != "" && section != "")
			{
				User guy = DatabaseUtil.getUser(netID);

				guy.setFirstName(firstName);
				guy.setLastName(lastName);
				guy.setUnivID(univID);
				guy.setYear(year);
				guy.setMajor(major);
				guy.setSection(section);
				if(password != null && password != "")
				{
					guy.setHashedPassword(password);
				}
				
				//overwrite the old user
				DatabaseUtil.addUser(guy);
				
				String directTo = "/JSPPages/Director_Student_View.jsp?student="+netID+"&successfulSave='true'";
				//Send them back to the Director Student View
				resp.sendRedirect(directTo);
			}
			else
			{
				//Throw an alert that they didn't add a field
				System.out.println("some fields were invalid");
				resp.sendRedirect("/JSPPages/Director_Student_View.jsp?student="+netID+"&successfulSave='false'");
	
			}
		}
	}
}
