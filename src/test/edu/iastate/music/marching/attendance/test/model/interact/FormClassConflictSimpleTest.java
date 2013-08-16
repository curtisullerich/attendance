package edu.iastate.music.marching.attendance.test.model.interact;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.junit.Test;

import edu.iastate.music.marching.attendance.App.WeekDay;
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
import edu.iastate.music.marching.attendance.testlib.TestUsers;
import edu.iastate.music.marching.attendance.util.Util;

public class FormClassConflictSimpleTest extends AbstractDatastoreTest {

	// absence: class times + to/from buffer must eclipse event
	// tardy: checkin time must be before class end time + from buffer
	// eco: checkout time must be after class start time - to buffer

	// we'll have the same form type bug. So see if we can set the Absence.Type
	// param in the form B creator

	private static final WeekDay FORM_WEEKDAY = WeekDay.Monday;
	private static final int FORM_TRAVEL_TIME = 10; // 10 minutes
	private static final LocalDate FORM_START_DATE = new LocalDate(2012, 8, 20);
	private static final LocalDate FORM_END_DATE = new LocalDate(2012, 12, 20);

	private static final LocalDate EVENT_DATE = FORM_START_DATE;
	private static final LocalTime EVENT_START_TIME = new LocalTime(16, 30, 0);
	private static final LocalTime EVENT_END_TIME = new LocalTime(17, 50, 0);

	private static final LocalTime FORM_START_TIME = new LocalTime(16, 10, 0);
	private static final LocalTime FORM_END_TIME = new LocalTime(17, 0, 0);

	/**
	 * class times overlap event start, tardy time within form times
	 */
	@Test
	public void testTardyWithFormB01x() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formStopTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 8, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time within form time
	 */
	@Test
	public void testTardyWithFormB02x() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 40, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy at end of buffer
	 */
	@Test
	public void testTardyWithFormB03x() {
		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalDate eventDate = FORM_START_DATE.plusDays(7);
		LocalTime eventStart = new LocalTime(16, 30, 0);
		LocalTime eventEnd = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(17, 9, 59);

		absenceWithClassConflictFormHelper(eventDate, eventStart, eventEnd,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time after buffer
	 */
	@Test
	public void testTardyWithFormB04x() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 30, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times overlap event start, tardy before event
	 */
	@Test
	public void testTardyWithFormB05x() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 15, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times before event, tardy time outside both
	 */
	@Test
	public void testTardyWithFormB01() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(13, 10, 0);
		LocalTime formEndTime = new LocalTime(14, 0, 0);

		LocalTime absenceTime = new LocalTime(8, 15, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times before event, tardy time inside event
	 */
	@Test
	public void testTardyWithFormB02() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(13, 10, 0);
		LocalTime formEndTime = new LocalTime(14, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 45, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times before event, tardy time inside class (outside event)
	 */
	@Test
	public void testTardyWithFormB03() {
		LocalDate eventDate = new LocalDate(2012, 8, 29);
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(13, 10, 0);
		LocalTime formEndTime = new LocalTime(14, 0, 0);

		LocalTime absenceTime = new LocalTime(13, 15, 0);

		absenceWithClassConflictFormHelper(eventDate, eventStartTime,
				eventStopTime, formStartTime, formEndTime, absenceTime,
				Absence.Type.Tardy, Absence.Status.Pending);
	}

	/**
	 * class times overlap event start, tardy time before buffer
	 */
	@Test
	public void testTardyWithFormB04() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(8, 15, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times overlap event start, tardy time within class, before event
	 */
	@Test
	public void testTardyWithFormB05() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 20, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times overlap event start, tardy time within class, during event
	 */
	@Test
	public void testTardyWithFormB06() {
		LocalDate eventDate = new LocalDate(2012, 8, 27);
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 50, 0);

		absenceWithClassConflictFormHelper(eventDate, eventStartTime,
				eventStopTime, formStartTime, formEndTime, absenceTime,
				Absence.Type.Tardy, Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time on event start
	 */
	@Test
	public void testTardyWithFormB07() {
		LocalDate formStartDate = new LocalDate(2012, 8, 6);
		LocalDate formEndDate = FORM_END_DATE;
		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalDateTime eventStartTime = formStartDate
				.toLocalDateTime(new LocalTime(16, 30, 0));
		LocalDateTime eventStopTime = formStartDate
				.toLocalDateTime(new LocalTime(17, 50, 0));

		LocalDateTime absenceDateTime = formStartDate
				.toLocalDateTime(new LocalTime(16, 30, 0));

		absenceWithClassConflictFormHelper(formStartDate, formEndDate,
				formStartTime, formEndTime, absenceDateTime, eventStartTime,
				eventStopTime, Absence.Type.Tardy, Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time on class end
	 */
	@Test
	public void testTardyWithFormB08() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 0, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time within end buffer
	 */
	@Test
	public void testTardyWithFormB09() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 5, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time on buffer end
	 */
	@Test
	public void testTardyWithFormB10() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 9, 59);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy after buffer, in event
	 */
	@Test
	public void testTardyWithFormB11() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formStopTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 15, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times match event, tardy time in buffer, before event
	 */
	@Test
	public void testTardyWithFormB12() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formStopTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(16, 25, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times match event, tardy time on event start
	 */
	@Test
	public void testTardyWithFormB13() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formStopTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(16, 30, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times match event, tardy time in event
	 */
	@Test
	public void testTardyWithFormB14() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formStopTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(16, 40, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times match event, tardy time on event end
	 */
	@Test
	public void testTardyWithFormB15() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formStopTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(17, 49, 59);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times match event, tardy time after event
	 */
	@Test
	public void testTardyWithFormB16() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formStopTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(18, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times within event, tardy time before buffer, in event
	 */
	@Test
	public void testTardyWithFormB17() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(16, 35, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times within event, tardy time in start buffer
	 */
	@Test
	public void testTardyWithFormB18() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(16, 55, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times within event, tardy time in class
	 */
	@Test
	public void testTardyWithFormB19() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(17, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times within event, tardy time in end buffer
	 */
	@Test
	public void testTardyWithFormB20() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(17, 23, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times within event, tardy time after end buffer
	 */
	@Test
	public void testTardyWithFormB21() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(17, 40, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times within event, tardy time outside event
	 */
	@Test
	public void testTardyWithFormB22() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(18, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times eclipse event, tardy time inside class outside event
	 */
	@Test
	public void testTardyWithFormB23() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 0, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times eclipse event, tardy time inside class inside event
	 */
	@Test
	public void testTardyWithFormB24() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 0, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 50, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times overlap event end, tardy time in event before buffer
	 */
	@Test
	public void testTardyWithFormB25() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 10, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 40, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times overlap event end, tardy time in event in class
	 */
	@Test
	public void testTardyWithFormB26() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 10, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times overlap event end, tardy time in class after event
	 */
	@Test
	public void testTardyWithFormB27() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 10, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 55, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy time before all
	 */
	@Test
	public void testTardyWithFormB28() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy in buffer before
	 * event
	 */
	@Test
	public void testTardyWithFormB29() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 28, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy time on event start
	 */
	@Test
	public void testTardyWithFormB30() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 30, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in buffer in
	 * event
	 */
	@Test
	public void testTardyWithFormB31() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 40, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in middle
	 */
	@Test
	public void testTardyWithFormB32() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 50, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in buffer after
	 * class
	 */
	@Test
	public void testTardyWithFormB33() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(17, 52, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in buffer on
	 * event end
	 */
	@Test
	public void testTardyWithFormB34() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(17, 49, 59);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Approved);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in buffer after
	 * event
	 */
	@Test
	public void testTardyWithFormB35() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(17, 53, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy time after all
	 */
	@Test
	public void testTardyWithFormB36() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(18, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime, Absence.Type.Tardy,
				Absence.Status.Pending);
	}

	/**
	 * class times before event, tardy time outside both.
	 * 
	 * Identical cases as testTardyWithFormBXX, just with earlycheckouts
	 */
	@Test
	public void testECOWithFormB01() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(13, 10, 0);
		LocalTime formStopTime = new LocalTime(14, 0, 0);

		LocalTime absenceTime = new LocalTime(8, 15, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times before event, tardy time inside event
	 */
	@Test
	public void testECOWithFormB02() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(13, 10, 0);
		LocalTime formStopTime = new LocalTime(14, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 45, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times before event, tardy time inside class (outside event)
	 */
	@Test
	public void testECOWithFormB03() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(13, 10, 0);
		LocalTime formStopTime = new LocalTime(14, 0, 0);

		LocalTime absenceTime = new LocalTime(13, 45, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times overlap event start, tardy time before buffer
	 */
	@Test
	public void testECOWithFormB04() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formStopTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(8, 15, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times overlap event start, tardy time within class, before event
	 */
	@Test
	public void testECOWithFormB05() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formStopTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 20, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times overlap event start, tardy time within class, during event
	 */
	@Test
	public void testECOWithFormB06() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formStopTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 50, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time on event start
	 */
	@Test
	public void testECOWithFormB07() {
		LocalDate eventDate = new LocalDate(2012, 11, 26);
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formStopTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(16, 30, 0);

		absenceWithClassConflictFormHelper(eventDate, eventStartTime,
				eventStopTime, formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time on class end
	 */
	@Test
	public void testECOWithFormB08() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formStopTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 0, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time within end buffer
	 */
	@Test
	public void testECOWithFormB09() {
		LocalDate eventDate = new LocalDate(2012, 12, 17);
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formStopTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(17, 5, 0);

		absenceWithClassConflictFormHelper(eventDate, eventStartTime,
				eventStopTime, formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy time on buffer end
	 */
	@Test
	public void testECOWithFormB10() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 9, 59);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times overlap event start, tardy after buffer, in event
	 */
	@Test
	public void testECOWithFormB11() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formEndTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 15, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times match event, tardy time in buffer, before event
	 */
	@Test
	public void testECOWithFormB12() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formEndTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(16, 25, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formEndTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times match event, tardy time on event start
	 */
	@Test
	public void testECOWithFormB13() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formStopTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 30, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times match event, tardy time in event
	 */
	@Test
	public void testECOWithFormB14() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formStopTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(16, 40, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times match event, tardy time on event end
	 */
	@Test
	public void testECOWithFormB15() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 30, 0);
		LocalTime formStopTime = new LocalTime(17, 50, 0);

		LocalTime absenceTime = new LocalTime(17, 49, 59);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times match event, tardy time after event
	 */
	@Test
	public void testECOWithFormB16() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formStopTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(18, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times within event, tardy time before buffer, in event
	 */
	@Test
	public void testECOWithFormB17() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 10, 0);
		LocalTime formStopTime = new LocalTime(17, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 35, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times within event, tardy time in start buffer
	 */
	@Test
	public void testECOWithFormB18() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(16, 55, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times within event, tardy time in class
	 */
	@Test
	public void testECOWithFormB19() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(17, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times within event, tardy time in end buffer
	 */
	@Test
	public void testECOWithFormB20() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(17, 23, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times within event, tardy time after end buffer
	 */
	@Test
	public void testECOWithFormB21() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(17, 40, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times within event, tardy time outside event
	 */
	@Test
	public void testECOWithFormB22() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 0, 0);
		LocalTime formStopTime = new LocalTime(17, 20, 0);

		LocalTime absenceTime = new LocalTime(18, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times eclipse event, tardy time inside class outside event
	 */
	@Test
	public void testECOWithFormB23() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 0, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times eclipse event, tardy time inside class inside event
	 */
	@Test
	public void testECOWithFormB24() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 0, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 50, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times overlap event end, tardy time in event before buffer
	 */
	@Test
	public void testECOWithFormB25() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 10, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(16, 40, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times overlap event end, tardy time in event in class
	 */
	@Test
	public void testECOWithFormB26() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 10, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times overlap event end, tardy time in class after event
	 */
	@Test
	public void testECOWithFormB27() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(17, 10, 0);
		LocalTime formStopTime = new LocalTime(18, 0, 0);

		LocalTime absenceTime = new LocalTime(17, 55, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy time before all
	 */
	@Test
	public void testECOWithFormB28() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy in buffer before
	 * event
	 */
	@Test
	public void testECOWithFormB29() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 28, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy time on event start
	 */
	@Test
	public void testECOWithFormB30() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 30, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in buffer in
	 * event
	 */
	@Test
	public void testECOWithFormB31() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 40, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in middle
	 */
	@Test
	public void testECOWithFormB32() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(16, 50, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in buffer after
	 * class
	 */
	@Test
	public void testECOWithFormB33() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(17, 52, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in buffer on
	 * event end
	 */
	@Test
	public void testECOWithFormB34() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(17, 49, 59);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Approved);
	}

	/**
	 * class times within event but buffer eclipses, tardy time in buffer after
	 * event
	 */
	@Test
	public void testECOWithFormB35() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(17, 53, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	/**
	 * class times within event but buffer eclipses, tardy time after all
	 */
	@Test
	public void testECOWithFormB36() {
		LocalTime eventStartTime = new LocalTime(16, 30, 0);
		LocalTime eventStopTime = new LocalTime(17, 50, 0);

		LocalTime formStartTime = new LocalTime(16, 35, 0);
		LocalTime formStopTime = new LocalTime(17, 45, 0);

		LocalTime absenceTime = new LocalTime(18, 10, 0);

		absenceWithClassConflictFormHelper(eventStartTime, eventStopTime,
				formStartTime, formStopTime, absenceTime,
				Absence.Type.EarlyCheckOut, Absence.Status.Pending);
	}

	private void absenceWithClassConflictFormHelper(LocalTime eventStart,
			LocalTime eventEnd, LocalTime formStartTime, LocalTime formEndTime,
			LocalTime absenceTime, Absence.Type absenceType,
			Absence.Status expectedStatus) {
		absenceWithClassConflictFormHelper(EVENT_DATE, eventStart, eventEnd,
				formStartTime, formEndTime, absenceTime, absenceType,
				expectedStatus);
	}

	private void absenceWithClassConflictFormHelper(LocalDate eventDate,
			LocalTime eventStart, LocalTime eventEnd, LocalTime formStartTime,
			LocalTime formEndTime, LocalTime absenceTime,
			Absence.Type absenceType, Absence.Status expectedStatus) {
		LocalDateTime absenceDateTime = eventDate.toLocalDateTime(absenceTime);
		LocalDateTime eventStartDateTime = eventDate
				.toLocalDateTime(eventStart);
		LocalDateTime eventEndDateTime = eventDate.toLocalDateTime(eventEnd);

		absenceWithClassConflictFormHelper(FORM_START_DATE, FORM_END_DATE,
				formStartTime, formEndTime, absenceDateTime,
				eventStartDateTime, eventEndDateTime, absenceType,
				expectedStatus);
	}

	public void absenceWithClassConflictFormHelper(LocalDate formStartDate,
			LocalDate formEndDate, LocalTime formStartTime,
			LocalTime formEndTime, LocalDateTime absenceDateTime,
			LocalDateTime eventStart, LocalDateTime eventEnd,
			Absence.Type absenceType, Absence.Status expectedStatus) {
		DataTrain train = getDataTrain();
		DateTimeZone zone = train.appData().get().getTimeZone();

		int travelTime = FORM_TRAVEL_TIME;
		WeekDay weekday = FORM_WEEKDAY;
		Interval formDateInterval = Util.datesToFullDaysInterval(formStartDate,
				formEndDate, zone);
		Interval eventInterval = new Interval(eventStart.toDateTime(zone),
				eventEnd.toDateTime(zone));

		UserManager uc = train.users();
		EventManager ec = train.events();
		AbsenceManager ac = train.absences();
		FormManager fc = train.forms();

		User student = TestUsers.createDefaultStudent(uc);

		Form form = fc.createClassConflictForm(student, "department", "course",
				"section", "building", formDateInterval, formStartTime,
				formEndTime, weekday, "details", travelTime,
				Absence.Type.EarlyCheckOut);

		ec.createOrUpdate(Event.Type.Rehearsal, eventInterval);

		Absence a;
		switch (absenceType) {
		case EarlyCheckOut:
			a = ac.createOrUpdateEarlyCheckout(student,
					absenceDateTime.toDateTime(zone));
			break;
		case Tardy:
			a = ac.createOrUpdateTardy(student,
					absenceDateTime.toDateTime(zone));
			break;
		default:
			throw new UnsupportedOperationException();
		}

		form.setStatus(Form.Status.Approved);
		fc.update(form);

		assertEquals(expectedStatus, a.getStatus());
	}
}
