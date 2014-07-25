package edu.iastate.music.marching.attendance.test.model.interact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.junit.Test;

import edu.iastate.music.marching.attendance.model.interact.AbsenceManager;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.EventManager;
import edu.iastate.music.marching.attendance.model.interact.FormManager;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.testlib.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.testlib.Users;

@SuppressWarnings("deprecation")
public class AbsenceManagerTest extends AbstractDatastoreTest {

	@Test
	public void removeListOfAbsencesAndTardy() {

		// Arrange
		DataTrain train = getDataTrain();
		UserManager uc = train.users();

		User student = Users.createDefaultStudent(uc);
		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime start1 = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime end1 = start1.plusHours(1);

		DateTime start2 = new DateTime(2012, 7, 17, 5, 0, 0, 0, zone);
		DateTime end2 = start2.plusHours(1);

		DateTime start3 = new DateTime(2012, 7, 18, 5, 0, 0, 0, zone);
		DateTime end3 = start3.plusHours(1);

		DateTime tardyStart = start3.plusMinutes(15);

		Event event1 = train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start1, end1));
		Event event2 = train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start2, end2));
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start3, end3));

		Absence abs1 = train.absences().createOrUpdateAbsence(student, event1);
		Absence abs2 = train.absences().createOrUpdateAbsence(student, event2);
		Absence tardy = train.absences().createOrUpdateTardy(student,
				tardyStart);

		// Act
		train.absences().remove(Arrays.asList(abs1, abs2, tardy));

		// Assert
		assertEquals(0, train.absences().getCount().intValue());
	}

	/**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */
	@Test
	public void autoLinkAbsenceOnUpdate_Succeeds() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime start = new DateTime(2012, 7, 16, 16, 0, 0, 0, zone);
		DateTime end = start.plusHours(2);

		DateTime absenceStart = start.minusDays(1);
		DateTime absenceEnd = end.minusDays(1);

		Event event = train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));

		Absence absence = train.absences().createOrUpdateAbsence(student,
				new Interval(absenceStart, absenceEnd));

		// upDateTime absence to be during event
		absence.setInterval(new Interval(start, end));

		train.absences().updateAbsence(absence);

		assertEquals(event, absence.getEvent());
	}

	/**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */

	@Test
	public void autoLinkAbsenceOnUpdate_Fails() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 4, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime absenceStart = eventStart.minusDays(1);
		DateTime absenceEnd = eventEnd.minusDays(1);

		eventStart.minusHours(2);
		DateTime absenceEnd2 = eventEnd.minusHours(2);

		Event event = train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		Absence absence = train.absences().createOrUpdateAbsence(student,
				new Interval(absenceStart, absenceEnd));

		// upDateTime absence to be during event
		// absence.setStart(absenceStart2);
		absence.setInterval(new Interval(absence.getInterval(zone).getStart(),
				absenceEnd2));
		train.absences().updateAbsence(absence);

		assertNotSame(event, absence.getEvent());
	}

	/**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */

	@Test
	public void autoLinkTardyOnUpdate_Succeeds() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 16, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime absenceTime1 = new DateTime(2012, 7, 15, 16, 0, 0, 0, zone);
		DateTime absenceTime2 = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);

		Event event = train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		Absence absence = train.absences().createOrUpdateTardy(student,
				absenceTime1);

		// update absence to be during event
		absence.setCheckin(absenceTime2);
		train.absences().updateAbsence(absence);

		assertEquals(event, absence.getEvent());
	}

	/**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */

	@Test
	public void autoLinkTardyOnUpdate_Fails() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 16, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime absenceTime1 = new DateTime(2012, 7, 15, 16, 0, 0, 0, zone);
		DateTime absenceTime2 = new DateTime(2012, 7, 14, 17, 0, 0, 0, zone);

		Event event = train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		Absence absence = train.absences().createOrUpdateTardy(student,
				absenceTime1);

		// upDateTime absence to be during event
		absence.setCheckin(absenceTime2);
		train.absences().updateAbsence(absence);

		assertNotSame(event, absence.getEvent());
	}

	/**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */

	@Test
	public void autoLinkECOOnUpdate_Succeeds() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 16, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime absenceTime1 = new DateTime(2012, 7, 15, 16, 0, 0, 0, zone);
		DateTime absenceTime2 = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);

		Event event = train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		Absence absence = train.absences().createOrUpdateEarlyCheckout(student,
				absenceTime1);

		// update absence to be during event
		absence.setCheckout(absenceTime2);
		train.absences().updateAbsence(absence);

		assertEquals(event, absence.getEvent());
	}

	/**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */
	@Test
	public void autoLinkECOOnUpdate_Fails() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 16, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime absenceTime1 = new DateTime(2012, 7, 15, 16, 0, 0, 0, zone);
		DateTime absenceTime2 = new DateTime(2012, 7, 14, 17, 0, 0, 0, zone);

		Event event = train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		Absence absence = train.absences().createOrUpdateEarlyCheckout(student,
				absenceTime1);

		// upDateTime absence to be during event
		absence.setCheckout(absenceTime2);
		train.absences().updateAbsence(absence);

		assertNotSame(event, absence.getEvent());
	}

	@Test
	public void testCreateAbsence() {

		// Test to see if adding an absence actually works

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 16, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));
		train.absences().createOrUpdateAbsence(student,
				new Interval(eventStart, eventEnd));

		List<Absence> studentAbsences = train.absences().get(student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getInterval(zone).equals(
				new Interval(eventStart, eventEnd)));
		assertTrue(absence.getType() == Absence.Type.Absence);
	}

	// note that the expected option here doesn't work because this class
	// extends TestCase, which is a JUnit 3 methodology
	@Test(expected = IllegalArgumentException.class)
	public void testCreateAbsenceFail() {
			DataTrain train = getDataTrain();

			UserManager uc = train.users();
			User student = Users.createDefaultStudent(uc);

			DateTimeZone zone = train.appData().get().getTimeZone();
			DateTime eventStart = new DateTime(2012, 7, 15, 16, 0, 0, 0, zone);
			DateTime eventEnd = eventStart.minusHours(1);

			train.events().createOrUpdate(Event.Type.Performance,
					new Interval(eventStart, eventEnd));
			train.absences().createOrUpdateAbsence(student,
					new Interval(eventStart, eventEnd));
	}

	@Test
	public void testAbsenceVsAbsenceSameTime() {

		// When we have two absences at the same time only one of them should be
		// added

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime contesterStart = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);
		DateTime contesterEnd = new DateTime(2012, 7, 16, 19, 0, 0, 0, zone);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		train.absences().createOrUpdateAbsence(student,
				new Interval(eventStart, eventEnd));
		train.absences().createOrUpdateAbsence(student,
				new Interval(contesterStart, contesterEnd));

		List<Absence> studentAbsences = train.absences().get(student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getInterval(zone).equals(
				new Interval(eventStart, eventEnd)));
		assertTrue(absence.getType() == Absence.Type.Absence);
	}

	@Test
	public void testAbsenceVsAbsenceDiffTime() {

		// When we have two absences at different times for different events
		// they should both remain

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime contesterStart = new DateTime(2012, 7, 16, 20, 0, 0, 0, zone);
		DateTime contesterEnd = new DateTime(2012, 7, 16, 21, 0, 0, 0, zone);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(contesterStart, contesterEnd));

		train.absences().createOrUpdateAbsence(student,
				new Interval(eventStart, eventEnd));
		train.absences().createOrUpdateAbsence(student,
				new Interval(contesterStart, contesterEnd));

		List<Absence> studentAbsences = train.absences().get(student);

		assertEquals(2, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getInterval(zone).equals(
				new Interval(eventStart, eventEnd))
				|| absence.getInterval(zone).equals(
						new Interval(contesterStart, contesterEnd)));
		assertTrue(absence.getType() == Absence.Type.Absence);
		absence = studentAbsences.get(1);

		assertTrue(absence.getInterval(zone).equals(
				new Interval(eventStart, eventEnd))
				|| absence.getInterval(zone).equals(
						new Interval(contesterStart, contesterEnd)));
		assertTrue(absence.getType() == Absence.Type.Absence);
	}

	@Test
	public void testAbsenceVsTardySameEvent() {

		/*
		 * If we have an absence and a tardy added where the tardy happens
		 * during the absence, the absence should be removed and the tardy
		 * should remain. This test adds the absence first and the tardy second
		 */
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);
		DateTime tardy = eventStart.plusMinutes(15);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		train.absences().createOrUpdateAbsence(student,
				new Interval(eventStart, eventEnd));
		train.absences().createOrUpdateTardy(student, tardy);

		List<Absence> studentAbsences = train.absences().get(student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getCheckin(zone).equals(tardy));
		assertTrue(absence.getType() == Absence.Type.Tardy);
	}

	@Test
	public void testAbsenceVsTardyDiffEvent() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime event1Start = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);
		DateTime event1End = event1Start.plusHours(2);

		DateTime event2Start = new DateTime(2012, 8, 16, 17, 0, 0, 0, zone);
		DateTime event2End = event2Start.plusHours(2);

		DateTime tardyDate = event2Start.plusMinutes(15);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(event1Start, event1End));
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(event2Start, event2End));

		train.absences().createOrUpdateAbsence(student,
				new Interval(event1Start, event1End));
		train.absences().createOrUpdateTardy(student, tardyDate);

		List<Absence> studentAbsences = train.absences().get(student);

		assertEquals(2, studentAbsences.size());
		Absence absence = (studentAbsences.get(0).getType() == Absence.Type.Absence) ? studentAbsences
				.get(0) : studentAbsences.get(1);
		Absence tardy = (studentAbsences.get(0).getType() == Absence.Type.Tardy) ? studentAbsences
				.get(0) : studentAbsences.get(1);

		assertTrue(absence.getInterval(zone).equals(
				new Interval(event1Start, event1End)));

		assertTrue(tardy.getCheckin(zone).equals(tardyDate));
	}

	@Test
	public void testAbsenceVsEarlySameEvent() {

		// If we have an absence and an early checkout added during the same
		// interval, they should both remain

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);
		DateTime early = eventStart.plusMinutes(15);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		train.absences().createOrUpdateAbsence(student,
				new Interval(eventStart, eventEnd));
		train.absences().createOrUpdateEarlyCheckout(student, early);

		List<Absence> studentAbsences = train.absences().get(student);

		assertEquals(2, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getType() == Absence.Type.Absence);
	}

	@Test
	public void testAbsenceVsEarlyDiffEvent() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime event1Start = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);
		DateTime event1End = event1Start.plusHours(2);

		DateTime event2Start = new DateTime(2012, 8, 16, 17, 0, 0, 0, zone);
		DateTime event2End = event2Start.plusHours(2);

		DateTime earlyDateTime = event2Start.plusMinutes(15);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(event1Start, event1End));
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(event2Start, event2End));

		train.absences().createOrUpdateAbsence(student,
				new Interval(event1Start, event1End));
		train.absences().createOrUpdateEarlyCheckout(student, earlyDateTime);

		List<Absence> studentAbsences = train.absences().get(student);

		assertEquals(2, studentAbsences.size());
		Absence absence = (studentAbsences.get(0).getType() == Absence.Type.Absence) ? studentAbsences
				.get(0) : studentAbsences.get(1);
		Absence early = (studentAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? studentAbsences
				.get(0) : studentAbsences.get(1);

		assertTrue(absence.getInterval(zone).equals(
				new Interval(event1Start, event1End)));

		assertTrue(early.getCheckout(zone).equals(earlyDateTime));

	}

	@Test
	public void testTardyVsAbsence() {

		// This test is the same idea as the testAbsenceVsTardy method only it
		// adds the tardy first

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime tardy = eventStart.plusMinutes(15);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		train.absences().createOrUpdateTardy(student, tardy);
		train.absences().createOrUpdateAbsence(student,
				new Interval(eventStart, eventEnd));

		List<Absence> studentAbsences = train.absences().get(student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getCheckin(zone).equals(tardy));
		assertTrue(absence.getType() == Absence.Type.Tardy);
	}

	@Test
	public void testTardyVsTardySameEvent() {

		// If we have two tardies added for the same event they should both
		// remain

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student1 = Users.createStudent(uc, "student1", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456782",
				"First", "last", 2, "major", User.Section.AltoSax);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 17, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime tardy = eventStart.plusMinutes(15);
		DateTime tardyLate = tardy.plusMinutes(5);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd)); // Adding the early tardy
														// before the late tardy
		train.absences().createOrUpdateTardy(student1, tardy);
		train.absences().createOrUpdateTardy(student1, tardyLate);

		List<Absence> studentAbsences = train.absences().get(student1);

		assertEquals(2, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getType() == Absence.Type.Tardy);

		// Adding the late tardy before the early tardy
		train.absences().createOrUpdateTardy(student2, tardyLate);
		train.absences().createOrUpdateTardy(student2, tardy);

		List<Absence> student2Absences = train.absences().get(student2);

		assertEquals(2, studentAbsences.size());
		Absence absence2 = student2Absences.get(0);

		assertTrue(absence2.getType() == Absence.Type.Tardy);
	}

	@Test
	public void testTardyVsTardyDiffEvent() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student1 = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime event1Start = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime event1End = event1Start.plusHours(2);

		DateTime event2Start = new DateTime(2012, 7, 16, 8, 0, 0, 0, zone);
		DateTime event2End = event2Start.plusHours(1);

		DateTime tardy = event1Start.plusMinutes(15);
		DateTime otherTardy = event2Start.plusMinutes(20);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(event1Start, event1End));
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(event2Start, event2End));

		train.absences().createOrUpdateTardy(student1, tardy);
		train.absences().createOrUpdateTardy(student1, otherTardy);

		List<Absence> studentAbsences = train.absences().get(student1);

		assertEquals(2, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getCheckin(zone).equals(tardy)
				|| absence.getCheckin(zone).equals(otherTardy));
		assertTrue(absence.getType() == Absence.Type.Tardy);

		absence = studentAbsences.get(1);

		assertTrue(absence.getCheckin(zone).equals(tardy)
				|| absence.getCheckin(zone).equals(otherTardy));
		assertTrue(absence.getType() == Absence.Type.Tardy);
	}

	@Test
	public void testTardyVsEarly() {

		// This method tests these four cases: Case 1: don't overlap, both
		// should
		// remain. a: Adding Tardy first b: Adding EarlyCheckout first
		//
		// Case 2: do overlap, become an absence a: Adding Tardy first b: Adding
		// EarlyCheckout first
		//
		// Both should remain in all cases.

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User nonOverTardyFirst = Users.createStudent(uc, "student1",
				"123456780", "First", "last", 2, "major", User.Section.AltoSax);
		User nonOverEarlyFirst = Users.createStudent(uc, "student2",
				"123456789", "First", "last", 2, "major", User.Section.AltoSax);

		User overlapTardyFirst = Users.createStudent(uc, "student3",
				"123456781", "First", "last", 2, "major", User.Section.AltoSax);
		User overlapEarlyFirst = Users.createStudent(uc, "student4",
				"123456782", "First", "last", 2, "major", User.Section.AltoSax);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime tardyDate = eventStart.plusMinutes(15);
		DateTime earlyNon = eventEnd.minusMinutes(10);
		DateTime earlyOverlap = eventStart.plusMinutes(10);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd)); // Case 1.a
		train.absences().createOrUpdateTardy(nonOverTardyFirst, tardyDate);
		train.absences().createOrUpdateEarlyCheckout(nonOverTardyFirst,
				earlyNon);

		List<Absence> nonOverTardyFirstAbsences = train.absences().get(
				nonOverTardyFirst);

		assertEquals(2, nonOverTardyFirstAbsences.size());
		Absence tardy = (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ? nonOverTardyFirstAbsences
				.get(0) : nonOverTardyFirstAbsences.get(1);
		Absence earlyCheckOut = (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? nonOverTardyFirstAbsences
				.get(0) : nonOverTardyFirstAbsences.get(1);

		assertTrue(tardy.getType() == Absence.Type.Tardy);
		assertTrue(tardy.getCheckin(zone).equals(tardyDate));

		assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(earlyCheckOut.getCheckout(zone).equals(earlyNon));

		// Case 1.b
		train.absences().createOrUpdateEarlyCheckout(nonOverEarlyFirst,
				earlyNon);
		train.absences().createOrUpdateTardy(nonOverEarlyFirst, tardyDate);

		List<Absence> nonOverEarlyFirstAbsences = train.absences().get(
				nonOverEarlyFirst);

		assertEquals(2, nonOverEarlyFirstAbsences.size());
		tardy = (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ? nonOverEarlyFirstAbsences
				.get(0) : nonOverEarlyFirstAbsences.get(1);
		earlyCheckOut = (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? nonOverEarlyFirstAbsences
				.get(0) : nonOverEarlyFirstAbsences.get(1);

		assertTrue(tardy.getType() == Absence.Type.Tardy);
		assertTrue(tardy.getCheckin(zone).equals(tardyDate));

		assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(earlyCheckOut.getCheckout(zone).equals(earlyNon));

		// Case 2.a
		train.absences().createOrUpdateTardy(overlapTardyFirst, tardyDate);
		train.absences().createOrUpdateEarlyCheckout(overlapTardyFirst,
				earlyOverlap);

		List<Absence> overlapTardyFirstAbsences = train.absences().get(
				overlapTardyFirst);

		assertEquals(2, overlapTardyFirstAbsences.size());
		overlapTardyFirstAbsences.get(0);

		// Case 2.b
		train.absences().createOrUpdateEarlyCheckout(overlapEarlyFirst,
				earlyOverlap);
		train.absences().createOrUpdateTardy(overlapEarlyFirst, tardyDate);

		List<Absence> overlapEarlyFirstAbsences = train.absences().get(
				overlapEarlyFirst);

		assertEquals(2, overlapEarlyFirstAbsences.size());
		overlapEarlyFirstAbsences.get(0);

	}

	@Test
	public void testEarlyVsAbsence() {

		// Same as the testAbsenceVsEarly method only this adds the Early
		// checkout before the absence

		DataTrain train = getDataTrain();
		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime early = eventStart.plusMinutes(15);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd));

		train.absences().createOrUpdateEarlyCheckout(student, early);
		train.absences().createOrUpdateAbsence(student,
				new Interval(eventStart, eventEnd));

		List<Absence> studentAbsences = train.absences().get(student);

		assertEquals(2, studentAbsences.size());
		studentAbsences.get(0);
	}

	@Test
	public void testEarlyVsEarlySameEvent() {

		// If we have two early checkouts added for the same time zone we need
		// to
		// make sure that both remain.

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student1 = Users.createStudent(uc, "student1", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456780",
				"First", "last", 2, "major", User.Section.AltoSax);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime early = eventStart.plusMinutes(50);
		DateTime earlyEarlier = eventStart.plusMinutes(45);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(eventStart, eventEnd)); // Adding the early
														// checkout before the
														// earlier checkout
		train.absences().createOrUpdateEarlyCheckout(student1, early);
		train.absences().createOrUpdateEarlyCheckout(student1, earlyEarlier);

		List<Absence> studentAbsences = train.absences().get(student1);

		assertEquals(2, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getCheckout(zone).equals(early));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);

		// Adding the earlier early before the early
		train.absences().createOrUpdateEarlyCheckout(student2, earlyEarlier);
		train.absences().createOrUpdateEarlyCheckout(student2, early);

		List<Absence> student2Absences = train.absences().get(student2);

		assertEquals(2, studentAbsences.size());
		Absence absence2 = student2Absences.get(0);

		assertTrue(absence2.getCheckout(zone).equals(earlyEarlier));
		assertTrue(absence2.getType() == Absence.Type.EarlyCheckOut);
	}

	@Test
	public void testEarlyVsEarlyDiffEvent() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student1 = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime event1Start = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime event1End = event1Start.plusHours(2);

		DateTime event2Start = new DateTime(2012, 7, 16, 8, 0, 0, 0, zone);
		DateTime event2End = event2Start.plusHours(1);

		DateTime early = event1Start.plusMinutes(50);
		DateTime otherEarly = event2Start.plusMinutes(45);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(event1Start, event1End));
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(event2Start, event2End));

		train.absences().createOrUpdateEarlyCheckout(student1, early);
		train.absences().createOrUpdateEarlyCheckout(student1, otherEarly);

		List<Absence> studentAbsences = train.absences().get(student1);

		assertEquals(2, studentAbsences.size());

		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getCheckout(zone).equals(early)
				|| absence.getCheckout(zone).equals(otherEarly));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);

		absence = studentAbsences.get(0);
		assertTrue(absence.getCheckout(zone).equals(early)
				|| absence.getCheckout(zone).equals(otherEarly));
		assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);
	}

	@Test
	public void testApproveAbsence() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime start = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime end = start.plusHours(2);

		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));
		Absence abs = train.absences().createOrUpdateAbsence(student,
				new Interval(start, end));
		abs.setStatus(Absence.Status.Approved);
		train.absences().updateAbsence(abs);

		List<Absence> studentAbs = train.absences().get(student);
		assertEquals(1, studentAbs.size());

		Absence absence = studentAbs.get(0);
		assertTrue(absence.getInterval(zone).equals(new Interval(start, end)));
		assertTrue(absence.getType() == Absence.Type.Absence);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}

	@Test
	public void testApprovedAbsenceDominatesAbsence() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime start = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime end = start.plusHours(1);

		// Approved saved first
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));
		Absence abs = train.absences().createOrUpdateAbsence(student,
				new Interval(start, end));
		abs.setStatus(Absence.Status.Approved);
		train.absences().updateAbsence(abs);

		train.absences().createOrUpdateAbsence(student,
				new Interval(start, end));

		List<Absence> studentAbs = train.absences().get(student);
		assertEquals(1, studentAbs.size());

		Absence absence = studentAbs.get(0);
		assertTrue(absence.getInterval(zone).equals(new Interval(start, end)));
		assertTrue(absence.getType() == Absence.Type.Absence);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}

	@Test
	public void testTardyDominatesApprovedAbsence() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime start = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime end = start.plusHours(1);
		DateTime tardyStart = start.plusMinutes(15);

		// Approved saved first
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));
		Absence abs = train.absences().createOrUpdateAbsence(student,
				new Interval(start, end));
		abs.setStatus(Absence.Status.Approved);
		train.absences().updateAbsence(abs);

		train.absences().createOrUpdateTardy(student, tardyStart);

		List<Absence> studentAbs = train.absences().get(student);
		assertEquals(1, studentAbs.size());

		Absence absence = studentAbs.get(0);
		assertTrue(absence.getCheckin(zone).equals(tardyStart));
		assertTrue(absence.getType() == Absence.Type.Tardy);
		assertTrue(absence.getStatus() == Absence.Status.Pending);
	}

	@Test
	public void testApprovedAbsenceVersusEarly() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime start = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime end = start.plusHours(1);
		DateTime tardyStart = start.plusMinutes(15);

		// Approved saved first
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));
		Absence abs = train.absences().createOrUpdateAbsence(student,
				new Interval(start, end));
		abs.setStatus(Absence.Status.Approved);
		train.absences().updateAbsence(abs);

		train.absences().createOrUpdateEarlyCheckout(student, tardyStart);

		List<Absence> studentAbs = train.absences().get(student);
		assertEquals(2, studentAbs.size());

		studentAbs.get(0);
	}

	@Test
	public void testApprovedTardyVsAbsence() {

		// This test is the same idea as the testAbsenceVsTardy method only it
		// adds
		// the tardy first

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();

		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);
		DateTime tardyDate = eventStart.plusMinutes(15);

		ec.createOrUpdate(Event.Type.Performance, new Interval(eventStart,
				eventEnd));

		Absence tardy = ac.createOrUpdateTardy(student, tardyDate);
		tardy.setStatus(Absence.Status.Approved);
		ac.updateAbsence(tardy);

		ac.createOrUpdateAbsence(student, new Interval(eventStart, eventEnd));

		List<Absence> studentAbsences = ac.get(student);

		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getCheckin(zone).equals(tardyDate));
		assertTrue(absence.getType() == Absence.Type.Tardy);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}

	@Test
	public void testApprovedTardyDominatesTardySameTime() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createStudent(uc, "student", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student1 = Users.createStudent(uc, "student1", "123456780",
				"First", "last", 2, "major", User.Section.AltoSax);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime start = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime end = start.plusHours(1);

		// Approved saved first
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));
		Absence abs = train.absences().createOrUpdateTardy(student, start);
		abs.setStatus(Absence.Status.Approved);
		train.absences().updateAbsence(abs);

		train.absences().createOrUpdateTardy(student, start);

		List<Absence> studentAbs = train.absences().get(student);
		assertEquals(1, studentAbs.size());

		Absence absence = studentAbs.get(0);
		assertTrue(absence.getCheckin(zone).equals(start));
		assertTrue(absence.getType() == Absence.Type.Tardy);
		assertTrue(absence.getStatus() == Absence.Status.Approved);

		// Approved saved second
		train.absences().createOrUpdateTardy(student1, start);
		abs = train.absences().createOrUpdateTardy(student1, start);
		abs.setStatus(Absence.Status.Approved);
		train.absences().updateAbsence(abs);

		studentAbs = train.absences().get(student1);
		assertEquals(1, studentAbs.size());

		absence = studentAbs.get(0);
		assertTrue(absence.getCheckin(zone).equals(start));
		assertTrue(absence.getType() == Absence.Type.Tardy);
		assertTrue(absence.getStatus() == Absence.Status.Approved);
	}

	@Test
	public void testApprovedTardyVersusTardyDiffTime() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();

		User student1 = Users.createStudent(uc, "student1", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456782",
				"First", "last", 2, "major", User.Section.AltoSax);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime tardy = eventStart.plusMinutes(15);
		DateTime tardyLate = eventStart.plusMinutes(20);

		ec.createOrUpdate(Event.Type.Performance, new Interval(eventStart,
				eventEnd));

		// Approving the early tardy
		Absence t = ac.createOrUpdateTardy(student1, tardy);
		t.setStatus(Absence.Status.Approved);
		ac.updateAbsence(t);

		ac.createOrUpdateTardy(student1, tardyLate);

		List<Absence> studentAbsences = ac.get(student1);

		// Should be the approved tardy values
		assertEquals(2, studentAbsences.size());
		Absence absence = studentAbsences.get(0);

		assertTrue(absence.getCheckin(zone).equals(tardy));
		assertTrue(absence.getStatus() == Absence.Status.Approved);
		assertTrue(absence.getType() == Absence.Type.Tardy);

		// Approving the later tardy
		t = ac.createOrUpdateTardy(student2, tardyLate);
		t.setStatus(Absence.Status.Approved);
		ac.updateAbsence(t);
		ac.createOrUpdateTardy(student2, tardy);

		List<Absence> student2Absences = ac.get(student2);

		// Should still be the approved values
		assertEquals(2, student2Absences.size());
	}

	@Test
	public void testApprovedTardyVsEarly() {

		// This method tests these four cases: Case 1: don't overlap, both
		// should remain. a: Adding Tardy first b: Adding EarlyCheckout first

		// Case 2: do overlap, become an absence a: Adding Tardy first b: Adding
		// EarlyCheckout first
		// --revision. both should remain in all cases.

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();

		User nonOverTardyFirst = Users.createStudent(uc, "student1",
				"123456780", "First", "last", 2, "major", User.Section.AltoSax);
		User nonOverEarlyFirst = Users.createStudent(uc, "student2",
				"123456789", "First", "last", 2, "major", User.Section.AltoSax);

		User overlapTardyFirst = Users.createStudent(uc, "student3",
				"123456781", "First", "last", 2, "major", User.Section.AltoSax);
		User overlapEarlyFirst = Users.createStudent(uc, "student4",
				"123456782", "First", "last", 2, "major", User.Section.AltoSax);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime eventStart = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime eventEnd = eventStart.plusHours(2);

		DateTime tardyDate = eventStart.plusMinutes(15);
		DateTime earlyNon = eventEnd.minusMinutes(10);
		DateTime earlyOverlap = eventStart.plusMinutes(10);

		ec.createOrUpdate(Event.Type.Performance, new Interval(eventStart,
				eventEnd));
		// Case 1.a
		Absence t = ac.createOrUpdateTardy(nonOverTardyFirst, tardyDate);
		t.setStatus(Absence.Status.Approved);
		ac.updateAbsence(t);
		ac.createOrUpdateEarlyCheckout(nonOverTardyFirst, earlyNon);

		List<Absence> nonOverTardyFirstAbsences = ac.get(nonOverTardyFirst);

		assertEquals(2, nonOverTardyFirstAbsences.size());
		Absence tardy = (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ? nonOverTardyFirstAbsences
				.get(0) : nonOverTardyFirstAbsences.get(1);
		Absence earlyCheckOut = (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? nonOverTardyFirstAbsences
				.get(0) : nonOverTardyFirstAbsences.get(1);

		assertTrue(tardy.getType() == Absence.Type.Tardy);
		assertTrue(tardy.getStatus() == Absence.Status.Approved);
		assertTrue(tardy.getCheckin(zone).equals(tardyDate));

		assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(earlyCheckOut.getStatus() == Absence.Status.Pending);
		assertTrue(earlyCheckOut.getCheckout(zone).equals(earlyNon));

		// Case 1.b
		ac.createOrUpdateEarlyCheckout(nonOverEarlyFirst, earlyNon);
		t = ac.createOrUpdateTardy(nonOverEarlyFirst, tardyDate);
		t.setStatus(Absence.Status.Approved);
		ac.updateAbsence(t);

		List<Absence> nonOverEarlyFirstAbsences = train.absences().get(
				nonOverEarlyFirst);

		assertEquals(2, nonOverEarlyFirstAbsences.size());
		tardy = (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ? nonOverEarlyFirstAbsences
				.get(0) : nonOverEarlyFirstAbsences.get(1);
		earlyCheckOut = (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ? nonOverEarlyFirstAbsences
				.get(0) : nonOverEarlyFirstAbsences.get(1);

		assertTrue(tardy.getType() == Absence.Type.Tardy);
		assertTrue(tardy.getStatus() == Absence.Status.Approved);
		assertTrue(tardy.getCheckin(zone).equals(tardyDate));

		assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
		assertTrue(earlyCheckOut.getStatus() == Absence.Status.Pending);
		assertTrue(earlyCheckOut.getCheckout(zone).equals(earlyNon));

		// Case 2.a 
		t = ac.createOrUpdateTardy(overlapTardyFirst, tardyDate);
		t.setStatus(Absence.Status.Approved);
		ac.updateAbsence(t);

		ac.createOrUpdateEarlyCheckout(overlapTardyFirst, earlyOverlap);

		List<Absence> overlapTardyFirstAbsences = train.absences().get(
				overlapTardyFirst);

		assertEquals(2, overlapTardyFirstAbsences.size());
		overlapTardyFirstAbsences.get(0);

		// Case 2.b
		ac.createOrUpdateEarlyCheckout(overlapEarlyFirst, earlyOverlap);
		ac.createOrUpdateTardy(overlapEarlyFirst, tardyDate);

		List<Absence> overlapEarlyFirstAbsences = train.absences().get(
				overlapEarlyFirst);

		assertEquals(2, overlapEarlyFirstAbsences.size());
	}

	@Test
	public void testApprovedEarlyVersusEarly() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		User student = Users.createDefaultStudent(uc);

		DateTimeZone zone = train.appData().get().getTimeZone();
		DateTime start = new DateTime(2012, 7, 16, 5, 0, 0, 0, zone);
		DateTime end = start.plusHours(1);
		DateTime checkout = end.minusMinutes(10);

		// Approved saved first
		train.events().createOrUpdate(Event.Type.Performance,
				new Interval(start, end));
		Absence abs = train.absences().createOrUpdateEarlyCheckout(student,
				checkout);
		abs.setStatus(Absence.Status.Approved);
		train.absences().updateAbsence(abs);
		train.absences().createOrUpdateEarlyCheckout(student, checkout);

		List<Absence> studentAbs = train.absences().get(student);
		assertEquals(1, studentAbs.size());
		assertEquals(Absence.Status.Approved, studentAbs.get(0).getStatus());
	}

	/**
	 * Add a form A, approve it, add an absence, check that it is approved.
	 */
	@Test
	public void testAutoApproveWithFormA() {
		DataTrain train = getDataTrain();
		DateTimeZone zone = train.appData().get().getTimeZone();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = Users.createDefaultStudent(uc);

		LocalDate date = new LocalDate(2012, 7, 7);

		DateTime start = new DateTime(2012, 7, 7, 16, 30, 0, zone);
		DateTime end = new DateTime(2012, 7, 7, 17, 50, 0, zone);
		Interval eventInterval = new Interval(start, end);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");
		form.setStatus(Form.Status.Approved);
		fc.update(form);

		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
		Absence a = ac.createOrUpdateAbsence(student, e);
		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	@Test
	public void testGettingUnanchored() {
		DataTrain train = getDataTrain();
		DateTimeZone zone = train.appData().get().getTimeZone();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();

		List<Absence> unanchored = new ArrayList<Absence>();

		User student1 = Users.createStudent(uc, "student1", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456788",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student3 = Users.createStudent(uc, "student3", "123456787",
				"First", "last", 2, "major", User.Section.AltoSax);

		DateTime unanchoredStart = new DateTime(2012, 10, 21, 6, 0, 0, zone);
		DateTime unanchoredEnd = unanchoredStart.plusHours(1);

		DateTime anchoredStart = unanchoredStart.plusDays(1);
		DateTime anchoredEnd = anchoredStart.plusHours(1);
		unanchored.add(ac.createOrUpdateAbsence(student1, new Interval(
				unanchoredStart, unanchoredEnd)));
		unanchored.add(ac.createOrUpdateTardy(student2, unanchoredStart));
		unanchored.add(ac
				.createOrUpdateEarlyCheckout(student3, unanchoredStart));

		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(anchoredStart,
				anchoredEnd));
		ac.createOrUpdateAbsence(student1, new Interval(anchoredStart,
				anchoredEnd));
		ac.createOrUpdateTardy(student2, anchoredStart);
		ac.createOrUpdateEarlyCheckout(student3, anchoredStart);

		List<Absence> allUnanchored = ac.getUnanchored();
		assertEquals(unanchored.size(), allUnanchored.size());

		boolean absenceWorked = false;
		boolean tardyWorked = false;
		boolean earlyWorked = false;

		for (Absence known : unanchored) {
			for (Absence toCheck : allUnanchored) {
				if (known.equals(toCheck)) {
					if (known.getType() == Absence.Type.Absence) {
						absenceWorked = true;
					} else if (known.getType() == Absence.Type.Tardy) {
						tardyWorked = true;
					} else if (known.getType() == Absence.Type.EarlyCheckOut) {
						earlyWorked = true;
					}
				}
			}
		}

		assertTrue(absenceWorked);
		assertTrue(tardyWorked);
		assertTrue(earlyWorked);

		List<Absence> allAbs = ac.getAll();

		assertEquals(6, allAbs.size());
		int numAnchored = 0;
		int numUnanchored = 0;
		for (Absence e : allAbs) {
			if (e.getEvent() == null) {
				++numUnanchored;
			} else {
				++numAnchored;
			}
		}
		assertEquals(3, numUnanchored);
		assertEquals(3, numAnchored);
	}

}
