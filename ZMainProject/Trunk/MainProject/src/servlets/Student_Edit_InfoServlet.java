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
 * @author Yifei Zhu
 *
 */
@SuppressWarnings("serial")
public class Student_Edit_InfoServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException 
			{
		//Reads in the stuff from NetID, Password, and Re-Enter Password then makes a person from there
		String buttonPressed = req.getParameter("SaveInfo");
		String  password, reEnter, firstName, lastName, univID, major, section;
		int year;
		
		if(buttonPressed!= null)
		{
		
			//Grab all the data from the fields
			password = req.getParameter("Hashed Password");
			firstName = req.getParameter("FirstName");
			lastName = req.getParameter("LastName");
			univID = req.getParameter("UniversityID");
			year = Integer.parseInt(req.getParameter("Year"));
			major = req.getParameter("Major");
			section = req.getParameter("Section");
			
//			if ( password != null && firstName != null && lastName != null && univID != null && year > 0 && major != null && section != null)
//			{
				User guy = DatabaseUtil.getUser(""+req.getSession().getAttribute("user"));

				guy.setFirstName(firstName);
				guy.setLastName(lastName);
				guy.setUnivID(univID);
				guy.setYear(year);
				guy.setMajor(major);
				guy.setSection(section);
				guy.setHashedPassword(password);
				
				//overwrite the old user
				DatabaseUtil.addUser(guy);
				
				//Send them back to the login page?
				System.out.println("some fields were invalid");
				resp.sendRedirect("/JSPPages/Student_Page.jsp");
			}
//		}
		else
		{
			//Throw an alert that they didn't add a field
			System.out.println("some fields were invalid");
		}
		
		
			}

}
