package edu.iastate.music.marching.attendance.model;

import com.google.code.twig.annotation.Child;
import com.google.code.twig.annotation.Parent;

public class Form {

	public static enum Status {
		Pending, Approved, Denied
	};
	
	public static enum Type {
		A, B, C, D
	};

	public static final String FIELD_STUDENT = "student";

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
	
	/**
	 * Owning student
	 * 
	 */
	@Parent
	private User student;
	
	private Type type;
	
	private Status status;
	
	private Status emailStatus;
	
	@Child
	private MessageThread messages;
}
