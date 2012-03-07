package practice;

import java.io.IOException;

import java.util.LinkedList;
import java.util.List;

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
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException 
	{
		resp.setContentType("text/plain");
		//PrintWriter out = resp.getWriter();
		resp.getWriter().println("All the students in the datastore:");
		List<Person> students = makeStudents();
		
		DatastoreUtil.deleteAll();
		
		DatastoreUtil.addList(students);
		System.out.println(DatastoreUtil.datastore.getIndexes().size());
		List<Person> foundStuds = DatastoreUtil.getAll();
		for (int i = 0; i < foundStuds.size(); i++)
		{
			resp.getWriter().println(foundStuds.get(i));
		}
	}
	
	private List<Person> makeStudents()
	{
		List<Person> list = new LinkedList<Person>();
		list.add(new Person("bmaxwell", "Brandon", "Maxwell", "password", "Computer Science", 
				"Gloria", "Tenors"));
		list.add(new Person("cullerich", "Curtis", "Ullerich", "password", "Computer Engineer", 
				"Vicky", "Uphonium"));
		list.add(new Person("twegter", "Todd", "Wegter", "password", "Computer Engineer", 
				"Vicky", "Sax"));
		list.add(new Person("yhz", "Fei", "Zhu", "password", "Computer Science", 
				"Gloria", "Piano"));
//		list.add(new Person("lakdf", "Fei", "Zhu", "password", "Computer Science", 
//				"Gloria", "Piano"));
		return list;
	}
}
