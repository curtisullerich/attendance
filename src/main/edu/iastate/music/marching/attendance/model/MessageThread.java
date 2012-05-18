package edu.iastate.music.marching.attendance.model;

import java.util.List;
import java.util.Set;

import com.google.code.twig.annotation.Embedded;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;

public class MessageThread {

	/**
	 * Create users through UserController (DataModel.users().create(...)
	 */
	MessageThread() {
		// Defaults
		
		// Empty thread is resolved by default
		resolved = true;
	}
	
	@Id
	private long id;

	@Index
	private boolean resolved;
	
	/**
	 * List of all users who have messages in this conversation
	 */
	@Index
	private Set<User> participants;

	@Embedded
	private List<Message> messages;
	
	public long getId()
	{
		return id;
	}
	
	public boolean isResolved()
	{
		return resolved;
	}
	
	public void setResolved(boolean resolved)
	{
		this.resolved = resolved;
	}

}
