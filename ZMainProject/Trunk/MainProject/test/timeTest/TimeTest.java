package timeTest;

import static org.junit.Assert.fail;

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

	@Test
	


}
