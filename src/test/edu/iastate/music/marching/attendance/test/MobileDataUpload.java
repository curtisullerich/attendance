package edu.iastate.music.marching.attendance.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import edu.iastate.music.marching.attendance.controllers.DataTrain;

public class MobileDataUpload {

	private static String SIMPLE_ABSENCE_TESTDATA = "absentStudentRehearsal&split&Daniel&split&Stiner&split&stiner&split&2012-05-03&split&1630&split&1750&split&null,storedRehearsal&split&rehearsal&split&|&split&|&split&2012-05-03&split&1630&split&1750&split&|,absentStudentRehearsal&split&Yifei&split&Zhu&split&zyf&split&2012-05-03&split&1630&split&1750&split&4,";

	private static String SIMPLE_TARDY_TESTDATA = "tardyStudent&split&Daniel&split&Stiner&split&stiner&split&2012-05-03&split&0109&split&|&split&null,tardyStudent&split&Daniel&split&Stiner&split&stiner&split&2012-05-03&split&0116&split&|&split&null,tardyStudent&split&Brandon&split&Maxwell&split&bmaxwell&split&2012-05-03&split&0108&split&|&split&|,tardyStudent&split&Daniel&split&Stiner&split&stiner&split&2012-05-03&split&0107&split&|&split&null,tardyStudent&split&Daniel&split&Stiner&split&stiner&split&2012-05-03&split&0108&split&|&split&null,tardyStudent&split&Yifei&split&Zhu&split&zyf&split&2012-05-03&split&0108&split&|&split&4,";

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
	}

	@After
	public void tearDown() {
		helper.tearDown();
	}

	@Test
	public void simpleAbsenceInsertionThroughController() {
		
		DataTrain train = getDataTrain();
		
		train.
		
		fail("Not yet implemented");
		
		
	}
	
	private DataTrain getDataTrain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Test
	public void simpleTardyInsertionThroughController() {
		fail("Not yet implemented");
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
