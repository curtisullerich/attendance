package edu.iastate.music.marching.attendance.model.legacy;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.datastore.Blob;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Version;

@Version(0)
@Entity(kind="edu.iastate.music.marching.attendance.model.MessageThread", allocateIdsBy=0)
public class MessageThread_V0 {

	public static final String FIELD_PARTICIPANTS = "participants";

	public static final String FIELD_RESOLVED = "resolved";

	/**
	 * Sorts by the latest message in the thread in descending order,
	 * meaning that threads with new messages will come first in a sortr
	 * 
	 * Does not attempt to sort empty threads with no messages.
	 */
	public static final Comparator<? super MessageThread_V0> SORT_LATEST_MESSAGE_DESC = new Comparator<MessageThread_V0>() {

		@Override
		public int compare(MessageThread_V0 o1, MessageThread_V0 o2) {
			List<Message_V0> m1 = o1.getMessages();
			List<Message_V0> m2 = o2.getMessages();
			
			if(m1 == null || m2 == null)
				return 0;
			
			if(m1.size() < 1 || m2.size() < 1)
				return 0;
			
			return m2.get(0).getTimestamp().compareTo(m1.get(0).getTimestamp());
		}
	};

	/**
	 * Create users through UserController (DataModel.users().create(...)
	 */
	MessageThread_V0() {
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
	private Set<User_V0> participants;

	@Type(Blob.class)
	private List<Message_V0> messages;

	public long getId() {
		return id;
	}
	
	public boolean isResolved() {
		return resolved;
	}

	public void setResolved(boolean resolved) {
		this.resolved = resolved;
	}

	public Set<User_V0> getParticipants() {
		return this.participants;
	}

	public void setParticipants(Set<User_V0> participants) {
		this.participants = participants;
	}

	public List<Message_V0> getMessages() {
		return this.messages;
	}

	public void setMessages(List<Message_V0> messages) {
		this.messages = messages;
	}

	public void addParticipant(User_V0 u) {
		this.participants.add(u);
		
	}
}
