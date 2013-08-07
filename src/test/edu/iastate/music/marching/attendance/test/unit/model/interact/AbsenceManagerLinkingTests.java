package edu.iastate.music.marching.attendance.test.unit.model.interact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.junit.Test;

import edu.iastate.music.marching.attendance.model.interact.AbsenceManager;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.EventManager;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Absence.Type;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.test.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.test.util.Users;

@SuppressWarnings("deprecation")
public class AbsenceManagerLinkingTests extends AbstractDatastoreTest {

	private static final LocalDateTime ADATETIME = new LocalDateTime(2012, 8,
			8, 17, 0, 0, 0);

	private static final LocalDateTime EDATETIME = new LocalDateTime(2012, 9,
			21, 6, 0, 0, 0);

	private enum OverlapLink {
		First, Second, Neither
	}

	@Test
	public void testOverlappingEventsWithTardyDuringEarlyEvent() {

		/*
		 * When we have 2 overlapping events adding a tardy during just the
		 * first event shouldn't cause any problems and it should be linked to
		 * the first event
		 */

		LocalDateTime startFirst = EDATETIME;
		LocalDateTime endFirst = EDATETIME.plusHours(1);

		LocalDateTime startSecond = EDATETIME.plusMinutes(45);
		LocalDateTime endSecond = EDATETIME.plusHours(1).plusMinutes(45);

		LocalDateTime tardyStart = EDATETIME.plusMinutes(15);

		overlappingEventsTestLinkingHelper(startFirst, endFirst, startSecond,
				endSecond, tardyStart, null, Absence.Type.Tardy,
				OverlapLink.First);
	}

	@Test
	public void testOverlappingEventsWithTardyDuringLaterEvent() {
		LocalDateTime startFirst = EDATETIME;
		LocalDateTime endFirst = EDATETIME.plusHours(1);

		LocalDateTime startSecond = EDATETIME.plusMinutes(45);
		LocalDateTime endSecond = EDATETIME.plusHours(1).plusMinutes(45);

		LocalDateTime tardyStart = EDATETIME.plusHours(1).plusMinutes(15);

		overlappingEventsTestLinkingHelper(startFirst, endFirst, startSecond,
				endSecond, tardyStart, Absence.Type.Tardy, OverlapLink.Second);
	}

	@Test
	public void testOverlappingEventsWithTardyDuringBothEvents() {
		LocalDateTime startFirst = EDATETIME;
		LocalDateTime endFirst = EDATETIME.plusHours(1);

		LocalDateTime startSecond = EDATETIME.plusMinutes(45);
		LocalDateTime endSecond = EDATETIME.plusHours(1).plusMinutes(45);

		LocalDateTime tardyStart = EDATETIME.plusMinutes(50);

		overlappingEventsTestLinkingHelper(startFirst, endFirst, startSecond,
				endSecond, tardyStart, Absence.Type.Tardy, OverlapLink.Neither);
	}

	@Test
	public void testOverlappingEventsWithAbsenceDuringEarlyEvent() {
		LocalDateTime startFirst = EDATETIME;
		LocalDateTime endFirst = EDATETIME.plusHours(1);

		LocalDateTime startSecond = EDATETIME.plusMinutes(45);
		LocalDateTime endSecond = EDATETIME.plusHours(1).plusMinutes(45);

		LocalDateTime absenceStart = EDATETIME.plusMinutes(15);
		LocalDateTime absenceEnd = EDATETIME.plusMinutes(30);

		overlappingEventsTestLinkingHelper(startFirst, endFirst, startSecond,
				endSecond, absenceStart, absenceEnd, Absence.Type.Absence,
				OverlapLink.Neither);
	}

	@Test
	public void testOverlappingEventsWithAbsenceDuringLaterEvent() {
		LocalDateTime startFirst = EDATETIME;
		LocalDateTime endFirst = EDATETIME.plusHours(1);

		LocalDateTime startSecond = EDATETIME.plusMinutes(45);
		LocalDateTime endSecond = EDATETIME.plusHours(1).plusMinutes(45);

		LocalDateTime absenceStart = EDATETIME.plusHours(1).plusMinutes(15);
		LocalDateTime absenceEnd = EDATETIME.plusHours(1).plusMinutes(45);

		overlappingEventsTestLinkingHelper(startFirst, endFirst, startSecond,
				endSecond, absenceStart, absenceEnd, Absence.Type.Absence,
				OverlapLink.Neither);
	}

	@Test
	public void testOverlappingEventsWithEarlyDuringEarlyEvent() {
		LocalDateTime startFirst = EDATETIME;
		LocalDateTime endFirst = EDATETIME.plusHours(1);

		LocalDateTime startSecond = EDATETIME.plusMinutes(45);
		LocalDateTime endSecond = EDATETIME.plusHours(1).plusMinutes(45);

		LocalDateTime checkout = EDATETIME.plusMinutes(40);

		overlappingEventsTestLinkingHelper(startFirst, endFirst, startSecond,
				endSecond, checkout, Absence.Type.EarlyCheckOut,
				OverlapLink.First);
	}

	@Test
	public void testOverlappingEventsWithEarlyDuringLaterEvent() {
		LocalDateTime startFirst = EDATETIME; //0:00
		LocalDateTime endFirst = EDATETIME.plusHours(1); //1:00

		LocalDateTime startSecond = EDATETIME.plusMinutes(45);//0:45
		LocalDateTime endSecond = EDATETIME.plusHours(1).plusMinutes(45);//1:45

		LocalDateTime checkout = EDATETIME.plusHours(1).plusMinutes(30);//1:30

		overlappingEventsTestLinkingHelper(startFirst, endFirst, startSecond,
				endSecond, checkout, Absence.Type.EarlyCheckOut,
				OverlapLink.Second);
	}

	@Test
	public void testOverlappingEventsWithEarlyDuringBothEvents() {
		LocalDateTime startFirst = EDATETIME;
		LocalDateTime endFirst = EDATETIME.plusHours(1);

		LocalDateTime startSecond = EDATETIME.plusMinutes(45);
		LocalDateTime endSecond = EDATETIME.plusHours(1).plusMinutes(45);

		LocalDateTime checkout = EDATETIME.plusMinutes(50);

		overlappingEventsTestLinkingHelper(startFirst, endFirst, startSecond,
				endSecond, checkout, Absence.Type.EarlyCheckOut,
				OverlapLink.Neither);
	}

	@Test
	public void testAbsenceDOESNTAutoLinkEventDeleted() {

		DataTrain train = getDataTrain();
		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		Interval firstInterval = new Interval(EDATETIME.toDateTime(zone),
				new Period().plusHours(1));

		Interval secondInterval = new Interval(EDATETIME.plusDays(1)
				.toDateTime(zone), new Period().plusHours(1));

		Event event1 = ec.createOrUpdate(Event.Type.Rehearsal, firstInterval);
		Event event2 = ec.createOrUpdate(Event.Type.Performance, firstInterval);

		ec.createOrUpdate(Event.Type.Rehearsal, secondInterval);
		ec.createOrUpdate(Event.Type.Performance, secondInterval);

		ac.createOrUpdateAbsence(student, secondInterval);
		ac.createOrUpdateAbsence(student, firstInterval);

		// Delete event, should link first absence to remaining event during
		// that interval
		ec.delete(event1, false);

		List<Absence> allAbs = ac.get(student);

		assertEquals(2, allAbs.size());
		Absence firstAbsence = null;
		Absence secondAbsence = null;
		for (Absence a : allAbs) {
			if (firstInterval.equals(a.getInterval(zone))) {
				firstAbsence = a;
			} else if (secondInterval.equals(a.getInterval(zone))) {
				secondAbsence = a;
			}
		}
		assertNotNull(firstAbsence);
		assertNotNull(secondAbsence);

		assertEquals(event2, firstAbsence.getEvent());
		assertNull(secondAbsence.getEvent());
	}

	@Test
	public void testTardyDOESNTAutoLinkEventDeleted() {
		DataTrain train = getDataTrain();
		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		Interval firstInterval = new Interval(EDATETIME.toDateTime(zone),
				new Period().plusHours(1));

		Interval secondInterval = new Interval(EDATETIME.plusDays(1)
				.toDateTime(zone), new Period().plusHours(1));
		
		DateTime absenceTime1 = firstInterval.getStart().plusMinutes(15);
		DateTime absenceTime2 = secondInterval.getStart().plusMinutes(15);

		Event event1 = ec.createOrUpdate(Event.Type.Rehearsal, firstInterval);
		Event event2 = ec.createOrUpdate(Event.Type.Performance, firstInterval);

		ec.createOrUpdate(Event.Type.Rehearsal, secondInterval);
		ec.createOrUpdate(Event.Type.Performance, secondInterval);

		ac.createOrUpdateTardy(student, absenceTime1);
		ac.createOrUpdateTardy(student, absenceTime2);

		// Delete event, should link first tardy to remaining event during
		// that interval
		ec.delete(event1, false);

		List<Absence> allAbs = ac.get(student);

		assertEquals(2, allAbs.size());
		Absence firstAbsence = null;
		Absence secondAbsence = null;
		for (Absence a : allAbs) {
			if (absenceTime1.equals(a.getCheckin(zone))) {
				firstAbsence = a;
			} else if (absenceTime2.equals(a.getCheckin(zone))) {
				secondAbsence = a;
			}
		}
		assertNotNull(firstAbsence);
		assertNotNull(secondAbsence);

		assertEquals(event2, firstAbsence.getEvent());
		assertNull(secondAbsence.getEvent());
	}

	@Test
	public void testEarlyDOESNTAutoLinkEventDeleted() {
		DataTrain train = getDataTrain();
		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		Interval firstInterval = new Interval(EDATETIME.toDateTime(zone),
				new Period().plusHours(1));

		Interval secondInterval = new Interval(EDATETIME.plusDays(1)
				.toDateTime(zone), new Period().plusHours(1));
		
		DateTime absenceTime1 = firstInterval.getStart().plusMinutes(15);
		DateTime absenceTime2 = secondInterval.getStart().plusMinutes(15);

		Event event1 = ec.createOrUpdate(Event.Type.Rehearsal, firstInterval);
		Event event2 = ec.createOrUpdate(Event.Type.Performance, firstInterval);

		ec.createOrUpdate(Event.Type.Rehearsal, secondInterval);
		ec.createOrUpdate(Event.Type.Performance, secondInterval);

		ac.createOrUpdateEarlyCheckout(student, absenceTime1);
		ac.createOrUpdateEarlyCheckout(student, absenceTime2);

		// Delete event, should link first tardy to remaining event during
		// that interval
		ec.delete(event1, false);

		List<Absence> allAbs = ac.get(student);

		assertEquals(2, allAbs.size());
		Absence firstAbsence = null;
		Absence secondAbsence = null;
		for (Absence a : allAbs) {
			if (absenceTime1.equals(a.getCheckout(zone))) {
				firstAbsence = a;
			} else if (absenceTime2.equals(a.getCheckout(zone))) {
				secondAbsence = a;
			}
		}
		assertNotNull(firstAbsence);
		assertNotNull(secondAbsence);

		assertEquals(event2, firstAbsence.getEvent());
		assertNull(secondAbsence.getEvent());
	}

	@Test
	public void testTardyLastMillisecond() {
		LocalDateTime absenceTime = ADATETIME.plusMinutes(1).minusMillis(1);
		LocalDateTime eventStart = ADATETIME;
		LocalDateTime eventEnd = ADATETIME.plusMinutes(1);
		boolean shouldLink = true;

		absenceLinkingTestHelper(absenceTime, eventStart, eventEnd,
				Event.Type.Rehearsal, Absence.Type.Tardy, shouldLink);
	}

	@Test
	public void testECOLastMillisecond() {
		LocalDateTime absenceTime = ADATETIME.plusMinutes(1).minusMillis(1);
		LocalDateTime eventStart = ADATETIME;
		LocalDateTime eventEnd = ADATETIME.plusMinutes(1);
		boolean shouldLink = true;

		absenceLinkingTestHelper(absenceTime, eventStart, eventEnd,
				Event.Type.Rehearsal, Absence.Type.EarlyCheckOut, shouldLink);
	}

	@Test
	public void testTardyOnEnd() {
		LocalDateTime absenceTime = ADATETIME.plusMinutes(1).minusSeconds(1);
		LocalDateTime eventStart = ADATETIME;
		LocalDateTime eventEnd = ADATETIME.plusMinutes(1);
		boolean shouldLink = true;

		absenceLinkingTestHelper(absenceTime, eventStart, eventEnd,
				Event.Type.Rehearsal, Absence.Type.Tardy, shouldLink);
	}

	@Test
	public void testECOOnEnd() {
		LocalDateTime absenceTime = ADATETIME.plusMinutes(1).minusSeconds(1);
		LocalDateTime eventStart = ADATETIME;
		LocalDateTime eventEnd = ADATETIME.plusMinutes(1);
		boolean shouldLink = true;

		absenceLinkingTestHelper(absenceTime, eventStart, eventEnd,
				Event.Type.Rehearsal, Absence.Type.EarlyCheckOut, shouldLink);
	}

	public void testTardyOverEnd() {
		LocalDateTime absenceTime = ADATETIME.plusMinutes(1).plusMillis(1);
		LocalDateTime eventStart = ADATETIME;
		LocalDateTime eventEnd = ADATETIME.plusMinutes(1);
		boolean shouldLink = false;

		absenceLinkingTestHelper(absenceTime, eventStart, eventEnd,
				Event.Type.Rehearsal, Absence.Type.Tardy, shouldLink);
	}

	@Test
	public void testECOOverEnd() {
		LocalDateTime absenceTime = ADATETIME.plusMinutes(1).plusMillis(1);
		LocalDateTime eventStart = ADATETIME;
		LocalDateTime eventEnd = ADATETIME.plusMinutes(1);
		boolean shouldLink = false;

		absenceLinkingTestHelper(absenceTime, eventStart, eventEnd,
				Event.Type.Rehearsal, Absence.Type.EarlyCheckOut, shouldLink);
	}

	public void absenceLinkingTestHelper(LocalDateTime absenceTime,
			LocalDateTime eventStart, LocalDateTime eventEnd,
			Event.Type eventType, Absence.Type absenceType, boolean shouldLink) {
		DataTrain train = getDataTrain();
		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		Event event1 = ec.createOrUpdate(
				eventType,
				new Interval(eventStart.toDateTime(zone), eventEnd
						.toDateTime(zone)));

		Absence a1;

		switch (absenceType) {
		case EarlyCheckOut:
			a1 = ac.createOrUpdateEarlyCheckout(student,
					absenceTime.toDateTime(zone));
			break;
		case Tardy:
			a1 = ac.createOrUpdateTardy(student, absenceTime.toDateTime(zone));
			break;
		default:
			throw new UnsupportedOperationException();
		}

		if (shouldLink) {
			assertEquals(event1, a1.getEvent());
		} else {
			assertNotSame(event1, a1.getEvent());
		}
	}

	private void overlappingEventsTestLinkingHelper(LocalDateTime startFirst,
			LocalDateTime endFirst, LocalDateTime startSecond,
			LocalDateTime endSecond, LocalDateTime checkinoutTime,
			Type absenceType, OverlapLink expectedLink) {
		overlappingEventsTestLinkingHelper(startFirst, endFirst, startSecond,
				endSecond, checkinoutTime, null, absenceType, expectedLink);
	}

	private void overlappingEventsTestLinkingHelper(LocalDateTime startFirst,
			LocalDateTime endFirst, LocalDateTime startSecond,
			LocalDateTime endSecond, LocalDateTime absenceTime,
			LocalDateTime absenceSecondTime, Type absenceType,
			OverlapLink expectedLink) {

		DataTrain train = getDataTrain();
		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		Interval firstInterval = new Interval(startFirst.toDateTime(zone),
				endFirst.toDateTime(zone));
		Interval secondInterval = new Interval(startSecond.toDateTime(zone),
				endSecond.toDateTime(zone));

		ec.createOrUpdate(Event.Type.Rehearsal, firstInterval);
		ec.createOrUpdate(Event.Type.Performance, secondInterval);
		assertEquals(ec.getAll().size(), 2);

		List<Absence> abs;
		switch (absenceType) {
		case EarlyCheckOut:
			ac.createOrUpdateEarlyCheckout(student,
					absenceTime.toDateTime(zone));

			abs = ac.get(student);
			assertEquals(1, abs.size());
			assertEquals(absenceTime.toDateTime(zone), abs.get(0).getCheckout(zone));

			break;
		case Tardy:
			ac.createOrUpdateTardy(student, absenceTime.toDateTime(zone));

			abs = ac.get(student);
			assertEquals(1, abs.size());
			assertEquals(absenceTime.toDateTime(zone), abs.get(0).getCheckin(zone));

			break;
		case Absence:
			Interval abscenseInterval = new Interval(
					absenceTime.toDateTime(zone),
					absenceSecondTime.toDateTime(zone));
			ac.createOrUpdateAbsence(student, abscenseInterval);

			abs = ac.get(student);
			assertEquals(1, abs.size());
			// The events overlap so we say they can deal with it 8|
			assertTrue(abs.get(0).getEvent() == null);
			assertEquals(abscenseInterval, abs.get(0).getInterval(zone));

			break;
		default:
			throw new UnsupportedOperationException();
		}

		assertEquals(1, abs.size());
		Event event = abs.get(0).getEvent();
		assertEquals(ec.getAll().size(), 2);
		switch (expectedLink) {
		case First:
			assertNotNull(event);
			assertEquals(firstInterval, event.getInterval(zone));
			break;
		case Second:
			assertNotNull(event);
			assertEquals(secondInterval, event.getInterval(zone));
			break;
		case Neither:
			assertNull(event);
			break;
		default:
			throw new UnsupportedOperationException();
		}
	}
}
