package edu.iastate.music.marching.attendance.controllers;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.integration.AbstractTestCase;

public class UserControllerTest extends AbstractTestCase {

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void testCreateSingleOfAllTypes() {
		
		ObjectDatastore datastore = getObjectDataStore();
		
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		
		uc.create(User.Type.Director, "director", 0, "I am", "The Director");
		
		QueryResultIterator<User> director = datastore.find(User.class, User.FIELD_NETID, "director");
		
		fail("Not yet implemented");
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet() {
		fail("Not yet implemented");
	}

}
