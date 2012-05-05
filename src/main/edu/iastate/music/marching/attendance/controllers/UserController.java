package edu.iastate.music.marching.attendance.controllers;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.ModelException;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.model.User.Type;

public class UserController extends AbstractController {

	DataTrain datatrain;

	UserController(DataTrain dataTrain) {
		super(dataTrain);
		this.datatrain = dataTrain;
	}

	public List<User> getAll() {
		return this.datatrain.getDataStore().find().type(User.class)
				.returnAll().now();
	}

	public List<User> get(User.Type... types) {

		RootFindCommand<User> find = this.datatrain.getDataStore().find()
				.type(User.class);
		find.addFilter(User.FIELD_TYPE, FilterOperator.IN, Arrays.asList(types));

		return find.returnAll().now();
	}

	public User createStudent(String netID, int univID, String firstName,
			String lastName, int year, String major) {

		User user = ModelFactory.newUser(User.Type.Student, netID, univID);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setYear(year);
		user.setMajor(major);

		this.datatrain.getDataStore().store(user);

		return user;
	}
	
	public User createDirector(String netID, int univID, String firstName,
			String lastName) {

		User user = ModelFactory.newUser(User.Type.Director, netID, univID);
		user.setFirstName(firstName);
		user.setLastName(lastName);

		this.datatrain.getDataStore().store(user);

		return user;
	}

	// PersistenceManager pm = getPersistenceManager();
	//
	// try {
	//
	// // Check if user already in the system
	// Query query = pm.newQuery(User.class);
	// query.setFilter("netID == netIDParam");
	// query.declareParameters("String netIDParam");
	// if (((List<User>) query.execute(netID)).size() > 0) {
	// throw new ModelException("NetID already exists in the system");
	// }
	//
	// Key key = KeyFactory.createKey(User.class.getSimpleName(), netID);
	// User u = ModelFactory.getUser();
	//
	// // TODO
	// // u.setKey(key);
	//
	// // Add in given parameters
	// u.setType(type);
	// u.setNetID(netID);
	//
	// // Save to datastore
	// pm.makePersistent(u);
	//
	// return pm.detachCopy(u);
	//
	// } finally {
	// // closePersistenceManager(pm);
	// }

	public void update(User user) {
		// if (user == null)
		// return;
		//
		// PersistenceManager pm = getPersistenceManager();
		//
		// try {
		// // User u = pm.getObjectById(User.class, user.getKey());
		// pm.makePersistent(user);
		// // pm.refresh(user);
		//
		// } finally {
		// // closePersistenceManager(pm);
		// }
	}

	/**
	 * 
	 * @param netid
	 * @return User object for the netid, or null if there is no such netid
	 */
	public User get(String netid) {

		List<User> users = this.datatrain.getDataStore().find()
				.type(User.class)
				.addFilter(User.FIELD_NETID, FilterOperator.EQUAL, netid)
				.returnAll().now();

		if (users.size() == 1)
			return users.get(0);
		else if (users.size() > 1)
			throw new IllegalStateException(
					"Found more than one user with same netid");
		else
			return null;
	}
}
