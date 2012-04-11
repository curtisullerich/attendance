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
public class TA_Edit_InfoServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException 
			{
		//Reads in the stuff from NetID, Password, and Re-Enter Password then makes a person from there
		String buttonPressed = req.getParameter("SaveInfo");
		String  password;
		
		if(buttonPressed!= null)
		{
			//Grab all the data from the fields
			password = req.getParameter("Hashed Password");
			
			if ( password != null)
			{
				User guy = DatabaseUtil.getUser(""+req.getSession().getAttribute("user"));
				
				guy.setHashedPassword(password);
				
				//overwrite the old user
				DatabaseUtil.addUser(guy);
				
				//Send them back to the login page?
				resp.sendRedirect("/JSPPages/TA_Page.jsp?successfulSave='true'");
			}
			else
			{
				//Throw an alert that they didn't add a field
				System.out.println("some fields were invalid");
				resp.sendRedirect("/JSPPages/TA_Page.jsp?successfulSave='false'");
	
			}
		}
	}
}
