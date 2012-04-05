package comment;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import people.User;
import time.Time;

/**
 * 
 * @author Yifei Zhu, Todd Wegter
 *
 */

@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  //creates id for entry
	private Long id;
	
	private String senderNetID;
	private String recipientNetID;
	private boolean read;
	private String content;
	private String time;
	
	//search for messages by this string
	private String correspondingAttendanceItem;
	
	public Message(String from, String to, String content, String correspondingAttendanceItem)
	{
		this.senderNetID = from;
		this.recipientNetID = to;
		this.content= content;
		this.read = false;
		this.correspondingAttendanceItem = correspondingAttendanceItem;
		this.time = new Time().toString(24);
	}
	
	public String getSender() {
		return senderNetID;
	}
	public void setSender(String netID) {
		this.senderNetID = netID;
	}
	public boolean isRead() {
		return read;
	}
	public void setRead(boolean read) {
		this.read = read;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Time getTime() {
		return new Time(time);
	}
	public void setTime(Time time) {
		this.time = time.toString(24);
	}
	
	public String getCorrespondingItem(){
		return correspondingAttendanceItem;
	}
	
	public void setCorrespondingItem(String correspondingItem){
		correspondingAttendanceItem = correspondingItem;
	}

}
