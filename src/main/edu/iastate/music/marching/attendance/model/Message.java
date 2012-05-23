package edu.iastate.music.marching.attendance.model;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

public class Message {

	Message() {

	}

	private User author;

	@Type(Text.class)
	private String text;

	public User getAuthor() {
		return this.author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
