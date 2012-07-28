package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.AppDataController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.FormController;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;
import edu.iastate.music.marching.attendance.util.ValidationExceptions;

@SuppressWarnings("deprecation")
public class FormDTest extends AbstractTest {

	// note that form D does not autoapprove, because we can't know which
	// absence SHOULD be approved. We instead increment the number of available
	// minutes in the student object.

	// just need to test that the form is emailapproved and approved.
	// if it's approved twice, the minutes should not increment further

	/**
	 * Approve and emailapprove, test that the minutes increment.
	 */
	@Test
	public void testApprove() {
		DataTrain train = getDataTrain();
		AppDataController adc = train.getAppDataController();
		AppData ad = adc.get();
		List<String> emails = new ArrayList<String>();
		emails.add("user@domain.com");
		ad.setTimeWorkedEmails(emails);
		adc.save(ad);

		UserController uc = train.getUsersController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormD(student, "user@domain.com", date.getTime(),
				10, "details");

		assertEquals(0, student.getMinutesAvailable());
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(0, student.getMinutesAvailable());

		form.setEmailStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());
	}

	/**
	 * test that exception is thrown when an email is used that doesn't exist
	 */
	@Test
	public void testWrongEmail() {
		DataTrain train = getDataTrain();
		AppDataController adc = train.getAppDataController();
		AppData ad = adc.get();
		List<String> emails = new ArrayList<String>();
		emails.add("user@domain.com");
		ad.setTimeWorkedEmails(emails);
		adc.save(ad);

		UserController uc = train.getUsersController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		boolean caught = false;
		try {
			Form form = fc.createFormD(student, "user2@domain.com",
					date.getTime(), 10, "details");
		} catch (ValidationExceptions v) {
			caught = true;
		}
		assertEquals(true, caught);
	}

	/**
	 * Test that it fails to increment twice when you try to emailapprove a form
	 * twice
	 */
	@Test
	public void testEmailApproveTwice() {
		DataTrain train = getDataTrain();
		AppDataController adc = train.getAppDataController();
		AppData ad = adc.get();
		List<String> emails = new ArrayList<String>();
		emails.add("user@domain.com");
		ad.setTimeWorkedEmails(emails);
		adc.save(ad);

		UserController uc = train.getUsersController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormD(student, "user@domain.com", date.getTime(),
				10, "details");

		assertEquals(0, student.getMinutesAvailable());
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(0, student.getMinutesAvailable());

		form.setEmailStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

		form.setEmailStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

	}

	/**
	 * Test that it fails to increment twice when you try to emaildeny after
	 * emailapproving
	 */
	@Test
	public void testEmailApproveDeny() {
		DataTrain train = getDataTrain();
		AppDataController adc = train.getAppDataController();
		AppData ad = adc.get();
		List<String> emails = new ArrayList<String>();
		emails.add("user@domain.com");
		ad.setTimeWorkedEmails(emails);
		adc.save(ad);

		UserController uc = train.getUsersController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormD(student, "user@domain.com", date.getTime(),
				10, "details");

		assertEquals(0, student.getMinutesAvailable());
		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(0, student.getMinutesAvailable());

		form.setEmailStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

		form.setEmailStatus(Form.Status.Denied);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

		form.setEmailStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());
	}

	/**
	 * Test that it fails to increment twice when you try to approve a form
	 * twice
	 */
	@Test
	public void testApproveTwice() {
		DataTrain train = getDataTrain();
		AppDataController adc = train.getAppDataController();
		AppData ad = adc.get();
		List<String> emails = new ArrayList<String>();
		emails.add("user@domain.com");
		ad.setTimeWorkedEmails(emails);
		adc.save(ad);

		UserController uc = train.getUsersController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormD(student, "user@domain.com", date.getTime(),
				10, "details");

		assertEquals(0, student.getMinutesAvailable());
		form.setEmailStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(0, student.getMinutesAvailable());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

		form.setStatus(Form.Status.Approved);
		fc.update(form);
		assertEquals(10, student.getMinutesAvailable());

	}

	/**
	 * Test that it fails to increment twice when you try to deny after
	 * approving
	 */
	@Test
	public void testApproveDeny() {
		DataTrain train = getDataTrain();
		AppDataController adc = train.getAppDataController();
		AppData ad = adc.get();
		List<String> emails = new ArrayList<String>();
		emails.add("user@domain.com");
		ad.setTimeWorkedEmails(emails);
		adc.save(ad);

		UserController uc = train.getUsersController();
		FormController fc = train.getFormsController();

		User student = Users.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		Calendar date = Calendar.getInstance();
		date.set(2012, 7, 7, 0, 0, 0);
		Calendar start = Calendar.getInstance();
		start.set(2012, 7, 7, 16, 30, 0);
		Calendar end = Calendar.getInstance();
		end.set(2012, 7, 7, 17, 50, 0);

		Form form = fc.createFormD(student, "user@domain.com", date.getTime(),
				10, "details");

		assertEquals(0, student.getMinutesAvailable());
		form.setEmailStatus(Form.Status.Approved);
		fc.update(form);
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
	}
}
