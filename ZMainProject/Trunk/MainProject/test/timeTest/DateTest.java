package timeTest;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import time.Date;

public class DateTest {

/*********************TEST YEAR**********************************/
	
	/**
	 * invalid input: year too small
	 * 
	 * Year that <2000 
	 * 		--> throw exception
	 */
	@Test
	public void testYear1() 
	{
		
		boolean thrown = false;
		try
		{
			Date d = new Date(1990, 10,10);
		}
		catch(Exception e)
		{
			thrown=true;
		}
		assertTrue(thrown);
	}
	
	/**
	 * valid input
	 * 
	 * normal test: Year = 2000
	 * @throws Exception
	 */
	@Test
	public void testYear2() throws Exception
	{
		Date d = new Date(2000,10,10);
		assertEquals("Test Normal Year", 2000 , d.getYear());
	}
	

	/**
	 * invalid input: input too large
	 * 
	 *  Year = 3000 > 2999
	 */
	@Test
	public void testYear3()
	{
		boolean thrown = false;
		try
		{
			Date d = new Date(3000,10,10);
		}
		catch(IllegalArgumentException e)
		{
			thrown = true;
		}
		assertTrue(thrown);
	}

	/*********************TEST MONTH**********************************/

	/**
	 * Invalid Input: Month <1
	 */
	@Test
	public void testMonth1()
	{
		boolean thrown = false;
		try
		{
			Date d = new Date(2000,-1,10);
		}
		catch(IllegalArgumentException e)
		{
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	/**
	 * valid: random month: 1--12
	 */
	@Test 
	public void testMonth2()
	{
		for(int i=0; i<120;i++)
		{
			Random r = new Random();
			int num = r.nextInt(12)+1;
			Date d = new Date(2000 ,num, 20);
			assertEquals("Test Month", num, d.getMonth());
		}
	}
	
	/**
	 * Invalid Month : month>12
	 */
	@Test
	public void testMonth3()
	{
		boolean thrown = false;
		try
		{
			Date d = new Date(2000,100,10);
		}
		catch(IllegalArgumentException e)
		{
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	/*********************TEST DAY**********************************/

	/**
	 * Invalid: day <1
	 */
	@Test
	public void testDay1()
	{
		boolean thrown = false;
		try{
			Date d = new Date(2000,10, -1);
		}
		catch(IllegalArgumentException e)
		{
			thrown = true;
		}
		assertTrue(thrown);
	}
	
	/**
	 * Valid Day: 1--31 ( not include feb)
	 */
	@Test
	public void testDay2()
	{
		Date d = new Date(2000, 6, 30);
		assertEquals("Test Random Day ( not include (Feb)",d.getDay(), 30);
		d = new Date(2000, 3, 13);
		assertEquals("Test Random Day ( not include (Feb)",d.getDay(), 13);
	}
	
	/**
	 * Invalid: day >31
	 */
	@Test
	public void testDay3()
	{
		boolean thrown = false;
		try
		{
			Date d = new Date(2000,1, 200);
		}
		catch(IllegalArgumentException e)
		{
			thrown = true;
		}
		assertTrue(thrown);
	}

	
	/**
	 * valid Day: Feb >28
	 */
	@Test
	public void testDay4()
	{
		boolean thrown = false;
		try{
		Date d = new Date(2000,2, 30);
		}
		catch(IllegalArgumentException e)
		{
			thrown = true;
		}
		assertTrue(thrown);
	}
	

	/**
	 * valid Day: Feb ==29 (leap year)
	 */
	@Test 
	public void testDay5()
	{
		Date d = new Date(2000,2, 29);
		assertEquals("Leap year", d.getDay(), 29);
	}
	
	/**
	 * Invalid: day =31 in April
	 */
	@Test (expected = IllegalArgumentException.class)
	public void testDay6()
	{
		new Date(2000,4,31);
	}
	
	
	/*********************TEST LEAP YEAR**********************************/
	/**
	 * Leap Year:
	 * year %400 ==0
	 */
	@Test
	public void testLeapYear1()
	{
		Date d = new Date(2000, 1,1);
		assertTrue(d.isLeapYear());
	}
	/**
	 * Leap year:
	 * year%4==0 and year % 100!=0
	 */
	@Test
	public void testLeapYear2()
	{
		Date d = new Date(2012,1,1);
		assertTrue(d.isLeapYear());
	}
	/**
	 * Not a Leap year:
	 */
	@Test
	public void testLeapYear3()
	{
		Date d = new Date(2013,1,1);
		assertFalse(d.isLeapYear());
	}
	
/*********************TEST LEAP YEAR**********************************/
	
	/**
	 * Compare to date 
	 *d1(day ) <d2 (day)--output <0 
	 */
	@Test
	public void testCompareTo1()
	{
		Date d1 = new Date(2012,01,02);
		Date d2 = new Date(2012,01,03);
		assertEquals("should return -1",d1.compareTo(d2),-1);
	}
	
	/**
	 * Compare to date 
	 *d1(month ) <d2 (month)-- output<0
	 */
	@Test
	public void testCompareTo2()
	{
		Date d1 = new Date(2012,01,20);
		Date d2 = new Date(2012,05,02);
		assertTrue(d1.compareTo(d2)<0);
	}
	
	/**
	 * Compare to date 
	 *d1(year ) <d2 (year)--output <0 
	 */
	@Test
	public void testCompareTo3()
	{
		Date d1 = new Date(2012,01,02);
		Date d2 = new Date(2016,01,02);
		assertTrue(d1.compareTo(d2)<0);
	}
	
	/**
	 * Compare to date 
	 *d1(d1 year is smaller ,but month, day is bigger ) <d2 ()--output<0
	 */
	@Test
	public void testCompareTo4()
	{
		Date d1 = new Date(2012,06,12);
		Date d2 = new Date(2016,01,02);
		assertTrue(d1.compareTo(d2)<0);
	}
	
	/**
	 * CompareToDate 
	 * 
	 * d1==d2 ---output 0
	 */
	@Test
	public void testCompareTo5()
	{
		Date d1 = new Date(2012,06,12);
		Date d2 = new Date(2012,06,12);
		assertTrue(d1.compareTo(d2)==0);
	}
	
	/**
	 * CompareToDate 
	 * 
	 * d1 day > d2 day ---output >0
	 */
	@Test
	public void testCompareTo6()
	{
		Date d1 = new Date(2012,06,13);
		Date d2 = new Date(2012,06,12);
		assertTrue(d1.compareTo(d2)>0);
	}
	
	/**
	 * CompareToDate 
	 * 
	 * d1 month > d2 month ---output >0
	 */
	@Test
	public void testCompareT7()
	{
		Date d1 = new Date(2012,10,13);
		Date d2 = new Date(2012,6,12);
		assertTrue(d1.compareTo(d2)>0);
	}
	
	/**
	 * CompareToDate 
	 * 
	 * d1 year > d2 year ---output >0
	 */
	@Test
	public void testCompareT8()
	{
		Date d1 = new Date(2016,10,13);
		Date d2 = new Date(2012,12,12);
		assertTrue(d1.compareTo(d2)>0);
	}
	/*********************TEST toString**********************************/

	/**
	 * test ToString method
	 * 
	 * month & day <10
	 */
	@Test
	public void testToString1()
	{
		Date d = new Date(2012,10,18);
		assertEquals("ToString method",d.toString(),"2012-10-18");
	}
	
	/**
	 * test ToString method
	 * 
	 * month & day > 10, should add 0 in front
	 */
	@Test
	public void testToString2()
	{
		Date d = new Date(2012,1,8);
		assertEquals("ToString method",d.toString(),"2012-01-08");
	}
	
	/*********************TEST DAY Of WEEK**********************************/

	/**
	 * test DayOfWeek 
	 * 
	 *Sunday
	 */
	@Test
	public void testDayOfWeek1()
	{
		Date d = new Date(2012,3,25);
		assertEquals("get dayOfWeek",d.getDayOfWeek(),"Sunday");
	}
	
	/**
	 * test DayOfWeek 
	 * 
	 *Monday
	 */
	@Test
	public void testDayOfWeek2()
	{
		Date d = new Date(2012,3,26);
		assertEquals("get dayOfWeek",d.getDayOfWeek(),"Monday");
	}
	/**
	 * test DayOfWeek 
	 * 
	 *Tues.
	 */
	@Test
	public void testDayOfWeek3()
	{
		Date d = new Date(2012,3,27);
		assertEquals("get dayOfWeek",d.getDayOfWeek(),"Tuesday");
	}
	/**
	 * test DayOfWeek 
	 * 
	 *Wed.
	 */
	@Test
	public void testDayOfWeek4()
	{
		Date d = new Date(2012,3,28);
		assertEquals("get dayOfWeek",d.getDayOfWeek(),"Wednesday");
	}
	/**
	 * test DayOfWeek 
	 * 
	 *Thur.
	 */
	@Test
	public void testDayOfWeek5()
	{
		Date d = new Date(2012,3,29);
		assertEquals("get dayOfWeek",d.getDayOfWeek(),"Thursday");
	}
	/**
	 * test DayOfWeek 
	 * 
	 *Fri.
	 */
	@Test
	public void testDayOfWeek6()
	{
		Date d = new Date(2012,3,30);
		assertEquals("get dayOfWeek",d.getDayOfWeek(),"Friday");
	}
	/**
	 * test DayOfWeek 
	 * 
	 *Sat.
	 */
	@Test
	public void testDayOfWeek7()
	{
		Date d = new Date(2012,3,31);
		assertEquals("get dayOfWeek",d.getDayOfWeek(),"Saturday");
	}

}
