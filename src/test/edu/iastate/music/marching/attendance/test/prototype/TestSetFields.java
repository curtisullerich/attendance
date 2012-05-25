package edu.iastate.music.marching.attendance.test.prototype;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Type;

import edu.iastate.music.marching.attendance.test.AbstractTestCase;

public class TestSetFields extends AbstractTestCase {

	@Test
	public void test() {
		
		List<String> testSet = new ArrayList<String>();
		testSet.add("String 1");
		testSet.add("String 2");
		testSet.add("String 3");

		// Store
		ObjectDatastore datastore = getObjectDataStore();
		TestSetObject store = new TestSetObject();
		store.field = "arstarst"; //Arrays.asList(testSet.toArray());
		Key k = datastore.store(store);
		datastore.disassociate(store);
		// Independently load
		ObjectDatastore datastore2 = getObjectDataStore();
		TestSetObject result = datastore.load(TestSetObject.class, store.id);
//		
//		assertNotNull(result);
//		assertNotNull(result.field);
//		assertEquals(3, result.field.size());
		
	}
	
	public static class TestSetObject {
		
		@Id
		public long id;
		
		@Index
		@Type(Text.class)
		public String field;
	}

}
