package timeTest;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import time.Date;
import time.Time;

public class TimeTest {

	Time t;
	Date d;
	@Before
	public void setUp() throws Exception 
	{
		d = new Date(2012,3,13);
		t=new Time(2,15,30,d);
	}
/*********************TEST HOUR****************************************************/
	/**
	 * hr < 1 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHour1()
	{
		t=new Time(-1,0,0,d);
	}
	/**
	 * hr > 24
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testHour2()
	{
		t=new Time(25,0,0,d);
	}
	/**
	 * normal hr ( 3 normal test cases)
	 */
	public void testHour3()
	{
		assertEquals("Test Hour", t.getHour(), 2);
		t=new Time(12,15,30,d);
		assertEquals("Test Hour", t.getHour(), 12);
		t=new Time(18,15,30,d);
		assertEquals("Test Hour", t.getHour(), 18);
	}
	
	/*********************TEST MINUTS****************************************************/
	/**
	 * min < 1 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testMinute1()
	{
		t=new Time(12,-5,0,d);
	}
	/**
	 * min > 59
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testMinute2()
	{
		t=new Time(25,90,0,d);
	}
	/**
	 * normal min ( 3 normal test cases)
	 */
	public void testMinute3()
	{
		assertEquals("Test Minute", t.getMinute(), 30);
		t=new Time(18,8,30,d);
		assertEquals("Test Minute", t.getMinute(), 8);
		t=new Time(18,59,30,d);
		assertEquals("Test Minute", t.getMinute(), 8);
	}
	
	/*********************TEST SECOND****************************************************/
	/**
	 * sec < 1 
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSecond1()
	{
		t=new Time(12,5,-9,d);
	}
	/**
	 * sec > 59
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testSecond2()
	{
		t=new Time(25,0,500,d);
	}
	/**
	 * normal sec ( 3 normal test cases)
	 */
	public void testSecond3()
	{
		assertEquals("Test Second",t.getSecond(), 15);
		t=new Time(18,8,30,d);
		assertEquals("Test Second", t.getSecond(), 30);
		t=new Time(18,59,0,d);
		assertEquals("Test Second", t.getSecond(), 0);
	}
	
	/*********************TEST DATE****************************************************/
	/**
	 * getDate()
	 *	3 normal test cases
	 */
	@Test
	public void testDate()
	{
		assertEquals("Test Date", d.toString(), "2012-03-13");
		d=new Date(2011,2,8);
		assertEquals("Test Date", d.toString(), "2011-02-08");
		d=new Date(2011,3,31);
		assertEquals("Test Date", d.toString(), "2011-03-31");
	}
	/*********************TEST 24 Format (ToString)****************************************************/

	/**
	 * get24Format()
	 *	-normal cases:
	 *	hr<12, min<10, sec <10
	 *	hr>12,min<10, sec =0
	 */
	@Test
	public void ToString24Format()
	{
		assertEquals("Test toString method ","2012-03-13 02:15:30",t.toString(24));
		t=new Time(18,3,0,d);
		assertEquals("Test toString method ","2012-03-13 18:03:00",t.toString(24));
	}
	
	/**
	 * get24Format()
	 *	-normal cases:
	 *	hr<12, min<10, sec <10
	 *	hr>12,min<10, sec =0
	 */
	@Test
	public void ToString12Format()
	{
		assertEquals("Test toString method ","2012-03-13 02:15:30AM",t.toString(12));
		t=new Time(18,3,0,d);
		assertEquals("Test toString method ","2012-03-13 06:03:00PM",t.toString(12));
	}
	
	/**
	 * compareTo()
		-date1==date2 hr1<hr2, min1>min2, sec1>sec2	>0
		-date1==date2 hr1=hr2, min1>min2, sec1>sec2	>0
		-date1==date2 hr1=hr2, min1=min2, sec1>sec2	=0
		-date1==date2 hr1<hr2, min1>min2, sec1>sec2	<0
		-date1>date2 time1<time2			>0
	 */
	@Test
	public void testCompareTo()
	{
		/**
		d = new Date(2012,3,13);
		t=new Time(2,15,30,d);
		 */
		//-date1==date2 hr1<hr2, min1>min2, sec1>sec2	>0
		Time t1=new Time(1,50,35,d);
		assertTrue(t.compareTo(t1)>0);
		
		//-date1==date2 hr1=hr2, min1>min2, sec1>sec2	>0
		t1=new Time(2,5,35,d);
		assertTrue(t.compareTo(t1)>0);
		
		//-date1==date2 hr1=hr2, min1=min2, sec1>sec2	=0
		t1=new Time(2,15,3,d);
		assertTrue(t.compareTo(t1)>0);
		
		//-date1==date2 hr1<hr2, min1>min2, sec1>sec2	<0
		t1=new Time(2,15,50,d);
		assertTrue(t.compareTo(t1)<0);
		
		//-date1>date2 time1<time2			>0
		Date d1= new Date(2012,2,1);
		t1= new Time(1,15,30,d1);
		assertTrue(t.compareTo(t1)>0);
		
	}
	

}
