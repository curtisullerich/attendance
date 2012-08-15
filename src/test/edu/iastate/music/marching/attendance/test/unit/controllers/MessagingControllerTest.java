package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;

public class MessagingControllerTest extends AbstractTest {

	/**
	 * Creates two independent message threads and adds messages to them
	 * to ensure no side-effects between threads happen
	 */
	@Test
	public void testGetUserFilter() {

		// Arrange
		DataTrain train = getDataTrain();

		// Setup two users
		UserController uc = train.getUsersController();
		User director = Users.createDirector(uc, "director", "I am",
				"The Director");
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		// Act
		MessageThread mts = train.getMessagingController()
				.createMessageThread();
		MessageThread mtd = train.getMessagingController()
				.createMessageThread();

		train.getMessagingController().addMessage(mtd, director, "D");
		train.getMessagingController().addMessage(mts, student, "S");

		// Assert
		List<MessageThread> results_student = train.getMessagingController()
				.get(student);
		
		// Check resulting list for student
		assertEquals(1, results_student.size());

		// Check returned object for student
		MessageThread result_student = results_student.get(0);
		assertNotNull(result_student);
		assertEquals(1, result_student.getParticipants().size());
		assertEquals(student, result_student.getParticipants().iterator()
				.next());
		assertEquals(1, result_student.getMessages().size());
		assertEquals(student, result_student.getMessages().get(0).getAuthor());
		assertEquals("S", result_student.getMessages().get(0).getText());
		assertNotNull(result_student.getMessages().get(0).getTimestamp());
		
		// Director checks
		List<MessageThread> resultsDirector = train.getMessagingController().get(director);
		
		//Checking result list for director
		assertEquals(1, resultsDirector.size());
		
		//Check actual message
		MessageThread resultDirector = resultsDirector.get(0);
		assertNotNull(resultDirector);
		assertEquals(1, resultDirector.getParticipants().size());
		//Now I understand why we had to use the iterator...it's a set not a list :P
		assertEquals(director, resultDirector.getParticipants().iterator().next());
		assertEquals(1, resultDirector.getMessages().size());
		assertEquals(director, resultDirector.getMessages().get(0).getAuthor());
		assertEquals("D", resultDirector.getMessages().get(0).getText());
		assertNotNull(resultDirector.getMessages().get(0).getTimestamp());
	}

	@Test
	public void testCreateMessageThreadWithTwoParticipantsAndFourMessages() {
		// Arrange
		DataTrain train = getDataTrain();

		// Setup two users
		UserController uc = train.getUsersController();
		User director = Users.createSingleTestDirector(uc);
		User student = Users.createSingleTestStudent(uc);

		// Act
		MessageThread mt = train.getMessagingController().createMessageThread();

		train.getMessagingController().addMessage(mt, director, "Begin");
		train.getMessagingController().addMessage(mt, director, "Middle");
		train.getMessagingController().addMessage(mt, student, "Middle");
		train.getMessagingController().addMessage(mt, student, "End");

		// Assert
		MessageThread result = train.getMessagingController().get(mt.getId());

		// Check returned object
		assertNotNull(result);
		assertEquals(2, result.getParticipants().size());
		assertTrue(result.getParticipants().contains(student));
		assertTrue(result.getParticipants().contains(director));
		
		assertEquals(4, result.getMessages().size());
		assertEquals("Begin", result.getMessages().get(3).getText());
		assertEquals("Middle", result.getMessages().get(2).getText());
		assertEquals("Middle", result.getMessages().get(1).getText());
		assertEquals("End", result.getMessages().get(0).getText());
	}

	/**
	 * Rational: jstl in the JSP assumes messages[0] is the latest message in a
	 * thread
	 */
	@Test
	public void testMostRecentMessage() {

		DataTrain train = getDataTrain();

		// Arrange
		UserController uc = train.getUsersController();
		User director = Users.createDirector(uc, "director", "I am",
				"The Director");
		User student = Users.createStudent(uc, "studenttt", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);

		// Act
		MessageThread mt = train.getMessagingController().createMessageThread();

		train.getMessagingController().addMessage(mt, director, "Begin");
		train.getMessagingController().addMessage(mt, director, "Middle");
		train.getMessagingController().addMessage(mt, student, "Middle");
		train.getMessagingController().addMessage(mt, student, "End");

		// Assert
		MessageThread result = train.getMessagingController().get(mt.getId());

		// Check returned object
		assertNotNull(result);
		
		assertEquals("End", result.getMessages().get(0).getText());
	}

}
