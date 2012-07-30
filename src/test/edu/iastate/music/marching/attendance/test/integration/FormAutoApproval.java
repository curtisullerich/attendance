package edu.iastate.music.marching.attendance.test.integration;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;

public class FormAutoApproval extends AbstractTest {
	
	private final static TimeZone TIMEZONE = TimeZone.getTimeZone("UTC");

	@Test
	public void AutoApproveAbsence_Succeeds_FormA_FormSecond() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		Calendar c = getFixedCalendar();
		c.roll(Calendar.HOUR, 1);

		// Create a 1 hour event for absence
		train.getEventController().createOrUpdate(Event.Type.Performance,
				getFixedCalendar().getTime(), c.getTime());

		// Create a 1 hour absence
		Absence a = train.getAbsenceController().createOrUpdateAbsence(
				student1, getFixedCalendar().getTime(), c.getTime());

		// Verify absence is pending and able to be approved
		assertEquals(Absence.Status.Pending, a.getStatus());

		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1,
				getFixedCalendar().getTime(), "reason");
		train.getFormsController().approve(form);

		// Verify both form and absence are now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	@Test
	public void AutoApproveAbsence_Succeeds_FormA_FormFirst() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		Calendar c = Calendar.getInstance();
		c.setTime(getFixedCalendar().getTime());
		c.roll(Calendar.HOUR, 1);

		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1,
				getFixedCalendar().getTime(), "reason");
		train.getFormsController().approve(form);

		// Create a 1 hour event for absence
		train.getEventController().createOrUpdate(Event.Type.Performance,
				getFixedCalendar().getTime(), c.getTime());

		// Create a 1 hour absence
		Absence a = train.getAbsenceController().createOrUpdateAbsence(
				student1, getFixedCalendar().getTime(), c.getTime());

		// Verify both form and absence are now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	@Test
	public void AutoApproveAbsence_Succeeds_FormB_ExactOverlap() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		Calendar c = Calendar.getInstance();
		c.setTime(getFixedCalendar().getTime());
		c.roll(Calendar.HOUR, 1);

		// Create a 1 hour event for absence
		train.getEventController().createOrUpdate(Event.Type.Performance,
				getFixedCalendar().getTime(), c.getTime());

		// Create a 1 hour absence
		Absence a = train.getAbsenceController().createOrUpdateAbsence(
				student1, getFixedCalendar().getTime(), c.getTime());

		// Verify absence is pending and able to be approved
		assertEquals(Absence.Status.Pending, a.getStatus());

		// Create and approve form to excuse absence
		int day = c.get(Calendar.DAY_OF_WEEK);
		Form form = train.getFormsController().createFormB(student1,
				"department", "course", "section", "building", getFixedCalendar().getTime(),
				getFixedCalendar().getTime(), day, getFixedCalendar().getTime(), c.getTime(), "details", 0,
				Absence.Type.Absence);
		train.getFormsController().approve(form);

		// Verify both form and absence are now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	@Test
	public void AutoApproveAbsence_FromFormC() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		Calendar c = Calendar.getInstance();
		c.setTime(getFixedCalendar().getTime());
		c.roll(Calendar.HOUR, 1);

		// Create a 1 hour event for absence
		train.getEventController().createOrUpdate(Event.Type.Performance,
				getFixedCalendar().getTime(), c.getTime());

		// Create a 1 hour absence
		Absence a = train.getAbsenceController().createOrUpdateAbsence(
				student1, getFixedCalendar().getTime(), c.getTime());

		// Verify absence is pending and able to be approved
		assertEquals(Absence.Status.Pending, a.getStatus());

		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1,
				getFixedCalendar().getTime(), "reason");
		train.getFormsController().approve(form);

		// Verify both form and absence are now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	@Test
	public void AutoApproveAbsence_Succeeds_FromFormD() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		Calendar c = Calendar.getInstance();
		c.setTime(getFixedCalendar().getTime());
		c.roll(Calendar.HOUR, 1);

		// Create a 1 hour event for absence
		train.getEventController().createOrUpdate(Event.Type.Performance,
				getFixedCalendar().getTime(), c.getTime());

		// Create a 1 hour absence
		Absence a = train.getAbsenceController().createOrUpdateAbsence(
				student1, getFixedCalendar().getTime(), c.getTime());

		// Verify absence is pending and able to be approved
		assertEquals(Absence.Status.Pending, a.getStatus());

		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1,
				getFixedCalendar().getTime(), "reason");
		train.getFormsController().approve(form);

		// Verify both form and absence are now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Approved, a.getStatus());
	}

	@Test
	public void ApproveDeniedAbsence_Fails_FromFormA() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		Calendar c = Calendar.getInstance();
		c.setTime(getFixedCalendar().getTime());
		c.roll(Calendar.HOUR, 1);

		// Create a 1 hour event for absence
		train.getEventController().createOrUpdate(Event.Type.Performance,
				getFixedCalendar().getTime(), c.getTime());

		// Create a 1 hour absence
		Absence a = train.getAbsenceController().createOrUpdateAbsence(
				student1, getFixedCalendar().getTime(), c.getTime());
		a.setStatus(Absence.Status.Denied);

		// Verify absence is denied and unable to be approved
		assertEquals(Absence.Status.Denied, a.getStatus());

		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1,
				getFixedCalendar().getTime(), "reason");
		train.getFormsController().approve(form);

		// Verify only form is now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	@Test
	public void ApproveDeniedAbsence_Fails_FromFormB() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		Calendar c = Calendar.getInstance();
		c.setTime(getFixedCalendar().getTime());
		c.roll(Calendar.HOUR, 1);

		// Create a 1 hour event for absence
		train.getEventController().createOrUpdate(Event.Type.Performance,
				getFixedCalendar().getTime(), c.getTime());

		// Create a 1 hour absence
		Absence a = train.getAbsenceController().createOrUpdateAbsence(
				student1, getFixedCalendar().getTime(), c.getTime());

		a.setStatus(Absence.Status.Denied);

		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1,
				getFixedCalendar().getTime(), "reason");
		train.getFormsController().approve(form);

		// Verify only form is now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	@Test
	public void ApproveDeniedAbsence_Fails_FromFormC() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		Calendar c = Calendar.getInstance();
		c.setTime(getFixedCalendar().getTime());
		c.roll(Calendar.HOUR, 1);

		// Create a 1 hour event for absence
		train.getEventController().createOrUpdate(Event.Type.Performance,
				getFixedCalendar().getTime(), c.getTime());

		// Create a 1 hour absence
		Absence a = train.getAbsenceController().createOrUpdateAbsence(
				student1, getFixedCalendar().getTime(), c.getTime());

		a.setStatus(Absence.Status.Denied);

		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1,
				getFixedCalendar().getTime(), "reason");
		train.getFormsController().approve(form);

		// Verify only form is now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	@Test
	public void ApproveDeniedAbsence_Fails_FromFormD() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		Calendar c = Calendar.getInstance();
		c.setTime(getFixedCalendar().getTime());
		c.roll(Calendar.HOUR, 1);

		// Create a 1 hour event for absence
		train.getEventController().createOrUpdate(Event.Type.Performance,
				getFixedCalendar().getTime(), c.getTime());

		// Create a 1 hour absence
		Absence a = train.getAbsenceController().createOrUpdateAbsence(
				student1, getFixedCalendar().getTime(), c.getTime());

		a.setStatus(Absence.Status.Denied);

		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1,
				getFixedCalendar().getTime(), "reason");
		train.getFormsController().approve(form);

		// Verify only form is now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Denied, a.getStatus());
	}

	private User student1;

	private User student2;

	private User student3;

	private User ta;

	private void createUsersHelper() {
		UserController uc = DataTrain.getAndStartTrain().getUsersController();

		student1 = Users.createStudent(uc, "student1", "123456789", "First",
				"Last", 1, "major", User.Section.AltoSax);

		student2 = Users.createStudent(uc, "student2", "123456781", "First",
				"Last", 2, "major", User.Section.AltoSax);

		student3 = Users.createStudent(uc, "student3", "123456782", "First",
				"Last", 3, "major", User.Section.AltoSax);

		ta = Users.createTA(uc, "ta", "123456783", "Fisrt", "Last", 4, "major",
				User.Section.AltoSax);
	}

	private Calendar getFixedCalendar() {
		Calendar c = Calendar.getInstance(TIMEZONE);
		c.set(2004, 8, 20, 17, 42, 51);
		c.set(Calendar.MILLISECOND, 555);
		return c;
	}
}
