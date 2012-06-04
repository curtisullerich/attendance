package edu.iastate.music.marching.attendance.test.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
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
		Event event;

		ObjectDatastore datastore = getObjectDataStore();

		DataTrain train = DataTrain.getAndStartTrain();

		User ta = ModelFactory.newUser(User.Type.TA, null, "ta", 3);
		datastore.store(ta);
		
		User s = ModelFactory.newUser(User.Type.Student, null, "s", 1);
		datastore.store(s);

		User z = ModelFactory.newUser(User.Type.Student, null, "zf", 2);
		datastore.store(z);

		train.getMobileDataController().pushMobileData(SIMPLE_ABSENCE_TESTDATA, ta);

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
				assertFalse("There should only be one abscence for user s", foundS);
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

		train.getMobileDataController().pushMobileData(SIMPLE_TARDY_TESTDATA, ta);

		// Verify insertion lengths
		assertEquals(0, datastore.find().type(Event.class).returnCount().now()
				.intValue());

		assertEquals(6, datastore.find().type(Absence.class).returnCount()
				.now().intValue());

		// TODO: Check actual data returned
	}

}
