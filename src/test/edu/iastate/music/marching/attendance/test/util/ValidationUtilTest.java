package edu.iastate.music.marching.attendance.test.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.testlib.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.testlib.TestConfig;
import edu.iastate.music.marching.attendance.util.ValidationUtil;

public class ValidationUtilTest extends AbstractDatastoreTest {

	@Test
	public void testValidGoogleUsers() {
		DataTrain train = getDataTrain();
		
		String validIaStateEmail = "bmax@" + TestConfig.getEmailDomain();
		String nonValidRealEmail = "bmax921@gmail.com";
		String nonValidMadeUpEmail = "lkajslkfdjasdf@lkasdlkfj.com";
		
		assertTrue(ValidationUtil.validPrimaryEmail(new Email(validIaStateEmail), train));
		assertFalse(ValidationUtil.validPrimaryEmail(new Email(nonValidRealEmail), train));
		assertFalse(ValidationUtil.validPrimaryEmail(new Email(nonValidMadeUpEmail), train));
	}
	
	@Test
	public void testIsValidName() {
		assertTrue(ValidationUtil.isValidName("Test Name-Complicated"));
		assertTrue(ValidationUtil.isValidName("Test Name"));

		// TODO: https://github.com/curtisullerich/attendance/issues/125
		// Should we allow non-ascii characters in names?
		// assertTrue(ValidationUtil.isValidName("" + '\u4E2D' + '\u56FD' +
		// '\u8BDD' + '\u4E0D' + '\u7528' + ' ' + '\u7528' + '\u5F41' + '\u5B57'
		// + '\u3002'));
		// assertFalse(ValidationUtil.isValidName("1234567890"));
	}

	@Test
	public void testIsValidText_Length() {
		StringBuilder sb = new StringBuilder("");
		// Short
		sb.append("Lorem ipsum dolor sit amet");
		assertTrue(ValidationUtil.isValidText(sb.toString(), false));
		assertTrue(ValidationUtil.isValidText(sb.toString(), true));

		// Medium

		for (int i = 0; i < 100; i++) {
			sb.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent sit amet commodo arcu.");
		}
		assertTrue(ValidationUtil.isValidText(sb.toString(), false));
		assertTrue(ValidationUtil.isValidText(sb.toString(), true));

		// Super-long
		for (int i = 0; i < 10000; i++) {
			sb.append("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent sit amet commodo arcu."
					+ "Nulla ac fermentum sem. In non sapien nunc, at varius magna. Nulla id adipiscing mi. "
					+ "Cras diam neque, vehicula sit amet viverra ac, aliquet id urna. Cras cursus lacinia hendrerit. "
					+ "Sed eu lacus tellus. Phasellus quis leo nec massa molestie feugiat eu quis mi. "
					+ "Ut erat augue, iaculis ut tristique sit amet, mattis eget orci. Donec ac lectus neque, "
					+ "non pulvinar quam. Etiam placerat, lectus quis porta blandit, massa sem placerat enim, "
					+ "in lacinia velit augue eu nisi. Aenean vehicula, nunc vitae posuere condimentum, "
					+ "felis nibh vestibulum dolor, ut auctor nulla libero quis tortor. Cras at leo vel nisl cursus feugiat.");
		}
		assertFalse(ValidationUtil.isValidText(sb.toString(), false));
		assertFalse(ValidationUtil.isValidText(sb.toString(), true));
	}

	@Test
	public void testIsValidText_Emptyness() {
		assertFalse(ValidationUtil.isValidText("", false));
		assertTrue(ValidationUtil.isValidText("", true));
	}
}
