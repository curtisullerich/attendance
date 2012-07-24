package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.List;

import org.junit.Test;

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

		Users.createDirector(uc, "director", "I am", "The Director");

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
		User s4 = Users.createStudent(uc, "stiner", 34234, "ars", "l", 3,
				"astart", User.Section.AltoSax);

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

	@Test
	public void testGrade() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User s1 = Users.createStudent(uc, "student1", 121, "John", "Cox", 2,
				"major", User.Section.AltoSax);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 18, 16, 30);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 18, 17, 20);

		Event e1 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a1 = ac.createOrUpdateAbsence(s1, e1);
		uc.update(s1);
		assertEquals(User.Grade.B, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		Event e2 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a2 = ac.createOrUpdateAbsence(s1, e2);
		uc.update(s1);
		assertEquals(User.Grade.C, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		Event e3 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a3 = ac.createOrUpdateAbsence(s1, e3);
		uc.update(s1);
		assertEquals(User.Grade.D, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		Event e4 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a4 = ac.createOrUpdateAbsence(s1, e4);
		uc.update(s1);
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
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

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		Calendar tardyTime = (Calendar) start.clone();
		tardyTime.add(Calendar.MINUTE, 10);
		Calendar outTime = (Calendar) start.clone();

		// creating an un-anchored tardy
		Absence a6 = ac.createOrUpdateTardy(s1, tardyTime.getTime());
		ac.updateAbsence(a6);
		assertEquals(User.Grade.Bminus, uc.get(s1.getId()).getGrade());

		outTime.add(Calendar.MINUTE, 20);
		Absence a7 = ac.createOrUpdateEarlyCheckout(s1, outTime.getTime());
		a7 = ac.updateAbsence(a7);
		assertEquals(Absence.Type.EarlyCheckOut, a7.getType());
		assertEquals(User.Grade.Bminus, uc.get(s1.getId()).getGrade());

		// auto-linking should happen here, upon creation
		Event e6 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		// a6.setEvent(e6);
		// TODO my question here: is there a way to know for sure the types of
		// a6 and a7 after creating the event? Because automatic linking
		// happens. Additionally, does it matter? -curtis
		// a6 = ac.updateAbsence(a6);
		// assertEquals(Absence.Type.Tardy, a6.getType());
		// assertEquals(User.Grade.Cplus, uc.get(s1.getId()).getGrade());

		// a7.setEvent(e6);
		// a7 = ac.updateAbsence(a7);
		// assertEquals(Absence.Type.EarlyCheckOut, a7.getType());
		assertEquals(User.Grade.C, uc.get(s1.getId()).getGrade());
	}

	@Test
	public void testGrade2() {
		// TODO test with earlycheckout and tardies
		// TODO test that grade is fixed after approving things
		// TODO test that grade is affected after linking, but not before
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User s1 = Users.createStudent(uc, "student1", 121, "John", "Cox", 2,
				"major", User.Section.AltoSax);

		// should be A initially
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

		Calendar start = Calendar.getInstance();
		start.set(2012, 9, 18, 16, 30);
		Calendar end = Calendar.getInstance();
		end.set(2012, 9, 18, 17, 50);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 9, 18, 16, 40);
		ac.createOrUpdateTardy(s1, tardy.getTime());

		uc.update(s1);

		// there's a tardy, but it's not linked
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());
		ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(), end.getTime());

		// now that there's a matching event, it should link
		assertEquals(User.Grade.Aminus, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		tardy.add(Calendar.DATE, 1);
		ac.createOrUpdateEarlyCheckout(s1, tardy.getTime());
		ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(), end.getTime());
		assertEquals(User.Grade.Bplus, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		tardy.add(Calendar.DATE, 1);
		ac.createOrUpdateEarlyCheckout(s1, tardy.getTime());
		ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		assertEquals(User.Grade.Bminus, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		tardy.add(Calendar.DATE, 1);
		ac.createOrUpdateTardy(s1, tardy.getTime());
		ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		assertEquals(User.Grade.C, uc.get(s1.getId()).getGrade());

		start.add(Calendar.DATE, 1);
		end.add(Calendar.DATE, 1);
		tardy.add(Calendar.DATE, 1);
		ac.createOrUpdateAbsence(
				s1,
				ec.createOrUpdate(Event.Type.Performance, start.getTime(),
						end.getTime()));
		assertEquals(User.Grade.F, uc.get(s1.getId()).getGrade());

		for (Absence a : ac.get(s1)) {
			a.setStatus(Absence.Status.Approved);
			ac.updateAbsence(a);
		}
		assertEquals(User.Grade.A, uc.get(s1.getId()).getGrade());

	}

	@Test
	public void nonOverLappingAbsencesTest() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User s1 = Users.createStudent(uc, "student1", 121, "John", "Cox", 2,
				"major", User.Section.AltoSax);

		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 18, 16, 30);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 18, 17, 50);

		Calendar tardy = (Calendar) start.clone();
		Calendar early = (Calendar) start.clone();
		System.out.println("tardy: " + tardy.getTime());
		System.out.println("early: " + early.getTime());
		tardy.add(Calendar.MINUTE, 10);
		System.out.println("tardy: " + tardy.getTime());
		System.out.println("early: " + early.getTime());
		early.add(Calendar.MINUTE, 30);
		System.out.println("tardy: " + tardy.getTime());
		System.out.println("early: " + early.getTime());
		// creating an un-anchored tardy
		Absence a6 = ac.createOrUpdateTardy(s1, tardy.getTime());
		ac.updateAbsence(a6);

		Absence a7 = ac.createOrUpdateEarlyCheckout(s1, early.getTime());
		ac.updateAbsence(a7);

		Event e6 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		a6.setEvent(e6);
		a6 = ac.updateAbsence(a6);
		assertEquals(Absence.Type.Tardy, a6.getType());

		a7.setEvent(e6);
		a7 = ac.updateAbsence(a7);
		assertEquals(Absence.Type.EarlyCheckOut, a7.getType());
	}

	@Test
	public void overLappingAbsencesTest() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();

		User s1 = Users.createStudent(uc, "student1", 121, "John", "Cox", 2,
				"major", User.Section.AltoSax);

		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 18, 16, 30);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 18, 17, 50);

		Calendar tardy = (Calendar) start.clone();
		Calendar early = (Calendar) start.clone();

		// so the check IN is AFTER the check OUT (shouldn't happen in real
		// life)
		tardy.add(Calendar.MINUTE, 20);
		early.add(Calendar.MINUTE, 10);

		// creating an un-anchored tardy
		Absence a6 = ac.createOrUpdateTardy(s1, tardy.getTime());
		ac.updateAbsence(a6);

		Absence a7 = ac.createOrUpdateEarlyCheckout(s1, early.getTime());
		ac.updateAbsence(a7);

		Event e6 = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		assertEquals(Absence.Type.Absence, a7.getType());

		// So, at this point, the logiv in the event creation has automatically
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

}
