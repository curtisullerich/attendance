package servlets;

import java.io.IOException;

import javax.servlet.http.*;

public class FormA extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		if (req.getParameter("Back") != null) resp.sendRedirect("/Student_Page.jsp");
		if (req.getParameter("Submit") != null)
		{
			System.out.println("Hello");
		}
	}

}
