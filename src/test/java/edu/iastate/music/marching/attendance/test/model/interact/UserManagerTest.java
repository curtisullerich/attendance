package edu.iastate.music.marching.attendance.test.model.interact;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.junit.Test;

import edu.iastate.music.marching.attendance.WeekDay;
import edu.iastate.music.marching.attendance.model.interact.AbsenceManager;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.EventManager;
import edu.iastate.music.marching.attendance.model.interact.FormManager;
import edu.iastate.music.marching.attendance.model.interact.MobileDataManager;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.testlib.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.testlib.Config;
import edu.iastate.music.marching.attendance.testlib.Users;
import edu.iastate.music.marching.attendance.util.Util;

public class UserManagerTest extends AbstractDatastoreTest {

	public static final String SINGLE_ABSENCE_STUDENT1_TESTDATA = "tardyStudent&split&el&split&Starster&split&studenttt&split&2012-05-03&split&0109&split&|&split&null&newline&";

	public static final String SINGLE_ABSENCE_STUDENT2_TESTDATA = "tardyStudent&split&el&split&Starster&split&studenttt2&split&2012-05-03&split&0109&split&|&split&null&newline&";

	@Test
	public void testCreateSingleDirector() {

		// Setup
		DataTrain train = getDataTrain();

		UserManager uc = train.users();

		Users.createDirector(uc, "director", "I am", "The Director");

		// Verify
		List<User> users = uc.getAll();

		assertNotNull(users);
		assertEquals(1, users.size());

		User d = users.get(0);

		// Check returned object
		assertNotNull(d);
		assertEquals(User.Type.Director, d.getType());
		assertEquals("director@" + Config.getEmailDomain(), d
				.getPrimaryEmail().getEmail());
		assertEquals("I am", d.getFirstName());
		assertEquals("The Director", d.getLastName());
	}

	@Test
	public void testCreateSingleStudent() {

		DataTrain train = getDataTrain();

		UserManager uc = train.users();

		Users.createStudent(uc, "studenttt", "123456789", "I am",
				"A Student", 10, "Being Silly", User.Section.AltoSax);

		// Verify
		List<User> users = uc.getAll();

		assertNotNull(users);
		assertEquals(1, users.size());

		User s = users.get(0);

		// Check returned object
		assertNotNull(s);
		assertEquals(User.Type.Student, s.getType());
		assertEquals("studenttt@" + Config.getEmailDomain(), s
				.getPrimaryEmail().getEmail());
		assertEquals("123456789", s.getUniversityID());
		assertEquals("I am", s.getFirstName());
		assertEquals("A Student", s.getLastName());
		assertEquals(10, s.getYear());
		assertEquals("Being Silly", s.getMajor());
		assertEquals(User.Section.AltoSax, s.getSection());
	}

	/**
	 * Ensure full cleanup of user happens without affecting other things in the
	 * datastore
	 * 
	 * This includes:
	 * 
	 * Absences, Forms mobile data uploads
	 * 
	 * @author Daniel Stiner <daniel.stiner@gmail.com>
	 */
	@Test
	public void testDeleteSingleStudent() {

		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();
		MobileDataManager mdc = train.mobileData();

		// Student 1 setup, the user to be deleted
		User student1 = Users.createStudent(uc, "studenttt", "123456789",
				"I am", "A Student", 10, "Being Silly", User.Section.AltoSax);

		ac.createOrUpdateAbsence(student1, (Event) null);
		ac.createOrUpdateAbsence(student1, (Event) null);

		fc.createPerformanceAbsenceForm(student1, new LocalDate(),
				"Some reason");

		mdc.pushMobileData(SINGLE_ABSENCE_STUDENT1_TESTDATA, student1);

		// Student 2 setup, the user to keep
		User student2 = Users
				.createStudent(uc, "studenttt2", "123456780", "I am2",
						"A Student2", 10, "Being Silly2", User.Section.AltoSax);
		ac.createOrUpdateAbsence(student2, (Event) null);

		fc.createPerformanceAbsenceForm(student2, new LocalDate(),
				"Some other reason");

		mdc.pushMobileData(SINGLE_ABSENCE_STUDENT2_TESTDATA, student2);

		// Act
		uc.delete(student1);

		// Assert
		List<User> users = uc.getAll();

		assertNotNull(users);
		assertEquals(1, users.size());

		User survivingStudent = users.get(0);

		// Check student
		assertNotNull(survivingStudent);
		assertEquals("123456780", survivingStudent.getUniversityID());

		// Check only student2 absences remain
		assertEquals(2, ac.getAll().size());
		assertEquals(2, ac.get(survivingStudent).size());

		// Check only student2's form remains
		assertEquals(1, fc.getAll().size());
		assertEquals(1, fc.get(survivingStudent).size());

		// Check mobile data upload from student2 remains
		assertEquals(2, mdc.getUploads().size());
		assertEquals(1, mdc.getUploads(survivingStudent).size());

		// And that un-assigned one still exists from student 1
		assertEquals(1, mdc.getUploads(null).size());
	}

	// @Test
	// public void averageGrade() {
	//
	// DataTrain train = getDataTrain();
	//
	// UserManager uc = train.users();
	//
	// User s1 = Users.createStudent(uc, "student1", "123456789", "First",
	// "last", 2, "major", User.Section.AltoSax);
	// User s2 = Users.createStudent(uc, "student2", "123456780", "First",
	// "Last", 2, "major", User.Section.AltoSax);
	// User s3 = Users.createStudent(uc, "student3", "123456781", "First",
	// "Last", 2, "major", User.Section.AltoSax);
	// User s4 = Users.createStudent(uc, "stiner", "123456782", "ars", "l", 3,
	// "astart", User.Section.AltoSax);
	//
	// s1.setGrade(User.Grade.A);
	// s2.setGrade(User.Grade.A);
	// s3.setGrade(User.Grade.A);
	// s4.setGrade(User.Grade.A);
	//
	// assertEquals(User.Grade.A, uc.averageGrade());
	//
	// s1.setGrade(User.Grade.A);
	// s2.setGrade(User.Grade.B);
	// s3.setGrade(User.Grade.C);
	// s4.setGrade(User.Grade.D);
	//
	// assertEquals(User.Grade.Bminus, uc.averageGrade());
	//
	// s1.setGrade(User.Grade.A);
	// s2.setGrade(User.Grade.Aminus);
	// s3.setGrade(User.Grade.A);
	// s4.setGrade(User.Grade.Aminus);
	//
	// assertEquals(User.Grade.A, uc.averageGrade());
	//
	// }
	//

	@Test
	public void testGrade1() {
		DataTrain train = getDataTrain();

		DateTimeZone zone = train.appData().get().getTimeZone();
		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();

		User s1 = Users.createDefaultStudent(uc);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		DateTime start = new DateTime(2012, 7, 18, 16, 30, 0, 0, zone);
		DateTime end = new DateTime(2012, 7, 18, 17, 50, 0, 0, zone);

		Interval i1 = new Interval(start, end);
		Event e1 = ec.createOrUpdate(Event.Type.Rehearsal, i1);
		Absence a1 = ac.createOrUpdateAbsence(s1, e1);
		uc.update(s1);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(80, uc.get(s1.getId()).getMinutesMissed());

		Interval i2 = new Interval(start.plusDays(1), end.plusDays(1));
		Event e2 = ec.createOrUpdate(Event.Type.Rehearsal, i2);
		Absence a2 = ac.createOrUpdateAbsence(s1, e2);
		uc.update(s1);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(160, uc.get(s1.getId()).getMinutesMissed());

		Interval i3 = new Interval(start.plusDays(2), end.plusDays(2));
		Event e3 = ec.createOrUpdate(Event.Type.Rehearsal, i3);
		Absence a3 = ac.createOrUpdateAbsence(s1, e3);
		uc.update(s1);
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());
		assertEquals(240, uc.get(s1.getId()).getMinutesMissed());

		Interval i4 = new Interval(start.plusDays(3), end.plusDays(3));
		Event e4 = ec.createOrUpdate(Event.Type.Rehearsal, i4);
		Absence a4 = ac.createOrUpdateAbsence(s1, e4);
		uc.update(s1);
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());
		assertEquals(320, uc.get(s1.getId()).getMinutesMissed());

		Interval i5 = new Interval(start.plusDays(4), end.plusDays(4));
		Event e5 = ec.createOrUpdate(Event.Type.Rehearsal, i5);
		Absence a5 = ac.createOrUpdateAbsence(s1, e5);
		uc.update(s1);
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());
		assertEquals(400, uc.get(s1.getId()).getMinutesMissed());

		a1.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a1);
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());
		assertEquals(320, uc.get(s1.getId()).getMinutesMissed());
		a2.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a2);
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());
		assertEquals(240, uc.get(s1.getId()).getMinutesMissed());

		a3.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a3);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(160, uc.get(s1.getId()).getMinutesMissed());

		a4.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a4);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(80, uc.get(s1.getId()).getMinutesMissed());

		a5.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a5);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(0, uc.get(s1.getId()).getMinutesMissed());

		a1.setType(Absence.Type.Tardy);
		a1.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a1);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(0, uc.get(s1.getId()).getMinutesMissed());

		a2.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a2);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(80, uc.get(s1.getId()).getMinutesMissed());

		Interval i6 = new Interval(start.plusDays(5), end.plusDays(5));
		DateTime tardyTime = i6.getStart().plusMinutes(10);
		DateTime outTime = i6.getStart().plusMinutes(20);

		// creating an un-anchored tardy
		Absence a6 = ac.createOrUpdateTardy(s1, tardyTime);
		ac.updateAbsence(a6);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(80, uc.get(s1.getId()).getMinutesMissed());

		Absence a7 = ac.createOrUpdateEarlyCheckout(s1, outTime);
		a7 = ac.updateAbsence(a7);
		assertEquals(Absence.Type.EarlyCheckOut, a7.getType());
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(80, uc.get(s1.getId()).getMinutesMissed());

		// auto-linking should happen here, upon creation
		ec.createOrUpdate(Event.Type.Rehearsal, i6);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		assertEquals(160, uc.get(s1.getId()).getMinutesMissed());
	}

	@Test
	public void testGrade2() {
		// TODO test with earlycheckout and tardies
		// TODO test that grade is fixed after approving things
		// TODO test that grade is affected after linking, but not before
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(0, uc.get(student.getId()).getMinutesMissed());

		DateTime start = new DateTime(2012, 9, 18, 16, 30, 0, 0, zone);
		DateTime end = start.plusMinutes(80);
		DateTime checkIOtime = start.plusMinutes(10);

		ac.createOrUpdateTardy(student, checkIOtime);
		uc.update(student);

		// there's a tardy, but it's not linked
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(0, uc.get(student.getId()).getMinutesMissed());
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end)); // total
																			// =
																			// 10

		// now that there's a matching event, it should link, but still be
		// within the limit
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(10, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		checkIOtime = start.plusMinutes(10);
		ac.createOrUpdateEarlyCheckout(student, checkIOtime); // total = 90
		assertEquals(10, uc.get(student.getId()).getMinutesMissed());
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(90, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		checkIOtime = start.plusMinutes(10);
		ac.createOrUpdateEarlyCheckout(student, checkIOtime); // total = 170
		assertEquals(90, uc.get(student.getId()).getMinutesMissed());
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		// now link the last eco
		ec.createOrUpdate(Event.Type.Performance, new Interval(start, end));
		assertEquals(User.Grade.B, uc.get(student.getId()).getGrade());
		assertEquals(170, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = end.plusDays(1);
		checkIOtime = checkIOtime.plusDays(1);
		ac.createOrUpdateTardy(student, checkIOtime);
		ec.createOrUpdate(Event.Type.Performance, new Interval(start, end));
		assertEquals(User.Grade.C, uc.get(student.getId()).getGrade());
		assertEquals(180, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = end.plusDays(1);
		checkIOtime = checkIOtime.plusDays(1);
		ac.createOrUpdateAbsence(student, ec.createOrUpdate(
				Event.Type.Performance, new Interval(start, end)));
		assertEquals(User.Grade.F, uc.get(student.getId()).getGrade());
		assertEquals(260, uc.get(student.getId()).getMinutesMissed());

		for (Absence a : ac.get(student)) {
			a.setStatus(Absence.Status.Approved);
			ac.updateAbsence(a);
		}
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(0, uc.get(student.getId()).getMinutesMissed());

	}

	@Test
	public void testGrade3() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		DateTime start = new DateTime(2012, 9, 18, 16, 30, 0, 0, zone);
		DateTime end = start.plusMinutes(80);
		DateTime checkIOtime = start.plusMinutes(10);

		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		ac.createOrUpdateTardy(student, checkIOtime);
		// 160 - 10
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(10, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		checkIOtime = start.plusMinutes(35);// counts as 80
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		Absence a1 = ac.createOrUpdateTardy(student, checkIOtime);
		// 160 - 10 - 80
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(90, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		checkIOtime = end.minusMinutes(45);// counts as 80
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		Absence a2 = ac.createOrUpdateEarlyCheckout(student, checkIOtime);
		// 160 - 10 - 80 - 80
		assertEquals(User.Grade.B, uc.get(student.getId()).getGrade());
		assertEquals(170, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		checkIOtime = start.plusMinutes(20);
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		ac.createOrUpdateTardy(student, checkIOtime);
		// 160 - 10 - 80 - 80 - 20
		assertEquals(User.Grade.C, uc.get(student.getId()).getGrade());
		assertEquals(190, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		checkIOtime = end.minusMinutes(14);
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		ac.createOrUpdateEarlyCheckout(student, checkIOtime);
		// 160 - 10 - 80 - 80 - 20 - 14
		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(204, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		checkIOtime = end.minusMinutes(25);
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		ac.createOrUpdateEarlyCheckout(student, checkIOtime);
		// 160 - 10 - 80 - 80 - 20 - 14 - 25
		assertEquals(User.Grade.F, uc.get(student.getId()).getGrade());
		assertEquals(229, uc.get(student.getId()).getMinutesMissed());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		Event e = ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start,
				end));
		ac.createOrUpdateAbsence(student, e);
		// 160 - 10 - 80 - 80 - 20 - 16 - 25 - 80
		assertEquals(User.Grade.F, uc.get(student.getId()).getGrade());
		assertEquals(309, uc.get(student.getId()).getMinutesMissed());

		a1.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a1);
		// 160 - 10 - 80 - 80 - 20 - 16 - 25 - 80 + 80
		assertEquals(User.Grade.F, uc.get(student.getId()).getGrade());
		assertEquals(229, uc.get(student.getId()).getMinutesMissed());

		a2.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a2);
		// 160 - 10 - 80 - 80 - 20 - 16 - 25 - 80 + 80 + 80
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(149, uc.get(student.getId()).getMinutesMissed());
	}

	@Test
	public void testGradeWithComplexAbsences() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		DateTime start = new DateTime(2012, 9, 18, 12, 30, 0, 0, zone);
		DateTime end = start.plusMinutes(200);
		// missed first 10
		DateTime checkIn1 = start.plusMinutes(10);
		DateTime checkOut1 = checkIn1.plusMinutes(10);
		// gone for 18
		DateTime checkIn2 = checkOut1.plusMinutes(18);
		DateTime checkOut2 = checkIn2.plusMinutes(30);
		// gone for 40
		DateTime checkIn3 = checkOut2.plusMinutes(40);
		// should be docked for 200 total and have a D

		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		ac.createOrUpdateTardy(student, checkIn1);
		// missed 10
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(10, uc.get(student.getId()).getMinutesMissed());

		ac.createOrUpdateEarlyCheckout(student, checkOut1);
		// missed 10 + 180, counts as 200. 40 over allowance
		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(200, uc.get(student.getId()).getMinutesMissed());

		ac.createOrUpdateTardy(student, checkIn2);
		// missed 10 + 18. within allowance
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(28, uc.get(student.getId()).getMinutesMissed());

		ac.createOrUpdateEarlyCheckout(student, checkOut2);
		// missed 10 + 18 + rest of rehearsal (counts as 200)
		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(200, uc.get(student.getId()).getMinutesMissed());

		ac.createOrUpdateTardy(student, checkIn3);
		// missed 10 + 18 + 40. counts as 200
		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(200, uc.get(student.getId()).getMinutesMissed());

		// 160 - 10
		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(200, uc.get(student.getId()).getMinutesMissed());
	}

	@Test
	public void testGradeWithComplexAbsencesAndApprovedAbsences() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(0, uc.get(student.getId()).getMinutesMissed());

		DateTime start = new DateTime(2012, 9, 18, 12, 30, 0, 0, zone);
		DateTime end = start.plusMinutes(200);
		// missed first 10

		DateTime checkIn1 = start.plusMinutes(20);
		DateTime checkOut1 = end.minusMinutes(20);
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start,
				end));

		Absence tardy = ac.createOrUpdateTardy(student, checkIn1);
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(20, uc.get(student.getId()).getMinutesMissed());

		ac.createOrUpdateEarlyCheckout(student, checkOut1);
		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(200, uc.get(student.getId()).getMinutesMissed());

		tardy.setStatus(Absence.Status.Approved);
		ac.updateAbsence(tardy);
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(20, uc.get(student.getId()).getMinutesMissed());
	}

	@Test
	public void testGradeWithComplexAbsencesAndApprovedAbsences2() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		DateTime start = new DateTime(2012, 9, 18, 12, 30, 0, 0, zone);
		DateTime end = start.plusMinutes(200);
		// missed first 10

		DateTime checkIn1 = start.plusMinutes(20);
		DateTime checkOut1 = checkIn1.plusMinutes(20);
		DateTime checkIn2 = checkOut1.plusMinutes(20);
		DateTime checkOut2 = checkIn2.plusMinutes(20);
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start,
				end));

		ac.createOrUpdateTardy(student, checkIn1);
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());
		assertEquals(20, uc.get(student.getId()).getMinutesMissed());

		Absence eco1 = ac.createOrUpdateEarlyCheckout(student, checkOut1);
		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(200, uc.get(student.getId()).getMinutesMissed());

		ac.createOrUpdateTardy(student, checkIn2);
		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(200, uc.get(student.getId()).getMinutesMissed());

		ac.createOrUpdateEarlyCheckout(student, checkOut2);
		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(200, uc.get(student.getId()).getMinutesMissed());

		eco1.setStatus(Absence.Status.Approved);
		ac.updateAbsence(eco1);
		// this means there are basically two tardies in a row now, at 20 and 60
		// minutes in

		assertEquals(User.Grade.D, uc.get(student.getId()).getGrade());
		assertEquals(200, uc.get(student.getId()).getMinutesMissed());
	}

	@Test
	public void testGradeWithTimeWorked() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User student = Users.createDefaultStudent(uc);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		DateTime start = new DateTime(2012, 9, 18, 16, 30, 0, 0, zone);
		DateTime end = start.plusMinutes(80);
		DateTime checkIOtime = start.plusMinutes(10);

		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		ac.createOrUpdateTardy(student, checkIOtime);
		// 160 - 10
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		checkIOtime = start.plusMinutes(35);// counts as 80
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		ac.createOrUpdateTardy(student, checkIOtime);
		// 160 - 10 - 80
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		start = start.plusDays(1);
		end = start.plusMinutes(80);
		Event e = ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start,
				end));
		ac.createOrUpdateAbsence(student, e);
		// 160 - 10 - 80 - 80
		assertEquals(User.Grade.B, uc.get(student.getId()).getGrade());

		Form f = fc.createTimeWorkedForm(student, start.toLocalDate(), 20,
				"arst");
		assertEquals(User.Grade.B, uc.get(student.getId()).getGrade());
		f.setStatus(Form.Status.Approved);
		fc.update(f);
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		Form f2 = fc.createTimeWorkedForm(student, start.toLocalDate(), 350,
				"arst");
		assertEquals(User.Grade.A, uc.get(student.getId()).getGrade());

		start = start.plusDays(1);
		end = start.plusMinutes(200);
		Event e2 = ec.createOrUpdate(Event.Type.Performance, new Interval(
				start, end));
		ac.createOrUpdateAbsence(student, e2);
		// 160 - 10 - 80 - 80 + 10 - 200
		assertEquals(User.Grade.F, uc.get(student.getId()).getGrade());

		f2.setStatus(Form.Status.Denied);
		fc.update(f2);
		assertEquals(User.Grade.F, uc.get(student.getId()).getGrade());

		f2.setStatus(Form.Status.Pending);
		fc.update(f2);
		assertEquals(User.Grade.F, uc.get(student.getId()).getGrade());

		f2.setStatus(Form.Status.Approved);
		fc.update(f2);
		// 160 - 10 - 80 - 80 + 10 - 200 + 100 = -25
		assertEquals(User.Grade.C, uc.get(student.getId()).getGrade());

	}

	@Test
	public void testNonOverlappingAbsences() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User s1 = Users.createDefaultStudent(uc);

		DateTime start = new DateTime(2012, 7, 18, 16, 30, 0, 0, zone);
		DateTime end = new DateTime(2012, 7, 18, 17, 50, 0, 0, zone);
		DateTime tardy = start.plusMinutes(10);
		DateTime early = start.plusMinutes(30);

		// creating an un-anchored tardy
		Absence a6 = ac.createOrUpdateTardy(s1, tardy);
		ac.updateAbsence(a6);

		Absence a7 = ac.createOrUpdateEarlyCheckout(s1, early);
		ac.updateAbsence(a7);

		Event e6 = ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start,
				end));
		a6.setEvent(e6);
		a6 = ac.updateAbsence(a6);
		assertEquals(Absence.Type.Tardy, a6.getType());

		a7.setEvent(e6);
		a7 = ac.updateAbsence(a7);
		assertEquals(Absence.Type.EarlyCheckOut, a7.getType());
	}

	@Test
	public void testOverlappingAbsences() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User s1 = Users.createDefaultStudent(uc);

		DateTime start = new DateTime(2012, 7, 18, 16, 30, 0, 0, zone);
		DateTime end = new DateTime(2012, 7, 18, 17, 50, 0, 0, zone);

		// so the check IN is AFTER the check OUT (shouldn't happen in real
		// life)
		DateTime tardy = start.plusMinutes(20);
		DateTime early = start.plusMinutes(10);

		// creating an un-anchored tardy
		Absence a6 = ac.createOrUpdateTardy(s1, tardy);
		ac.updateAbsence(a6);

		Absence a7 = ac.createOrUpdateEarlyCheckout(s1, early);
		ac.updateAbsence(a7);

		// Event e6 =
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(start, end));
		assertEquals(Absence.Type.EarlyCheckOut, a7.getType());

		// So, at this point, the logic in the event creation has automatically
		// linked the absence to the event. Setting the event is redundant.
		// I left this code here because it causes here, yet used to work
		// (before adding the event royale logic. Now I'm not sure what to think
		// of what happens if you keep playing with these references) -curtis
		// a6.setEvent(e6);
		// a6 = ac.updateAbsence(a6);
		// assertEquals(Absence.Type.Tardy, a6.getType());
		//
		// a7.setEvent(e6);
		// a7 = ac.updateAbsence(a7);
		// assertEquals(Absence.Type.Absence, a7.getType());
	}

	@Test
	public void testGradeWithClassConflictLateness() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		FormManager fc = train.forms();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User s1 = Users.createDefaultStudent(uc);

		LocalTime classStart = new LocalTime(16, 10);
		LocalTime classEnd = new LocalTime(17, 20);
		Interval interval = Util.datesToFullDaysInterval(new LocalDate(2012, 8,
				20), new LocalDate(2012, 12, 20), zone);
		Form form = fc.createClassConflictForm(s1, "department", "course",
				"section", "building", interval, classStart, classEnd,
				WeekDay.Monday, "details", 10, Absence.Type.Absence);

		LocalDate eventDate = new LocalDate(2012, 9, 17);
		LocalTime eventStartTime = new LocalTime(16, 30);
		DateTime eventStart = eventDate.toDateTime(eventStartTime, zone);
		DateTime eventEnd = eventStart.plusMinutes(80);
		DateTime checkIOtime1 = eventStart.plusMinutes(70);
		DateTime checkIOtime2 = eventStart.plusMinutes(70).plusDays(7);
		DateTime checkIOtime3 = eventStart.plusMinutes(70).plusDays(14);

		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(
				eventStart, eventEnd));
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(
				eventStart.plusDays(7), eventEnd.plusDays(7)));
		ec.createOrUpdate(Event.Type.Rehearsal, new Interval(
				eventStart.plusDays(14), eventEnd.plusDays(14)));

		Absence a1 = ac.createOrUpdateTardy(s1, checkIOtime1);
		Absence a2 = ac.createOrUpdateTardy(s1, checkIOtime2);
		Absence a3 = ac.createOrUpdateTardy(s1, checkIOtime3);
		assertEquals(240, uc.get(s1.getId()).getMinutesMissed());
		assertEquals(User.Grade.F, s1.getGrade());
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		s1 = uc.get(s1.getId());
		assertEquals(Absence.Status.Pending, a1.getStatus());
		assertEquals(Absence.Status.Pending, a2.getStatus());
		assertEquals(Absence.Status.Pending, a3.getStatus());
		assertEquals(30, uc.get(s1.getId()).getMinutesMissed());
		assertEquals(User.Grade.A, s1.getGrade());
	}

	@Test
	public void testGradeWithClassConflictFullAbsence() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		FormManager fc = train.forms();
		AbsenceManager ac = train.absences();
		DateTimeZone zone = train.appData().get().getTimeZone();

		User s1 = Users.createDefaultStudent(uc);

		LocalTime classStart = new LocalTime(16, 10);
		LocalTime classEnd = new LocalTime(17, 0);
		Interval interval = Util.datesToFullDaysInterval(new LocalDate(2013, 8,
				20), new LocalDate(2013, 12, 20), zone);
		Form form = fc.createClassConflictForm(s1, "department", "course",
				"section", "building", interval, classStart, classEnd,
				WeekDay.Tuesday, "details", 20, Absence.Type.Absence);

		LocalDate eventDate = new LocalDate(2013, 11, 12);
		LocalTime eventStartTime = new LocalTime(16, 30);
		DateTime eventStart = eventDate.toDateTime(eventStartTime, zone);
		DateTime eventEnd = eventStart.plusMinutes(80);

		Event e1 = ec.createOrUpdate(Event.Type.Rehearsal, new Interval(
				eventStart, eventEnd));

		Absence a = ac.createOrUpdateAbsence(s1, e1);
		assertEquals(80, uc.get(s1.getId()).getMinutesMissed());
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		s1 = uc.get(s1.getId());
		assertEquals(Absence.Status.Pending, a.getStatus());
		assertEquals(30, uc.get(s1.getId()).getMinutesMissed());
	}
}
