package edu.iastate.music.marching.attendance.controllers;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.integration.AbstractTestCase;

public class UserControllerTest extends AbstractTestCase {

	private static final String DOMAIN = "iastate.edu";

	@Test
	public void testCreateSingleDirector() {

		ObjectDatastore datastore = getObjectDataStore();

		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		
		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User("director@" + DOMAIN, "gmail.com");

		uc.createDirector(google_user, 123, "I am", "The Director");

		QueryResultIterator<User> directorq = datastore.find(User.class);

		// Grab a single user
		assertTrue(directorq.hasNext());
		User d = directorq.next();
		assertFalse(directorq.hasNext());

		// Check returned object
		assertNotNull(d);
		assertEquals(User.Type.Director, d.getType());
		assertEquals("director", d.getNetID());
		assertEquals(123, d.getUniversityID());
		assertEquals("I am", d.getFirstName());
		assertEquals("The Director", d.getLastName());
	}

	@Test
	public void testCreateSingleStudent() {

		ObjectDatastore datastore = getObjectDataStore();

		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();

		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User("studenttt@" + DOMAIN, "gmail.com");
		
		uc.createStudent(google_user, 121, "I am", "A Student", 10, "Being Silly",
				User.Section.AltoSax);

		QueryResultIterator<User> studentq = datastore.find(User.class);

		// Grab a single user
		assertTrue(studentq.hasNext());
		User s = studentq.next();
		assertFalse(studentq.hasNext());

		// Check returned object
		assertNotNull(s);
		assertEquals(User.Type.Student, s.getType());
		assertEquals("studenttt", s.getNetID());
		assertEquals(121, s.getUniversityID());
		assertEquals("I am", s.getFirstName());
		assertEquals("A Student", s.getLastName());
		assertEquals(10, s.getYear());
		assertEquals("Being Silly", s.getMajor());
		assertEquals(User.Section.AltoSax, s.getSection());
	}
}
