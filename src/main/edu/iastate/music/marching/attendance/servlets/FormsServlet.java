package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.servlets.StudentServlet.Page;

public class FormsServlet extends AbstractBaseServlet {
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!isLoggedIn(req, resp, User.Type.Student, User.Type.Director) )
			return;

		Page page = pathInfoToPage(req, resp, Page.class);
		if (page == null) {
			do404(req, resp);
			return;
		}
		
		switch(page) {
		case forma:
			
			break;
		case formb:
			
			break;
		case formc:
		
			break;
		case formd:
			
			break;
		case index:

			break;
		case view:

			break;
		default:
			do404(req, resp);
			return;
			break;
		
		
		
		}
		
		
	}

	private enum Page {
		forma, formb, formc, formd, index, view; 
	}
	
	@Override
	protected String getJspPath() {
		return "forms";
	}

}
