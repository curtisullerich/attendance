package edu.iastate.music.marching.attendance.test.unit.model.interact;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.test.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class AuthManagerTest extends AbstractDatastoreTest{

	@Test
	public void testValidGoogleUsers() {
		DataTrain train = getDataTrain();
		//Maybe this should be in a ValidationUtil class?
		String validIaStateEmail = "bmaxwell@iastate.edu";
		String nonValidRealEmail = "bmaxwell921@gmail.com";
		String nonValidMadeUpEmail = "lkajslkfdjasdf@lkasdlkfj.com";
		
		assertTrue(ValidationUtil.validPrimaryEmail(new Email(validIaStateEmail), train));
		assertFalse(ValidationUtil.validPrimaryEmail(new Email(nonValidRealEmail), train));
		assertFalse(ValidationUtil.validPrimaryEmail(new Email(nonValidMadeUpEmail), train));
	}
	
}
