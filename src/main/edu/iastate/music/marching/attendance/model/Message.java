package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

public class Message {

	Message() {

	}

	//the author. Either a director or a student.
	private User author;
	
	@Type(Text.class)
	private String text;
	
	//the time at which the message was sent
	private Date timestamp;

}
