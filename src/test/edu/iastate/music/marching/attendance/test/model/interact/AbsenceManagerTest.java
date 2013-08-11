package edu.iastate.music.marching.attendance.test.model.interact;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
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
import edu.iastate.music.marching.attendance.testlib.TestUsers;

@SuppressWarnings("deprecation")
public class AbsenceManagerTest extends AbstractDatastoreTest {

//	@Test
//	public void removeListOfAbsencesAndTardy() {
//
//		// Arrange
//		DataTrain train = getDataTrain();
//		UserManager uc = train.getUsersManager();
//
//		User student = Users.createSingleTestStudent(uc);
//
//		DateTime start1 = makeDate("2012-06-16 0500");
//		DateTime end1 = makeDate("2012-06-16 0600");
//		DateTime start2 = makeDate("2012-06-17 0500");
//		DateTime end2 = makeDate("2012-06-17 0600");
//		DateTime start3 = makeDate("2012-06-18 0500");
//		DateTime end3 = makeDate("2012-06-18 0600");
//		DateTime tardyStart = makeDate("2012-06-18 0515");
//
//		Event event1 = train.getEventManager().createOrUpdate(
//				Event.Type.Performance, start1, end1);
//		Event event2 = train.getEventManager().createOrUpdate(
//				Event.Type.Performance, start2, end2);
//		train.getEventManager().createOrUpdate(Event.Type.Performance, start3,
//				end3);
//
//		Absence abs1 = train.getAbsenceManager().createOrUpdateAbsence(student,
//				event1);
//		Absence abs2 = train.getAbsenceManager().createOrUpdateAbsence(student,
//				event2);
//		Absence tardy = train.getAbsenceManager().createOrUpdateTardy(student,
//				tardyStart);
//
//		// Act
//		train.getAbsenceManager().remove(Arrays.asList(abs1, abs2, tardy));
//
//		// Assert
//		assertEquals(0, train.getAbsenceManager().getCount().intValue());
//	}
//
//	/**
//	 * Test to see if updating an absence to be during an event will link the
//	 * absence to it
//	 */
//	@Test
//	public void autoLinkAbsenceOnUpdate_Succeeds() {
//		DataTrain train = getDataTrain();
//
//		UserManager uc = train.getUsersManager();
//		User student = Users.createStudent(uc, "studenttt", "123456789",
//				"First", "last", 2, "major", User.Section.AltoSax);
//
//		DateTime eventStart = makeDate("2012-06-16 0400");
//		DateTime eventEnd = makeDate("2012-06-16 0600");
//
//		DateTime absenceStart = makeDate("2012-06-15 0400");
//		DateTime absenceEnd = makeDate("2012-06-15 0600");
//
//		Event event = train.events().createOrUpdate(Event.Type.Performance,
//				eventStart, eventEnd);
//
//		Absence absence = train.absences().createOrUpdateAbsence(student,
//				absenceStart, absenceEnd);
//
//		// upDateTime absence to be during event
//		absence.setStart(eventStart);
//		absence.setEnd(eventEnd);
//		train.absences().updateAbsence(absence);
//
//		assertEquals(event, absence.getEvent());
//	}

	/**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */
	/*
	 * @Test public void autoLinkAbsenceOnUpdate_Fails() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0400"); DateTime eventEnd =
	 * makeDate("2012-06-16 0600");
	 * 
	 * DateTime absenceStart = makeDate("2012-06-15 0400"); DateTime absenceEnd
	 * = makeDate("2012-06-15 0600");
	 * 
	 * DateTime absenceStart2 = makeDate("2012-06-16 0200"); DateTime
	 * absenceEnd2 = makeDate("2012-06-16 0400");
	 * 
	 * Event event = train.getEventManager().createOrUpdate(
	 * Event.Type.Performance, eventStart, eventEnd);
	 * 
	 * Absence absence = train.getAbsenceManager().createOrUpdateAbsence(
	 * student, absenceStart, absenceEnd);
	 * 
	 * // upDateTime absence to be during event absence.setStart(absenceStart2);
	 * absence.setEnd(absenceEnd2);
	 * train.getAbsenceManager().updateAbsence(absence);
	 * 
	 * assertNotSame(event, absence.getEvent()); }
	 *//**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */
	/*
	 * @Test public void autoLinkTardyOnUpdate_Succeeds() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0400"); DateTime eventEnd =
	 * makeDate("2012-06-16 0600");
	 * 
	 * DateTime absenceTime1 = makeDate("2012-06-15 0400"); DateTime
	 * absenceTime2 = makeDate("2012-06-16 0500");
	 * 
	 * Event event = train.getEventManager().createOrUpdate(
	 * Event.Type.Performance, eventStart, eventEnd);
	 * 
	 * Absence absence = train.getAbsenceManager().createOrUpdateTardy( student,
	 * absenceTime1);
	 * 
	 * // upDateTime absence to be during event
	 * absence.setDatetime(absenceTime2);
	 * train.getAbsenceManager().updateAbsence(absence);
	 * 
	 * assertEquals(event, absence.getEvent()); }
	 *//**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */
	/*
	 * @Test public void autoLinkTardyOnUpdate_Fails() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0400"); DateTime eventEnd =
	 * makeDate("2012-06-16 0600");
	 * 
	 * DateTime absenceTime1 = makeDate("2012-06-15 0400"); DateTime
	 * absenceTime2 = makeDate("2012-06-14 0500");
	 * 
	 * Event event = train.getEventManager().createOrUpdate(
	 * Event.Type.Performance, eventStart, eventEnd);
	 * 
	 * Absence absence = train.getAbsenceManager().createOrUpdateTardy( student,
	 * absenceTime1);
	 * 
	 * // upDateTime absence to be during event
	 * absence.setDatetime(absenceTime2);
	 * train.getAbsenceManager().updateAbsence(absence);
	 * 
	 * assertNotSame(event, absence.getEvent()); }
	 *//**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */
	/*
	 * @Test public void autoLinkECOOnUpdate_Succeeds() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0400"); DateTime eventEnd =
	 * makeDate("2012-06-16 0600");
	 * 
	 * DateTime absenceTime1 = makeDate("2012-06-15 0400"); DateTime
	 * absenceTime2 = makeDate("2012-06-16 0500");
	 * 
	 * Event event = train.getEventManager().createOrUpdate(
	 * Event.Type.Performance, eventStart, eventEnd);
	 * 
	 * Absence absence = train.getAbsenceManager()
	 * .createOrUpdateEarlyCheckout(student, absenceTime1);
	 * 
	 * // upDateTime absence to be during event
	 * absence.setDatetime(absenceTime2);
	 * train.getAbsenceManager().updateAbsence(absence);
	 * 
	 * assertEquals(event, absence.getEvent()); }
	 *//**
	 * Test to see if updating an absence to be during an event will link the
	 * absence to it
	 */
	/*
	 * @Test public void autoLinkECOOnUpdate_Fails() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0400"); DateTime eventEnd =
	 * makeDate("2012-06-16 0600");
	 * 
	 * DateTime absenceTime1 = makeDate("2012-06-15 0400"); DateTime
	 * absenceTime2 = makeDate("2012-06-14 0500");
	 * 
	 * Event event = train.getEventManager().createOrUpdate(
	 * Event.Type.Performance, eventStart, eventEnd);
	 * 
	 * Absence absence = train.getAbsenceManager()
	 * .createOrUpdateEarlyCheckout(student, absenceTime1);
	 * 
	 * // upDateTime absence to be during event
	 * absence.setDatetime(absenceTime2);
	 * train.getAbsenceManager().updateAbsence(absence);
	 * 
	 * assertNotSame(event, absence.getEvent()); }
	 * 
	 * @Test public void testCreateAbsence() {
	 * 
	 * Test to see if adding an absence actually works
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0400"); DateTime eventEnd =
	 * makeDate("2012-06-16 0600");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd);
	 * train.getAbsenceManager().createOrUpdateAbsence(student, eventStart,
	 * eventEnd);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student);
	 * 
	 * assertEquals(1, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getStart().equals(eventStart));
	 * assertTrue(absence.getEnd().equals(eventEnd));
	 * assertTrue(absence.getType() == Absence.Type.Absence); }
	 * 
	 * @Test(expected = IllegalArgumentException.class) public void
	 * testCreateAbsenceFail() { DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0400"); DateTime eventEnd =
	 * makeDate("2012-06-16 0300");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd);
	 * train.getAbsenceManager().createOrUpdateAbsence(student, eventStart,
	 * eventEnd); }
	 * 
	 * @Test public void testAbsenceVsAbsenceSameTime() {
	 * 
	 * When we have two absences at the same time only one of them should be
	 * added
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime contesterStart = makeDate("2012-06-16 0500"); DateTime
	 * contesterEnd = makeDate("2012-06-16 0700");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd);
	 * 
	 * train.getAbsenceManager().createOrUpdateAbsence(student, eventStart,
	 * eventEnd); train.getAbsenceManager().createOrUpdateAbsence(student,
	 * contesterStart, contesterEnd);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student);
	 * 
	 * assertEquals(1, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getStart().equals(eventStart));
	 * assertTrue(absence.getEnd().equals(eventEnd));
	 * assertTrue(absence.getType() == Absence.Type.Absence); }
	 * 
	 * @Test public void testAbsenceVsAbsenceDiffTime() {
	 * 
	 * When we have two absences at different times for different events they
	 * should both remain
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime contesterStart = makeDate("2012-06-16 0800"); DateTime
	 * contesterEnd = makeDate("2012-06-16 0900");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd);
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * contesterStart, contesterEnd);
	 * 
	 * train.getAbsenceManager().createOrUpdateAbsence(student, eventStart,
	 * eventEnd); train.getAbsenceManager().createOrUpdateAbsence(student,
	 * contesterStart, contesterEnd);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getStart().equals(eventStart) ||
	 * absence.getStart().equals(contesterStart));
	 * assertTrue(absence.getEnd().equals(eventEnd) ||
	 * absence.getEnd().equals(contesterEnd)); assertTrue(absence.getType() ==
	 * Absence.Type.Absence);
	 * 
	 * absence = studentAbsences.get(1);
	 * 
	 * assertTrue(absence.getStart().equals(eventStart) ||
	 * absence.getStart().equals(contesterStart));
	 * assertTrue(absence.getEnd().equals(eventEnd) ||
	 * absence.getEnd().equals(contesterEnd)); assertTrue(absence.getType() ==
	 * Absence.Type.Absence); }
	 * 
	 * @Test public void testAbsenceVsTardySameEvent() {
	 * 
	 * If we have an absence and a tardy added where the tardy happens during
	 * the absence, the absence should be removed and the tardy should remain.
	 * This test adds the absence first and the tardy second
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700"); DateTime tardy =
	 * makeDate("2012-06-16 0515");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd);
	 * 
	 * train.getAbsenceManager().createOrUpdateAbsence(student, eventStart,
	 * eventEnd); train.getAbsenceManager().createOrUpdateTardy(student, tardy);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student);
	 * 
	 * assertEquals(1, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getStart().equals(tardy));
	 * assertTrue(absence.getType() == Absence.Type.Tardy); }
	 * 
	 * @Test public void testAbsenceVsTardyDiffEvent() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime event1Start = makeDate("2012-06-16 0500"); DateTime event1End =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime event2Start = makeDate("2012-07-16 0500"); DateTime event2End =
	 * makeDate("2012-07-16 0700");
	 * 
	 * DateTime tardyDateTime = makeDate("2012-07-16 0515");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * event1Start, event1End);
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * event2Start, event2End);
	 * 
	 * train.getAbsenceManager().createOrUpdateAbsence(student, event1Start,
	 * event1End); train.getAbsenceManager().createOrUpdateTardy(student,
	 * tardyDate);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence =
	 * (studentAbsences.get(0).getType() == Absence.Type.Absence) ?
	 * studentAbsences .get(0) : studentAbsences.get(1); Absence tardy =
	 * (studentAbsences.get(0).getType() == Absence.Type.Tardy) ?
	 * studentAbsences .get(0) : studentAbsences.get(1);
	 * 
	 * assertTrue(absence.getStart().equals(event1Start));
	 * assertTrue(absence.getEnd().equals(event1End));
	 * 
	 * assertTrue(tardy.getStart().equals(tardyDate)); }
	 * 
	 * @Test public void testAbsenceVsEarlySameEvent() {
	 * 
	 * If we have an absence and an early checkout added during the same
	 * interval, they should both remain
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700"); DateTime early =
	 * makeDate("2012-06-16 0515");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd);
	 * 
	 * train.getAbsenceManager().createOrUpdateAbsence(student, eventStart,
	 * eventEnd); train.getAbsenceManager().createOrUpdateEarlyCheckout(student,
	 * early);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getType() == Absence.Type.Absence); }
	 * 
	 * @Test public void testAbsenceVsEarlyDiffEvent() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime event1Start = makeDate("2012-06-16 0500"); DateTime event1End =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime event2Start = makeDate("2012-07-16 0500"); DateTime event2End =
	 * makeDate("2012-07-16 0700");
	 * 
	 * DateTime earlyDateTime = makeDate("2012-07-16 0515");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * event1Start, event1End);
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * event2Start, event2End);
	 * 
	 * train.getAbsenceManager().createOrUpdateAbsence(student, event1Start,
	 * event1End);
	 * train.getAbsenceManager().createOrUpdateEarlyCheckout(student,
	 * earlyDate);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence =
	 * (studentAbsences.get(0).getType() == Absence.Type.Absence) ?
	 * studentAbsences .get(0) : studentAbsences.get(1); Absence early =
	 * (studentAbsences.get(0).getType() == Absence.Type.EarlyCheckOut) ?
	 * studentAbsences .get(0) : studentAbsences.get(1);
	 * 
	 * assertTrue(absence.getStart().equals(event1Start));
	 * assertTrue(absence.getEnd().equals(event1End));
	 * 
	 * assertTrue(early.getStart().equals(earlyDate));
	 * 
	 * }
	 * 
	 * @Test public void testTardyVsAbsence() {
	 * 
	 * This test is the same idea as the testAbsenceVsTardy method only it adds
	 * the tardy first
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700"); DateTime tardy =
	 * makeDate("2012-06-16 0515");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd);
	 * 
	 * train.getAbsenceManager().createOrUpdateTardy(student, tardy);
	 * train.getAbsenceManager().createOrUpdateAbsence(student, eventStart,
	 * eventEnd);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student);
	 * 
	 * assertEquals(1, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getStart().equals(tardy));
	 * assertTrue(absence.getType() == Absence.Type.Tardy); }
	 * 
	 * @Test public void testTardyVsTardySameEvent() {
	 * 
	 * If we have two tardies added for the same event they should both remain
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student1 =
	 * Users.createStudent(uc, "student1", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax); User student2 = Users.createStudent(uc,
	 * "student2", "123456782", "First", "last", 2, "major",
	 * User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime tardy = makeDate("2012-06-16 0515"); DateTime tardyLate =
	 * makeDate("2012-06-16 0520");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd); // Adding the early tardy before the late tardy
	 * train.getAbsenceManager().createOrUpdateTardy(student1, tardy);
	 * train.getAbsenceManager().createOrUpdateTardy(student1, tardyLate);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student1);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getType() == Absence.Type.Tardy);
	 * 
	 * // Adding the late tardy before the early tardy
	 * train.getAbsenceManager().createOrUpdateTardy(student2, tardyLate);
	 * train.getAbsenceManager().createOrUpdateTardy(student2, tardy);
	 * 
	 * List<Absence> student2Absences = train.getAbsenceManager()
	 * .get(student2);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence2 =
	 * student2Absences.get(0);
	 * 
	 * assertTrue(absence2.getType() == Absence.Type.Tardy); }
	 * 
	 * @Test public void testTardyVsTardyDiffEvent() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student1 =
	 * Users.createStudent(uc, "student1", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime event1Start = makeDate("2012-06-16 0500"); DateTime event1End =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime event2Start = makeDate("2012-06-16 0800"); DateTime event2End =
	 * makeDate("2012-06-16 0900");
	 * 
	 * DateTime tardy = makeDate("2012-06-16 0515"); DateTime otherTardy =
	 * makeDate("2012-06-16 0820");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * event1Start, event1End);
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * event2Start, event2End);
	 * 
	 * train.getAbsenceManager().createOrUpdateTardy(student1, tardy);
	 * train.getAbsenceManager().createOrUpdateTardy(student1, otherTardy);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student1);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getStart().equals(tardy) ||
	 * absence.getStart().equals(otherTardy)); assertTrue(absence.getType() ==
	 * Absence.Type.Tardy);
	 * 
	 * absence = studentAbsences.get(1);
	 * 
	 * assertTrue(absence.getStart().equals(tardy) ||
	 * absence.getStart().equals(otherTardy)); assertTrue(absence.getType() ==
	 * Absence.Type.Tardy); }
	 * 
	 * @Test public void testTardyVsEarly() {
	 * 
	 * This method tests these four cases: Case 1: don't overlap, both should
	 * remain. a: Adding Tardy first b: Adding EarlyCheckout first
	 * 
	 * Case 2: do overlap, become an absence a: Adding Tardy first b: Adding
	 * EarlyCheckout first
	 * 
	 * Both should remain in all cases.
	 * 
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User nonOverTardyFirst =
	 * Users.createStudent(uc, "student1", "123456780", "First", "last", 2,
	 * "major", User.Section.AltoSax); User nonOverEarlyFirst =
	 * Users.createStudent(uc, "student2", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * User overlapTardyFirst = Users.createStudent(uc, "student3", "123456781",
	 * "First", "last", 2, "major", User.Section.AltoSax); User
	 * overlapEarlyFirst = Users.createStudent(uc, "student4", "123456782",
	 * "First", "last", 2, "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime tardyDateTime = makeDate("2012-06-16 0515"); DateTime earlyNon =
	 * makeDate("2012-06-16 0650"); DateTime earlyOverlap =
	 * makeDate("2012-06-16 0510");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd); // Case 1.a
	 * train.getAbsenceManager().createOrUpdateTardy(nonOverTardyFirst,
	 * tardyDate); train.getAbsenceManager().createOrUpdateEarlyCheckout(
	 * nonOverTardyFirst, earlyNon);
	 * 
	 * List<Absence> nonOverTardyFirstAbsences = train.getAbsenceManager()
	 * .get(nonOverTardyFirst);
	 * 
	 * assertEquals(2, nonOverTardyFirstAbsences.size()); Absence tardy =
	 * (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ?
	 * nonOverTardyFirstAbsences .get(0) : nonOverTardyFirstAbsences.get(1);
	 * Absence earlyCheckOut = (nonOverTardyFirstAbsences.get(0).getType() ==
	 * Absence.Type.EarlyCheckOut) ? nonOverTardyFirstAbsences .get(0) :
	 * nonOverTardyFirstAbsences.get(1);
	 * 
	 * assertTrue(tardy.getType() == Absence.Type.Tardy);
	 * assertTrue(tardy.getStart().equals(tardyDate));
	 * 
	 * assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
	 * assertTrue(earlyCheckOut.getStart().equals(earlyNon));
	 * 
	 * // Case 1.b train.getAbsenceManager().createOrUpdateEarlyCheckout(
	 * nonOverEarlyFirst, earlyNon);
	 * train.getAbsenceManager().createOrUpdateTardy(nonOverEarlyFirst,
	 * tardyDate);
	 * 
	 * List<Absence> nonOverEarlyFirstAbsences = train.getAbsenceManager()
	 * .get(nonOverEarlyFirst);
	 * 
	 * assertEquals(2, nonOverEarlyFirstAbsences.size()); tardy =
	 * (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ?
	 * nonOverEarlyFirstAbsences .get(0) : nonOverEarlyFirstAbsences.get(1);
	 * earlyCheckOut = (nonOverEarlyFirstAbsences.get(0).getType() ==
	 * Absence.Type.EarlyCheckOut) ? nonOverEarlyFirstAbsences .get(0) :
	 * nonOverEarlyFirstAbsences.get(1);
	 * 
	 * assertTrue(tardy.getType() == Absence.Type.Tardy);
	 * assertTrue(tardy.getStart().equals(tardyDate));
	 * 
	 * assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
	 * assertTrue(earlyCheckOut.getStart().equals(earlyNon));
	 * 
	 * // Case 2.a
	 * train.getAbsenceManager().createOrUpdateTardy(overlapTardyFirst,
	 * tardyDate); train.getAbsenceManager().createOrUpdateEarlyCheckout(
	 * overlapTardyFirst, earlyOverlap);
	 * 
	 * List<Absence> overlapTardyFirstAbsences = train.getAbsenceManager()
	 * .get(overlapTardyFirst);
	 * 
	 * assertEquals(2, overlapTardyFirstAbsences.size()); Absence createdAbsence
	 * = overlapTardyFirstAbsences.get(0);
	 * 
	 * // Case 2.b train.getAbsenceManager().createOrUpdateEarlyCheckout(
	 * overlapEarlyFirst, earlyOverlap);
	 * train.getAbsenceManager().createOrUpdateTardy(overlapEarlyFirst,
	 * tardyDate);
	 * 
	 * List<Absence> overlapEarlyFirstAbsences = train.getAbsenceManager()
	 * .get(overlapEarlyFirst);
	 * 
	 * assertEquals(2, overlapEarlyFirstAbsences.size()); createdAbsence =
	 * overlapEarlyFirstAbsences.get(0);
	 * 
	 * }
	 * 
	 * @Test public void testEarlyVsAbsence() {
	 * 
	 * Same as the testAbsenceVsEarly method only this adds the Early checkout
	 * before the absence
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "studenttt", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700"); DateTime early =
	 * makeDate("2012-06-16 0515");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd);
	 * 
	 * train.getAbsenceManager().createOrUpdateEarlyCheckout(student, early);
	 * train.getAbsenceManager().createOrUpdateAbsence(student, eventStart,
	 * eventEnd);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0); }
	 * 
	 * @Test public void testEarlyVsEarlySameEvent() {
	 * 
	 * If we have two early checkouts added for the same time zone we need to
	 * make sure that both remain.
	 * 
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student1 =
	 * Users.createStudent(uc, "student1", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax); User student2 = Users.createStudent(uc,
	 * "student2", "123456780", "First", "last", 2, "major",
	 * User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime early = makeDate("2012-06-16 0550"); DateTime earlyEarlier =
	 * makeDate("2012-06-16 0545");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * eventStart, eventEnd); // Adding the early checkout before the earlier
	 * checkout train.getAbsenceManager().createOrUpdateEarlyCheckout(student1,
	 * early); train.getAbsenceManager().createOrUpdateEarlyCheckout(student1,
	 * earlyEarlier);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student1);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getStart().equals(early));
	 * assertTrue(absence.getType() == Absence.Type.EarlyCheckOut);
	 * 
	 * // Adding the earlier early before the early
	 * train.getAbsenceManager().createOrUpdateEarlyCheckout(student2,
	 * earlyEarlier);
	 * train.getAbsenceManager().createOrUpdateEarlyCheckout(student2, early);
	 * 
	 * List<Absence> student2Absences = train.getAbsenceManager()
	 * .get(student2);
	 * 
	 * assertEquals(2, studentAbsences.size()); Absence absence2 =
	 * student2Absences.get(0);
	 * 
	 * assertTrue(absence2.getStart().equals(earlyEarlier));
	 * assertTrue(absence2.getType() == Absence.Type.EarlyCheckOut); }
	 * 
	 * @Test public void testEarlyVsEarlyDiffEvent() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student1 =
	 * Users.createStudent(uc, "student1", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime event1Start = makeDate("2012-06-16 0500"); DateTime event1End =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime event2Start = makeDate("2012-06-16 0800"); DateTime event2End =
	 * makeDate("2012-06-16 0900");
	 * 
	 * DateTime early = makeDate("2012-06-16 0550"); DateTime otherEarly =
	 * makeDate("2012-06-16 0845");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * event1Start, event1End);
	 * train.getEventManager().createOrUpdate(Event.Type.Performance,
	 * event2Start, event2End);
	 * 
	 * train.getAbsenceManager().createOrUpdateEarlyCheckout(student1, early);
	 * train.getAbsenceManager().createOrUpdateEarlyCheckout(student1,
	 * otherEarly);
	 * 
	 * List<Absence> studentAbsences = train.getAbsenceManager().get(student1);
	 * 
	 * assertEquals(2, studentAbsences.size());
	 * 
	 * Absence absence = studentAbsences.get(0);
	 * assertTrue(absence.getStart().equals(early) ||
	 * absence.getStart().equals(otherEarly)); assertTrue(absence.getType() ==
	 * Absence.Type.EarlyCheckOut);
	 * 
	 * absence = studentAbsences.get(0);
	 * assertTrue(absence.getStart().equals(early) ||
	 * absence.getStart().equals(otherEarly)); assertTrue(absence.getType() ==
	 * Absence.Type.EarlyCheckOut); }
	 * 
	 * @Test public void testApproveAbsence() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "student1", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime start = makeDate("2012-06-16 0500"); DateTime end =
	 * makeDate("2012-06-16 0700");
	 * 
	 * train.getEventManager().createOrUpdate(Event.Type.Performance, start,
	 * end); Absence abs =
	 * train.getAbsenceManager().createOrUpdateAbsence(student, start, end);
	 * abs.setStatus(Absence.Status.Approved);
	 * train.getAbsenceManager().updateAbsence(abs);
	 * 
	 * List<Absence> studentAbs = train.getAbsenceManager().get(student);
	 * assertEquals(1, studentAbs.size());
	 * 
	 * Absence absence = studentAbs.get(0);
	 * assertTrue(absence.getStart().equals(start));
	 * assertTrue(absence.getEnd().equals(end)); assertTrue(absence.getType() ==
	 * Absence.Type.Absence); assertTrue(absence.getStatus() ==
	 * Absence.Status.Approved); }
	 * 
	 * @Test public void testApprovedAbsenceDominatesAbsence() { DataTrain train
	 * = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "student", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime start = makeDate("2012-06-16 0500"); DateTime end =
	 * makeDate("2012-06-16 0600");
	 * 
	 * // Approved saved first
	 * train.getEventManager().createOrUpdate(Event.Type.Performance, start,
	 * end); Absence abs =
	 * train.getAbsenceManager().createOrUpdateAbsence(student, start, end);
	 * abs.setStatus(Absence.Status.Approved);
	 * train.getAbsenceManager().updateAbsence(abs);
	 * 
	 * train.getAbsenceManager().createOrUpdateAbsence(student, start, end);
	 * 
	 * List<Absence> studentAbs = train.getAbsenceManager().get(student);
	 * assertEquals(1, studentAbs.size());
	 * 
	 * Absence absence = studentAbs.get(0);
	 * assertTrue(absence.getStart().equals(start));
	 * assertTrue(absence.getEnd().equals(end)); assertTrue(absence.getType() ==
	 * Absence.Type.Absence); assertTrue(absence.getStatus() ==
	 * Absence.Status.Approved); }
	 * 
	 * @Test public void testTardyDominatesApprovedAbsence() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "student", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime start = makeDate("2012-06-16 0500"); DateTime tardyStart =
	 * makeDate("2012-06-16 0515"); DateTime end = makeDate("2012-06-16 0600");
	 * 
	 * // Approved saved first
	 * train.getEventManager().createOrUpdate(Event.Type.Performance, start,
	 * end); Absence abs =
	 * train.getAbsenceManager().createOrUpdateAbsence(student, start, end);
	 * abs.setStatus(Absence.Status.Approved);
	 * train.getAbsenceManager().updateAbsence(abs);
	 * 
	 * train.getAbsenceManager().createOrUpdateTardy(student, tardyStart);
	 * 
	 * List<Absence> studentAbs = train.getAbsenceManager().get(student);
	 * assertEquals(1, studentAbs.size());
	 * 
	 * Absence absence = studentAbs.get(0);
	 * assertTrue(absence.getStart().equals(tardyStart));
	 * assertTrue(absence.getType() == Absence.Type.Tardy);
	 * assertTrue(absence.getStatus() == Absence.Status.Pending); }
	 * 
	 * @Test public void testApprovedAbsenceVersusEarly() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "student", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime start = makeDate("2012-06-16 0500"); DateTime tardyStart =
	 * makeDate("2012-06-16 0515"); DateTime end = makeDate("2012-06-16 0600");
	 * 
	 * // Approved saved first
	 * train.getEventManager().createOrUpdate(Event.Type.Performance, start,
	 * end); Absence abs =
	 * train.getAbsenceManager().createOrUpdateAbsence(student, start, end);
	 * abs.setStatus(Absence.Status.Approved);
	 * train.getAbsenceManager().updateAbsence(abs);
	 * 
	 * train.getAbsenceManager().createOrUpdateEarlyCheckout(student,
	 * tardyStart);
	 * 
	 * List<Absence> studentAbs = train.getAbsenceManager().get(student);
	 * assertEquals(2, studentAbs.size());
	 * 
	 * Absence absence = studentAbs.get(0); }
	 * 
	 * @Test public void testApprovedTardyVsAbsence() {
	 * 
	 * This test is the same idea as the testAbsenceVsTardy method only it adds
	 * the tardy first
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); EventManager ec =
	 * train.getEventManager(); AbsenceManager ac = train.getAbsenceManager();
	 * 
	 * User student = Users.createStudent(uc, "studenttt", "123456789", "First",
	 * "last", 2, "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700"); DateTime tardyDateTime =
	 * makeDate("2012-06-16 0515");
	 * 
	 * ec.createOrUpdate(Event.Type.Performance, eventStart, eventEnd);
	 * 
	 * Absence tardy = ac.createOrUpdateTardy(student, tardyDate);
	 * tardy.setStatus(Absence.Status.Approved); ac.updateAbsence(tardy);
	 * 
	 * ac.createOrUpdateAbsence(student, eventStart, eventEnd);
	 * 
	 * List<Absence> studentAbsences = ac.get(student);
	 * 
	 * assertEquals(1, studentAbsences.size()); Absence absence =
	 * studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getStart().equals(tardyDate));
	 * assertTrue(absence.getType() == Absence.Type.Tardy);
	 * assertTrue(absence.getStatus() == Absence.Status.Approved); }
	 * 
	 * @Test public void testApprovedTardyDominatesTardySameTime() { DataTrain
	 * train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "student", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax); User student1 = Users.createStudent(uc,
	 * "student1", "123456780", "First", "last", 2, "major",
	 * User.Section.AltoSax);
	 * 
	 * DateTime start = makeDate("2012-06-16 0500"); DateTime end =
	 * makeDate("2012-06-16 0600");
	 * 
	 * // Approved saved first
	 * train.getEventManager().createOrUpdate(Event.Type.Performance, start,
	 * end); Absence abs =
	 * train.getAbsenceManager().createOrUpdateTardy(student, start);
	 * abs.setStatus(Absence.Status.Approved);
	 * train.getAbsenceManager().updateAbsence(abs);
	 * 
	 * train.getAbsenceManager().createOrUpdateTardy(student, start);
	 * 
	 * List<Absence> studentAbs = train.getAbsenceManager().get(student);
	 * assertEquals(1, studentAbs.size());
	 * 
	 * Absence absence = studentAbs.get(0);
	 * assertTrue(absence.getStart().equals(start));
	 * assertTrue(absence.getType() == Absence.Type.Tardy);
	 * assertTrue(absence.getStatus() == Absence.Status.Approved);
	 * 
	 * // Approved saved second
	 * train.getAbsenceManager().createOrUpdateTardy(student1, start); abs =
	 * train.getAbsenceManager().createOrUpdateTardy(student1, start);
	 * abs.setStatus(Absence.Status.Approved);
	 * train.getAbsenceManager().updateAbsence(abs);
	 * 
	 * studentAbs = train.getAbsenceManager().get(student1); assertEquals(1,
	 * studentAbs.size());
	 * 
	 * absence = studentAbs.get(0);
	 * assertTrue(absence.getStart().equals(start));
	 * assertTrue(absence.getType() == Absence.Type.Tardy);
	 * assertTrue(absence.getStatus() == Absence.Status.Approved); }
	 * 
	 * @Test public void testApprovedTardyVersusTardyDiffTime() { DataTrain
	 * train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); EventManager ec =
	 * train.getEventManager(); AbsenceManager ac = train.getAbsenceManager();
	 * 
	 * User student1 = Users.createStudent(uc, "student1", "123456789", "First",
	 * "last", 2, "major", User.Section.AltoSax); User student2 =
	 * Users.createStudent(uc, "student2", "123456782", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime tardy = makeDate("2012-06-16 0515"); DateTime tardyLate =
	 * makeDate("2012-06-16 0520");
	 * 
	 * ec.createOrUpdate(Event.Type.Performance, eventStart, eventEnd);
	 * 
	 * // Approving the early tardy Absence t = ac.createOrUpdateTardy(student1,
	 * tardy); t.setStatus(Absence.Status.Approved); ac.updateAbsence(t);
	 * 
	 * ac.createOrUpdateTardy(student1, tardyLate);
	 * 
	 * List<Absence> studentAbsences = ac.get(student1);
	 * 
	 * // Should be the approved tardy values assertEquals(2,
	 * studentAbsences.size()); Absence absence = studentAbsences.get(0);
	 * 
	 * assertTrue(absence.getStart().equals(tardy));
	 * assertTrue(absence.getStatus() == Absence.Status.Approved);
	 * assertTrue(absence.getType() == Absence.Type.Tardy);
	 * 
	 * // Approving the later tardy t = ac.createOrUpdateTardy(student2,
	 * tardyLate); t.setStatus(Absence.Status.Approved); ac.updateAbsence(t);
	 * ac.createOrUpdateTardy(student2, tardy);
	 * 
	 * List<Absence> student2Absences = ac.get(student2);
	 * 
	 * // Should still be the approved values assertEquals(2,
	 * student2Absences.size()); }
	 * 
	 * @Test public void testApprovedTardyVsEarly() {
	 * 
	 * This method tests these four cases: Case 1: don't overlap, both should
	 * remain. a: Adding Tardy first b: Adding EarlyCheckout first
	 * 
	 * Case 2: do overlap, become an absence a: Adding Tardy first b: Adding
	 * EarlyCheckout first
	 * 
	 * --revision. both should remain in all cases.
	 * 
	 * 
	 * DataTrain train = getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); EventManager ec =
	 * train.getEventManager(); AbsenceManager ac = train.getAbsenceManager();
	 * 
	 * User nonOverTardyFirst = Users.createStudent(uc, "student1", "123456780",
	 * "First", "last", 2, "major", User.Section.AltoSax); User
	 * nonOverEarlyFirst = Users.createStudent(uc, "student2", "123456789",
	 * "First", "last", 2, "major", User.Section.AltoSax);
	 * 
	 * User overlapTardyFirst = Users.createStudent(uc, "student3", "123456781",
	 * "First", "last", 2, "major", User.Section.AltoSax); User
	 * overlapEarlyFirst = Users.createStudent(uc, "student4", "123456782",
	 * "First", "last", 2, "major", User.Section.AltoSax);
	 * 
	 * DateTime eventStart = makeDate("2012-06-16 0500"); DateTime eventEnd =
	 * makeDate("2012-06-16 0700");
	 * 
	 * DateTime tardyDateTime = makeDate("2012-06-16 0515"); DateTime earlyNon =
	 * makeDate("2012-06-16 0650"); DateTime earlyOverlap =
	 * makeDate("2012-06-16 0510");
	 * 
	 * ec.createOrUpdate(Event.Type.Performance, eventStart, eventEnd); // Case
	 * 1.a Absence t = ac.createOrUpdateTardy(nonOverTardyFirst, tardyDate);
	 * t.setStatus(Absence.Status.Approved); ac.updateAbsence(t);
	 * ac.createOrUpdateEarlyCheckout(nonOverTardyFirst, earlyNon);
	 * 
	 * List<Absence> nonOverTardyFirstAbsences = ac.get(nonOverTardyFirst);
	 * 
	 * assertEquals(2, nonOverTardyFirstAbsences.size()); Absence tardy =
	 * (nonOverTardyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ?
	 * nonOverTardyFirstAbsences .get(0) : nonOverTardyFirstAbsences.get(1);
	 * Absence earlyCheckOut = (nonOverTardyFirstAbsences.get(0).getType() ==
	 * Absence.Type.EarlyCheckOut) ? nonOverTardyFirstAbsences .get(0) :
	 * nonOverTardyFirstAbsences.get(1);
	 * 
	 * assertTrue(tardy.getType() == Absence.Type.Tardy);
	 * assertTrue(tardy.getStatus() == Absence.Status.Approved);
	 * assertTrue(tardy.getStart().equals(tardyDate));
	 * 
	 * assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
	 * assertTrue(earlyCheckOut.getStatus() == Absence.Status.Pending);
	 * assertTrue(earlyCheckOut.getStart().equals(earlyNon));
	 * 
	 * // Case 1.b ac.createOrUpdateEarlyCheckout(nonOverEarlyFirst, earlyNon);
	 * t = ac.createOrUpdateTardy(nonOverEarlyFirst, tardyDate);
	 * t.setStatus(Absence.Status.Approved); ac.updateAbsence(t);
	 * 
	 * List<Absence> nonOverEarlyFirstAbsences = train.getAbsenceManager()
	 * .get(nonOverEarlyFirst);
	 * 
	 * assertEquals(2, nonOverEarlyFirstAbsences.size()); tardy =
	 * (nonOverEarlyFirstAbsences.get(0).getType() == Absence.Type.Tardy) ?
	 * nonOverEarlyFirstAbsences .get(0) : nonOverEarlyFirstAbsences.get(1);
	 * earlyCheckOut = (nonOverEarlyFirstAbsences.get(0).getType() ==
	 * Absence.Type.EarlyCheckOut) ? nonOverEarlyFirstAbsences .get(0) :
	 * nonOverEarlyFirstAbsences.get(1);
	 * 
	 * assertTrue(tardy.getType() == Absence.Type.Tardy);
	 * assertTrue(tardy.getStatus() == Absence.Status.Approved);
	 * assertTrue(tardy.getStart().equals(tardyDate));
	 * 
	 * assertTrue(earlyCheckOut.getType() == Absence.Type.EarlyCheckOut);
	 * assertTrue(earlyCheckOut.getStatus() == Absence.Status.Pending);
	 * assertTrue(earlyCheckOut.getStart().equals(earlyNon));
	 * 
	 * // Case 2.a t = ac.createOrUpdateTardy(overlapTardyFirst, tardyDate);
	 * t.setStatus(Absence.Status.Approved); ac.updateAbsence(t);
	 * 
	 * ac.createOrUpdateEarlyCheckout(overlapTardyFirst, earlyOverlap);
	 * 
	 * List<Absence> overlapTardyFirstAbsences = train.getAbsenceManager()
	 * .get(overlapTardyFirst);
	 * 
	 * assertEquals(2, overlapTardyFirstAbsences.size()); Absence createdAbsence
	 * = overlapTardyFirstAbsences.get(0);
	 * 
	 * // Case 2.b ac.createOrUpdateEarlyCheckout(overlapEarlyFirst,
	 * earlyOverlap); ac.createOrUpdateTardy(overlapEarlyFirst, tardyDate);
	 * 
	 * List<Absence> overlapEarlyFirstAbsences = train.getAbsenceManager()
	 * .get(overlapEarlyFirst);
	 * 
	 * assertEquals(2, overlapEarlyFirstAbsences.size()); }
	 * 
	 * @Test public void testApprovedEarlyVersusEarly() { DataTrain train =
	 * getDataTrain();
	 * 
	 * UserManager uc = train.getUsersManager(); User student =
	 * Users.createStudent(uc, "student", "123456789", "First", "last", 2,
	 * "major", User.Section.AltoSax);
	 * 
	 * DateTime start = makeDate("2012-06-16 0500"); DateTime checkout =
	 * makeDate("2012-06-16 0550"); DateTime end = makeDate("2012-06-16 0600");
	 * 
	 * // Approved saved first
	 * train.getEventManager().createOrUpdate(Event.Type.Performance, start,
	 * end); Absence abs =
	 * train.getAbsenceManager().createOrUpdateEarlyCheckout( student,
	 * checkout); abs.setStatus(Absence.Status.Approved);
	 * train.getAbsenceManager().updateAbsence(abs); train.getAbsenceManager()
	 * .createOrUpdateEarlyCheckout(student, checkout);
	 * 
	 * List<Absence> studentAbs = train.getAbsenceManager().get(student);
	 * assertEquals(1, studentAbs.size()); assertEquals(Absence.Status.Approved,
	 * studentAbs.get(0).getStatus()); }
	 *//**
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

		User student = TestUsers.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

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
/*
	@Test
	public void testGettingUnanchored() {
		DataTrain train = getDataTrain();

		UserManager uc = train.getUsersManager();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();

		List<Absence> unanchored = new ArrayList<Absence>();

		User student1 = Users.createStudent(uc, "student1", "123456789",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456788",
				"First", "last", 2, "major", User.Section.AltoSax);
		User student3 = Users.createStudent(uc, "student3", "123456787",
				"First", "last", 2, "major", User.Section.AltoSax);

		DateTime unanchoredStart = makeDate("2012-09-21 0600");
		DateTime unanchoredEnd = makeDate("2012-09-21 0700");

		DateTime anchoredStart = makeDate("2012-09-22 0600");
		DateTime anchoredEnd = makeDate("2012-09-22 0700");

		unanchored.add(ac.createOrUpdateAbsence(student1, unanchoredStart,
				unanchoredEnd));
		unanchored.add(ac.createOrUpdateTardy(student2, unanchoredStart));
		unanchored.add(ac
				.createOrUpdateEarlyCheckout(student3, unanchoredStart));

		ec.createOrUpdate(Event.Type.Rehearsal, anchoredStart, anchoredEnd);
		ac.createOrUpdateAbsence(student1, anchoredStart, anchoredEnd);
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
*/
}
