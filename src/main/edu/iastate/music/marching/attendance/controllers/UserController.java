package edu.iastate.music.marching.attendance.controllers;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import edu.iastate.music.marching.attendance.model.ModelException;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.model.User.Type;

public class UserController extends AbstractController {

	UserController() {
		super();
	}

	UserController(PersistenceManager pm) {
		super(pm);
	}

	@SuppressWarnings("unchecked")
	public User create(Type type, String netID, byte[] password, byte[] salt)
			throws ModelException {
		PersistenceManager pm = getPersistenceManager();

		try {

			// Check if user already in the system
			Query query = pm.newQuery(User.class);
			query.setFilter("netID == netIDParam");
			query.declareParameters("String netIDParam");
			if (((List<User>) query.execute(netID)).size() > 0) {
				throw new ModelException("NetID already exists in the system");
			}

			Key key = KeyFactory.createKey(User.class.getSimpleName(), netID);
			User u = ModelFactory.getUser();
			
			// TODO
			//u.setKey(key);

			// Add in given parameters
			u.setType(type);
			u.setNetID(netID);

			// Save to datastore
			pm.makePersistent(u);

			return pm.detachCopy(u);

		} finally {
			// closePersistenceManager(pm);
		}
	}

	private User getDetached(Key key) {
		PersistenceManager pm = getPersistenceManager();

		try {
			User u = pm.getObjectById(User.class, key);

			return pm.detachCopy(u);
		} finally {
			closePersistenceManager(pm);
		}
	}

	public void update(User user) {
		if (user == null)
			return;

		PersistenceManager pm = getPersistenceManager();

		try {
			// User u = pm.getObjectById(User.class, user.getKey());
			pm.makePersistent(user);
			// pm.refresh(user);

		} finally {
			// closePersistenceManager(pm);
		}
	}

	/**
	 * 
	 * @param netid
	 * @return User object for the netid, or null if there is no such netid
	 */
	public User get(String netid) {

		PersistenceManager pm = getPersistenceManager();

		try {
			// Check if user already in the system
			Query query = pm.newQuery(User.class);
			query.setFilter("netID == netIDParam");
			query.declareParameters("String netIDParam");

			@SuppressWarnings("unchecked")
			List<User> users = (List<User>) query.execute(netid);

			if (users.size() == 0) {
				return null;
			}

			return users.get(0);

		} finally {
			closePersistenceManager(pm);
		}
	}
}
