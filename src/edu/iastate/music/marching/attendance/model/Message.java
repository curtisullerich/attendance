package edu.iastate.music.marching.attendance.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable="true")
public class Message {

	/**
	 * Create users through UserController (DataModel.users().create(...)
	 */
	Message() {

	}
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	public void setKey(Key key) {
        this.key = key;
    }
	
	/**
	 * Owning form
	 * 
	 */
	@Persistent
	private Form form;

}
