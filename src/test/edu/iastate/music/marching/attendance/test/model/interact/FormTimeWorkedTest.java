package edu.iastate.music.marching.attendance.test.model.interact;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.Test;

import edu.iastate.music.marching.attendance.model.interact.AppDataManager;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.FormManager;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.AppData;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.testlib.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.testlib.TestUsers;

public class FormTimeWorkedTest extends AbstractDatastoreTest {
/*
	// note that form D does not autoapprove, because we can't know which
	// absence SHOULD be approved. We instead increment the number of available
	// minutes in the student object.

	// just need to test that the form is approved.
	// if it's approved twice, the minutes should not increment further

	*//**
	 * Approve, test that the minutes increment.
	 */
	@Test
	public void testApprove() {
		DataTrain train = getDataTrain();
		AppDataManager adc = train.appData();
		AppData ad = adc.get();
		adc.save(ad);

		UserManager uc = train.users();
		FormManager fc = train.forms();

		User student = TestUsers.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		LocalDate date = new LocalDate(2012, 7, 7);

		Form form = fc.createTimeWorkedForm(student, date, 20, "details");

		assertEquals(0, student.getMinutesAvailable());
		form.setStatus(Form.Status.Approved);
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
		AppDataManager adc = train.appData();
		AppData ad = adc.get();
		adc.save(ad);

		UserManager uc = train.users();
		FormManager fc = train.forms();

		User student = TestUsers.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		LocalDate date = new LocalDate(2012, 7, 7);

		Form form = fc.createTimeWorkedForm(student, date, 20, "details");

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
		AppDataManager adc = train.appData();
		AppData ad = adc.get();
		adc.save(ad);

		UserManager uc = train.users();
		FormManager fc = train.forms();

		User student = TestUsers.createStudent(uc, "student1", "123456789", "John",
				"Cox", 2, "major", User.Section.AltoSax);

		LocalDate date = new LocalDate(2012, 7, 7);

		Form form = fc.createTimeWorkedForm(student, date, 20, "details");

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
