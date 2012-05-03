package edu.iastate.music.marching.attendance.model;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

public class Message {

	Message() {

	}

	private User author;

	@Type(Text.class)
	private String text;

}
