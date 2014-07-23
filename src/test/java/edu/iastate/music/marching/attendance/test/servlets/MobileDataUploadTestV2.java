package edu.iastate.music.marching.attendance.test.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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

public class MobileDataUploadTestV2 extends AbstractDatastoreTest {

	private static final String SIMPLE_ABSENCE_TESTDATA_V2 = "[ { \"absences\": [ { \"type\": \"Absence\", \"netid\": \"ehayles\" }, { \"type\": \"Absence\", \"netid\": \"alusk\" } ], \"type\": \"Rehearsal\", \"startDateTime\": \"2012-05-03T16:30:00.000Z\", \"endDateTime\": \"2012-05-03T17:50:00.000Z\" } ]";
	private static final String SIMPLE_TARDY_TESTDATA_V2 = "[ { \"absences\": [ { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:09:00.000Z\", \"netid\": \"ehayles\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:16:00.000Z\", \"netid\": \"ehayles\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:08:00.000Z\", \"netid\": \"jbade\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:07:00.000Z\", \"netid\": \"ehayles\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:08:00.000Z\", \"netid\": \"ehayles\" }, { \"type\": \"Tardy\", \"time\": \"2012-05-03T01:08:00.000Z\", \"netid\": \"alusk\" } ], \"type\": \"Rehearsal\", \"startDateTime\": \"2014-04-23T16:30:00.511Z\", \"endDateTime\": \"2014-04-23T17:50:00.511Z\" } ]";

	private static final String SIMPLE_V2_TEST_DATA = "[{ \"absences\": [{ \"type\": \"Tardy\", \"time\": \"2014-04-23T16:45:05.511Z\", \"netid\": \"ehayles\" }], \"type\": \"Rehearsal\", \"startDateTime\": \"2014-04-23T16:30:00.511Z\", \"endDateTime\": \"2014-04-23T17:50:00.511Z\" }]";

	@Test
	public void simpleAbsenceInsertionThroughServlet_NullStudent()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		DataTrain train = getDataTrain();
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req,
				TestUsers.createDefaultTA(train.users()));
		ServletMocks.setPostedContent(req, SIMPLE_ABSENCE_TESTDATA_V2);

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

		User ta = TestUsers.createTA(train.users(), "ehornbuckle", "123456780",
				"first", "last", 2, "major", User.Section.Staff);
		TestUsers.createStudent(train.users(), "ehayles", "123456719", "first",
				"last", 1, "major", User.Section.Baritone);
		TestUsers.createStudent(train.users(), "alusk", "123456782", "first",
				"last", 1, "major", User.Section.Drumline_Bass);

		Reader r = new StringReader(SIMPLE_ABSENCE_TESTDATA_V2);
		String result = train.mobileData().pushMobileDataV2(r, ta);

		simpleAbsenceInsertionVerification();
	}

	@Test
	public void testSimpleAbsenceInsertionThroughServlet()
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		// Arrange
		DataTrain train = getDataTrain();
		TestUsers.createStudent(train.users(), "ehayles", "123456788", "first",
				"last", 1, "major", User.Section.Clarinet);
		TestUsers.createStudent(train.users(), "alusk", "123456782", "first",
				"last", 1, "major", User.Section.TenorSax);

		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		ServletMocks.setUserSession(req,
				TestUsers.createDefaultTA(train.users()));
		ServletMocks.setPostedContent(req, SIMPLE_ABSENCE_TESTDATA_V2);
		// TODO how to set the page?
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
					.equals("ehayles@" + TestConfig.getEmailDomain())) {
				assertFalse(
						"There should only be one abscence for user ehayles",
						foundS);
				foundS = true;
				// TODO https://github.com/curtisullerich/attendance/issues/123
				// assert information about inserted absence
			} else if (a.getStudent().getPrimaryEmail().getEmail()
					.equals("alusk@" + TestConfig.getEmailDomain())) {
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

		TestUsers.createStudent(uc, "ehayles", "123456780", "test1", "tester",
				1, "major", User.Section.AltoSax);
		TestUsers.createStudent(uc, "alusk", "123456781", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		TestUsers.createStudent(uc, "jbade", "123456782", "test1", "tester", 1,
				"major", User.Section.AltoSax);
		User ta = TestUsers.createTA(uc, "ehornbuckle", "123456783", "test1",
				"tester", 1, "major", User.Section.AltoSax);

		Reader r = new StringReader(SIMPLE_TARDY_TESTDATA_V2);
		train.mobileData().pushMobileDataV2(r, ta);

		// Verify insertion lengths
		assertEquals(0, train.events().getCount().intValue());

		assertEquals(6, train.absences().getCount().intValue());

		// TODO: https://github.com/curtisullerich/attendance/issues/123
		// Check actual data returned
	}

}
