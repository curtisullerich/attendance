package servlets;

import java.io.IOException;
import javax.servlet.http.*;

import classes.Person;

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
			String password = req.getParameter("Password");
			//Check that they actually added something
			
			//Hash the password
			if(!validateLogin(DatabaseUtil.getPerson(netID), password))
			{
				directTo = "/JSPPages/invalidLogin.jsp";
			}
			
			//Will go somewhere else once we have more stuff implemented
			//directTo = "somewhere else";

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
		if (attemptedAccount.getPassword().equals(attemptedPassword))
			return true;
		return false;
	}
}
