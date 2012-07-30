package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

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
public class FormCTest extends AbstractTest {

	// form c has start and end dates that essentially define the timespan
	// during which a student is permitted to have absences

	// test that it approves a rehearsal absence only, not a performance
	// absence
	// only if the form is approved, obviously
	// only if student on form and absence is the same
	// test creating the form and absence in both orders
	// test orphaned and anchored
	// all three types of absences

	/**
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC1() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form c, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormC2() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

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
	 * Add a form c, add an approved absence, approve the form, check the
	 * absence is approved.
	 */
	@Test
	public void testApproveWithFormC3() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

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
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, update it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC4() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormC5() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is approved.
	 */
	@Test
	public void testApproveWithFormC6() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add an absence, add a form c, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC7() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form c, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormC8() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form c, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormC9() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/*
	 * Same tests, just ORPHANED (using the deprecated method that doesn't take
	 * an event as a param)
	 */
	/**
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC11() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
		EventController ec = train.getEventController();
		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add a form c, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormC12() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Denied, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add a form c, add an approved absence, approve the form, check the
	 * absence is approved.
	 */
	@Test
	public void testApproveWithFormC13() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, update it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC14() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		EventController ec = train.getEventController();
		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Pending);
		ac.updateAbsence(a);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormC15() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());
		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Denied);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	/**
	 * Add a form c, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is approved.
	 */
	@Test
	public void testApproveWithFormC16() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Denied);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form c, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC17() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form c, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormC18() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form c, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormC19() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/*
	 * Subset of the original tests, with a different student on the form and
	 * absence
	 */
	/**
	 * Add a form c with student 1, add an absence with student 2, approve the
	 * form, check the absence is pending.
	 */
	@Test
	public void testApproveWithFormC21() {
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

		Form form = fc.createFormC(student2, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add a form c with student 1, add an absence with student 2, deny the
	 * absence, approve the form, check the absence is still denied.
	 */
	@Test
	public void testApproveWithFormC22() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student2, e);

		a.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Denied, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence with student 1, add a form c with student 2, approve the
	 * form, check the absence is pending.
	 */
	@Test
	public void testApproveWithFormC27() {
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

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student2, e);

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
	}

	/* Tests the same set, but with a rehearsal absence instead of a performance */
	/**
	 * Add a form c, add an absence, approve the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormC31() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * Add a form c, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormC32() {
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
	 * Add a form c, add an approved absence, approve the form, check the
	 * absence is pending.
	 */
	@Test
	public void testApproveWithFormC33() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

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
	 * Add a form c, add an absence, approve the form, check the absence is
	 * pending, set the absence as pending, update it, check that it is pending.
	 */
	@Test
	public void testApproveWithFormC34() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
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
	 * Add a form c, add an absence, approve the form, check the absence is
	 * pending, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormC35() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
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
	 * Add a form c, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is pending.
	 */
	@Test
	public void testApproveWithFormC36() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
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
	 * Add an absence, add a form a, approve the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormC37() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form c, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormC38() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form c, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormC39() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved. Same test, but with orphaned and rehearsal.
	 */
	@Test
	public void testApproveWithFormC41() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Absence a = ac.createOrUpdateAbsence(student, start.getTime(),
				end.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
		EventController ec = train.getEventController();
		Event e = ec.createOrUpdate(Event.Type.Performance, start.getTime(),
				end.getTime());
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/* same subset, just with tardies */
	/**
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC51() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		assertEquals(Absence.Status.Pending, a.getStatus());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form c, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormC52() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an approved absence, approve the form, check the
	 * absence is approved.
	 */
	@Test
	public void testApproveWithFormC53() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, update it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC54() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormC55() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is approved.
	 */
	@Test
	public void testApproveWithFormC56() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add an absence, add a form c, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC57() {
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
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form c, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormC58() {
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
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form c, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormC59() {
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
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/* same subset, just with earlycheckouts */
	/**
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC61() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form c, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormC62() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an approved absence, approve the form, check the
	 * absence is approved.
	 */
	@Test
	public void testApproveWithFormC63() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, update it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC64() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, update it, check that it is DENIED.
	 */
	@Test
	public void testApproveWithFormC65() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add a form c, add an absence, deny the form, check the absence is
	 * pending, approve the form, check the absence is approved.
	 */
	@Test
	public void testApproveWithFormC66() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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
	 * Add an absence, add a form c, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormC67() {
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
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form c, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormC68() {
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
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		form.setStatus(Form.Status.Denied);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add an absence, add a form c, do nothing to the form, check the absence
	 * is pending.
	 */
	@Test
	public void testApproveWithFormC69() {
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
		start.add(Calendar.MINUTE, 20);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * With two absences (tardy and EOC)
	 */
	@Test
	public void testApproveWithFormC71() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
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

	/* New cases to test intervals */

	/**
	 * event starting at midnight
	 */
	@Test
	public void testAbsence01() {
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
		start.set(2012, 7, 7, 0, 0, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 11, 59, 59);

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * event for entire day
	 * 
	 */
	@Test
	public void testAbsence02() {
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
		start.set(2012, 7, 7, 0, 0, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 23, 59, 59);

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * event during day
	 * 
	 */
	@Test
	public void testAbsence03() {
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
		start.set(2012, 7, 7, 13, 10, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 14, 59, 59);

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * event ends at midnight-1s
	 */
	@Test
	public void testAbsence04() {
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
		end.set(2012, 7, 7, 11, 59, 59);

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * form c for an absence also approves a tardy
	 */
	@Test
	public void testAbsence05() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		start.add(Calendar.MINUTE, 10);
		Absence a = ac.createOrUpdateTardy(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * form c for an absence also approves an EOC
	 */
	@Test
	public void testAbsence06() {
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

		Form form = fc.createFormC(student, date.getTime(),
				Absence.Type.Absence, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		start.add(Calendar.MINUTE, 60);
		Absence a = ac.createOrUpdateEarlyCheckout(student, start.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/*
	 * 
	 * tardy time = before time = start time = middle time = end time = after
	 * 
	 * absence range before range overlap start range == range within range
	 * overlap end range after range eclipses
	 * 
	 * eoc time = before time = start time = middle time = end time = after
	 */
	/**
	 * tardy time = before event
	 */
	@Test
	public void testTardy01() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 16, 0, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 40, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * tardy time = start of event
	 */
	@Test
	public void testTardy02() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 16, 30, 0);
		tardy.set(Calendar.MILLISECOND, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 30, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		start.set(Calendar.MILLISECOND, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * tardy time = middle of event
	 */
	@Test
	public void testTardy03() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 16, 40, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 40, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * tardy time = end of event
	 */
	@Test
	public void testTardy04() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 17, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 17, 50, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * tardy time = after event
	 */
	@Test
	public void testTardy05() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 18, 0, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 40, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * tardy form middle checkin before form time
	 */
	@Test
	public void testTardy06() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 16, 40, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 50, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * tardy form middle checkout on form time
	 */
	@Test
	public void testTardy07() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 16, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 50, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * tardy form middle checkin after form time
	 */
	@Test
	public void testTardy08() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 16, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 40, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * tardy form time before event
	 */
	@Test
	public void testTardy09() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 16, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * tardy form time after event
	 */
	@Test
	public void testTardy10() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 16, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 20, 40, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());

		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * eoc time = before event
	 */
	@Test
	public void testECO01() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eoc = Calendar.getInstance();
		eoc.set(2012, 7, 7, 16, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, eoc.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eoc.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		// pending because it's not during the absence
		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * eoc time = start of event
	 */
	@Test
	public void testECO02() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eoc = Calendar.getInstance();
		eoc.set(2012, 7, 7, 16, 30, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, eoc.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eoc.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * eoc time = middle of event
	 */
	@Test
	public void testECO03() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eoc = Calendar.getInstance();
		eoc.set(2012, 7, 7, 16, 50, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, eoc.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eoc.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * eoc time = end of event
	 */
	@Test
	public void testECO04() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eoc = Calendar.getInstance();
		eoc.set(2012, 7, 7, 17, 50, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, eoc.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eoc.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * eoc time = after event
	 */
	@Test
	public void testEOC05() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eoc = Calendar.getInstance();
		eoc.set(2012, 7, 7, 18, 30, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, eoc.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eoc.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * eoc form middle checkout before form time
	 */
	@Test
	public void testECO06() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eco = Calendar.getInstance();
		eco.set(2012, 7, 7, 16, 35, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 40, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eco.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * eoc form middle checkout on form time
	 */
	@Test
	public void testECO07() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eco = Calendar.getInstance();
		eco.set(2012, 7, 7, 16, 50, 0);
		eco.set(Calendar.MILLISECOND, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 50, 0);
		formtime.set(Calendar.MILLISECOND, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		start.set(Calendar.MILLISECOND, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);
		end.set(Calendar.MILLISECOND, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eco.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * eoc form middle checkout after form time
	 */
	@Test
	public void testECO08() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eco = Calendar.getInstance();
		eco.set(2012, 7, 7, 16, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 16, 40, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eco.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * eoc form time before event
	 */
	@Test
	public void testECO09() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eco = Calendar.getInstance();
		eco.set(2012, 7, 7, 16, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 13, 40, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eco.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * eoc form time after event
	 */
	@Test
	public void testECO10() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eco = Calendar.getInstance();
		eco.set(2012, 7, 7, 16, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 20, 40, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eco.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * tardy form, eco before time
	 */
	@Test
	public void testECOvsTardy01() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eco = Calendar.getInstance();
		eco.set(2012, 7, 7, 16, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 17, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eco.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * tardy form, eco after time
	 */
	@Test
	public void testECOvsTardy02() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eco = Calendar.getInstance();
		eco.set(2012, 7, 7, 17, 10, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 17, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateEarlyCheckout(student, eco.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * eco form, tardy after time
	 */
	@Test
	public void testECOvsTardy03() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 17, 10, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 17, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * eco form, tardy before time
	 */
	@Test
	public void testECOvsTardy04() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar tardy = Calendar.getInstance();
		tardy.set(2012, 7, 7, 16, 50, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 17, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateTardy(student, tardy.getTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * tardy form, absence absence
	 */
	@Test
	public void testTardyvsAbsence01() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eco = Calendar.getInstance();
		eco.set(2012, 7, 7, 17, 10, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 17, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.Tardy, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}

	/**
	 * tardy form, absence absence
	 */
	@Test
	public void testECOvsAbsence01() {
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		EventController ec = train.getEventController();
		AbsenceController ac = train.getAbsenceController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar eco = Calendar.getInstance();
		eco.set(2012, 7, 7, 17, 10, 0);
		Calendar formtime = Calendar.getInstance();
		formtime.set(2012, 7, 7, 17, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormC(student, formtime.getTime(),
				Absence.Type.EarlyCheckOut, "reason", false);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, start.getTime(),
				end.getTime());
		Absence a = ac.createOrUpdateAbsence(student, e);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, a.getStatus());
	}
}
