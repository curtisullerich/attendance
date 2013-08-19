package edu.iastate.music.marching.attendance.testlib;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.AttendanceDatastore;

@Ignore
public class AbstractDatastoreTest {

	private DataTrain datatrain = null;

	private LocalServiceTestHelper helper = null;
	
	static {
		App.CachingEnabled = false;
	}

	@Before
	public void setUp() {
		helper = new LocalServiceTestHelper(
				new LocalDatastoreServiceTestConfig(),
				new LocalUserServiceTestConfig());
		helper.setUp();
		datatrain = DataTrain.depart();
	}

	@After
	public void tearDown() {
		helper.tearDown();
		datatrain = null;
		helper = null;
	}

	protected final DataTrain getDataTrain() {
		if (datatrain == null) {
			throw new IllegalAccessError(
					"Access data train outside of a test case is not allowed.");
		} else {
			return datatrain;
		}
	}
}
