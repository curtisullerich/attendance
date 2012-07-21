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

public class EventControllerTest extends AbstractTest {

	@Test
	public void testCreateEvent() throws ParseException {
		// Arrange
		DataTrain train = getDataTrain();

		Date eventStart = null;
		Date eventEnd = null;

		eventStart = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0500");
		eventEnd = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0700");

		// Act
		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		// Assert
		List<Event> events = train.getEventController().getAll();

		assertEquals(1, events.size());

		Event e = events.get(0);

		assertEquals(eventStart, e.getStart());
		assertEquals(eventEnd, e.getEnd());
		assertEquals(Event.Type.Performance, e.getType());
	}

	@Test
	public void testCreateDuplicateEvents() throws ParseException {

		// Arrange
		DataTrain train = getDataTrain();

		Date eventStart = null;
		Date eventEnd = null;

		eventStart = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0500");
		eventEnd = new SimpleDateFormat("yyyy-MM-dd HHmm")
				.parse("2012-06-16 0700");

		// Act
		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);
		train.getEventController().createOrUpdate(Event.Type.Performance,
				eventStart, eventEnd);

		// Assert
		List<Event> events = train.getEventController().getAll();

		assertEquals(1, events.size());

		Event e = events.get(0);

		assertEquals(eventStart, e.getStart());
		assertEquals(eventEnd, e.getEnd());
		assertEquals(Event.Type.Performance, e.getType());
	}

}
