package edu.iastate.music.marching.attendance.test.unit.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class ValidationUtilTest {

	private final static TimeZone TIMEZONE = TimeZone.getTimeZone("UTC");

	@Test
	public void testIsValidName()
	{
		assertTrue(ValidationUtil.isValidName("Test Name-Complicated"));
		assertTrue(ValidationUtil.isValidName("Test Name"));
		
		// TODO: https://github.com/curtisullerich/attendance/issues/125 
		//Should we allow non-ascii characters in names?
		//assertTrue(ValidationUtil.isValidName("" + '\u4E2D' + '\u56FD' + '\u8BDD' + '\u4E0D' + '\u7528' + ' ' + '\u7528' + '\u5F41' + '\u5B57' + '\u3002'));
		//assertFalse(ValidationUtil.isValidName("1234567890"));
	}
	
	@Test
	public void testIsValidText_Length() {
		StringBuilder sb = new StringBuilder("");
		// Short
		sb.append("Lorem ipsum dolor sit amet");
		assertTrue(ValidationUtil.isValidText(sb.toString(), false));
		assertTrue(ValidationUtil.isValidText(sb.toString(), true));
		
		// Medium

		for (int i = 0; i < 100; i++) {
			sb.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent sit amet commodo arcu.");
		}
		assertTrue(ValidationUtil.isValidText(sb.toString(), false));
		assertTrue(ValidationUtil.isValidText(sb.toString(), true));
		
		// Super-long
		for (int i = 0; i < 10000; i++) {
			sb.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent sit amet commodo arcu."
					+ "Nulla ac fermentum sem. In non sapien nunc, at varius magna. Nulla id adipiscing mi. "
					+ "Cras diam neque, vehicula sit amet viverra ac, aliquet id urna. Cras cursus lacinia hendrerit. "
					+ "Sed eu lacus tellus. Phasellus quis leo nec massa molestie feugiat eu quis mi. "
					+ "Ut erat augue, iaculis ut tristique sit amet, mattis eget orci. Donec ac lectus neque, "
					+ "non pulvinar quam. Etiam placerat, lectus quis porta blandit, massa sem placerat enim, "
					+ "in lacinia velit augue eu nisi. Aenean vehicula, nunc vitae posuere condimentum, "
					+ "felis nibh vestibulum dolor, ut auctor nulla libero quis tortor. Cras at leo vel nisl cursus feugiat.");
		}
		assertFalse(ValidationUtil.isValidText(sb.toString(), false));
		assertFalse(ValidationUtil.isValidText(sb.toString(), true));
	}

	@Test
	public void testIsValidText_Emptyness() {
		assertFalse(ValidationUtil.isValidText("", false));
		assertTrue(ValidationUtil.isValidText("", true));
	}

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
