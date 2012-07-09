package edu.iastate.music.marching.attendance.test;

import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.User;

public class AbstractTest {
	;

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig(),
			new LocalUserServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	protected final DataTrain getDataTrain() {
		return DataTrain.getAndStartTrain();
	}

	protected final ObjectDatastore getObjectDataStore() {

		return new AnnotationObjectDatastore();
	}

	protected static final User createDirector(UserController uc,
			String email_firstpart, int univID, String firstName,
			String lastName) {
		return uc.createDirector(
				email_firstpart + "@" + TestConfig.getEmailDomain(),
				email_firstpart + ".secondemail" + "@"
						+ TestConfig.getEmailDomain(), univID, firstName,
				lastName);
	}

	protected static final User createStudent(UserController uc,
			String email_firstpart, int univID, String firstName,
			String lastName, int year, String major, User.Section section) {

		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + TestConfig.getEmailDomain(),
				"gmail.com");

		return uc.createStudent(google_user, univID, firstName, lastName, year,
				major, section, new Email(""));
	}

	protected static final User createTA(UserController uc,
			String email_firstpart, int univID, String firstName,
			String lastName, int year, String major, User.Section section) {

		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + TestConfig.getEmailDomain(),
				"gmail.com");

		User u = uc.createStudent(google_user, univID, firstName, lastName,
				year, major, section, new Email(""));
		u.setType(User.Type.TA);
		uc.update(u);
		return u;
	}

	protected <T extends HttpServlet> void doGet(Class<T> clazz,
			HttpServletRequest req, HttpServletResponse resp)
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		commonDoGetPostSetup(req, resp);

		when(req.getMethod()).thenReturn("GET");

		HttpServlet s = clazz.newInstance();
		s.service(req, resp);
	}

	protected <T extends HttpServlet> void doPost(Class<T> clazz,
			HttpServletRequest req, HttpServletResponse resp)
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		commonDoGetPostSetup(req, resp);

		when(req.getMethod()).thenReturn("POST");

		HttpServlet s = clazz.newInstance();
		s.service(req, resp);
	}

	private void commonDoGetPostSetup(HttpServletRequest req,
			HttpServletResponse resp) {
		when(req.getHeader("User-Agent"))
				.thenReturn(
						"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5");
	}
}
