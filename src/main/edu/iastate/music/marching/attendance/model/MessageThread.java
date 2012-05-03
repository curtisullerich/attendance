package edu.iastate.music.marching.attendance.model;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;

import com.google.code.twig.annotation.Child;
import com.google.code.twig.annotation.Embedded;
import com.google.code.twig.annotation.Parent;

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
