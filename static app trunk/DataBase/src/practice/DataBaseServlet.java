package practice;

import java.io.IOException;
import javax.servlet.http.*;

import com.google.appengine.api.datastore.Entity;


@SuppressWarnings("serial")
public class DataBaseServlet extends HttpServlet 
{
	 //private static final Logger logger = Logger.getLogger(DatastoreUtil.class.getCanonicalName());

//	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException
//	{
////		logger.log(Level.INFO, "Obtaining Item listing");
////		PrintWriter out = resp.getWriter();
////		Entity found = DatastoreUtil.findEntity("bmaxwell");
////		out.println("This is my nameSpace: " + found.getNamespace());
//	}
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException {
		resp.setContentType("text/plain");
		//PrintWriter out = resp.getWriter();
		resp.getWriter().println("Allows you to add a person");
		
		Person toAdd = new Person("bmaxwell", "Brandon", "Maxwell", "password", "Computer Science", 
				"Gloria", "Tenors");
		DatastoreUtil.addPerson(toAdd);
		
		Entity found = DatastoreUtil.findEntity("bmaxwell");
		if (found == null) 
			resp.getWriter().println("Didn't find me");
		else
			resp.getWriter().println("This is my nameSpace: " + found.getNamespace());
	}
}
