package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;

public class MessagingControllerTest extends AbstractTest {

	@Test
	public void testGetUserFilter() {

		DataTrain train = getDataTrain();

		// Setup two users
		UserController uc = train.getUsersController();
		User director = Users.createDirector(uc, "director", 123, "I am", "The Director");
		User student = Users.createStudent(uc, "studenttt", 121, "First", "last", 2, "major", User.Section.AltoSax);

		MessageThread mts = train.getMessagingController()
				.createMessageThread();
		MessageThread mtd = train.getMessagingController()
				.createMessageThread();

		train.getMessagingController().addMessage(mtd, director, "D");
		train.getMessagingController().addMessage(mts, student, "S");

		List<MessageThread> results_student = train.getMessagingController().get(
				student);
		// Check resulting list
		assertEquals(1, results_student.size());
		
		// Check returned object
		MessageThread result_student = results_student.get(0);
		assertNotNull(result_student);
		assertEquals(1, result_student.getParticipants().size());
		assertEquals(student, result_student.getParticipants().iterator().next());
		assertEquals(1, result_student.getMessages().size());
		assertEquals(student, result_student.getMessages().get(0).getAuthor());
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
		User director = Users.createDirector(uc, "director", 123, "I am", "The Director");
		User student = Users.createStudent(uc, "studenttt", 121, "First", "last", 2, "major", User.Section.AltoSax);

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
		User director = Users.createDirector(uc, "director", 123, "I am", "The Director");
		User student = Users.createStudent(uc, "studenttt", 121, "First", "last", 2, "major", User.Section.AltoSax);

		MessageThread mt = train.getMessagingController().createMessageThread();

		train.getMessagingController().addMessage(mt, director, "Begin");
		train.getMessagingController().addMessage(mt, director, "Middle");
		train.getMessagingController().addMessage(mt, student, "Middle");
		train.getMessagingController().addMessage(mt, student, "End");

		// Load from datastore and compare
		MessageThread result = train.getMessagingController().get(mt.getId());

		// Check returned object
		assertNotNull(result);
		assertEquals(2, result.getParticipants().size());
		assertEquals(4, result.getMessages().size());
		// TODO more checks
	}
	
	/**
	 * Rational: jstl in the JSP assumes messages[0] is the latest message in a thread
	 */
	@Test
	public void testMostRecentMessage() {

		DataTrain train = getDataTrain();

		// Setup two users
		UserController uc = train.getUsersController();
		User director = Users.createDirector(uc, "director", 123, "I am", "The Director");
		User student = Users.createStudent(uc, "studenttt", 121, "First", "last", 2, "major", User.Section.AltoSax);

		MessageThread mt = train.getMessagingController().createMessageThread();

		train.getMessagingController().addMessage(mt, director, "Begin");
		train.getMessagingController().addMessage(mt, director, "Middle");
		train.getMessagingController().addMessage(mt, student, "Middle");
		train.getMessagingController().addMessage(mt, student, "End");

		// Load again and compare
		MessageThread result = train.getMessagingController().get(mt.getId());

		// Check returned object
		assertNotNull(result);
		assertEquals(2, result.getParticipants().size());
		assertEquals(4, result.getMessages().size());
		// TODO more checks
		assertEquals("End", result.getMessages().get(0).getText());
	}

}
