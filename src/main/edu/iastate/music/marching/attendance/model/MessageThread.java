package edu.iastate.music.marching.attendance.model;

import java.util.List;
import java.util.Set;

import com.google.code.twig.annotation.Embedded;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;

public class MessageThread {

	public static final String FIELD_PARTICIPANTS = "participants";

	public static final String FIELD_RESOLVED = "resolved";

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

	public long getId() {
		return id;
	}

	/**
	 * Returns the most recent message added to this MessageThread.
	 * 
	 * @author curtisu
	 * @return
	 */
	public Message mostRecent() {
		if (messages.size() == 0 ) {
			return null;
		}
		Message mostRecent = messages.get(0);
		for (Message m : messages) {
			if(m.getTimestamp().after(mostRecent.getTimestamp())) {
				mostRecent = m;
			}
		}
		return mostRecent;
	}
	
	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public Set<User> getParticipants() {
		return this.participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}

	public List<Message> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

}
