package edu.iastate.music.marching.attendance.model;

import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable = "true")
public class Form {

	public static enum Status {
		Pending, Approved, Denied
	};
	
	public static enum Type {
		A, B, C, D
	};

	/**
	 * Create users through UserController (DataModel.users().create(...)
	 */
	Form() {
	}

	// Select query using enums
	// Query query =
	// pm.newQuery("SELECT FROM com.xxx.yyy.User WHERE role == p1 ORDER BY key desc RANGE 0,50");
	// query.declareParameters("Enum p1");
	// AbstractQueryResult results = (AbstractQueryResult)
	// pm.newQuery(q).execute(admin);
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public void setKey(Key key) {
        this.key = key;
    }

	/**
	 * Owning student
	 * 
	 */
	@Persistent
	private User student;
	
	@Persistent
	private Type type;
	
	@Persistent
	private Status status;
	
	@Persistent
	private Status emailStatus;
	
	@Persistent(mappedBy = "form")
	private List<Message> messages;
}
