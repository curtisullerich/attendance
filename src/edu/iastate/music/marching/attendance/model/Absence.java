package edu.iastate.music.marching.attendance.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable = "true")
public class Absence {
	/**
	 * Create absence through AbsenceController (DataModel.absence().create(...)
	 */
	Absence() {

	}
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public void setKey(Key key) {
        this.key = key;
    }
	
	/**
	 * For event
	 * 
	 */
	@Persistent
	private Event event;
	
	/**
	 * For student
	 * 
	 */
	@Persistent
	private User student;
}
