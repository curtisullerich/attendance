package edu.iastate.music.marching.attendance.test.prototype;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;

public class TestCalendarToDate {

	@Test
	public void test() {
		Calendar c1 = Calendar.getInstance(TimeZone.getTimeZone("PST"));
		Calendar c2 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		
		//c1.
		//fail("Not yet implemented");
	}

}
