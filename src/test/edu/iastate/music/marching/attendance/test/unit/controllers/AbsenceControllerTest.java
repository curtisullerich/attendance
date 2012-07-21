package edu.iastate.music.marching.attendance.test.unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;
import edu.iastate.music.marching.attendance.test.util.Users;

public class AbsenceControllerTest extends AbstractTest {


	@Test
	public void testCreateAbsence() {
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", 121, "First", "last", 2, "major", User.Section.AltoSax);
		
		Date eventStart = null;
		Date eventEnd = null;
		try {
			eventStart = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2012-06-16 0400");
			eventEnd = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2012-06-16 0600");
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		train.getEventController().createOrUpdate(Event.Type.Performance, eventStart, eventEnd);
		train.getAbsenceController().createOrUpdateAbsence(student, eventStart, eventEnd);
		
		List<Absence> studentAbsences = train.getAbsenceController().get(student);
		
		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);
		
		assertTrue(absence.getStart().equals(eventStart));
		assertTrue(absence.getEnd().equals(eventEnd));
		assertTrue(absence.getType() == Absence.Type.Absence);
	}
	
	@Test
	public void testAbsenceVsAbsence() {
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", 121, "First", "last", 2, "major", User.Section.AltoSax);
		
		Date eventStart = null;
		Date eventEnd = null;
		
		Date contesterStart = null;
		Date contesterEnd = null;
		try {
			eventStart = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2012-06-16 0500");
			eventEnd = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2012-06-16 0700");
			
			contesterStart = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2012-06-16 0500");
			contesterEnd = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2012-06-16 0700");
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}
		
		createAbsence(train, student, eventStart, eventEnd);
		createAbsence(train, student, contesterStart, contesterEnd);
		
		List<Absence> studentAbsences = train.getAbsenceController().get(student);
		
		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);
		
		assertTrue(absence.getStart().equals(eventStart));
		assertTrue(absence.getEnd().equals(eventEnd));
		assertTrue(absence.getType() == Absence.Type.Absence);
	}
	
	@Test
	public void testAbsenceVsTardy() throws ParseException {
		DataTrain train = getDataTrain();
		
		UserController uc = train.getUsersController();
		User student = Users.createStudent(uc, "studenttt", 121, "First", "last", 2, "major", User.Section.AltoSax);
		
		Date eventStart = null;
		Date eventEnd = null;
		Date tardy = null;
		
		eventStart = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2012-06-16 0500");
		eventEnd = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2012-06-16 0700");
		
		tardy = new SimpleDateFormat("yyyy-MM-dd HHmm").parse("2012-06-16 0515");
		
		createAbsence(train, student, eventStart, eventEnd);
		createTardy(train, student, eventStart, eventEnd, tardy);
		
		List<Absence> studentAbsences = train.getAbsenceController().get(student);
		
		assertEquals(1, studentAbsences.size());
		Absence absence = studentAbsences.get(0);
		
		assertTrue(absence.getStart().equals(tardy));
		assertTrue(absence.getType() == Absence.Type.Tardy);
	}
	
	private Absence createAbsence(DataTrain train, User student, Date start, Date end) {
		train.getEventController().createOrUpdate(Event.Type.Performance, start, end);
		return train.getAbsenceController().createOrUpdateAbsence(student, start, end);
	}
	
	private Absence createTardy(DataTrain train, User student, Date eventStart, 
			Date eventEnd, Date date) {
		train.getEventController().createOrUpdate(Event.Type.Performance, eventStart, eventEnd);
		return train.getAbsenceController().createOrUpdateTardy(student, date);
	}
}
