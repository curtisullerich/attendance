package edu.iastate.music.marching.attendance.test.prototype;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTestCase;

public class TestMostRecentMessage extends AbstractTestCase {
	
	private static final String DOMAIN = "iastate.edu";

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
		result = getDataTrain().getMessagingController().get(mt.getId());

		// Check returned object
		assertNotNull(result);
		assertEquals(2, result.getParticipants().size());
		assertEquals(4, result.getMessages().size());
		// TODO more checks
		assertEquals("End", result.getMessages().get(0).getText());
	}

}
