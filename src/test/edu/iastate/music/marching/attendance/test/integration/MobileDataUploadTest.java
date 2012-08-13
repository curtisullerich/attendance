package edu.iastate.music.marching.attendance.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.servlets.MobileAppDataServlet;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.TestConfig;
import edu.iastate.music.marching.attendance.test.mock.MockServletInputStream;
import edu.iastate.music.marching.attendance.test.mock.ServletMock;
import edu.iastate.music.marching.attendance.test.util.Users;

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

		User ta = Users.createTA(train.getUsersController(), "ta", "123456780",
				"first", "last", 2, "major", User.Section.Staff);
		Users.createStudent(train.getUsersController(), "s", "123456719",
				"first", "last", 1, "major", User.Section.Baritone);
		Users.createStudent(train.getUsersController(), "zf", "123456782",
				"first", "last", 1, "major", User.Section.Drumline_Bass);

		train.getMobileDataController().pushMobileData(SIMPLE_ABSENCE_TESTDATA,
				ta);

		simpleAbsenceInsertionVerification();
	}

	@Test
	public void simpleAbsenceInsertionThroughServlet()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		DataTrain train = getDataTrain();

		Users.createStudent(train.getUsersController(), "s", "123456788",
				"first", "last", 1, "major", User.Section.Clarinet);
		Users.createStudent(train.getUsersController(), "zf", "123456782",
				"first", "last", 1, "major", User.Section.TenorSax);

		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setTASession(req);

		setPostedContent(req, SIMPLE_ABSENCE_TESTDATA);

		ServletOutputStream os = mock(ServletOutputStream.class);
		when(resp.getOutputStream()).thenReturn(os);

		ServletMock.doPost(MobileAppDataServlet.class, req, resp);

		verify(os)
				.print("{\"error\":\"success\",\"message\":\"Inserted 1/1 events.\\nInserted 2/2 absences/tardies/early checkouts.\\n\"}");

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

		ServletMock.doPost(MobileAppDataServlet.class, req, resp);

		verify(os)
				.print("{\"error\":\"exception\",\"message\":\"Tried to create absence for null user\"}");

		// ObjectDatastore datastore = getObjectDataStore();

		// Verify insertion lengths
		// When transactional support is re-added, uncomment this
		// assertEquals(0,
		// datastore.find().type(Event.class).returnCount().now()
		// .intValue());
		// assertEquals(0, datastore.find().type(Absence.class).returnCount()
		// .now().intValue());
	}

	private void simpleAbsenceInsertionVerification() {
		Event event;
		DataTrain train = getDataTrain();

		// Verify insertion lengths
		assertEquals(1, train.getEventController().getCount().intValue());
		assertEquals(2, train.getAbsenceController().getCount().intValue());

		// Verify event
		List<Event> events = train.getEventController().getAll();
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
		List<Absence> absences = train.getAbsenceController().getAll();
		assertEquals(2, absences.size());

		boolean foundS = false;
		for (Absence a : absences) {

			// Check parent relation
			assertEquals(a.getEvent(), event);

			if (a.getStudent().getPrimaryEmail().getEmail()
					.equals("s@" + TestConfig.getEmailDomain())) {
				assertFalse("There should only be one abscence for user s",
						foundS);
				foundS = true;
				// TODO https://github.com/curtisullerich/attendance/issues/123
				// assert information about inserted absence
			} else if (a.getStudent().getPrimaryEmail().getEmail()
					.equals("zf@" + TestConfig.getEmailDomain())) {
				// TODO https://github.com/curtisullerich/attendance/issues/123
				// assert information about inserted absence
			} else
				fail("Found an absence we didn't insert");
		}
	}

	@Test
	public void simpleTardyInsertionThroughController() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();

		Users.createStudent(uc, "s", "123456780", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		Users.createStudent(uc, "zf", "123456781", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		Users.createStudent(uc, "b", "123456782", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		User ta = Users.createTA(uc, "ta", "123456783", "test1", "tester", 1,
				"major", User.Section.AltoSax);

		train.getMobileDataController().pushMobileData(SIMPLE_TARDY_TESTDATA,
				ta);

		// Verify insertion lengths
		assertEquals(0, train.getEventController().getCount().intValue());

		assertEquals(6, train.getAbsenceController().getCount().intValue());

		// TODO: https://github.com/curtisullerich/attendance/issues/123
		// Check actual data returned
	}

	private HttpServletRequest setTASession(HttpServletRequest req) {
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute("authenticated_user")).thenReturn(
				Users.createTA(getDataTrain().getUsersController(), "ta",
						"123456789", "I am", "A TA", 10, "Being Silly",
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
}
