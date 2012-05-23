package edu.iastate.music.marching.attendance.controllers;

import java.util.List;

import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.User;

public class MessageThreadController extends AbstractController {

	private DataTrain dataTrain;

	public MessageThreadController(DataTrain dataTrain) {
		this.dataTrain = dataTrain;
	}

	public List<MessageThread> getAll() {
		return this.dataTrain.getDataStore().find().type(MessageThread.class)
				.returnAll().now();
	}

	/**
	 * TODO
	 * 
	 * @param user
	 *            Assume user is associated in data store
	 * @return
	 */
	public List<MessageThread> get(User user) {
		ObjectDatastore od = this.dataTrain.getDataStore();
		RootFindCommand<MessageThread> find = od.find().type(
				MessageThread.class);

		// Set the ancestor for this form, automatically limits results to be
		// forms of the user

		// TODO need to get a list of all MessageThreads for which this user is
		// a participant

		// find = find.addFilter(MessageThread.FIELD_STUDENT,
		// FilterOperator.EQUAL, user);
		return find.returnAll().now();
	}

	private void storeMessageThread(MessageThread mt) {
		// TODO: Reintroduce transactions

		// Perform store of this new form and update of student's grade in a
		// transaction to prevent inconsistent states
		// Track transaction = dataTrain.switchTracksInternal();

		// TODO check this with Dan
		// Store
		dataTrain.getDataStore().store(mt);

	}

//	public void addMessage(long threadid) {
//		this.dataTrain.getDataStore().g
//	}
	
	public MessageThread createMessageThread(String text) {
		return null; //TODO
	}
	
	
	public boolean removeMessageThread(Long id) {
		ObjectDatastore od = this.dataTrain.getDataStore();
		MessageThread mt = od.load(MessageThread.class, id);
		return true;
		// TODO will all messages in this thread automatically be deleted

	}

}
