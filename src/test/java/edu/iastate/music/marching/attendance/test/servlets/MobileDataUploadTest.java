package edu.iastate.music.marching.attendance.test.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import edu.iastate.music.marching.attendance.Lang;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.servlets.MobileAppDataServlet;
import edu.iastate.music.marching.attendance.testlib.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.testlib.ServletMocks;
import edu.iastate.music.marching.attendance.testlib.TestConfig;
import edu.iastate.music.marching.attendance.testlib.TestUsers;

public class MobileDataUploadTest extends AbstractDatastoreTest {

	private static final String SIMPLE_ABSENCE_TESTDATA = "absentStudentRehearsal&split&el&split&Starster&split&s&split&2012-05-03&split&1630&split&1750&split&null&newline&"
			+ "storedRehearsal&split&rehearsal&split&|&split&|&split&2012-05-03&split&1630&split&1750&split&|&newline&"
			+ "absentStudentRehearsal&split&P1&split&Z&split&zf&split&2012-05-03&split&1630&split&1750&split&4&newline&";

	private static final String SIMPLE_TARDY_TESTDATA = "tardyStudent&split&el&split&Starster&split&s&split&2012-05-03&split&0109&split&|&split&null&newline&"
			+ "tardyStudent&split&el&split&Starster&split&s&split&2012-05-03&split&0116&split&|&split&null&newline&"
			+ "tardyStudent&split&Bdog&split&Zemax&split&b&split&2012-05-03&split&0108&split&|&split&|&newline&"
			+ "tardyStudent&split&el&split&Starster&split&s&split&2012-05-03&split&0107&split&|&split&null&newline&"
			+ "tardyStudent&split&el&split&Starster&split&s&split&2012-05-03&split&0108&split&|&split&null&newline&"
			+ "tardyStudent&split&P1&split&Z&split&zf&split&2012-05-03&split&0108&split&|&split&4&newline&";

	@Test
	public void simpleAbsenceInsertionThroughServlet_NullStudent()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req,
				TestUsers.createDefaultTA(train.users()));
		ServletMocks.setPostedContent(req, SIMPLE_ABSENCE_TESTDATA);

		ServletOutputStream os = mock(ServletOutputStream.class);
		when(resp.getOutputStream()).thenReturn(os);

		ServletMocks.doPost(MobileAppDataServlet.class, req, resp);

		verify(os).print(
				"{\"error\":\"exception\",\"message\":\""
						+ Lang.ERROR_ABSENCE_FOR_NULL_USER + "\"}");

		// TODO: Verify insertion lengths
		// When transactional support is re-added, uncomment this
		// assertEquals(0,
		// datastore.find().type(Event.class).returnCount().now()
		// .intValue());
		// assertEquals(0, datastore.find().type(Absence.class).returnCount()
		// .now().intValue());
	}

	@Test
	public void testSimpleAbsenceInsertionThroughController() {

		DataTrain train = getDataTrain();

		User ta = TestUsers.createTA(train.users(), "ta", "123456780", "first",
				"last", 2, "major", User.Section.Staff);
		TestUsers.createStudent(train.users(), "s", "123456719", "first",
				"last", 1, "major", User.Section.Baritone);
		TestUsers.createStudent(train.users(), "zf", "123456782", "first",
				"last", 1, "major", User.Section.Drumline_Bass);

		train.mobileData().pushMobileData(SIMPLE_ABSENCE_TESTDATA, ta);

		simpleAbsenceInsertionVerification();
	}

	@Test
	public void testSimpleAbsenceInsertionThroughServlet()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		// Arrange
		DataTrain train = getDataTrain();
		TestUsers.createStudent(train.users(), "s", "123456788", "first",
				"last", 1, "major", User.Section.Clarinet);
		TestUsers.createStudent(train.users(), "zf", "123456782", "first",
				"last", 1, "major", User.Section.TenorSax);

		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req,
				TestUsers.createDefaultTA(train.users()));
		ServletMocks.setPostedContent(req, SIMPLE_ABSENCE_TESTDATA);

		ServletOutputStream os = mock(ServletOutputStream.class);
		when(resp.getOutputStream()).thenReturn(os);

		// Act
		ServletMocks.doPost(MobileAppDataServlet.class, req, resp);

		// Assert
		verify(os)
				.print("{\"error\":\"success\",\"message\":\"Inserted 1/1 events.\\nInserted 2/2 absences/tardies/early checkouts.\\n\"}");
		simpleAbsenceInsertionVerification();
	}

	private void simpleAbsenceInsertionVerification() {
		Event event;
		DataTrain train = getDataTrain();
		DateTimeZone zone = train.appData().get().getTimeZone();

		// Verify insertion lengths
		assertEquals(1, train.events().getCount().intValue());
		assertEquals(2, train.absences().getCount().intValue());

		// Verify event
		List<Event> events = train.events().getAll();
		assertEquals(1, events.size());

		event = events.get(0);

		Calendar cal = Calendar.getInstance(zone.toTimeZone());
		cal.setTimeInMillis(0);

		cal.set(2012, 04, 03, 16, 30, 0);
		assertEquals(cal.getTime(), event.getInterval(zone).getStart().toDate());

		cal.set(2012, 04, 03, 17, 50, 0);
		assertEquals(
				0,
				cal.getTime().compareTo(
						event.getInterval(zone).getEnd().toDate()));

		assertEquals(Event.Type.Rehearsal, event.getType());

		// Verify absences
		List<Absence> absences = train.absences().getAll();
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

		UserManager uc = train.users();

		TestUsers.createStudent(uc, "s", "123456780", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		TestUsers.createStudent(uc, "zf", "123456781", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		TestUsers.createStudent(uc, "b", "123456782", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		User ta = TestUsers.createTA(uc, "ta", "123456783", "test1", "tester",
				1, "major", User.Section.AltoSax);

		train.mobileData().pushMobileData(SIMPLE_TARDY_TESTDATA, ta);

		// Verify insertion lengths
		assertEquals(0, train.events().getCount().intValue());

		assertEquals(6, train.absences().getCount().intValue());

		// TODO: https://github.com/curtisullerich/attendance/issues/123
		// Check actual data returned
	}

}
