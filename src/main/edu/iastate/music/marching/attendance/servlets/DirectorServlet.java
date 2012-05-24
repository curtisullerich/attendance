package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
<<<<<<< HEAD
import java.io.PrintWriter;
import java.util.Enumeration;
=======
import java.util.Date;
>>>>>>> 155df6c0db15df0ae902b12398ccd8877c67313e
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

<<<<<<< HEAD
import edu.iastate.music.marching.attendance.controllers.AppDataController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.AppData;
=======
import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
>>>>>>> 155df6c0db15df0ae902b12398ccd8877c67313e
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class DirectorServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6100206975846317440L;

	public enum Page {
		index, appinfo, attendance, export, forms, unanchored, users, user, stats, info;
	}

	private static final String SERVLET_PATH = "director";

	public static final String INDEX_URL = pageToUrl(
			DirectorServlet.Page.index, SERVLET_PATH);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp, User.Type.Director)) {
			resp.sendRedirect(AuthServlet.URL_LOGIN);
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case index:
				showIndex(req, resp);
				break;
			case attendance:
				showAttendance(req, resp);
				break;
			case appinfo:
				showAppInfo(req, resp);
				break;
			case unanchored:
				showUnanchored(req, resp);
				break;
			case users:
				showUsers(req, resp);
				break;
			case user:
				showUserInfo(req, resp);
				break;
			case stats:
				showStats(req, resp);
				break;
			case info:
				showInfo(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private void showStats(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		DataTrain train = DataTrain.getAndStartTrain();
		
		PageBuilder page = new PageBuilder(Page.stats, SERVLET_PATH);

		//example value: train.getAppDataController().get();
		
		Date date = new Date();	
		
		int numAbsences = train.getAbsencesController().get(Absence.Type.Absence).size();
		int numTardy = train.getAbsencesController().get(Absence.Type.Tardy).size();
		int numLeaveEarly = train.getAbsencesController().get(Absence.Type.EarlyCheckOut).size();
		int numStudents = train.getUsersController().get(User.Type.Student).size();
		int numEvents = train.getEventsController().readAll().size(); 
		String avgPresent =  numEvents != 0 ? (numStudents-(numAbsences/numEvents))+"" : "No Recorded Events";
		String avgTardy = numEvents != 0 ? (numTardy/numEvents)+"" : "No Recorded Events";
		String avgLeaveEarly = numEvents != 0 ? (numLeaveEarly/numEvents)+"" : "No Recorded Events";		
		String avgAbsent = numEvents != 0 ? (numAbsences/numEvents)+"" : "No Recorded Events";
		String avgPresentWR = numEvents != 0 ? (numStudents-(numAbsences+numTardy+numLeaveEarly)/numEvents)+"" : "No Recorded Events";
		
		page.setAttribute("date", date);
		
		page.setAttribute("numStudents", numStudents);
		page.setAttribute("numEvents", numEvents);
		page.setAttribute("avgPresentStudents", avgPresent);
		page.setAttribute("avgTardyStudents", avgTardy);
		page.setAttribute("avgAbsentStudents", avgAbsent);
		page.setAttribute("avgPresentStudentsWR", avgPresentWR);
		page.setAttribute("avgLeaveEarly", avgLeaveEarly);

		page.setPageTitle("Statistics");
		
		page.passOffToJsp(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Admin login required by web.xml entry

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case appinfo:
				postAppInfo(req, resp);
				break;
			case users:
				postUserInfo(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private void postAppInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		AppDataController appDataController = new AppDataController(train);
		//get the train
		
		//check if there was a form submitted
			//no->//rebuild and return the page with errors
			//yes->//parse and validate
				//get the item from the controller
				//update the item and save it
				//return the jsp
		boolean validForm = true;
		List<String> errors = new LinkedList<String>();
		String[] emails = null;
		
		if (!ValidationUtil.isPost(req)) {
			//not a valid POST
			validForm = false;
		}
		else
		{
			emails = req.getParameter("hiddenEmails").split("delimit");
			if (emails != null) {
				AppData data = appDataController.get();
				List<String> emailList = new LinkedList<String>();
				for (int i = 0; i < emails.length; i++)
				{
					emailList.add(emails[i]);
				}
				
				data.setTimeWorkedEmails(emailList);
				
				//I think everything above saves all that stuff in the AppData
				//It just doesn't update on the actual page. I had changed this 
				//method to public so I had access to it but that didn't help.
				appDataController.save(data);
			}
			else {
				validForm = false;
			}
		}
		
		
		
		PageBuilder page = new PageBuilder(Page.appinfo, SERVLET_PATH);

		page.setAttribute("appinfo", train.getAppDataController().get());

		page.setPageTitle("Application Info");

		page.passOffToJsp(req, resp);
	}

	private void showAppInfo(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.appinfo, SERVLET_PATH);

		page.setAttribute("appinfo", train.getAppDataController().get());

		page.setPageTitle("Application Info");

		page.passOffToJsp(req, resp);
	}
	
	private void showAttendance(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.attendance, SERVLET_PATH);

		page.setAttribute("absences", train.getAbsencesController().getAll());

		page.setPageTitle("Attendance");

		page.passOffToJsp(req, resp);
	}
	
	private void showUnanchored(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.unanchored, SERVLET_PATH);

		page.setAttribute("absences", train.getAbsencesController().getUnanchored());

		page.setPageTitle("Unanchored");

		page.passOffToJsp(req, resp);
	}

	private void showInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		PageBuilder page = new PageBuilder(Page.info, SERVLET_PATH);

		page.setAttribute("user",
				AuthController.getCurrentUser(req.getSession()));

		page.passOffToJsp(req, resp);
	}
	
	
	private void postUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String netID, strType, firstName, lastName;

		// Grab all the data from the fields
		netID = req.getParameter("NetID");
		strType = req.getParameter("Type");
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");

		User.Type type = User.Type.valueOf(strType);

		UserController uc = DataTrain.getAndStartTrain().getUsersController();

		User localUser = uc.get(netID);

		localUser.setType(type);
		localUser.setFirstName(firstName);
		localUser.setLastName(lastName);

		// TODO May throw validation exceptions
		uc.update(localUser);

		showUsers(req, resp);

	}

	private void showUsers(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.users, SERVLET_PATH);

		page.setAttribute("users", train.getUsersController().getAll());

		page.passOffToJsp(req, resp);

	}

	private void showUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.user, SERVLET_PATH);

		// Grab the second part of the url as the user's netid
		String[] parts = req.getPathInfo().split("/");
		User u = null;

		if (parts.length >= 3) {
			String netid = parts[2];
			u = train.getUsersController().get(netid);
		}

		if (u == null)
			page.setAttribute("error_message", "No such user");

		page.setAttribute("user", u);

		page.setPageTitle("User Info");

		page.setAttribute("sections", User.Section.values());

		page.setAttribute("types", User.Type.values());

		page.passOffToJsp(req, resp);
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PageBuilder page = new PageBuilder(Page.index, SERVLET_PATH);

		page.setPageTitle("Director");

		page.passOffToJsp(req, resp);
	}

}
