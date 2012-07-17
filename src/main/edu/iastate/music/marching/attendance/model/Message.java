package edu.iastate.music.marching.attendance.model;

import java.io.Serializable;
import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Version;

@Version(AttendanceDatastore.VERSION)
@Entity(kind="Message", allocateIdsBy=0)
public class Message implements Serializable {

	public static final long serialVersionUID = -6053273580635162117L;

	Message() {

	}

	//the author. Either a director or a student.
	private User author;
	
	@Type(Text.class)
	private String text;
	
	//the time at which the message was sent
	private Date timestamp;

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

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
