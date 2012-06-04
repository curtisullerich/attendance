package edu.iastate.music.marching.attendance.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;

import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.servlets.MobileAppDataServlet;
import edu.iastate.music.marching.attendance.test.AbstractTest;

public class MobileDataUploadTest extends AbstractTest {

	private static String SIMPLE_ABSENCE_TESTDATA = "absentStudentRehearsal&split&el&split&Starster&split&s&split&2012-05-03&split&1630&split&1750&split&null&newline&"
			+ "storedRehearsal&split&rehearsal&split&|&split&|&split&2012-05-03&split&1630&split&1750&split&|&newline&"
			+ "absentStudentRehearsal&split&P1&split&Z&split&zf&split&2012-05-03&split&1630&split&1750&split&4&newline&";

	private static String SIMPLE_TARDY_TESTDATA = "tardyStudent&split&el&split&Starster&split&s&split&2012-05-03&split&0109&split&|&split&null&newline&"
			+ "tardyStudent&split&el&split&Starster&split&s&split&2012-05-03&split&0116&split&|&split&null&newline&"
			+ "tardyStudent&split&Bdog&split&Zemax&split&b&split&2012-05-03&split&0108&split&|&split&|&newline&"
			+ "tardyStudent&split&el&split&Starster&split&s&split&2012-05-03&split&0107&split&|&split&null&newline&"
			+ "tardyStudent&split&el&split&Starster&split&s&split&2012-05-03&split&0108&split&|&split&null&newline&"
			+ "tardyStudent&split&P1&split&Z&split&zf&split&2012-05-03&split&0108&split&|&split&4&newline&";

	@Test
	public void simpleAbsenceInsertionThroughController() {

		DataTrain train = DataTrain.getAndStartTrain();

		User ta = createTA(train.getUsersController(), "ta", 3, "first",
				"last", 2, "major", null);
		createStudent(train.getUsersController(), "s", 2, "first", "last", 1,
				"major", null);
		createStudent(train.getUsersController(), "zf", 1, "first", "last", 1,
				"major", null);

		train.getMobileDataController().pushMobileData(SIMPLE_ABSENCE_TESTDATA,
				ta);

		simpleAbsenceInsertionVerification();
	}

	@Test
	public void simpleAbsenceInsertionThroughServlet()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		DataTrain train = getDataTrain();

		createStudent(train.getUsersController(), "s", 2, "first", "last", 1,
				"major", null);
		createStudent(train.getUsersController(), "zf", 1, "first", "last", 1,
				"major", null);

		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setTASession(req);

		setPostedContent(req, SIMPLE_ABSENCE_TESTDATA);

		ServletOutputStream os = mock(ServletOutputStream.class);
		when(resp.getOutputStream()).thenReturn(os);

		doPost(MobileAppDataServlet.class, req, resp);

		verify(os)
				.print("{\"error\":\"success\",\"message\":\"TODO: return string about what was uploaded here\"}");

		simpleAbsenceInsertionVerification();
	}

	@Test
	public void simpleAbsenceInsertionThroughServlet_NullStudent()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setTASession(req);

		setPostedContent(req, SIMPLE_ABSENCE_TESTDATA);

		ServletOutputStream os = mock(ServletOutputStream.class);
		when(resp.getOutputStream()).thenReturn(os);

		doPost(MobileAppDataServlet.class, req, resp);

		verify(os)
				.print("{\"error\":\"exception\",\"message\":\"Tried to create absence for null user\"}");

		//ObjectDatastore datastore = getObjectDataStore();

		// Verify insertion lengths
		// When transactional support is re-added, uncomment this
//		assertEquals(0, datastore.find().type(Event.class).returnCount().now()
//				.intValue());
//		assertEquals(0, datastore.find().type(Absence.class).returnCount()
//				.now().intValue());
	}

	private void simpleAbsenceInsertionVerification() {
		Event event;

		ObjectDatastore datastore = getObjectDataStore();

		// Verify insertion lengths
		assertEquals(1, datastore.find().type(Event.class).returnCount().now()
				.intValue());
		assertEquals(2, datastore.find().type(Absence.class).returnCount()
				.now().intValue());

		// Verify event
		List<Event> events = datastore.find().type(Event.class).returnAll()
				.now();
		assertEquals(1, events.size());

		event = events.get(0);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(0);

		cal.set(2012, 04, 03, 16, 30, 0);
		assertEquals(cal.getTime(), event.getStart());

		cal.set(2012, 04, 03, 17, 50, 0);
		assertEquals(0, cal.getTime().compareTo(event.getEnd()));

		assertEquals(Event.Type.Rehearsal, event.getType());

		// Verify absences
		List<Absence> absences = datastore.find().type(Absence.class)
				.returnAll().now();
		assertEquals(2, absences.size());

		boolean foundS = false;
		for (Absence a : absences) {

			// Check parent relation
			assertEquals(a.getEvent(), event);

			if (a.getStudent().getNetID().equals("s")) {
				assertFalse("There should only be one abscence for user s",
						foundS);
				foundS = true;
				// TODO assert information about inserted absence
			} else if (a.getStudent().getNetID().equals("zf")) {
				// TODO assert information about inserted absence
			} else
				fail("Found an absence we didn't insert");
		}
	}

	@Test
	public void simpleTardyInsertionThroughController() {

		ObjectDatastore datastore = getObjectDataStore();

		DataTrain train = DataTrain.getAndStartTrain();

		User s = ModelFactory.newUser(User.Type.Student, null, "s", 1);
		datastore.store(s);

		User z = ModelFactory.newUser(User.Type.Student, null, "zf", 2);
		datastore.store(z);

		User b = ModelFactory.newUser(User.Type.Student, null, "b", 3);
		datastore.store(b);

		User ta = ModelFactory.newUser(User.Type.TA, null, "ta", 4);
		datastore.store(ta);

		train.getMobileDataController().pushMobileData(SIMPLE_TARDY_TESTDATA,
				ta);

		// Verify insertion lengths
		assertEquals(0, datastore.find().type(Event.class).returnCount().now()
				.intValue());

		assertEquals(6, datastore.find().type(Absence.class).returnCount()
				.now().intValue());

		// TODO: Check actual data returned
	}

	private HttpServletRequest setTASession(HttpServletRequest req) {
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute("authenticated_user"))
				.thenReturn(
						createTA(getDataTrain().getUsersController(), "ta",
								121, "I am", "A TA", 10, "Being Silly",
								User.Section.AltoSax));
		when(req.getSession()).thenReturn(session);
		return req;
	}

	private void setPostedContent(HttpServletRequest req, String data)
			throws IOException {
		ByteArrayInputStream realStream = new ByteArrayInputStream(
				data.getBytes("UTF-8"));
		ServletInputStream mockStream = new MockServletInputStream(realStream);
		when(req.getInputStream()).thenReturn(mockStream);
	}

	private class MockServletInputStream extends ServletInputStream {

		private InputStream mocked;

		public MockServletInputStream(InputStream from) {
			mocked = from;
		}

		@Override
		public int read() throws IOException {
			return mocked.read();
		}

	}

}
