package edu.iastate.music.marching.attendance.test;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.User;

public class AbstractDataStoreTest {

	private static final String EMAIL_DOMAIN = "iastate.edu";

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

		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + EMAIL_DOMAIN, "gmail.com");

		return uc.createDirector(google_user, univID, firstName, lastName);
	}

	protected static final User createStudent(UserController uc,
			String email_firstpart, int univID, String firstName,
			String lastName, int year, String major, User.Section section) {

		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + EMAIL_DOMAIN, "gmail.com");

		return uc.createStudent(google_user, univID, firstName, lastName, year,
				major, section);
	}
	
	protected static final User createTA(UserController uc,
			String email_firstpart, int univID, String firstName,
			String lastName, int year, String major, User.Section section) {

		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				email_firstpart + "@" + EMAIL_DOMAIN, "gmail.com");

		User u = uc.createStudent(google_user, univID, firstName, lastName, year,
				major, section);
		u.setType(User.Type.TA);
		uc.update(u);
		return u;
	}
}
