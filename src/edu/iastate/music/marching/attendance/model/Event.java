package edu.iastate.music.marching.attendance.model;

import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable = "true")
public class Event {
	/**
	 * Create events through UserController (DataModel.events().create(...)
	 */
	Event() {

	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public void setKey(Key key) {
        this.key = key;
    }
	
	/**
	 * Absent student
	 * 
	 */
	@Persistent
	private Set<Absence> absences;
}
