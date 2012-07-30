package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.AbsenceController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.EventController;
import edu.iastate.music.marching.attendance.controllers.FormController;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;

@SuppressWarnings("deprecation")
public class FormATest extends AbstractTest {

	// test that it approves a performance absence only, not a rehearsal
	// absence
	// only if the form is approved, obviously
	// only if student on form and absence is the same
	// test creating the form and absence in both orders
	// test orphaned and anchored
	// all three types of absences

	// question: what if there are two events on the same day?
	// answer: both absences will be approved, if there are indeed two. they are
	// treated independently.

	/* ANCHORED */
	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA1() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormA2() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form A, add an approved absence, approve the form, check the
	 * absence is approved.
	 */
	@Test
	public void testApproveWithFormA3() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, update it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA4() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Pending);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Approved, a.getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormA5() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is approved.
	 */
	@Test
	public void testApproveWithFormA6() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Denied);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA7() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA8() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form A, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormA9() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/*
	 * Same tests, just ORPHANED (using the deprecated method that doesn't take
	 * an event as a param)
	 */
	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA11() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
		EventController ec = train.getEventController();
		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add a form A, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormA12() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Denied, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add a form A, add an approved absence, approve the form, check the
	 * absence is approved.
	 */
	@Test
	public void testApproveWithFormA13() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, update it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA14() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		EventController ec = train.getEventController();
		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Pending);
		ac.updateAbsence(a);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormA15() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());
		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is approved.
	 */
	@Test
	public void testApproveWithFormA16() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Denied);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA17() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA18() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form A, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormA19() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/*
	 * Subset of the original tests, with a different student on the form and
	 * absence
	 */
	/**
	 * Add a form A with student 1, add an absence with student 2, approve the
	 * form, check the absence is pending.
	 */
	@Test
	public void testApproveWithFormA21() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456781",
				"John", "Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student2, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add a form A with student 1, add an absence with student 2, deny the
	 * absence, approve the form, check the absence is still denied.
	 */
	@Test
	public void testApproveWithFormA22() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456781",
				"John", "Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student2, e);

		a.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Denied, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence with student 1, add a form A with student 2, approve the
	 * form, check the absence is pending.
	 */
	@Test
	public void testApproveWithFormA27() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);
		User student2 = Users.createStudent(uc, "student2", "123456781",
				"John", "Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student2, e);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
	}

	/* Tests the same set, but with a rehearsal absence instead of a performance */
	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA31() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormA32() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form A, add an approved absence, approve the form, check the
	 * absence is pending.
	 */
	@Test
	public void testApproveWithFormA33() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * pending, set the absence as pending, update it, check that it is pending.
	 */
	@Test
	public void testApproveWithFormA34() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Pending);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Pending, a.getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * pending, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormA35() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is pending.
	 */
	@Test
	public void testApproveWithFormA36() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Denied);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, approve the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA37() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA38() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form A, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormA39() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved. Same test, but with orphaned and rehearsal.
	 */
	@Test
	public void testApproveWithFormA41() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
		EventController ec = train.getEventController();
		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/* same subset, just with tardies */
	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA51() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());

		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormA52() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form A, add an approved absence, approve the form, check the
	 * absence is approved.
	 */
	@Test
	public void testApproveWithFormA53() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, update it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA54() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Pending);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Approved, a.getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormA55() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is approved.
	 */
	@Test
	public void testApproveWithFormA56() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		form.setStatus(Form.Status.Denied);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA57() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA58() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form A, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormA59() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/* same subset, just with earlycheckouts */
	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA61() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());

		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormA62() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form A, add an approved absence, approve the form, check the
	 * absence is approved.
	 */
	@Test
	public void testApproveWithFormA63() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, update it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA64() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Pending);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Approved, a.getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormA65() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is approved.
	 */
	@Test
	public void testApproveWithFormA66() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		form.setStatus(Form.Status.Denied);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA67() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA68() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form A, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormA69() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * With two absences (tardy and EOC)
	 */
	@Test
	public void testApproveWithFormA71() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormA(student, date.getTime(), "I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());

		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());
		start.add(Calendar.MINUTE, -10);
		Absence b = ac.createOrUpdateTardy(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
		assertEquals(Absence.Status.Approved, b.getStatus());
	}
	
	@Test
	public void testNonAutoApproveOnDeniedAbsenceFormSecond() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();
		
		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);
		
		String sDate = "2012-09-21 0600";
		String eDate = "2012-09-21 0700";
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd HHmm").parse(sDate);
			endDate = new SimpleDateFormat("yyyy-MM-dd HHmm").parse(eDate);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ec.createOrUpdate(Event.Type.Performance, startDate, endDate);
		Absence a = ac.createOrUpdateAbsence(student, startDate, endDate);
		a.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a);
	
		Form form = fc.createFormA(student, startDate, "Oh god, how did I get in here, I am a cat :3");
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		
		List<Absence> abs = ac.get(student);
		
		assertEquals(Absence.Status.Denied, abs.get(0).getStatus());
	}
	
	@Test
	public void testNonAutoApproveOnDeniedAbsenceFormFirst() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();
		
		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);
		
		String sDate = "2012-09-21 0600";
		String eDate = "2012-09-21 0700";
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = new SimpleDateFormat("yyyy-MM-dd HHmm").parse(sDate);
			endDate = new SimpleDateFormat("yyyy-MM-dd HHmm").parse(eDate);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		ec.createOrUpdate(Event.Type.Performance, startDate, endDate);
		
		Form form = fc.createFormA(student, startDate, "Meow!");
		
		Absence a = ac.createOrUpdateAbsence(student, startDate, endDate);
		a.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a);
		
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		
		List<Absence> abs = ac.get(student);
		
		assertEquals(Absence.Status.Denied, abs.get(0).getStatus());
	}
}
