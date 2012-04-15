package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Yifei Zhu
 *
 *get rank from Director_Set_Rank.jsp and save it to database
 *
 */
@SuppressWarnings("serial")
public class Set_Rank extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws IOException
	{
		String buttonPressed = req.getParameter("Submit");
		String directTo = "/JSPPages/Director_Page.jsp";
		//System.out.println("HELLO");

		if(buttonPressed !=null)
		{
			String netID0 = req.getParameter("NetID0");
			String netID1 = req.getParameter("NetID1");
			System.out.println(netID0);
			System.out.println(netID1);
			System.out.println("HELLO");
			
		}
		
		
	}
	
	
	

}
