package servlets;

import java.io.IOException;
import javax.servlet.http.*;

import people.Person;

import serverLogic.DatabaseUtil;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet 
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException 
	{
		resp.setContentType("text/plain");
		String buttonPressed = req.getParameter("Login");
		String directTo = "/JSPPages/loginPage.jsp";
		if (buttonPressed != null)
		{
			//Check to see if the user name and password are correct
			String netID = req.getParameter("User Name");
			String hashedPassword = req.getParameter("Hashed Password");
			//Check that they actually added something
			
			Person user = DatabaseUtil.getPerson(netID);
			
			//Hash the password
			if(!validateLogin(user, hashedPassword))
			{
				directTo = "/JSPPages/invalidLogin.jsp";
			}
			//Will go somewhere else once we have more stuff implemented
			//directTo = "somewhere else";
			else
			{
				//Figure out if it was a Director, TA, or Student
				if (user.getClass() == people.Student.class) {
					directTo = "/JSPPages/7_Student_Page.jsp";
				} else if (user.getClass() == people.TA.class) {
					directTo = "/JSPPages/26_TA_Page.jsp";
				} else if (user.getClass() == people.Director.class){
					directTo = "/JSPPages/15_Director_Page.jsp";
				}
				
				//add the user to the session
				req.getSession().setAttribute("user", netID);
			}

		}
		else
		{
			buttonPressed = req.getParameter("Register");
			if (buttonPressed != null)
			{
				//Take us to another page to register
				directTo = "/JSPPages/register.jsp";
			}
		}
		resp.sendRedirect(directTo);
	
	}
	
	private boolean validateLogin(Person attemptedAccount, String attemptedPassword)
	{
		if (attemptedAccount != null && attemptedAccount.getPassword().equals(attemptedPassword))
			return true;
		return false;
	}
}
