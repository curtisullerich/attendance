package edu.iastate.music.marching.attendance.test.integration;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;

public class FormSubmission extends AbstractTest {
	
	private static final Date ABSOLUTE_DATE = new Date();

	@Test
	public void AutoApproveAbsence_FromFormA() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		// Create a 1 hour absence
		Calendar c = Calendar.getInstance();
		c.setTime(ABSOLUTE_DATE);
		c.roll(Calendar.HOUR, 1);
		Absence a = train.getAbsenceController().createOrUpdateAbsence(student1, ABSOLUTE_DATE, c.getTime());
		
		// Verify absence is pending and able to be approved
		assertEquals(Absence.Status.Pending, a.getStatus());
		
		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1, ABSOLUTE_DATE, "reason");
		train.getFormsController().approve(form);
		
		// Verify both form and absence are now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Approved, a.getStatus());
	}
	
	@Test
	public void AutoApproveAbsence_FromFormB() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		// Create a 1 hour absence
		Calendar c = Calendar.getInstance();
		c.setTime(ABSOLUTE_DATE);
		c.roll(Calendar.HOUR, 1);
		Absence a = train.getAbsenceController().createOrUpdateAbsence(student1, ABSOLUTE_DATE, c.getTime());
		
		// Verify absence is pending and able to be approved
		assertEquals(Absence.Status.Pending, a.getStatus());
		
		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1, ABSOLUTE_DATE, "reason");
		train.getFormsController().approve(form);
		
		// Verify both form and absence are now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Approved, a.getStatus());
	}
	
	@Test
	public void AutoApproveAbsence_FromFormC() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		// Create a 1 hour absence
		Calendar c = Calendar.getInstance();
		c.setTime(ABSOLUTE_DATE);
		c.roll(Calendar.HOUR, 1);
		Absence a = train.getAbsenceController().createOrUpdateAbsence(student1, ABSOLUTE_DATE, c.getTime());
		
		// Verify absence is pending and able to be approved
		assertEquals(Absence.Status.Pending, a.getStatus());
		
		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1, ABSOLUTE_DATE, "reason");
		train.getFormsController().approve(form);
		
		// Verify both form and absence are now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Approved, a.getStatus());
	}
	
	@Test
	public void AutoApproveAbsence_FromFormD() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		// Create a 1 hour absence
		Calendar c = Calendar.getInstance();
		c.setTime(ABSOLUTE_DATE);
		c.roll(Calendar.HOUR, 1);
		Absence a = train.getAbsenceController().createOrUpdateAbsence(student1, ABSOLUTE_DATE, c.getTime());
		
		// Verify absence is pending and able to be approved
		assertEquals(Absence.Status.Pending, a.getStatus());
		
		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1, ABSOLUTE_DATE, "reason");
		train.getFormsController().approve(form);
		
		// Verify both form and absence are now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Approved, a.getStatus());
	}
	
	@Test
	public void ApproveDeniedAbsence_FromFormA() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		// Create a 1 hour absence
		Calendar c = Calendar.getInstance();
		c.setTime(ABSOLUTE_DATE);
		c.roll(Calendar.HOUR, 1);
		Absence a = train.getAbsenceController().createOrUpdateAbsence(student1, ABSOLUTE_DATE, c.getTime());
		a.setStatus(Absence.Status.Denied);
		
		// Verify absence is denied and unable to be approved
		assertEquals(Absence.Status.Denied, a.getStatus());
		
		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1, ABSOLUTE_DATE, "reason");
		train.getFormsController().approve(form);
		
		// Verify only form is now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Denied, a.getStatus());
	}
	
	@Test
	public void ApproveDeniedAbsence_FromFormB() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		// Create a 1 hour absence
		Calendar c = Calendar.getInstance();
		c.setTime(ABSOLUTE_DATE);
		c.roll(Calendar.HOUR, 1);
		Absence a = train.getAbsenceController().createOrUpdateAbsence(student1, ABSOLUTE_DATE, c.getTime());
		a.setStatus(Absence.Status.Denied);
		
		// Verify absence is denied and unable to be approved
		assertEquals(Absence.Status.Denied, a.getStatus());
		
		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1, ABSOLUTE_DATE, "reason");
		train.getFormsController().approve(form);
		
		// Verify only form is now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Denied, a.getStatus());
	}
	
	@Test
	public void ApproveDeniedAbsence_FromFormC() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		// Create a 1 hour absence
		Calendar c = Calendar.getInstance();
		c.setTime(ABSOLUTE_DATE);
		c.roll(Calendar.HOUR, 1);
		Absence a = train.getAbsenceController().createOrUpdateAbsence(student1, ABSOLUTE_DATE, c.getTime());
		a.setStatus(Absence.Status.Denied);
		
		// Verify absence is denied and unable to be approved
		assertEquals(Absence.Status.Denied, a.getStatus());
		
		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1, ABSOLUTE_DATE, "reason");
		train.getFormsController().approve(form);
		
		// Verify only form is now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Denied, a.getStatus());
	}
	
	@Test
	public void ApproveDeniedAbsence_FromFormD() {
		createUsersHelper();
		DataTrain train = DataTrain.getAndStartTrain();

		// Create a 1 hour absence
		Calendar c = Calendar.getInstance();
		c.setTime(ABSOLUTE_DATE);
		c.roll(Calendar.HOUR, 1);
		Absence a = train.getAbsenceController().createOrUpdateAbsence(student1, ABSOLUTE_DATE, c.getTime());
		a.setStatus(Absence.Status.Denied);
		
		// Verify absence is denied and unable to be approved
		assertEquals(Absence.Status.Denied, a.getStatus());
		
		// Create and approve form to excuse absence
		Form form = train.getFormsController().createFormA(student1, ABSOLUTE_DATE, "reason");
		train.getFormsController().approve(form);
		
		// Verify only form is now approved
		assertEquals(Form.Status.Approved, form.getStatus());
		assertEquals(Absence.Status.Denied, a.getStatus());
	}
	
	private User student1;
	
	private User student2;
	
	private User student3;
	
	private User ta;

	private void createUsersHelper()
	{
		UserController uc = DataTrain.getAndStartTrain().getUsersController();
		
		student1 = createStudent(uc, "student1", 55055050, "First", "Last", 1, "major", User.Section.AltoSax);
		
		student2 = createStudent(uc, "student2", 55055052, "First", "Last", 2, "major", User.Section.AltoSax);
		
		student3 = createStudent(uc, "student3", 55055053, "First", "Last", 3, "major", User.Section.AltoSax);
		
		ta = createTA(uc, "ta", 55050050, "Fisrt", "Last", 4, "major", User.Section.AltoSax);
	}

}
