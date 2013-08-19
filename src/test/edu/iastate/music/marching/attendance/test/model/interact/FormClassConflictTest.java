package edu.iastate.music.marching.attendance.test.model.interact;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import edu.iastate.music.marching.attendance.App;
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
import edu.iastate.music.marching.attendance.util.Util;

public class FormClassConflictTest extends AbstractDatastoreTest {

	// absence: class times + to/from buffer must eclipse event
	// tardy: checkin time must be before class end time + from buffer
	// eco: checkout time must be after class start time - to buffer

	// we'll have the same form type bug. So see if we can set the Absence.Type
	// param in the form B creator

	private void testLinkedAbsence(LocalTime classStart, LocalTime classEnd,
			int eventMonth, int eventDay, Absence.Status expected,
			boolean deprecated) {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);
		DateTimeZone zone = train.appData().get().getTimeZone();

		LocalDate startDate = new LocalDate(2012, 8, 20);
		LocalDate endDate = new LocalDate(2012, 12, 20);
		Interval interval = Util.datesToFullDaysInterval(startDate, endDate,
				zone);

		Form form = fc.createClassConflictForm(student, "department", "course",
				"section", "building", interval, classStart, classEnd,
				App.WeekDay.Monday, "details", 10, Absence.Type.Absence);

		DateTime eventStart = new DateTime(2012, eventMonth, eventDay, 16, 30,
				0, zone);
		DateTime eventEnd = eventStart.plusMinutes(80);
		Event e = ec.createOrUpdate(Event.Type.Rehearsal, new Interval(
				eventStart, eventEnd));

		Absence a;

		if (deprecated) {
			a = ac.createOrUpdateAbsence(student, e.getInterval(zone));
		} else {
			a = ac.createOrUpdateAbsence(student, e);
		}

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		a = ac.get(a.getId());
		
		assertEquals(expected, a.getStatus());
	}

	/**
	 * class times before event
	 * 
	 * Now moving on to testing absences created using an event.
	 */
	@Test
	public void testLinkedAbsenceB01() {
		LocalTime startTime = new LocalTime(13, 10);
		LocalTime endTime = new LocalTime(14, 0);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Pending,
				false);
	}

	@Test
	public void testLinkedAbsenceB01Deprecated() {
		LocalTime startTime = new LocalTime(13, 10);
		LocalTime endTime = new LocalTime(14, 0);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Pending,
				true);
	}

	/**
	 * class times overlap event start
	 */
	@Test
	public void testLinkedAbsence02() {
		LocalTime startTime = new LocalTime(16, 10);
		LocalTime endTime = new LocalTime(17, 0);
		// TODO the original test said this should be approved. I disagree
		testLinkedAbsence(startTime, endTime, 12, 17, Absence.Status.Pending,
				false);
	}

	@Test
	public void testLinkedAbsence02Deprecated() {
		LocalTime startTime = new LocalTime(16, 10);
		LocalTime endTime = new LocalTime(17, 0);
		// TODO the original test said this should be approved. I disagree
		testLinkedAbsence(startTime, endTime, 12, 17, Absence.Status.Pending,
				true);
	}

	/**
	 * class times match event
	 */
	@Test
	public void testLinkedAbsence03() {
		LocalTime startTime = new LocalTime(16, 30);
		LocalTime endTime = new LocalTime(17, 50);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Approved,
				false);
	}

	@Test
	public void testLinkedAbsence03Deprecated() {
		LocalTime startTime = new LocalTime(16, 30);
		LocalTime endTime = new LocalTime(17, 50);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Approved,
				true);
	}

	/**
	 * class times within event
	 */
	@Test
	public void testLinkedAbsence04() {
		LocalTime startTime = new LocalTime(16, 50);
		LocalTime endTime = new LocalTime(17, 10);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Pending,
				false);
	}

	@Test
	public void testLinkedAbsence04Deprecated() {
		LocalTime startTime = new LocalTime(16, 50);
		LocalTime endTime = new LocalTime(17, 10);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Pending,
				true);
	}

	/**
	 * class times eclipse event
	 */
	@Test
	public void testLinkedAbsence05() {
		LocalTime startTime = new LocalTime(16, 10);
		LocalTime endTime = new LocalTime(18, 0);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Approved,
				false);
	}

	@Test
	public void testLinkedAbsence05Deprecated() {
		LocalTime startTime = new LocalTime(16, 10);
		LocalTime endTime = new LocalTime(18, 0);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Approved,
				true);
	}

	/**
	 * class times overlap event end
	 */
	@Test
	public void testLinkedAbsence06() {
		LocalTime startTime = new LocalTime(17, 10);
		LocalTime endTime = new LocalTime(18, 0);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Pending,
				false);
	}

	@Test
	public void testLinkedAbsence06Deprecated() {
		LocalTime startTime = new LocalTime(17, 10);
		LocalTime endTime = new LocalTime(18, 0);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Pending,
				true);
	}

	/**
	 * class times within event but buffer eclipses
	 */
	@Test
	public void testLinkedAbsence07() {
		LocalTime startTime = new LocalTime(16, 35);
		LocalTime endTime = new LocalTime(17, 45);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Approved,
				false);
	}

	@Test
	public void testLinkedAbsence07Deprecated() {
		LocalTime startTime = new LocalTime(16, 35);
		LocalTime endTime = new LocalTime(17, 45);
		testLinkedAbsence(startTime, endTime, 8, 20, Absence.Status.Approved,
				true);
	}
	//
	// /**
	// * class times before event
	// *
	// * testing identical absence cases, just with the deprecated method
	// */
	// @Test
	// public void testDeprecatedAbsenceWithFormB01() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 13, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 14, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	//
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start
	// */
	// @Test
	// public void testDeprecatedAbsenceWithFormB02() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// // because we now allow overlaps to approve the absence, this is
	// // approved instead of pending
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	// /**
	// * class times match event
	// */
	// @Test
	// public void testDeprecatedAbsenceWithFormB03() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	// /**
	// * class times within event
	// */
	// @Test
	// public void testDeprecatedAbsenceWithFormB04() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 50, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 10, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// // because we now allow overlaps to approve the absence, this is
	// // approved instead of pending
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	// /**
	// * class times eclipse event
	// */
	// @Test
	// public void testDeprecatedAbsenceWithFormB05() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 18, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event end
	// */
	// @Test
	// public void testDeprecatedAbsenceWithFormB06() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 18, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// // because we now allow overlaps to approve the absence, this is
	// // approved instead of pending
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	// /**
	// * class times within event but buffer eclipses
	// */
	// @Test
	// public void testDeprecatedAbsenceWithFormB07() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 35, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 45, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 8, 10, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 8, 10, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	//
	// /**
	// * class times overlap event start, tardy time within class, during event
	// *
	// * testing failure cases: day-of-week repetition
	// */
	// @Test
	// public void testTardyRepetitionWithFormB06() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 16, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.TUESDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy after buffer, in event
	// */
	// @Test
	// public void testTardyRepetitionWithFormB11() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 17, 15, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.TUESDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy time within class, before event
	// */
	// @Test
	// public void testECORepetitionWithFormB05() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 16, 20, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.THURSDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy time within class, during event
	// */
	// @Test
	// public void testECORepetitionWithFormB06() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 16, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.SUNDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start
	// */
	// @Test
	// public void testLinkedAbsenceRepetitionWithFormB02() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 11, 17, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 11, 17, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.SATURDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event
	// */
	// @Test
	// public void testLinkedAbsenceRepetitionWithFormB03() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.FRIDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event
	// */
	// @Test
	// public void testDeprecatedAbsenceRepetitionWithFormB03() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.WEDNESDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event
	// */
	// @Test
	// public void testDeprecatedAbsenceRepetitionWithFormB04() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 50, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 10, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.THURSDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// // failure cases: DateTime before range
	// //
	// // class times before event, tardy time outside both
	// //
	// @Test
	// public void testTardyDateBeforeRangeWithFormB01() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 13, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 14, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 6, 13, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 6, 13, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 6, 13, 8, 15, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy time within class, during event
	// */
	// @Test
	// public void testTardyDateBeforeRangeWithFormB06() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2011, 7, 18, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2011, 7, 18, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2011, 7, 18, 16, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times before event, tardy time inside event
	// */
	// @Test
	// public void testECOBeforeDateRangeWithFormB02() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 13, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 14, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 6, 13, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 6, 13, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 6, 13, 16, 45, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy time within class, during event
	// */
	// @Test
	// public void testECODateBeforeRangeWithFormB06() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2011, 7, 25, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2011, 7, 25, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2011, 7, 25, 16, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times before event
	// */
	// @Test
	// public void testLinkedAbsenceDateBeforeRangeWithFormB01() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 13, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 14, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2011, 7, 25, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2011, 7, 25, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event
	// */
	// @Test
	// public void testLinkedAbsenceDateBeforeRangeWithFormB03() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 6, 6, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 6, 6, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event
	// */
	// @Test
	// public void testDeprecatedAbsenceDateBeforeRangeWithFormB04() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 50, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 10, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2001, 7, 30, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2001, 7, 30, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times eclipse event
	// */
	// @Test
	// public void testDeprecatedAbsenceDateBeforeRangeWithFormB05() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 18, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 5, 18, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 5, 18, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /* failure cases: DateTime after range */
	// /**
	// * class times overlap event start, tardy time on event start
	// */
	// @Test
	// public void testTardyDateAfterRangeWithFormB07() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2013, 7, 22, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2013, 7, 22, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2013, 7, 22, 16, 30, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event, tardy time in buffer, before event
	// */
	// @Test
	// public void testTardyDateAfterRangeWithFormB12() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 11, 27, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 11, 27, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 11, 27, 16, 25, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy time on event start
	// */
	// @Test
	// public void testECODateAfterRangeWithFormB07() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 11, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 11, 24, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 11, 24, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 11, 24, 16, 30, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy after buffer, in event
	// */
	// @Test
	// public void testECODateAfterRangeWithFormB11() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2013, 7, 29, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2013, 7, 29, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2013, 7, 29, 17, 15, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event
	// */
	// @Test
	// public void testLinkedAbsenceDateAfterRangeWithFormB04() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 50, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 10, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2013, 1, 9, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2013, 1, 9, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times eclipse event
	// */
	// @Test
	// public void testLinkedAbsenceDateAfterRangeWithFormB05() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 18, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2014, 7, 14, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2014, 7, 14, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times before event
	// */
	// @Test
	// public void testDeprecatedAbsenceDateAfterRangeWithFormB01() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 13, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 14, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2037, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2037, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	//
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event
	// */
	// @Test
	// public void testDeprecatedAbsenceDateAfterRangeWithFormB03() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 11, 31, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 11, 31, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /* failure cases: performance */
	// /**
	// * class times match event, tardy time on event start
	// */
	// @Test
	// public void testTardyPerformanceWithFormB13() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 16, 30, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Performance,
	// eventstart.getTime(), eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event, tardy time after event
	// */
	// @Test
	// public void testTardyPerformanceWithFormB16() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 18, 10, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Performance,
	// eventstart.getTime(), eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event, tardy time in buffer, before event
	// */
	// @Test
	// public void testECOWithPerformanceFormB12() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 16, 25, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Performance,
	// eventstart.getTime(), eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event, tardy time on event start
	// */
	// @Test
	// public void testECOWithPerformanceFormB13() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 16, 30, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Performance,
	// eventstart.getTime(), eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event end
	// */
	// @Test
	// public void testLinkedAbsencePerformanceWithFormB06() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 18, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Performance,
	// eventstart.getTime(), eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event but buffer eclipses
	// */
	// @Test
	// public void testLinkedAbsencePerformanceWithFormB07() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 35, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 45, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Performance,
	// eventstart.getTime(), eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event end
	// */
	// @Test
	// public void testDeprecatedAbsencePerformanceWithFormB06() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 18, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Performance,
	// eventstart.getTime(), eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event but buffer eclipses
	// */
	// @Test
	// public void testDeprecatedAbsencePerformanceWithFormB07() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 35, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 45, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 8, 10, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 8, 10, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Performance,
	// eventstart.getTime(), eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /* failure cases: pending form */
	// /**
	// * class times within event, tardy time before buffer, in event
	// */
	// @Test
	// public void testTardyWithPendingFormB17() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 0, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 20, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 16, 35, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event, tardy time in start buffer
	// */
	// @Test
	// public void testTardyWithPendingFormB18() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 0, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 20, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 16, 55, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event, tardy time in event
	// */
	// @Test
	// public void testECOWithPendingFormB14() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 16, 40, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event, tardy time after event
	// */
	// @Test
	// public void testECOWithPendingFormB16() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 18, 10, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start
	// */
	// @Test
	// public void testLinkedAbsenceWithPendingFormB02() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 11, 17, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 11, 17, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event
	// */
	// @Test
	// public void testLinkedAbsenceWithPendingFormB03() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event
	// */
	// @Test
	// public void testDeprecatedAbsenceWithPendingFormB04() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 50, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 10, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times eclipse event
	// */
	// @Test
	// public void testDeprecatedAbsenceWithPendingFormB05() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 18, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /* failure cases: denied form */
	// /**
	// * class times within event, tardy time in class
	// */
	// @Test
	// public void testTardyWithDeniedFormB19() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 0, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 20, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 17, 10, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event, tardy time after end buffer
	// */
	// @Test
	// public void testTardyWithDeniedFormB21() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 0, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 20, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 17, 40, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Tardy);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event, tardy time in end buffer
	// */
	// @Test
	// public void testECOWithDeniedFormB20() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 0, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 20, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 17, 23, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event, tardy time after end buffer
	// */
	// @Test
	// public void testECOWithDeniedFormB21() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 0, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 20, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 20, 17, 40, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.EarlyCheckOut);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event end
	// */
	// @Test
	// public void testLinkedAbsenceWithDeniedFormB06() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 17, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 18, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times within event but buffer eclipses
	// */
	// @Test
	// public void testLinkedAbsenceWithDeniedFormB07() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 35, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 45, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times before event
	// */
	// @Test
	// public void testDeprecatedAbsenceWithDeniedFormB01() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 13, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 14, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	//
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times match event
	// */
	// @Test
	// public void testDeprecatedAbsenceWithDeniedFormB03() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 20, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 20, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * make sure final day of form range still works
	// */
	// @Test
	// public void testDeprecatedAbsenceOnFinalDay() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student4", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 20, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 17, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 30, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 50, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 11, 17, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 11, 17, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	// @Test
	// public void testNonAutoApproveOnDeniedAbsenceFormSecond() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student4", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// String sDateTime = "2012-09-21 0600";
	// String eDateTime = "2012-09-21 0700";
	// DateTime startDateTime = null;
	// DateTime endDateTime = null;
	// try {
	// startDateTime = new SimpleDateFormat("yyyy-MM-dd HHmm").parse(sDate);
	// endDateTime = new SimpleDateFormat("yyyy-MM-dd HHmm").parse(eDate);
	//
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// ec.createOrUpdate(Event.Type.Rehearsal, startDate, endDate);
	// Absence abs = ac.createOrUpdateAbsence(student, startDate, endDate);
	// abs.setStatus(Absence.Status.Denied);
	// ac.updateAbsence(abs);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startDate, endDate, Calendar.MONDAY, startDate,
	// endDate, "details", 10, Absence.Type.Absence);
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// List<Absence> absences = ac.get(student);
	//
	// assertEquals(Absence.Status.Denied, absences.get(0).getStatus());
	// }
	//
	// @Test
	// public void testNonAutoApproveOnDeniedAbsenceFormFirst() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student4", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// String sDateTime = "2012-09-21 0600";
	// String eDateTime = "2012-09-21 0700";
	// DateTime startDateTime = null;
	// DateTime endDateTime = null;
	// try {
	// startDateTime = new SimpleDateFormat("yyyy-MM-dd HHmm").parse(sDate);
	// endDateTime = new SimpleDateFormat("yyyy-MM-dd HHmm").parse(eDate);
	//
	// } catch (ParseException e) {
	// e.printStackTrace();
	// }
	//
	// ec.createOrUpdate(Event.Type.Rehearsal, startDate, endDate);
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startDate, endDate, Calendar.MONDAY, startDate,
	// endDate, "details", 10, Absence.Type.Absence);
	//
	// Absence abs = ac.createOrUpdateAbsence(student, startDate, endDate);
	// abs.setStatus(Absence.Status.Denied);
	// ac.updateAbsence(abs);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// List<Absence> absences = ac.get(student);
	//
	// assertEquals(Absence.Status.Denied, absences.get(0).getStatus());
	// }
	//
	// /**
	// * Add a form B, add an absence, deny the absence, approve the form, check
	// * the absence is still denied.
	// */
	// @Test
	// public void testApproveDeniedAbsenceWithFormB() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student4", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar DateTime = Calendar.getInstance();
	// date.set(2012, 7, 7, 0, 0, 0);
	// Calendar start = Calendar.getInstance();
	// start.set(2012, 7, 7, 16, 30, 0);
	// Calendar end = Calendar.getInstance();
	// end.set(2012, 7, 7, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", date.getTime(), date.getTime(), Calendar.MONDAY,
	// start.getTime(), end.getTime(), "details", 10,
	// Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
	// end.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, e);
	//
	// a.setStatus(Absence.Status.Denied);
	// a = ac.updateAbsence(a);
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Denied, a.getStatus());
	// }
	//
	// /**
	// * class times within event
	// */
	// @Test
	// public void testDeprecatedAbsenceBug() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 23, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 12, 6, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 15, 40, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 30, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 23, 16, 30, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 23, 17, 50, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.THURSDAY, starttime.getTime(), endtime.getTime(),
	// "details", 15, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateAbsence(student, eventstart.getTime(),
	// eventend.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	//
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy time during event, outside
	// class,
	// * form for an absence
	// */
	// @Test
	// public void testTardyWithFormB101() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 6, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 6, 16, 30, 0);
	// eventstart.set(Calendar.MILLISECOND, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 6, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 6, 17, 30, 0);
	// tardytime.set(Calendar.MILLISECOND, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	// a = ac.updateAbsence(a);
	//
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, ECO time during event, outside class,
	// * form for an absence
	// */
	// @Test
	// public void testECOWithFormB101() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 6, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 6, 16, 30, 0);
	// eventstart.set(Calendar.MILLISECOND, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 6, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 6, 17, 30, 0);
	// tardytime.set(Calendar.MILLISECOND, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Approved);
	// fc.update(form);
	// a = ac.updateAbsence(a);
	//
	// assertEquals(Absence.Status.Approved, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy time during event, outside
	// class,
	// * form for an absence
	// */
	// @Test
	// public void testTardyWithPendingFormB101() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 6, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 6, 16, 30, 0);
	// eventstart.set(Calendar.MILLISECOND, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 6, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 6, 17, 30, 0);
	// tardytime.set(Calendar.MILLISECOND, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	// a = ac.updateAbsence(a);
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, ECO time during event, outside class,
	// * form for an absence
	// */
	// @Test
	// public void testECOWithPendingFormB101() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 6, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 6, 16, 30, 0);
	// eventstart.set(Calendar.MILLISECOND, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 6, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 6, 17, 30, 0);
	// tardytime.set(Calendar.MILLISECOND, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Pending);
	// fc.update(form);
	// a = ac.updateAbsence(a);
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, tardy time during event, outside
	// class,
	// * form for an absence
	// */
	// @Test
	// public void testTardyWithDeniedFormB101() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 6, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 6, 16, 30, 0);
	// eventstart.set(Calendar.MILLISECOND, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 6, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 6, 17, 30, 0);
	// tardytime.set(Calendar.MILLISECOND, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac.createOrUpdateTardy(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	// a = ac.updateAbsence(a);
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
	//
	// /**
	// * class times overlap event start, ECO time during event, outside class,
	// * form for an absence
	// */
	// @Test
	// public void testECOWithDeniedFormB101() {
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	// EventManager ec = train.events();
	// AbsenceManager ac = train.absences();
	// FormManager fc = train.forms();
	//
	// User student = Users.createStudent(uc, "student1", "123456789", "John",
	// "Cox", 2, "major", User.Section.AltoSax);
	//
	// Calendar startDateTime = Calendar.getInstance();
	// startdate.set(2012, 7, 6, 0, 0, 0);
	// Calendar endDateTime = Calendar.getInstance();
	// enddate.set(2012, 11, 20, 0, 0, 0);
	// Calendar starttime = Calendar.getInstance();
	// starttime.set(0, 0, 0, 16, 10, 0);
	// Calendar endtime = Calendar.getInstance();
	// endtime.set(0, 0, 0, 17, 0, 0);
	//
	// // a normal rehearsal
	// Calendar eventstart = Calendar.getInstance();
	// eventstart.set(2012, 7, 6, 16, 30, 0);
	// eventstart.set(Calendar.MILLISECOND, 0);
	// Calendar eventend = Calendar.getInstance();
	// eventend.set(2012, 7, 6, 17, 50, 0);
	//
	// Calendar tardytime = Calendar.getInstance();
	// tardytime.set(2012, 7, 6, 17, 30, 0);
	// tardytime.set(Calendar.MILLISECOND, 0);
	//
	// Form form = fc.createClassConflictForm(student, "department", "course",
	// "section",
	// "building", startdate.getTime(), enddate.getTime(),
	// Calendar.MONDAY, starttime.getTime(), endtime.getTime(),
	// "details", 10, Absence.Type.Absence);
	//
	// Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventstart.getTime(),
	// eventend.getTime());
	// Absence a = ac
	// .createOrUpdateEarlyCheckout(student, tardytime.getTime());
	//
	// form.setStatus(Form.Status.Denied);
	// fc.update(form);
	// a = ac.updateAbsence(a);
	// assertEquals(Absence.Status.Pending, a.getStatus());
	// }
}
