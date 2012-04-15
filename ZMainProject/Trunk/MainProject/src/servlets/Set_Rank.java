package servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import serverLogic.DatabaseUtil;
import people.User;

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
//			System.out.println("START");
//			String netID0 = req.getParameter("netID0");
//			String netID1 = req.getParameter("netID1");
////			String netID1 = req.getParameter("Rank");
//			System.out.println(netID0);
//			System.out.println(netID1);
////			System.out.println("HELLO");

			int i=0;
			List<User> students = DatabaseUtil.getStudents();
			
			while(req.getParameter("netID"+i)!=null)
			{
				String netID = req.getParameter("netID"+i);
				for(User s :students)
				{
					if(s.getNetID().equals(netID))
					{
						
						s.setRank( req.getParameter("Rank"+i));
						DatabaseUtil.refreshUser(s);
						System.out.println(s.getRank());
					}
				}
				
				i++;
			}
			
			
		
		}
		
		
	}
	
	
	

}
