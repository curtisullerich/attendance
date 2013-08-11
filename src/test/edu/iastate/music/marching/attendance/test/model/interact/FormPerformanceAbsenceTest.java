package edu.iastate.music.marching.attendance.test.model.interact;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
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
import edu.iastate.music.marching.attendance.testlib.Dates;
import edu.iastate.music.marching.attendance.testlib.TestUsers;

@SuppressWarnings("deprecation")
public class FormPerformanceAbsenceTest extends AbstractDatastoreTest {

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

	// ANCHORED
	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA1() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");
		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
		Absence a = ac.createOrUpdateAbsence(student, e);

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, upDateTime it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA4() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");
		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
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
	 * approved, set the absence as DENIED, upDateTime it, check that it is
	 * DENIED.
	 */
	@Test
	public void testApproveWithFormA5() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");
		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");
		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");
		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
		Absence a = ac.createOrUpdateAbsence(student, e);

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");
		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateAbsence(student, e);

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");
		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateAbsence(student, e);

		fc.createPerformanceAbsenceForm(student, date, "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	// * Same tests, just ORPHANED (using the deprecated method that doesn't
	// take
	// * an event as a param)

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA11() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
		EventManager ec = train.events();
		ec.createOrUpdate(Event.Type.Performance, eventInterval);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add a form A, add an absence, deny the absence, approve the form, check
	 * the absence is still denied.
	 */
	@Test
	public void testApproveWithFormA12() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);

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

		UserManager uc = train.users();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, upDateTime it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA14() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);

		EventManager ec = train.events();
		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Pending);
		ac.updateAbsence(a);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, upDateTime it, check that it is
	 * DENIED.
	 */
	@Test
	public void testApproveWithFormA15() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);
		ec.createOrUpdate(Event.Type.Performance, eventInterval);

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);

		form.setStatus(Form.Status.Denied);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
		ec.createOrUpdate(Event.Type.Performance, eventInterval);

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		ec.createOrUpdate(Event.Type.Performance, eventInterval);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());
	}

	/**
	 * Add an absence, add a form A, deny the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA18() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

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

		UserManager uc = train.users();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);

		fc.createPerformanceAbsenceForm(student, date, "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	// * Subset of the original tests, with a different student on the form and
	// * absence

	/**
	 * Add a form A with student 1, add an absence with student 2, approve the
	 * form, check the absence is pending.
	 */
	@Test
	public void testApproveWithFormA21() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);
		User student2 = TestUsers.createStudent(uc, "student2", "123456781",
				"John", "Cox", 2, "major", User.Section.AltoSax);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student2, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);
		User student2 = TestUsers.createStudent(uc, "student2", "123456781",
				"John", "Cox", 2, "major", User.Section.AltoSax);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);
		User student2 = TestUsers.createStudent(uc, "student2", "123456781",
				"John", "Cox", 2, "major", User.Section.AltoSax);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		fc.createPerformanceAbsenceForm(student, date, "I love band.");
		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateAbsence(student2, e);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
	}

	// Tests the same set, but with a rehearsal absence instead of a performance
	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA31() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
		Absence a = ac.createOrUpdateAbsence(student, e);

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * pending, set the absence as pending, upDateTime it, check that it is
	 * pending.
	 */
	@Test
	public void testApproveWithFormA34() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
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
	 * pending, set the absence as DENIED, upDateTime it, check that it is
	 * DENIED.
	 */
	@Test
	public void testApproveWithFormA35() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
		Absence a = ac.createOrUpdateAbsence(student, e);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
		Absence a = ac.createOrUpdateAbsence(student, e);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Event e = ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
		Absence a = ac.createOrUpdateAbsence(student, e);

		fc.createPerformanceAbsenceForm(student, date, "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved. Same test, but with orphaned and rehearsal.
	 */
	@Test
	public void testApproveWithFormA41() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());
		EventManager ec = train.events();
		ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);
		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	// same subset, just with tardies
	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA51() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateTardy(student,
				Dates.getDefaultAdjustedStartTime());

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateTardy(student,
				Dates.getDefaultAdjustedStartTime());

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateTardy(student,
				Dates.getDefaultAdjustedStartTime());

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, upDateTime it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA54() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateTardy(student,
				Dates.getDefaultAdjustedStartTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Pending);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Approved, a.getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, upDateTime it, check that it is
	 * DENIED.
	 */
	@Test
	public void testApproveWithFormA55() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateTardy(student,
				Dates.getDefaultAdjustedStartTime());

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateTardy(student,
				Dates.getDefaultAdjustedStartTime());

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateTardy(student,
				Dates.getDefaultAdjustedStartTime());

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateTardy(student,
				Dates.getDefaultAdjustedStartTime());

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateTardy(student,
				Dates.getDefaultAdjustedStartTime());

		fc.createPerformanceAbsenceForm(student, date, "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	// same subset, just with earlycheckouts
	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA61() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);
		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

		a.setStatus(Absence.Status.Approved);
		a = ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as pending, upDateTime it, check that it is
	 * approved.
	 */
	@Test
	public void testApproveWithFormA64() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);
		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(Absence.Status.Approved, ac.get(a.getId()).getStatus());

		a.setStatus(Absence.Status.Pending);
		a = ac.updateAbsence(a);
		assertEquals(Absence.Status.Approved, a.getStatus());

	}

	/**
	 * Add a form A, add an absence, approve the form, check the absence is
	 * approved, set the absence as DENIED, upDateTime it, check that it is
	 * DENIED.
	 */
	@Test
	public void testApproveWithFormA65() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		ec.createOrUpdate(Event.Type.Performance, eventInterval);
		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

		a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

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

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");
		Event e = ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());

		fc.createPerformanceAbsenceForm(student, date, "I love band.");

		assertEquals(Absence.Status.Pending, ac.get(a.getId()).getStatus());

	}

	/**
	 * With two absences (tardy and EOC)
	 */
	@Test
	public void testApproveWithFormA71() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = Dates.getDefaultLocalDate();
		Interval eventInterval = Dates.getDefaultEventInterval(date);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"I love band.");

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Absence a = ac.createOrUpdateEarlyCheckout(student,
				Dates.getDefaultAdjustedStartTime());
		Absence b = ac.createOrUpdateTardy(student, Dates
				.getDefaultAdjustedStartTime().minusMinutes(15));

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(Absence.Status.Approved, a.getStatus());
		assertEquals(Absence.Status.Approved, b.getStatus());
	}

	@Test
	public void testNonAutoApproveOnDeniedAbsenceFormSecond() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();
		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = new LocalDate(2012, 9, 21);
		LocalTime end = new LocalTime(7, 0, 0);
		LocalTime start = new LocalTime(6, 0, 0);

		DateTimeZone zone = DataTrain.depart().appData().get().getTimeZone();
		Interval eventInterval = new Interval(date.toLocalDateTime(start)
				.toDateTime(zone), date.toLocalDateTime(end).toDateTime(zone));

		ec.createOrUpdate(Event.Type.Performance, eventInterval);
		Absence a = ac.createOrUpdateAbsence(student, eventInterval);
		a.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a);

		Form form = fc.createPerformanceAbsenceForm(student, date,
				"Oh god, how did I get in here, I am a cat :3");
		form.setStatus(Form.Status.Approved);
		fc.update(form);

		List<Absence> abs = ac.get(student);

		assertEquals(Absence.Status.Denied, abs.get(0).getStatus());
	}

	@Test
	public void testNonAutoApproveOnDeniedAbsenceFormFirst() {
		DataTrain train = getDataTrain();

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();
		User student = TestUsers.createDefaultStudent(uc);

		LocalDate date = new LocalDate(2012, 9, 21);
		LocalTime end = new LocalTime(7, 0, 0);
		LocalTime start = new LocalTime(6, 0, 0);

		DateTimeZone zone = DataTrain.depart().appData().get().getTimeZone();
		Interval eventInterval = new Interval(date.toLocalDateTime(start)
				.toDateTime(zone), date.toLocalDateTime(end).toDateTime(zone));

		ec.createOrUpdate(Event.Type.Performance, eventInterval);

		Form form = fc.createPerformanceAbsenceForm(student, date, "Meow!");

		Absence a = ac.createOrUpdateAbsence(student, eventInterval);
		a.setStatus(Absence.Status.Denied);
		ac.updateAbsence(a);

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		List<Absence> abs = ac.get(student);

		assertEquals(Absence.Status.Denied, abs.get(0).getStatus());
	}
}
