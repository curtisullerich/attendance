package edu.iastate.music.marching.attendance.test.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTestCase;

public class MessagingControllerTest extends AbstractTestCase {

	private static final String DOMAIN = "iastate.edu";

	@Test
	public void testGetUserFilter() {

		DataTrain train = getDataTrain();

		// Setup two users
		UserController uc = train.getUsersController();
		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				"director@" + DOMAIN, "gmail.com");
		User director = uc.createDirector(google_user, 123, "I am",
				"The Director");

		com.google.appengine.api.users.User google_user2 = new com.google.appengine.api.users.User(
				"studenttt@" + DOMAIN, "gmail.com");
		User student = uc.createStudent(google_user2, 121, "I am", "A Student",
				10, "Being Silly", User.Section.AltoSax);

		MessageThread mts = train.getMessagingController()
				.createMessageThread();
		MessageThread mtd = train.getMessagingController()
				.createMessageThread();

		train.getMessagingController().addMessage(mtd, director, "D");
		train.getMessagingController().addMessage(mts, student, "S");

		List<MessageThread> results_student = getDataTrain().getMessagingController().get(
				student);
		// Check resulting list
		assertEquals(1, results_student.size());
		
		// Check returned object
		MessageThread result_student = results_student.get(0);
		assertNotNull(result_student);
		assertEquals(1, result_student.getParticipants().size());
		assertEquals(student.getGoogleUser(), result_student.getParticipants().iterator().next().getGoogleUser());
		assertEquals(1, result_student.getMessages().size());
		assertEquals(student.getGoogleUser(), result_student.getMessages().get(0).getAuthor().getGoogleUser());
		assertEquals("S", result_student.getMessages().get(0).getText());
		assertNotNull(result_student.getMessages().get(0).getTimestamp());
		// TODO more checks

		MessageThread resultd = getDataTrain().getMessagingController().get(
				mtd.getId());
		// Check returned object
		assertNotNull(resultd);
		assertEquals(1, resultd.getParticipants().size());
		assertEquals(1, resultd.getMessages().size());
		// TODO more checks
	}

	@Test
	public void testCreateMessageThreadWithSingleMessage() {

		DataTrain train = getDataTrain();

		// Setup two users
		UserController uc = train.getUsersController();
		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				"director@" + DOMAIN, "gmail.com");
		User director = uc.createDirector(google_user, 123, "I am",
				"The Director");

		com.google.appengine.api.users.User google_user2 = new com.google.appengine.api.users.User(
				"studenttt@" + DOMAIN, "gmail.com");
		User student = uc.createStudent(google_user2, 121, "I am", "A Student",
				10, "Being Silly", User.Section.AltoSax);

		MessageThread mts = train.getMessagingController()
				.createMessageThread();
		MessageThread mtd = train.getMessagingController()
				.createMessageThread();

		train.getMessagingController().addMessage(mtd, director, "D");
		train.getMessagingController().addMessage(mts, student, "S");

		MessageThread results = getDataTrain().getMessagingController().get(
				mts.getId());
		// Check returned object
		assertNotNull(results);
		assertEquals(1, results.getParticipants().size());
		assertEquals(student, results.getParticipants().iterator().next());
		assertEquals(1, results.getMessages().size());
		assertEquals(student, results.getMessages().get(0).getAuthor());
		assertEquals("S", results.getMessages().get(0).getText());
		assertNotNull(results.getMessages().get(0).getTimestamp());
		// TODO more checks

		MessageThread resultd = getDataTrain().getMessagingController().get(
				mtd.getId());
		// Check returned object
		assertNotNull(resultd);
		assertEquals(1, resultd.getParticipants().size());
		assertEquals(1, resultd.getMessages().size());
		// TODO more checks
	}

	@Test
	public void testCreateMessageThreadWithTwoParticipantsAndFourMessages() {

		DataTrain train = getDataTrain();

		// Setup two users
		UserController uc = train.getUsersController();
		com.google.appengine.api.users.User google_user = new com.google.appengine.api.users.User(
				"director@" + DOMAIN, "gmail.com");
		User director = uc.createDirector(google_user, 123, "I am",
				"The Director");

		com.google.appengine.api.users.User google_user2 = new com.google.appengine.api.users.User(
				"studenttt@" + DOMAIN, "gmail.com");
		User student = uc.createStudent(google_user2, 121, "I am", "A Student",
				10, "Being Silly", User.Section.AltoSax);

		MessageThread mt = train.getMessagingController().createMessageThread();

		train.getMessagingController().addMessage(mt, director, "Begin");
		train.getMessagingController().addMessage(mt, director, "Middle");
		train.getMessagingController().addMessage(mt, student, "Middle");
		train.getMessagingController().addMessage(mt, student, "End");

		// Load from datastore and compare
		MessageThread result = getObjectDataStore().load(MessageThread.class,
				mt.getId());

		// Check returned object
		assertNotNull(result);
		assertEquals(2, result.getParticipants().size());
		assertEquals(4, result.getMessages().size());
		// TODO more checks
	}

}
