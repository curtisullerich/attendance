package edu.iastate.music.marching.attendance.model.legacy;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Version;

@Version(0)
public class Message_V0 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7053270580375162117L;

	Message_V0() {

	}

	//the author. Either a director or a student.
	private User_V0 author;
	
	@Type(Text.class)
	private String text;
	
	//the time at which the message was sent
	private Date timestamp;

	public User_V0 getAuthor() {
		return this.author;
	}

	public void setAuthor(User_V0 author) {
		this.author = author;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
