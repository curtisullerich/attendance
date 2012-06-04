package edu.iastate.music.marching.attendance.test.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.UserController;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.test.AbstractTest;

public class UserControllerTest extends AbstractTest {

	@Test
	public void testCreateSingleDirector() {

		// Setup
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		
		createDirector(uc, "director", 123, "I am", "The Director");
		
		// Verify
		List<User> users = uc.getAll();
		
		assertNotNull(users);
		assertEquals(1, users.size());

		User d = users.get(0);

		// Check returned object
		assertNotNull(d);
		assertEquals(User.Type.Director, d.getType());
		assertEquals("director", d.getNetID());
		assertEquals(123, d.getUniversityID());
		assertEquals("I am", d.getFirstName());
		assertEquals("The Director", d.getLastName());
	}

	@Test
	public void testCreateSingleStudent() {

		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();

		createStudent(uc, "studenttt", 121, "I am", "A Student", 10, "Being Silly",
				User.Section.AltoSax);

		// Verify
		List<User> users = uc.getAll();
		
		assertNotNull(users);
		assertEquals(1, users.size());

		User s = users.get(0);

		// Check returned object
		assertNotNull(s);
		assertEquals(User.Type.Student, s.getType());
		assertEquals("studenttt", s.getNetID());
		assertEquals(121, s.getUniversityID());
		assertEquals("I am", s.getFirstName());
		assertEquals("A Student", s.getLastName());
		assertEquals(10, s.getYear());
		assertEquals("Being Silly", s.getMajor());
		assertEquals(User.Section.AltoSax, s.getSection());
	}
	
	@Test
	public void averageGrade() {
		
		ObjectDatastore datastore = getObjectDataStore();
		
		DataTrain train = getDataTrain();

		UserController uc = train.getUsersController();
		
		User s1 = createStudent(uc, "student1", 121, "First", "last", 2, "major", User.Section.AltoSax);
		User s2 = createStudent(uc, "student2", 122, "First", "Last", 2, "major", User.Section.AltoSax);
		User s3 = createStudent(uc, "student3", 123, "First", "Last", 2, "major", User.Section.AltoSax);
		User s4 = createStudent(uc, "stiner", 34234, "ars", "l", 3, "astart", User.Section.AltoSax);
		
		// Using a separate data store just because
		// but to do so the users need to be associated with it
		datastore.associate(s1);
		datastore.associate(s2);
		datastore.associate(s3);
		datastore.associate(s4);
		
		s1.setGrade(User.Grade.A);
		s2.setGrade(User.Grade.A);
		s3.setGrade(User.Grade.A);
		s4.setGrade(User.Grade.A);
		datastore.update(s1);
		datastore.update(s2);
		datastore.update(s3);
		datastore.update(s4);
		
		assertEquals(User.Grade.A, uc.averageGrade());
		
		s1.setGrade(User.Grade.A);
		s2.setGrade(User.Grade.B);
		s3.setGrade(User.Grade.C);
		s4.setGrade(User.Grade.D);
		datastore.update(s1);
		datastore.update(s2);
		datastore.update(s3);
		datastore.update(s4);
		
		assertEquals(User.Grade.Bminus, uc.averageGrade());
		
		s1.setGrade(User.Grade.A);
		s2.setGrade(User.Grade.Aminus);
		s3.setGrade(User.Grade.A);
		s4.setGrade(User.Grade.Aminus);
		datastore.update(s1);
		datastore.update(s2);
		datastore.update(s3);
		datastore.update(s4);
		
		assertEquals(User.Grade.A, uc.averageGrade());
		
		
	}
}
