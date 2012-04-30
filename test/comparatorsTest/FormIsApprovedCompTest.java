package comparatorsTest;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comparators.FormIsApprovedComp;

import time.Date;
import time.Time;

import forms.Form;

public class FormIsApprovedCompTest {

	Form f1;
	Form f2;
	Time start;
	Time end;
	Date date;
	FormIsApprovedComp comp;
	
	@Before
	public void setUp() throws Exception 
	{
		date = new Date(2000,10,11);
		start = new Time(8,10,date);
		end = new Time(11,10,date);
		f1 = new Form("abc","reason",start,end,"a");
		f2 = new Form("abc","reason",start,end,"a");
		comp = new FormIsApprovedComp();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		comp.compare(f1, f2);
	}

}
