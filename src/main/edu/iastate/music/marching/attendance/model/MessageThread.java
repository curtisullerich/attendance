package edu.iastate.music.marching.attendance.model;

import java.util.List;

import com.google.code.twig.annotation.Embedded;

public class MessageThread {

	/**
	 * Create users through UserController (DataModel.users().create(...)
	 */
	MessageThread() {
	}

	private boolean resolved;

	@Embedded
	private List<Message> messages;

}
