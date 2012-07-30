package edu.iastate.music.marching.attendance.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.ObjectDatastore;
import com.google.common.collect.Sets;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.Message;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

public class MessagingController extends AbstractController {

	private DataTrain train;

	public MessagingController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public MessageThread createMessageThread(Form parentForm,
			User... initial_participants) {

		MessageThread thread = ModelFactory.newMessageThread();

		if (initial_participants != null) {
			thread.setParticipants(Sets.newHashSet(initial_participants));
		}

		// Default to resolved, no messages yet
		thread.setResolved(true);
		thread.setFormParent(parentForm);
		train.getDataStore().store(thread);

		return thread;
	}

	public MessageThread createMessageThread(Absence parentAbsence,
			User... initial_participants) {

		MessageThread thread = ModelFactory.newMessageThread();

		if (initial_participants != null) {
			thread.setParticipants(Sets.newHashSet(initial_participants));
		}

		// Default to resolved, no messages yet
		thread.setResolved(true);
		thread.setAbsenceParent(parentAbsence);
		train.getDataStore().store(thread);

		return thread;
	}

	public MessageThread addMessage(MessageThread thread, User sender,
			String message) {

		if (thread == null)
			throw new IllegalArgumentException("Null thread given");

		if (sender == null)
			throw new IllegalArgumentException("Null sender given");

		// Add sender as participant in conversation
		Set<User> thread_participants = thread.getParticipants();
		if (thread_participants == null) {
			thread_participants = new HashSet<User>();
			thread.setParticipants(thread_participants);
		}
		thread_participants.add(sender);

		// Add actual message
		List<Message> messages = thread.getMessages();
		if (messages == null) {
			messages = new ArrayList<Message>();
			thread.setMessages(messages);
		}
		messages.add(0, ModelFactory.newMessage(sender, message));

		// Always unresolved after adding a message
		thread.setResolved(false);

		update(thread);

		return thread;
	}

	public void update(MessageThread thread) {
		this.train.getDataStore().update(thread);
	}

	public List<MessageThread> get(User involved) {
		ObjectDatastore od = this.train.getDataStore();
		// TODO https://github.com/curtisullerich/attendance/issues/112
		// Daniel: Have to use Key types for an IN filter for now,
		// newer versions of twig-persist support using actual objects however
		Key k = od.associatedKey(involved);
		return this.train
				.find(MessageThread.class)
				.addFilter(MessageThread.FIELD_PARTICIPANTS, FilterOperator.IN,
						Arrays.asList(new Key[] { k })).returnAll().now();
	}

	public List<MessageThread> get(User involved, boolean resolved) {
		ObjectDatastore od = this.train.getDataStore();
		// TODO https://github.com/curtisullerich/attendance/issues/112
		// Daniel: Have to use Key types for an IN filter for now,
		// newer versions of twig-persist support using actual objects however
		Key k = od.associatedKey(involved);
		return this.train
				.find(MessageThread.class)
				.addFilter(MessageThread.FIELD_PARTICIPANTS, FilterOperator.IN,
						Arrays.asList(new Key[] { k }))
				.addFilter(MessageThread.FIELD_RESOLVED, FilterOperator.EQUAL,
						resolved).returnAll().now();
	}

	public List<MessageThread> get(boolean resolved) {
		return this.train
				.find(MessageThread.class)
				.addFilter(MessageThread.FIELD_RESOLVED, FilterOperator.EQUAL,
						resolved).returnAll().now();
	}

	public MessageThread get(long id) {
		return this.train.getDataStore().load(
				this.train.getTie(MessageThread.class, id));
	}

	public List<MessageThread> getAll() {
		return this.train.find(MessageThread.class).returnAll().now();
	}

	/**
	 * Returns the most recent message added to this MessageThread.
	 * 
	 * @author curtisu
	 * @return
	 */
	public Message getMostRecent(MessageThread mt) {
		List<Message> messages = mt.getMessages();
		if (messages.size() == 0) {
			return null;
		}
		Message mostRecent = messages.get(0);
		for (Message m : messages) {
			if (m.getTimestamp().after(mostRecent.getTimestamp())) {
				mostRecent = m;
			}
		}
		return mostRecent;
	}

	void scrubParticipant(User participant) {
		List<MessageThread> involved = this.get(participant);

		for (MessageThread thread : involved) {
			ListIterator<Message> iter = thread.getMessages().listIterator();

			while (iter.hasNext()) {
				Message message = iter.next();

				if (message != null && message.getAuthor() == participant) {
					iter.remove();
				}
			}

			thread.getParticipants().remove(participant);
		}
	}

	void delete(MessageThread mt) {
		this.train.getDataStore().delete(mt);
	}

}
