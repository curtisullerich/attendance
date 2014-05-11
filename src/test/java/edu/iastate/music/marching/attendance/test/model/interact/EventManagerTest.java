package edu.iastate.music.marching.attendance.test.model.interact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.junit.Test;

import edu.iastate.music.marching.attendance.model.interact.AbsenceManager;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.EventManager;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.testlib.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.testlib.TestUsers;

public class EventManagerTest extends AbstractDatastoreTest {

	@Test
	public void testLongEvent() {
		DataTrain train = getDataTrain();

		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime start = new DateTime(2012, 9, 21, 1, 0, zone);
		DateTime end = new DateTime(2012, 9, 21, 23, 0, zone);
		UserManager uc = train.users();
		User student = TestUsers.createDefaultStudent(uc);

		Event event = ec.createOrUpdate(Event.Type.Rehearsal, new Interval(
				start, end));
		ac.createOrUpdateTardy(student, start.plusMinutes(10));
		ac.createOrUpdateEarlyCheckout(student, end.minusMinutes(10));

		List<Absence> list = ac.getAll(event);
		assertEquals(2, list.size());
	}

	@Test
	public void testLongerEvent() {
		DataTrain train = getDataTrain();

		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime start = new DateTime(2012, 9, 21, 1, 0, zone);
		DateTime end = new DateTime(2012, 9, 23, 23, 0, zone);
		UserManager uc = train.users();
		User student = TestUsers.createDefaultStudent(uc);

		Event event = ec.createOrUpdate(Event.Type.Rehearsal, new Interval(
				start, end));
		ac.createOrUpdateTardy(student, start.plusMinutes(10));
		ac.createOrUpdateEarlyCheckout(student, end.minusMinutes(10));

		List<Absence> list = ac.getAll(event);
		assertEquals(2, list.size());
	}

	@Test
	public void testOverlappingEventsRehersal() {
		DataTrain train = getDataTrain();

		EventManager ec = train.events();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime start = new DateTime(2012, 9, 21, 6, 0, zone);
		DateTime end = new DateTime(2012, 9, 21, 7, 0, zone);

		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));

		List<Event> events = ec.getAll();

		assertEquals(1, events.size());
		assertEquals(start, events.get(0).getInterval(zone).getStart());
		assertEquals(end, events.get(0).getInterval(zone).getEnd());
	}

	@Test
	public void testOverlappingEventsPerformance() {
		DataTrain train = getDataTrain();

		EventManager ec = train.events();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime start = new DateTime(2012, 9, 21, 6, 0, zone);
		DateTime end = new DateTime(2012, 9, 21, 7, 0, zone);

		ec.createOrUpdate(Event.Type.Performance, new Interval(start, end));
		ec.createOrUpdate(Event.Type.Performance, new Interval(start, end));

		List<Event> events = ec.getAll();

		assertEquals(1, events.size());
		assertEquals(start, events.get(0).getInterval(zone).getStart());
		assertEquals(end, events.get(0).getInterval(zone).getEnd());
	}

	@Test
	public void testOverlappingEventsDifferentTypes() {
		DataTrain train = getDataTrain();

		EventManager ec = train.events();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime start = new DateTime(2012, 9, 21, 6, 0, zone);
		DateTime end = new DateTime(2012, 9, 21, 7, 0, zone);

		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		ec.createOrUpdate(Event.Type.Performance, new Interval(start, end));

		List<Event> events = ec.getAll();

		assertEquals(2, events.size());

		assertTrue(events.get(0).getType() == Event.Type.Rehearsal
				|| events.get(0).getType() == Event.Type.Performance);
		assertEquals(start, events.get(0).getInterval(zone).getStart());
		assertEquals(end, events.get(0).getInterval(zone).getEnd());

		assertTrue(events.get(1).getType() == Event.Type.Rehearsal
				|| events.get(1).getType() == Event.Type.Performance);
		assertEquals(start, events.get(1).getInterval(zone).getStart());
		assertEquals(end, events.get(1).getInterval(zone).getEnd());
	}

	@Test
	public void testNonOverlappingEventsRehearsal() {
		DataTrain train = getDataTrain();

		EventManager ec = train.events();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime startFirst = new DateTime(2012, 9, 21, 6, 0, zone);
		DateTime endFirst = new DateTime(2012, 9, 21, 7, 0, zone);

		DateTime startSecond = new DateTime(2012, 9, 22, 6, 0, zone);
		DateTime endSecond = new DateTime(2012, 9, 22, 7, 0, zone);

		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(startFirst,
				endFirst));
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(startSecond,
				endSecond));

		List<Event> events = ec.getAll();

		Event first = (events.get(0).getInterval(zone).getStart()
				.equals(startFirst)) ? events.get(0) : events.get(1);
		Event second = (events.get(0).getInterval(zone).getStart()
				.equals(startSecond)) ? events.get(0) : events.get(1);

		assertEquals(2, events.size());

		assertEquals(Event.Type.Rehearsal, first.getType());
		assertEquals(startFirst, first.getInterval(zone).getStart());
		assertEquals(endFirst, first.getInterval(zone).getEnd());

		assertEquals(Event.Type.Rehearsal, second.getType());
		assertEquals(startSecond, second.getInterval(zone).getStart());
		assertEquals(endSecond, second.getInterval(zone).getEnd());
	}

	@Test
	public void testNonOverlappingEventsPerformance() {
		DataTrain train = getDataTrain();

		EventManager ec = train.events();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime startFirst = new DateTime(2012, 9, 21, 6, 0, zone);
		DateTime endFirst = new DateTime(2012, 9, 21, 7, 0, zone);

		DateTime startSecond = new DateTime(2012, 9, 22, 6, 0, zone);
		DateTime endSecond = new DateTime(2012, 9, 22, 7, 0, zone);

		ec.createOrUpdate(Event.Type.Performance, new Interval(startFirst,
				endFirst));
		ec.createOrUpdate(Event.Type.Performance, new Interval(startSecond,
				endSecond));

		List<Event> events = ec.getAll();

		Event first = (events.get(0).getInterval(zone).getStart()
				.equals(startFirst)) ? events.get(0) : events.get(1);
		Event second = (events.get(0).getInterval(zone).getStart()
				.equals(startSecond)) ? events.get(0) : events.get(1);

		assertEquals(2, events.size());

		assertEquals(Event.Type.Performance, first.getType());
		assertEquals(startFirst, first.getInterval(zone).getStart());
		assertEquals(endFirst, first.getInterval(zone).getEnd());

		assertEquals(Event.Type.Performance, second.getType());
		assertEquals(startSecond, second.getInterval(zone).getStart());
		assertEquals(endSecond, second.getInterval(zone).getEnd());
	}

	@Test
	public void testNonOverlappingDiffTypes() {
		DataTrain train = getDataTrain();

		EventManager ec = train.events();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime startFirst = new DateTime(2012, 9, 21, 6, 0, zone);
		DateTime endFirst = new DateTime(2012, 9, 21, 7, 0, zone);

		DateTime startSecond = new DateTime(2012, 9, 22, 6, 0, zone);
		DateTime endSecond = new DateTime(2012, 9, 22, 7, 0, zone);

		ec.createOrUpdate(Event.Type.Performance, new Interval(startFirst,
				endFirst));
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(startSecond,
				endSecond));

		List<Event> events = ec.getAll();

		Event first = (events.get(0).getInterval(zone).getStart()
				.equals(startFirst)) ? events.get(0) : events.get(1);
		Event second = (events.get(0).getInterval(zone).getStart()
				.equals(startSecond)) ? events.get(0) : events.get(1);

		assertEquals(2, events.size());

		assertEquals(Event.Type.Performance, first.getType());
		assertEquals(startFirst, first.getInterval(zone).getStart());
		assertEquals(endFirst, first.getInterval(zone).getEnd());

		assertEquals(Event.Type.Rehearsal, second.getType());
		assertEquals(startSecond, second.getInterval(zone).getStart());
		assertEquals(endSecond, second.getInterval(zone).getEnd());
	}

	@Test
	public void testCreateEvent() throws ParseException {
		// Arrange
		DataTrain train = getDataTrain();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime start = new DateTime(2012, 6, 16, 5, 0, zone);
		DateTime end = new DateTime(2012, 6, 16, 7, 0, zone);

		// Act
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));

		// Assert
		List<Event> events = train.events().getAll();

		assertEquals(1, events.size());

		Event e = events.get(0);

		assertEquals(start, e.getInterval(zone).getStart());
		assertEquals(end, e.getInterval(zone).getEnd());
		assertEquals(Event.Type.Performance, e.getType());
	}

	@Test
	public void testCreateDuplicateEvents() throws ParseException {

		// Arrange
		DataTrain train = getDataTrain();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime start = new DateTime(2012, 6, 16, 5, 0, zone);
		DateTime end = new DateTime(2012, 6, 16, 7, 0, zone);

		// Act
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));

		// Assert
		List<Event> events = train.events().getAll();

		assertEquals(1, events.size());

		Event e = events.get(0);

		assertEquals(start, e.getInterval(zone).getStart());
		assertEquals(end, e.getInterval(zone).getEnd());
		assertEquals(Event.Type.Performance, e.getType());
	}

	@Test
	public void testAutomaticLinking() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime start = new DateTime(2012, 9, 18, 16, 30, zone);
		DateTime end = new DateTime(2012, 9, 18, 17, 50, zone);
		DateTime tardy1 = new DateTime(2012, 9, 18, 16, 40, zone);
		DateTime tardy2 = new DateTime(2012, 9, 18, 16, 35, zone);

		User s1 = TestUsers.createDefaultStudent(uc);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		ac.createOrUpdateEarlyCheckout(s1, tardy1);

		uc.update(s1);

		// there's a tardy, but it's not linked
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));

		// now that there's a matching event, it should link
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		ac.createOrUpdateTardy(s1, tardy2);
		uc.update(s1);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testAutomaticAbsenceLinking() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		DateTime start = new DateTime(2012, 9, 18, 16, 30, zone);
		DateTime end = new DateTime(2012, 9, 18, 17, 50, zone);
		DateTime start2 = start.plusDays(1);
		DateTime end2 = end.plusDays(1);
		DateTime start3 = start.plusDays(18);
		DateTime end3 = end.plusDays(18);

		User s1 = TestUsers.createDefaultStudent(uc);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		// we test the deprecated method because the mobile app uses it, and you
		// can't create an absence otherwise without an event
		ac.createOrUpdateAbsence(s1, new Interval(start, end));
		uc.update(s1);

		assertEquals(1, ac.get(s1).size());

		// there's an absence, but it's not linked
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		// now that there's a matching event, it should link
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		ac.createOrUpdateAbsence(s1, new Interval(start2, end2));

		assertEquals(2, ac.get(s1).size());

		// duplicate. But we can't exclude it automatically because it could
		// always be a performance OR rehearsal....
		ac.createOrUpdateAbsence(s1, new Interval(start2, end2));
		assertEquals(3, ac.get(s1).size());

		ec.createOrUpdate(Event.Type.Performance, new Interval(start2, end2));

		// now the absences are excluded
		assertEquals(2, ac.get(s1).size());
		// TODO there's gotta be a better way to verify that they linked
		// correctly
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		ac.createOrUpdateAbsence(s1, new Interval(start3, end3));
		assertEquals(3, ac.get(s1).size());

		ac.createOrUpdateAbsence(s1, ec.createOrUpdate(Event.Type.Performance,
				new Interval(start3, end3)));
		assertEquals(3, ac.get(s1).size());

		// TODO there's gotta be a better way to verify that they linked
		// correctly
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());
	}
}
