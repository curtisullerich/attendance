package edu.iastate.music.marching.attendance.test.unit.util;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class ValidationUtilTest {

	@Test
	public void testIsValidName() {
		fail("Not yet implemented");
	}

	@Test
	public void testValidGoogleUser() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsValidText() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsValidFormDEmail() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_YearAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.YEAR, -1);
		assertFalse(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_MonthAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.MONTH, -1);
		assertFalse(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_WeekAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.WEEK_OF_YEAR, -1);
		assertFalse(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_FourDaysAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, -4);
		assertFalse(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_ThreeDaysAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, -3);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_TwoDaysAgo() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, -2);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_Yesterday() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, -1);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_Today() {
		Date now = Calendar.getInstance(App.getTimeZone()).getTime();
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(now));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_Tommorrow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, 1);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_TwoDaysFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, 2);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_ThreeDaysFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, 3);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_FourDaysFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, 4);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_SixDaysFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, 6);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_SevenDaysFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, 7);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_EightDaysFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.DATE, 8);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_WeekFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.WEEK_OF_YEAR, 1);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_TwoWeeksFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.WEEK_OF_YEAR, 2);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
	
	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_MonthFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.MONTH, 1);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}

	@Test
	public void testDateIsAtLeastThreeWeekdaysFresh_YearFromNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.roll(Calendar.YEAR, 1);
		assertTrue(ValidationUtil.dateIsAtLeastThreeWeekdaysFresh(calendar.getTime()));
	}
}
