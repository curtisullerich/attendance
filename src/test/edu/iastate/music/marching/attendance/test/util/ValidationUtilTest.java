package edu.iastate.music.marching.attendance.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class ValidationUtilTest {

	private final static TimeZone TIMEZONE = TimeZone.getTimeZone("UTC");
	
// TODO: Most of the remaining methods are unused, don't test unused methods, delete them
//	@Test
//	public void testIsValidName() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testValidGoogleUser() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testIsValidText() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testIsValidFormDEmail() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testDateIsWeekdaysFrom_BackALongTime() {
		Date now = getFixedCalendarOnMonday().getTime();
		Calendar calendar = getFixedCalendarOnMonday();
		// Last Monday
		calendar.roll(Calendar.WEEK_OF_YEAR, -2);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Last Month
		calendar.roll(Calendar.MONTH, -1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Last Year
		calendar.roll(Calendar.YEAR, -1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
	}
	
	@Test
	public void testDateIsWeekdaysFrom_ForwardALongTime() {
		Date now = getFixedCalendarOnMonday().getTime();
		Calendar calendar = getFixedCalendarOnMonday();
		// Next next Monday
		calendar.roll(Calendar.WEEK_OF_YEAR, 2);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Next Month
		calendar.roll(Calendar.MONTH, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Next Year
		calendar.roll(Calendar.YEAR, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
	}

	@Test
	public void testDateIsWeekdaysFrom_BackAWeek_OverWeekend() {
		Date now = getFixedCalendarOnMonday().getTime();
		Calendar calendar = getFixedCalendarOnMonday();
		// Monday
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Sunday
		calendar.roll(Calendar.DATE, -1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Saturday
		calendar.roll(Calendar.DATE, -1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Friday
		calendar.roll(Calendar.DATE, -1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Thursday
		calendar.roll(Calendar.DATE, -1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Wednesday
		calendar.roll(Calendar.DATE, -1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Tuesday
		calendar.roll(Calendar.DATE, -1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Last Monday
		calendar.roll(Calendar.DATE, -1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
	}

	@Test
	public void testDateIsWeekdaysFrom_BackAWeek() {
		Date now = getFixedCalendarOnFriday().getTime();
		Calendar calendar = getFixedCalendarOnFriday();
		System.out.println(calendar.getTime().toString());
		// Friday
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Thursday
		calendar.roll(Calendar.DATE, -1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Wednesday
		calendar.roll(Calendar.DATE, -1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Tuesday
		calendar.roll(Calendar.DATE, -1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Monday
		calendar.roll(Calendar.DATE, -1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Sunday
		calendar.roll(Calendar.DATE, -1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Saturday
		calendar.roll(Calendar.DATE, -1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Last Friday
		calendar.roll(Calendar.DATE, -1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
	}

	@Test
	public void testDateIsWeekdaysFrom_Today() {
		Date now = getFixedCalendarOnMonday().getTime();
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, now, 3, TIMEZONE));
	}

	@Test
	public void testDateIsWeekdaysFrom_ForwardAWeek() {
		Date now = getFixedCalendarOnMonday().getTime();
		Calendar calendar = getFixedCalendarOnMonday();
		// Monday
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Tuesday
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Wednesday
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Thursday
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Friday
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Saturday
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Sunday
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
	}

	@Test
	public void testDateIsWeekdaysFrom_ForwardAWeek_OverWeekend() {
		Date now = getFixedCalendarOnMonday().getTime();
		Calendar calendar = getFixedCalendarOnFriday();
		// Monday
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Tuesday
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Wednesday
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Thursday
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Friday
		calendar.roll(Calendar.DATE, 1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Saturday
		calendar.roll(Calendar.DATE, 1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
		// Sunday
		calendar.roll(Calendar.DATE, 1);
		assertFalse(ValidationUtil.dateIsWeekdaysFrom(now, calendar.getTime(),
				3, TIMEZONE));
	}

	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_FarFuture() {
		Calendar calendar = Calendar.getInstance(TIMEZONE);
		// Next week
		calendar.roll(Calendar.WEEK_OF_YEAR, 2);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(
				calendar.getTime(), TIMEZONE));
		calendar.roll(Calendar.WEEK_OF_YEAR, 1);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(
				calendar.getTime(), TIMEZONE));
		calendar.roll(Calendar.MONTH, 1);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(
				calendar.getTime(), TIMEZONE));
		calendar.roll(Calendar.YEAR, 1);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(
				calendar.getTime(), TIMEZONE));
	}

	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_Now() {
		Calendar calendar = Calendar.getInstance(TIMEZONE);
		// Now
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(
				calendar.getTime(), TIMEZONE));
	}

	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_FarPast() {
		Calendar calendar = getFixedCalendarOnMonday();
		// Next week
		calendar.roll(Calendar.WEEK_OF_YEAR, -1);
		assertFalse(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(
				calendar.getTime(), TIMEZONE));
		calendar.roll(Calendar.WEEK_OF_YEAR, -1);
		assertFalse(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(
				calendar.getTime(), TIMEZONE));
		calendar.roll(Calendar.MONTH, -1);
		assertFalse(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(
				calendar.getTime(), TIMEZONE));
		calendar.roll(Calendar.YEAR, -1);
		assertFalse(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(
				calendar.getTime(), TIMEZONE));
	}

	private Calendar getFixedCalendarOnMonday() {
		Calendar c = Calendar.getInstance();
		c.set(2004, 8, 20, 17, 42, 51);
		c.set(Calendar.MILLISECOND, 555);
		return c;
	}

	private Calendar getFixedCalendarOnFriday() {
		Calendar c = Calendar.getInstance();
		c.set(2004, 8, 24, 17, 42, 51);
		c.set(Calendar.MILLISECOND, 555);
		return c;
	}
}
