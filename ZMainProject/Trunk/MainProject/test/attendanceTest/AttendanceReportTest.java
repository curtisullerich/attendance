package attendanceTest;

import static org.junit.Assert.*;

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
	FormIsApprovedComp comp;
	
	List newList;
	
	@Before
	public void setUp() throws Exception {
		date = new Date(2000,10,11);
		start = new Time(8,10,date);
		end = new Time(11,10,date);
		f1 = new Form("abc","reason",start,end,"a");
		
		f2 = new Form("abc","reason",start,end,"a");
//		f2.setApproved(true);
//		System.out.println(f2.isApproved());
		list = new ArrayList<Form>();
		list.add(f1);
		list.add(f2);
		report=new AttendanceReport("abc");
		comp=new FormIsApprovedComp();
		comp.compare(f1, f2);
//		report.setList(list);
		report.sortFormsAscending(comp);
		
		//
		newList = new ArrayList();
		newList.add(f1);
		newList.add(f2);
	}

	@After
	public void tearDown() throws Exception 
	{
	}

	/**
	 * Test two form, one is approved, the other is not, sort by: unapprove, approve
	 */
	@Test
	public void isApproveComTest() {
		report.sortFormsDescending(new FormIsApprovedComp());
		System.out.println(report.sortFormsDescending(new FormIsApprovedComp()).toString());
		assertEquals("output",newList,report.sortFormsDescending(new FormIsApprovedComp()));
	}
	
	/**
	 * test random time
	 */
	@Test
	public void testTimeComp()
	{
		for(int i=0; i<5; i++)
		{
			Time start = new Time();
			list.add(new Form("abc","reason",start,end,"a"));
		}
	}
	

}
