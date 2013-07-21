package edu.iastate.music.marching.attendance.test.unit.model.interact;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import edu.iastate.music.marching.attendance.model.interact.AppDataManager;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.FormManager;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.AppData;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;

@SuppressWarnings("deprecation")
public class FormDTest extends AbstractTest {
/*
	// note that form D does not autoapprove, because we can't know which
	// absence SHOULD be approved. We instead increment the number of available
	// minutes in the student object.

	// just need to test that the form is approved.
	// if it's approved twice, the minutes should not increment further

	*//**
	 * Approve, test that the minutes increment.
	 *//*
	@Test
	public void testApprove() {
		DataTrain train = getDataTrain();
		AppDataManager adc = train.getAppDataManager();
		AppData ad = adc.get();
		adc.save(ad);

		UserManager uc = train.getUsersManager();
		FormManager fc = train.getFormsManager();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar DateTime = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createTimeWorkedForm(student, date.getTime(), 10, "details");

		assertEquals(0, student.getMinutesAvailable());
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());
	}

	*//**
	 * Test that it fails to increment twice when you try to approve a form
	 * twice
	 *//*
	@Test
	public void testApproveTwice() {
		DataTrain train = getDataTrain();
		AppDataManager adc = train.getAppDataManager();
		AppData ad = adc.get();
		adc.save(ad);

		UserManager uc = train.getUsersManager();
		FormManager fc = train.getFormsManager();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar DateTime = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createTimeWorkedForm(student, date.getTime(), 10, "details");

		assertEquals(0, student.getMinutesAvailable());
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

	}

	*//**
	 * Test that it fails to increment twice when you try to deny after
	 * approving
	 *//*
	@Test
	public void testApproveDeny() {
		DataTrain train = getDataTrain();
		AppDataManager adc = train.getAppDataManager();
		AppData ad = adc.get();
		adc.save(ad);

		UserManager uc = train.getUsersManager();
		FormManager fc = train.getFormsManager();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar DateTime = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createTimeWorkedForm(student, date.getTime(), 10, "details");

		assertEquals(0, student.getMinutesAvailable());
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

		form.setStatus(Form.Status.Denied);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());
	}*/
}
