package testAttendance;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import comparators.FormIsApprovedComp;

import attendance.AttendanceReport;

import time.Date;
import time.Time;
import forms.Form;

public class AttendanceReportTest {

	Form f1 ;
	Form f2 ;
	Time start;
	Time end;
	Date date;
	List<Form> list;
	AttendanceReport report;
	
	@Before
	public void setUp() throws Exception {
		date = new Date(2000,10,11);
		start = new Time(8,10,date);
		end = new Time(11,10,date);
		f1 = new Form("abc","reason",start,end,"a");
		
		f2 = new Form("abc","reason",start,end,"a");
		f2.setApproved(true);
		System.out.println(f2.isApproved());
		list = new ArrayList<Form>();
		list.add(f1);
		list.add(f2);
		report=new AttendanceReport("abc");
		
	
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		report.sortFormsDescending(new FormIsApprovedComp());
		
	}

}
