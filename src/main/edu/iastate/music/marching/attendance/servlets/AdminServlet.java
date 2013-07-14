package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.model.interact.AbsenceManager;
import edu.iastate.music.marching.attendance.model.interact.AuthManager;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.FormManager;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.AttendanceDatastore;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.tasks.Tasks;
import edu.iastate.music.marching.attendance.util.PageBuilder;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class AdminServlet extends AbstractBaseServlet {

	private static final long serialVersionUID = 6636386568039228284L;

	private static final String SERVLET_PATH = "admin";

	public static final String INDEX_URL = pageToUrl(Page.index, SERVLET_PATH);

	private static final Logger log = Logger.getLogger(AdminServlet.class
			.getName());

	private enum Page {
		index, users, user, data, restore, backup, data_migrate, data_delete, register, bulkmake, student_register
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!AuthManager.isAdminLoggedIn()) {
			if (!isLoggedIn(req, resp)) {
				resp.sendRedirect(AuthServlet.getLoginUrl(req));
				return;
			} else if (!(isLoggedIn(req, resp, User.Type.Director))) {
				resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
				return;
			}
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case index:
				showIndex(req, resp);
				break;
			case users:
				showUsers(req, resp, null, null);
				break;
			case user:
				showUserInfo(req, resp);
				break;
			case data:
				showDataPage(req, resp);
				break;
			case register:
				showDirectorRegistrationPage(req, resp);
				break;
			case bulkmake:
				showBulkmake(req, resp);
				break;
			case student_register:
				showStudentRegistrationPage(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!AuthManager.isAdminLoggedIn()) {
			if (!isLoggedIn(req, resp)) {
				resp.sendRedirect(AuthServlet.getLoginUrl(req));
				return;
			} else if (!(isLoggedIn(req, resp, User.Type.Director))) {
				resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
				return;
			}
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case backup:
				queueDataExport(req, resp);
				break;
			case restore:
				doDataImport(req, resp);
				break;
			case users:
				postUserInfo(req, resp);
				break;
			case register:
				doDirectorRegistration(req, resp);
				break;
			case bulkmake:
				bulkmake(req, resp);
				break;
			case student_register:
				doStudentRegistration(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}
	}

	private void postUserInfo(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		String netID, strType, firstName, lastName;

		DataTrain train = DataTrain.getAndStartTrain();

		List<String> errors = new ArrayList<String>();
		String success = "";

		// Grab all the data from the fields
		netID = req.getParameter("NetID");
		strType = req.getParameter("Type");
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");

		User.Type type = User.Type.valueOf(strType);

		UserManager uc = train.getUsersManager();

		User localUser = uc.get(netID);

		localUser.setType(type);
		localUser.setFirstName(firstName);
		localUser.setLastName(lastName);

		// May throw validation exceptions
		try {
			uc.update(localUser);

			// Update user in session if we just changed the currently logged in
			// user
			if (localUser.equals(train.getAuthManager().getCurrentUser(
					req.getSession())))
				AuthManager.updateCurrentUser(localUser, req.getSession());
			success = "User information saved";
		} catch (IllegalArgumentException e) {
			// Invalid information
			errors.add("Unable to save user information: " + e.getMessage());
		}

		showUsers(req, resp, errors, success);

	}

	private void showUsers(HttpServletRequest req, HttpServletResponse resp,
			List<String> errors, String success) throws ServletException,
			IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.users, SERVLET_PATH);

		page.setAttribute("users", train.getUsersManager().getAll());

		page.setAttribute("error_messages", errors);

		page.setAttribute("success_message", success);

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
			u = train.getUsersManager().get(netid);
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

		page.setPageTitle("Users");

		page.passOffToJsp(req, resp);
	}

	private void showDataPage(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		PageBuilder page = new PageBuilder(Page.data, SERVLET_PATH);

		page.setPageTitle("Data Export/Import");

		page.setAttribute("ObjectDatastoreVersion", AttendanceDatastore.VERSION);

		page.passOffToJsp(req, resp);
	}

	private void doDataImport(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		try {
			ServletFileUpload upload = new ServletFileUpload();
			resp.setContentType("text/plain");

			FileItemIterator iterator = upload.getItemIterator(req);
			while (iterator.hasNext()) {
				FileItemStream item = iterator.next();
				InputStream stream = item.openStream();

				if (item.isFormField()) {
					log.info("Got a form field: " + item.getFieldName());
				} else {
					log.info("Got an uploaded file: " + item.getFieldName()
							+ ", name = " + item.getName());

					// Now we have the file name and contents, go ahead and
					// import
					train.getDataManager()
							.deleteEverthingInTheEntireDatabaseEvenThoughYouCannotUndoThis();
					train.getDataManager().importJSONDatabaseDump(
							new InputStreamReader(stream));
					// int len;
					// byte[] buffer = new byte[8192];
					// while ((len = stream.read(buffer, 0, buffer.length)) !=
					// -1) {
					// resp.getOutputStream().write(buffer, 0, len);
					// }
				}
			}
		} catch (Exception ex) {
			throw new ServletException(ex);
		}

		new PageBuilder(Page.restore, SERVLET_PATH).setPageTitle(
				"Data Restore Complete").passOffToJsp(req, resp);
	}

	private void showDirectorRegistrationPage(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		PageBuilder page = new PageBuilder(Page.register, SERVLET_PATH);

		page.setPageTitle("Director Registration");

		page.passOffToJsp(req, resp);
	}

	private void showStudentRegistrationPage(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		PageBuilder page = new PageBuilder(Page.student_register, SERVLET_PATH);

		page.setPageTitle("Manual Student Registration");

		page.setAttribute("sections", User.Section.values());

		page.passOffToJsp(req, resp);
	}

	private void showBulkmake(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PageBuilder page = new PageBuilder(Page.bulkmake, SERVLET_PATH);
		page.setPageTitle("BulkMake");

		page.passOffToJsp(req, resp);
	}

	private void bulkmake(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		List<String> errors = new LinkedList<String>();
		if (req.getParameter("Go") != null) {

			String data = req.getParameter("Data").replaceAll("\\s+", "");

			String[] lines = data.split(";");
			UserManager uc = train.getUsersManager();

			int i = 0;

			for (int x = 0; x < lines.length; x++) {
				try {
					uc.createFakeStudent(lines[x].trim().split(","));
					i++;
				} catch (Exception e) {
					errors.add(e.getMessage());
					log.severe(e.getMessage());
					log.severe(e.getStackTrace().toString());
				}
			}

			if (errors.size() == 0) {
				try {

				} catch (IllegalArgumentException e) {
					// Save validation errors
					errors.add(e.getMessage());
				}
			}

			PageBuilder page = new PageBuilder(Page.bulkmake, SERVLET_PATH);

			page.setPageTitle("makebulk");

			page.setAttribute("success_message", "registered " + i
					+ " students");

			page.setAttribute("error_messages", errors);

			page.passOffToJsp(req, resp);
		} else if (req.getParameter("DeleteAll") != null) {
			String success = null;
			try {
				train.getDataManager()
						.deleteEverthingInTheEntireDatabaseEvenThoughYouCannotUndoThis();
				success = "Successfully, irrevocably, and unequivically removed everything.";
			} catch (Throwable aDuh) {
				log.severe(aDuh.getMessage());
				log.severe(aDuh.getStackTrace().toString());
				errors.add(aDuh.toString());
			}
			PageBuilder page = new PageBuilder(Page.bulkmake, SERVLET_PATH);

			page.setPageTitle("makebulk");

			page.setAttribute("success_message", success);

			page.setAttribute("error_messages", errors);

			page.passOffToJsp(req, resp);
		} else if (req.getParameter("RefreshAbsences") != null) {
			String succex = null;
			AbsenceManager ac = train.getAbsenceManager();
			try {
				for (Absence a : ac.getAll()) {
					ac.updateAbsence(a);
				}
				succex = "Absences refreshed.";
			} catch (Throwable tehThrowable) {
				errors.add(tehThrowable.getMessage());
				log.severe(tehThrowable.getMessage());
				log.severe(tehThrowable.getStackTrace().toString());
			}
			PageBuilder page = new PageBuilder(Page.bulkmake, SERVLET_PATH);

			page.setPageTitle("bulkmake");

			page.setAttribute("success_message", succex);

			page.setAttribute("error_messages", errors);

			page.passOffToJsp(req, resp);
		} else if (req.getParameter("RefreshForms") != null) {
			String succex = null;
			FormManager fc = train.getFormsManager();
			try {
				for (Form f : fc.getAll()) {
					fc.update(f);
				}
				succex = "Forms refreshed.";
			} catch (Throwable tehThrowable) {
				errors.add(tehThrowable.getMessage());
				log.severe(tehThrowable.getMessage());
				log.severe(tehThrowable.getStackTrace().toString());
			}
			PageBuilder page = new PageBuilder(Page.bulkmake, SERVLET_PATH);

			page.setPageTitle("bulkmake");

			page.setAttribute("success_message", succex);

			page.setAttribute("error_messages", errors);

			page.passOffToJsp(req, resp);
		} else if (req.getParameter("RefreshUsers") != null) {
			String succex = null;
			UserManager uc = train.getUsersManager();
			try {
				for (User u : uc.getAll()) {
					uc.update(u);
				}
				succex = "Users refreshed. <(' '<) <(' ')> (>' ')>";
			} catch (Throwable tehThrowable) {
				errors.add(tehThrowable.getMessage());
				log.severe(tehThrowable.getMessage());
				log.severe(tehThrowable.getStackTrace().toString());
			}
			PageBuilder page = new PageBuilder(Page.bulkmake, SERVLET_PATH);

			page.setPageTitle("bulkmake");

			page.setAttribute("success_message", succex);

			page.setAttribute("error_messages", errors);

			page.passOffToJsp(req, resp);
		}
	}

	private void doDirectorRegistration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		String firstName;
		String lastName;
		Email primaryEmail;
		Email secondEmail;
		User new_user = null;
		List<String> errors = new LinkedList<String>();

		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");
		primaryEmail = new Email(req.getParameter("NetID"));
		secondEmail = new Email(req.getParameter("SecondEmail"));

		if (!ValidationUtil.validPrimaryEmail(primaryEmail, train)) {
			errors.add("Invalid primary (school) email");
		}

		if (!ValidationUtil.validSecondaryEmail(secondEmail, train)) {
			errors.add("Invalid secondary (google) email");
		}
		if (!ValidationUtil.isUniqueSecondaryEmail(secondEmail, primaryEmail,
				train)) {
			errors.add("Non-unique secondary (google) email");
		}

		if (errors.size() == 0) {
			try {
				new_user = train.getUsersManager().createDirector(
						primaryEmail.getEmail(), secondEmail.getEmail(),
						firstName, lastName);

			} catch (IllegalArgumentException e) {
				// Save validation errors
				errors.add(e.getMessage());
				System.out.println(e.getMessage());
				System.out.println(e.getStackTrace());
			}
		}

		if (new_user == null) {
			PageBuilder page = new PageBuilder(Page.register, SERVLET_PATH);

			page.setPageTitle("Director Registration");

			page.setAttribute("FirstName", req.getParameter("FirstName"));
			page.setAttribute("LastName", req.getParameter("LastName"));
			page.setAttribute("NetID", req.getParameter("NetID"));
			page.setAttribute("SecondEmail", req.getParameter("SecondEmail"));

			page.setAttribute("error_messages", errors);

			page.passOffToJsp(req, resp);
		} else {
			PageBuilder page = new PageBuilder(Page.register, SERVLET_PATH);

			page.setPageTitle("Director Registration");

			page.setAttribute("success_message",
					"Successfully registered Director " + new_user.getName());

			page.passOffToJsp(req, resp);
		}
	}

	private void doStudentRegistration(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		String firstName;
		String lastName;
		String major;
		Email primaryEmail;
		Email secondEmail;
		String univID;
		int year = -1;
		User.Section section = null;
		User new_user = null;
		List<String> errors = new LinkedList<String>();

		// Grab all the data from the form fields
		firstName = req.getParameter("FirstName");
		lastName = req.getParameter("LastName");
		major = req.getParameter("Major");
		univID = req.getParameter("UniversityID");
		primaryEmail = new Email(req.getParameter("PrimaryEmail"));
		secondEmail = new Email(req.getParameter("SecondEmail"));

		if (!ValidationUtil.isValidUniversityID(univID)) {
			errors.add("University ID was not valid");
		}

		if (!ValidationUtil.isUniqueId(univID, primaryEmail)) {
			errors.add("University ID was not unique");
		}

		try {
			year = Integer.parseInt(req.getParameter("Year"));
		} catch (NumberFormatException e) {
			errors.add("Invalid number of years in band entered, was not a number");
		}

		try {
			section = User.Section.valueOf(req.getParameter("Section"));
		} catch (IllegalArgumentException e) {
			errors.add("Invalid section");
		} catch (NullPointerException e) {
			errors.add("Invalid section");
		}

		if (!ValidationUtil.validPrimaryEmail(primaryEmail, train)) {
			errors.add("Invalid primary email, try logging out and then registering under your school email account");
		}

		if (!ValidationUtil.validSecondaryEmail(secondEmail, train)) {
			errors.add("Invalid secondary email");
		}
		if (!ValidationUtil.isUniqueSecondaryEmail(secondEmail, primaryEmail,
				train)) {
			errors.add("Non-unique secondary email");
		}

		if (errors.size() == 0) {
			try {
				new_user = train.getUsersManager().createStudent(
						primaryEmail, univID, firstName, lastName, year, major,
						section, secondEmail);

			} catch (IllegalArgumentException e) {
				// Save validation errors
				errors.add(e.getMessage());

			}
		}

		PageBuilder page = new PageBuilder(Page.student_register, SERVLET_PATH);

		if (new_user == null) {
			// Render registration page again

			page.setAttribute("FirstName", firstName);
			page.setAttribute("LastName", lastName);
			page.setAttribute("PrimaryEmail", primaryEmail.getEmail());
			page.setAttribute("Major", major);
			page.setAttribute("UniversityID", univID);
			page.setAttribute("Year", year);
			page.setAttribute("SecondEmail", secondEmail.getEmail());
			page.setAttribute("Section",
					(section == null) ? null : section.getValue());

			page.setAttribute("sections", User.Section.values());

			page.setAttribute("error_messages", errors);
			page.setPageTitle("Failed Registration");
		} else {
			// Did create a new user!
			page.setPageTitle("Student Registration");

			page.setAttribute("success_message",
					"Successfully registered Student " + new_user.getName());
		}

		page.passOffToJsp(req, resp);
	}

	private void queueDataExport(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {

		Tasks.exportData();

		log.info("Queueing data export");

		new PageBuilder(Page.backup, SERVLET_PATH).setPageTitle(
				"Data Backup Started").passOffToJsp(req, resp);
	}
}
