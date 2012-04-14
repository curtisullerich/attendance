package comment;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import time.Time;

/**
 * 
 * @author Yifei Zhu, Todd Wegter
 * 
 */
@Entity
public class Message implements Comparable<Message> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// creates id for entry
	private Long id;

	private String senderNetID;
	private String recipientNetID;
	private String contents;
	private String time;
	private String parentID;
	private String parentType;
	
	@Basic
	private String[] readers;

	public Message(String from, String to, String contents, String parentID, String parentType) {
		this.senderNetID = from;
		this.recipientNetID = to;
		this.contents = contents;
		this.readers = new String[2];
		readers[0] = senderNetID;
		readers[1] = "";
		this.time = new Time().toString(24);
		this.setParentID(parentID);
		this.setParentType(parentType);
	}

	public long getID() {
		
		return this.id;
	}

	/**
	 * @author Curtis Ullerich
	 * @date 4/14/12
	 */
	public void copyAllFrom(Message other) {//TODO is everything copied?
		this.senderNetID = other.senderNetID;
		this.recipientNetID = other.recipientNetID;
		this.contents = other.contents;
		this.readers = other.readers;
		this.time = other.time;
	}
	
	public String getSender() {
		return senderNetID;
	}

	public void setSender(String netID) {
		this.senderNetID = netID;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Time getTime() {
		return new Time(time);
	}

	public void setTime(Time time) {
		this.time = time.toString(24);
	}

	public String getRecipientNetID() {
		return recipientNetID;
	}

	public void setRecipientNetID(String netID) {
		this.recipientNetID = netID;
	}
	
	public String getSenderNetID() {
		return senderNetID;
	}
	
	public boolean readBy(String netID) {
		if (readers [1] == null) readers[1] = "";//TODO probably remove
		if (readers [0] == null) readers[0] = "";
		
		return (readers[0].equals(netID) || readers[1].equals(netID));
	}

	/**
	 * @author Curtis Ullerich
	 * @date 4/14/12
	 * @param netID
	 */
	public void setReadBy(String netID) {
		if (!readBy(netID)) {
			this.readers[1] = netID;
		}
	}
		
	@Override
	public int compareTo(Message o) {
		if(o==null)
		{
			throw new NullPointerException("date is null");
		}
		//Note. I reversed this on purpose, because messages should be sorted reverse-chronologically.
		return o.time.compareTo(this.time);
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getParentType() {
		return parentType;
	}

	public void setParentType(String parentType) {
		this.parentType = parentType;
	}

}
