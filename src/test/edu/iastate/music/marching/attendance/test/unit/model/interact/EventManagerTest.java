package edu.iastate.music.marching.attendance.test.unit.model.interact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import edu.iastate.music.marching.attendance.model.interact.AbsenceManager;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.EventManager;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;

public class EventManagerTest extends AbstractTest {
	
/*
	@Test
	public void testOverlappingEventsRehersal() {
		DataTrain train = getDataTrain();

		EventManager ec = train.getEventManager();

		DateTime startOverlap = makeDate("2012-09-21 0600");
		DateTime endOverlap = makeDate("2012-09-21 0700");

		ec.createOrUpdate(Event.Type.Rehearsal, startOverlap, endOverlap);
		ec.createOrUpdate(Event.Type.Rehearsal, startOverlap, endOverlap);

		List<Event> events = ec.getAll();

		assertEquals(1, events.size());
		assertEquals(startOverlap, events.get(0).getStart());
		assertEquals(endOverlap, events.get(0).getEnd());
	}

	@Test
	public void testOverlappingEventsPerformance() {
		DataTrain train = getDataTrain();

		EventManager ec = train.getEventManager();

		DateTime startOverlap = makeDate("2012-09-21 0600");
		DateTime endOverlap = makeDate("2012-09-21 0700");

		ec.createOrUpdate(Event.Type.Performance, startOverlap, endOverlap);
		ec.createOrUpdate(Event.Type.Performance, startOverlap, endOverlap);

		List<Event> events = ec.getAll();

		assertEquals(1, events.size());
		assertEquals(startOverlap, events.get(0).getStart());
		assertEquals(endOverlap, events.get(0).getEnd());
	}

	@Test
	public void testOverlappingEventsDifferentTypes() {
		DataTrain train = getDataTrain();

		EventManager ec = train.getEventManager();

		DateTime startOverlap = makeDate("2012-09-21 0600");
		DateTime endOverlap = makeDate("2012-09-21 0700");

		ec.createOrUpdate(Event.Type.Rehearsal, startOverlap, endOverlap);
		ec.createOrUpdate(Event.Type.Performance, startOverlap, endOverlap);

		List<Event> events = ec.getAll();

		assertEquals(2, events.size());

		assertTrue(events.get(0).getType() == Event.Type.Rehearsal
				|| events.get(0).getType() == Event.Type.Performance);
		assertEquals(startOverlap, events.get(0).getStart());
		assertEquals(endOverlap, events.get(0).getEnd());

		assertTrue(events.get(1).getType() == Event.Type.Rehearsal
				|| events.get(1).getType() == Event.Type.Performance);
		assertEquals(startOverlap, events.get(1).getStart());
		assertEquals(endOverlap, events.get(1).getEnd());
	}

	@Test
	public void testNonOverlappingEventsRehearsal() {
		DataTrain train = getDataTrain();

		EventManager ec = train.getEventManager();

		DateTime startFirst = makeDate("2012-09-21 0600");
		DateTime endFirst = makeDate("2012-09-21 0700");

		DateTime startSecond = makeDate("2012-09-22 0600");
		DateTime endSecond = makeDate("2012-09-22 0700");

		ec.createOrUpdate(Event.Type.Rehearsal, startFirst, endFirst);
		ec.createOrUpdate(Event.Type.Rehearsal, startSecond, endSecond);

		List<Event> events = ec.getAll();

		Event first = (events.get(0).getStart().equals(startFirst)) ? events
				.get(0) : events.get(1);
		Event second = (events.get(0).getStart().equals(startSecond)) ? events
				.get(0) : events.get(1);

		assertEquals(2, events.size());

		assertEquals(Event.Type.Rehearsal, first.getType());
		assertEquals(startFirst, first.getStart());
		assertEquals(endFirst, first.getEnd());

		assertEquals(Event.Type.Rehearsal, second.getType());
		assertEquals(startSecond, second.getStart());
		assertEquals(endSecond, second.getEnd());
	}

	@Test
	public void testNonOverlappingEventsPerformance() {
		DataTrain train = getDataTrain();

		EventManager ec = train.getEventManager();

		DateTime startFirst = makeDate("2012-09-21 0600");
		DateTime endFirst = makeDate("2012-09-21 0700");

		DateTime startSecond = makeDate("2012-09-22 0600");
		DateTime endSecond = makeDate("2012-09-22 0700");

		ec.createOrUpdate(Event.Type.Performance, startFirst, endFirst);
		ec.createOrUpdate(Event.Type.Performance, startSecond, endSecond);

		List<Event> events = ec.getAll();

		Event first = (events.get(0).getStart().equals(startFirst)) ? events
				.get(0) : events.get(1);
		Event second = (events.get(0).getStart().equals(startSecond)) ? events
				.get(0) : events.get(1);

		assertEquals(2, events.size());

		assertEquals(Event.Type.Performance, first.getType());
		assertEquals(startFirst, first.getStart());
		assertEquals(endFirst, first.getEnd());

		assertEquals(Event.Type.Performance, second.getType());
		assertEquals(startSecond, second.getStart());
		assertEquals(endSecond, second.getEnd());
	}

	@Test
	public void testNonOverlappingDiffTypes() {
		DataTrain train = getDataTrain();

		EventManager ec = train.getEventManager();

		DateTime startFirst = makeDate("2012-09-21 0600");
		DateTime endFirst = makeDate("2012-09-21 0700");

		DateTime startSecond = makeDate("2012-09-22 0600");
		DateTime endSecond = makeDate("2012-09-22 0700");

		ec.createOrUpdate(Event.Type.Performance, startFirst, endFirst);
		ec.createOrUpdate(Event.Type.Rehearsal, startSecond, endSecond);

		List<Event> events = ec.getAll();

		Event first = (events.get(0).getStart().equals(startFirst)) ? events
				.get(0) : events.get(1);
		Event second = (events.get(0).getStart().equals(startSecond)) ? events
				.get(0) : events.get(1);

		assertEquals(2, events.size());

		assertEquals(Event.Type.Performance, first.getType());
		assertEquals(startFirst, first.getStart());
		assertEquals(endFirst, first.getEnd());

		assertEquals(Event.Type.Rehearsal, second.getType());
		assertEquals(startSecond, second.getStart());
		assertEquals(endSecond, second.getEnd());
	}

	@Test
	public void testCreateEvent() throws ParseException {
		// Arrange
		DataTrain train = getDataTrain();

		DateTime eventStart = null;
		DateTime eventEnd = null;

		eventStart = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0500");
		eventEnd = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0700");

		// Act
		train.getEventManager().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		// Assert
		List<Event> events = train.getEventManager().getAll();

		assertEquals(1, events.size());

		Event e = events.get(0);

		assertEquals(eventStart, e.getStart());
		assertEquals(eventEnd, e.getEnd());
		assertEquals(Event.Type.Performance, e.getType());
	}

	@Test
	public void testCreateDuplicateEvents() throws ParseException {

		// Arrange
		DataTrain train = getDataTrain();

		DateTime eventStart = null;
		DateTime eventEnd = null;

		eventStart = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0500");
		eventEnd = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0700");

		// Act
		train.getEventManager().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		train.getEventManager().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		// Assert
		List<Event> events = train.getEventManager().getAll();

		assertEquals(1, events.size());

		Event e = events.get(0);

		assertEquals(eventStart, e.getStart());
		assertEquals(eventEnd, e.getEnd());
		assertEquals(Event.Type.Performance, e.getType());
	}

	@Test
	public void testAutomaticLinking() {
		DataTrain train = getDataTrain();

		UserManager uc = train.getUsersManager();
		EventManager ec = train.getEventManager();
		AbsenceManager ac = train.getAbsenceManager();

		User s1 = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		Calendar start = Calendar.getInstance();
		start.set(2012, 9, 18, 16, 30);
		Calendar end = Calendar.getInstance();
		end.set(2012, 9, 18, 17, 50);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 9, 18, 16, 40);
		Absence a1 = ac.createOrUpdateEarlyCheckout(s1, tardy.getTime());

		uc.update(s1);

		// there's a tardy, but it's not linked
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		Event e1 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		// now that there's a matching event, it should link
		assertEquals(User.Grade.Aminus, uc.get(s1.getId()).getGrade());

		tardy.set(2012, 9, 18, 16, 35);
		ac.createOrUpdateTardy(s1, tardy.getTime());
		uc.update(s1);
		assertEquals(User.Grade.Bplus, uc.get(s1.getId()).getGrade());
	}

	@Test
	public void testAutomaticAbsenceLinking() {
		DataTrain train = getDataTrain();

		UserManager uc = train.getUsersManager();
		EventManager ec = train.getEventManager();
		AbsenceManager ac = train.getAbsenceManager();

		User s1 = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		Calendar start = Calendar.getInstance();
		start.set(2012, 9, 18, 16, 30);
		Calendar end = Calendar.getInstance();
		end.set(2012, 9, 18, 17, 50);

		// we test the deprecated method because the mobile app uses it, and you
		// can't create an absence otherwise without an event
		ac.createOrUpdateAbsence(s1, start.getTime(), end.getTime());
		uc.update(s1);

		assertEquals(1, ac.get(s1).size());

		// there's an absence, but it's not linked
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(), end.getTime());
		// now that there's a matching event, it should link
		assertEquals(User.Grade.B, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		ac.createOrUpdateAbsence(s1, start.getTime(), end.getTime());

		assertEquals(2, ac.get(s1).size());

		// duplicate. But we can't exclude it automatically because it could
		// always be a performance OR rehearsal....
		ac.createOrUpdateAbsence(s1, start.getTime(), end.getTime());
		assertEquals(3, ac.get(s1).size());

		ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());

		// now the absences are excluded
		assertEquals(2, ac.get(s1).size());
		// TODO there's gotta be a better way to verify that they linked
		// correctly
		assertEquals(User.Grade.D, uc.get(s1.getId()).getGrade());
		start.add(Calendar.DATE, 17);
		end.add(Calendar.DATE, 17);
		ac.createOrUpdateAbsence(s1, start.getTime(), end.getTime());
		assertEquals(3, ac.get(s1).size());

		Absence a = ac.createOrUpdateAbsence(
				s1,
				ec.createOrUpdate(Event.Type.Performance, start.getTime(),
						end.getTime()));
		assertEquals(3, ac.get(s1).size());

		// TODO there's gotta be a better way to verify that they linked
		// correctly
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());
	}

	private DateTime makeDate(String sDate) {
		// Private method to make dates out of strings following the format I
		// always use
		try {
			return new SimpleDateFormat("yyyy-MM-dd HHmm").parse(sDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}*/
}
