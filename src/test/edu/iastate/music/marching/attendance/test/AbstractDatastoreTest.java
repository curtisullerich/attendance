package edu.iastate.music.marching.attendance.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;

@Ignore
public class AbstractDatastoreTest {
	
	private DataTrain datatrain = null;

	private final LocalServiceTestHelper helper = new LocalServiceTestHelper(
			new LocalDatastoreServiceTestConfig(),
			new LocalUserServiceTestConfig());

	@Before
	public void setUp() {
		helper.setUp();
		datatrain = DataTrain.depart();
	}

	@After
	public void tearDown() {
		helper.tearDown();
		datatrain = null;
	}

	protected final DataTrain getDataTrain() {
		if(datatrain == null)
		{
			throw new IllegalAccessError("Access data train outside of a test case is not allowed.");
		} else {
			return datatrain;
		}
	}
}
