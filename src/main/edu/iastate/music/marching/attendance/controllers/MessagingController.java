package edu.iastate.music.marching.attendance.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.common.collect.Sets;

import edu.iastate.music.marching.attendance.model.Message;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

public class MessagingController extends AbstractController {

	private DataTrain train;

	public MessagingController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public MessageThread createMessageThread(User... initial_participants) {

		MessageThread thread = ModelFactory.newMessageThread();

		if (initial_participants != null)
			thread.setParticipants(Sets.newHashSet(initial_participants));

		return thread;
	}

	public MessageThread appendMessage(MessageThread thread, User sender,
			String message) {

		if (thread == null)
			throw new IllegalArgumentException("Null thread given");

		if (sender == null)
			throw new IllegalArgumentException("Null sender given");

		// Add sender as partitipant in conversation
		Set<User> thread_participants = thread.getParticipants();
		if (thread_participants == null)
		{
			thread_participants = new HashSet<User>();
			thread.setParticipants(thread_participants);
		}
		thread_participants.add(sender);

		// Add actual message
		List<Message> messages = thread.getMessages();
		if (messages == null)
		{
			messages = new ArrayList<Message>();
			thread.setMessages(messages);
		}
		messages.add(ModelFactory.newMessage(sender, message));
		
		update(thread);

		return thread;
	}

	public void update(MessageThread thread) {
		this.train.getDataStore().update(thread);
	}

	public List<MessageThread> get(User involved) {
		return this.train
				.getDataStore()
				.find()
				.type(MessageThread.class)
				.addFilter(MessageThread.FIELD_PARTICIPANTS, FilterOperator.IN,
						Arrays.asList(new User[] { involved })).returnAll()
				.now();
	}

	public List<MessageThread> get(User involved, boolean resolved) {
		return this.train
				.getDataStore()
				.find()
				.type(MessageThread.class)
				.addFilter(MessageThread.FIELD_PARTICIPANTS, FilterOperator.IN,
						Arrays.asList(new User[] { involved }))
				.addFilter(MessageThread.FIELD_RESOLVED, FilterOperator.EQUAL,
						resolved).returnAll().now();
	}

	public List<MessageThread> get(boolean resolved) {
		return this.train
				.getDataStore()
				.find()
				.type(MessageThread.class)
				.addFilter(MessageThread.FIELD_RESOLVED, FilterOperator.EQUAL,
						resolved).returnAll().now();
	}

	public MessageThread get(long id) {
		return this.train.getDataStore().load(MessageThread.class, id);
	}

	public List<MessageThread> getAll() {
		return this.train.getDataStore().find().type(MessageThread.class)
				.returnAll().now();
	}

}
