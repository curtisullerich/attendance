package testComparators;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import time.Date;
import time.Time;

import comparators.FormIsApprovedComp;
import forms.Form;

public class FormIsApprovedCompTest {

	Form f1 ;
	Form f2 ;
	Time start;
	Time end;
	Date date;
//	FormIsApprovedComp comp;
	
	//String netID, String reason, Time startTime, Time endTime, String type
	@Before
	public void setUp() throws Exception {
		date = new Date(2000,10,11);
		start = new Time(8,10,date);
		end = new Time(11,10,date);
		f1 = new Form("abc","reason",start,end,"a");
		
		f2 = new Form("abc","reason",start,end,"a");
		f2.setApproved(true);
		
//		comp=FormIsApprovedComp();
		System.out.println(f2.isApproved());
	
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() 
	{
		assertEquals("not approved: 1",1,new FormIsApprovedComp().compare(f1, f2));
		assertEquals("approved: -1",-1,new FormIsApprovedComp().compare(f2,f1));
	}

}
