package edu.iastate.music.marching.attendance.testlib;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;

@Ignore
public class AbstractDatastoreTest extends TestCase {

	private DataTrain datatrain = null;

	private LocalServiceTestHelper helper = null;

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
