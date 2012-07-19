package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.controllers.AbsenceController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.EventController;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.TestConfig;
import edu.iastate.music.marching.attendance.test.util.Users;

public class UserControllerTest extends AbstractTest {

	@Test
	public void testCreateSingleDirector() {

		// Setup
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();

		Users.createDirector(uc, "director", 123, "I am", "The Director");

		// Verify
		List<User> users = uc.getAll();

		assertNotNull(users);
		assertEquals(1, users.size());

		User d = users.get(0);

		// Check returned object
		assertNotNull(d);
		assertEquals(User.Type.Director, d.getType());
		assertEquals("director@" + TestConfig.getEmailDomain(), d
				.getPrimaryEmail().getEmail());
		assertEquals(123, d.getUniversityID());
		assertEquals("I am", d.getFirstName());
		assertEquals("The Director", d.getLastName());
	}

	@Test
	public void testCreateSingleStudent() {

		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();

		Users.createStudent(uc, "studenttt", 121, "I am", "A Student", 10,
				"Being Silly", User.Section.AltoSax);

		// Verify
		List<User> users = uc.getAll();

		assertNotNull(users);
		assertEquals(1, users.size());

		User s = users.get(0);

		// Check returned object
		assertNotNull(s);
		assertEquals(User.Type.Student, s.getType());
		assertEquals("studenttt@" + TestConfig.getEmailDomain(), s
				.getPrimaryEmail().getEmail());
		assertEquals(121, s.getUniversityID());
		assertEquals("I am", s.getFirstName());
		assertEquals("A Student", s.getLastName());
		assertEquals(10, s.getYear());
		assertEquals("Being Silly", s.getMajor());
		assertEquals(User.Section.AltoSax, s.getSection());
	}

	@Test
	public void averageGrade() {

		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();

		User s1 = Users.createStudent(uc, "student1", 121, "First", "last", 2,
				"major", User.Section.AltoSax);
		User s2 = Users.createStudent(uc, "student2", 122, "First", "Last", 2,
				"major", User.Section.AltoSax);
		User s3 = Users.createStudent(uc, "student3", 123, "First", "Last", 2,
				"major", User.Section.AltoSax);
		User s4 = Users.createStudent(uc, "stiner", 34234, "ars", "l", 3, "astart",
				User.Section.AltoSax);

		s1.setGrade(User.Grade.A);
		s2.setGrade(User.Grade.A);
		s3.setGrade(User.Grade.A);
		s4.setGrade(User.Grade.A);

		assertEquals(User.Grade.A, uc.averageGrade());

		s1.setGrade(User.Grade.A);
		s2.setGrade(User.Grade.B);
		s3.setGrade(User.Grade.C);
		s4.setGrade(User.Grade.D);

		assertEquals(User.Grade.Bminus, uc.averageGrade());

		s1.setGrade(User.Grade.A);
		s2.setGrade(User.Grade.Aminus);
		s3.setGrade(User.Grade.A);
		s4.setGrade(User.Grade.Aminus);

		assertEquals(User.Grade.A, uc.averageGrade());

	}

	private static final Date ABSOLUTE_DATE = new Date();

	@Test
	public void testGrade() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User s1 = Users.createStudent(uc, "student1", 121, "John", "Cox", 2, "major",
				User.Section.AltoSax);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		Calendar start = Calendar.getInstance();
		start.setTime(ABSOLUTE_DATE);
		Calendar end = Calendar.getInstance();
		end.setTime(ABSOLUTE_DATE);
		end.roll(Calendar.MINUTE, 50);
		Event e1 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a1 = ac.createOrUpdateAbsence(s1, e1);
		uc.update(s1);
		assertEquals(User.Grade.B, uc.get(s1.getId()).getGrade());

		start.roll(Calendar.HOUR, 1);
		end.roll(Calendar.HOUR, 1);
		Event e2 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a2 = ac.createOrUpdateAbsence(s1, e2);
		uc.update(s1);
		assertEquals(User.Grade.C, uc.get(s1.getId()).getGrade());

		start.roll(Calendar.HOUR, 1);
		end.roll(Calendar.HOUR, 1);
		Event e3 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a3 = ac.createOrUpdateAbsence(s1, e3);
		uc.update(s1);
		assertEquals(User.Grade.D, uc.get(s1.getId()).getGrade());

		start.roll(Calendar.HOUR, 1);
		end.roll(Calendar.HOUR, 1);
		Event e4 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a4 = ac.createOrUpdateAbsence(s1, e4);
		uc.update(s1);
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());

		start.roll(Calendar.HOUR, 1);
		end.roll(Calendar.HOUR, 1);
		Event e5 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a5 = ac.createOrUpdateAbsence(s1, e5);
		uc.update(s1);
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());

		a1.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a1);
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());
		a2.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a2);
		assertEquals(User.Grade.D, uc.get(s1.getId()).getGrade());
		a3.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a3);
		assertEquals(User.Grade.C, uc.get(s1.getId()).getGrade());
		a4.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a4);
		assertEquals(User.Grade.B, uc.get(s1.getId()).getGrade());
		a5.setStatus(Absence.Status.Approved);
		ac.updateAbsence(a5);
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		a1.setType(Absence.Type.Tardy);
		a1.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a1);
		assertEquals(User.Grade.Aminus, uc.get(s1.getId()).getGrade());
		a2.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a2);
		assertEquals(User.Grade.Bminus, uc.get(s1.getId()).getGrade());
		
		start.roll(Calendar.HOUR, 1);
		end.roll(Calendar.HOUR, 1);
		Calendar middle = (Calendar) start.clone();
		middle.roll(Calendar.MINUTE, 10);

		//creating an un-anchored tardy
		Absence a6 = ac.createOrUpdateTardy(s1, middle.getTime());
		ac.updateAbsence(a6);
		assertEquals(User.Grade.Bminus, uc.get(s1.getId()).getGrade());
		
		middle.roll(Calendar.MINUTE,10);
		Absence a7 = ac.createOrUpdateEarlyCheckout(s1, middle.getTime());
		ac.updateAbsence(a7);
		assertEquals(User.Grade.Bminus, uc.get(s1.getId()).getGrade());
		
		Event e6 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(), end.getTime());
		a6.setEvent(e6);
		ac.updateAbsence(a6);
		assertEquals(User.Grade.Cplus, uc.get(s1.getId()).getGrade());

		a7.setEvent(e6);
		ac.updateAbsence(a7);
		assertEquals(User.Grade.C, uc.get(s1.getId()).getGrade());
		
	}
	
	
	@Test
	public void nonOverLappingAbsencesTest() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User s1 = Users.createStudent(uc, "student1", 121, "John", "Cox", 2, "major",
				User.Section.AltoSax);

		Calendar start = Calendar.getInstance();
		start.setTime(ABSOLUTE_DATE);
		Calendar end = Calendar.getInstance();
		end.setTime(ABSOLUTE_DATE);
		end.roll(Calendar.MINUTE, 50);

		Calendar tardy = (Calendar) start.clone();
		Calendar early = (Calendar) start.clone();

		tardy.roll(Calendar.MINUTE, 10);
		early.roll(Calendar.MINUTE, 30);

		//creating an un-anchored tardy
		Absence a6 = ac.createOrUpdateTardy(s1, tardy.getTime());
		ac.updateAbsence(a6);
		
		Absence a7 = ac.createOrUpdateEarlyCheckout(s1, early.getTime());
		ac.updateAbsence(a7);
		
		Event e6 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(), end.getTime());
		a6.setEvent(e6);
		a6 = ac.updateAbsence(a6);
		assertEquals(a6.getType(), Absence.Type.Tardy);

		a7.setEvent(e6);
		a7 = ac.updateAbsence(a7);
		assertEquals(a7.getType(), Absence.Type.EarlyCheckOut);
	}
	
	@Test
	public void overLappingAbsencesTest() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User s1 = Users.createStudent(uc, "student1", 121, "John", "Cox", 2, "major",
				User.Section.AltoSax);

		Calendar start = Calendar.getInstance();
		start.setTime(ABSOLUTE_DATE);
		Calendar end = Calendar.getInstance();
		end.setTime(ABSOLUTE_DATE);
		end.roll(Calendar.MINUTE, 50);

		Calendar tardy = (Calendar) start.clone();
		Calendar early = (Calendar) start.clone();

		tardy.roll(Calendar.MINUTE, 30);
		early.roll(Calendar.MINUTE, 10);

		//creating an un-anchored tardy
		Absence a6 = ac.createOrUpdateTardy(s1, tardy.getTime());
		ac.updateAbsence(a6);
		
		Absence a7 = ac.createOrUpdateEarlyCheckout(s1, early.getTime());
		ac.updateAbsence(a7);
		
		Event e6 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(), end.getTime());
		a6.setEvent(e6);
		a6 = ac.updateAbsence(a6);
		assertEquals(a6.getType(), Absence.Type.Tardy);

		a7.setEvent(e6);
		a7 = ac.updateAbsence(a7);
		assertEquals(a7.getType(), Absence.Type.Absence);
	}

}
