package edu.iastate.music.marching.attendance.test;

import org.junit.After;
import org.junit.Before;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import edu.iastate.music.marching.attendance.controllers.DataTrain;

public class AbstractTest {
	
	private DataTrain datatrain = null;

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig(),
			new LocalUserServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
		datatrain = DataTrain.getAndStartTrain();
	}

	@After
	public void tearDown() {
		helper.tearDown();
		datatrain = null;
	}

	protected final DataTrain getDataTrain() {
		if(datatrain == null)
		{
			throw new IllegalAccessError("Access data train outside of test case itself is not allowed.");
		} else {
			return datatrain;
		}
	}
}
