package attendance;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;

import serverLogic.DatabaseUtil;
import time.Time;

import comment.Message;

/**
 * 
 * @author Todd Wegter, Yifei Zhu, Curtis Ullerich
 * 
 */

// get and set messages with key field with corresponding times and type

@Entity
public class Tardy {

	@Id
	// creates id for entry
	private Long id;

	// The netID of the student that this Tardy belongs to
	private String netID;
	private String checkInTime;
	private String status;
	// Either rehearsal or performance or unknown if there was no Event
	private String type;

	@Basic
	private String[] messageIDs;
	private int currentIndex;

	public Tardy(String netID, Time time, String type) {
		this.netID = netID;
		this.checkInTime = time.toString(24);
		this.status = "pending";
		setType(type);
		this.id = hash(netID, time);
		this.currentIndex = 0;
		messageIDs = new String[10];
		for (int i = 0; i < messageIDs.length; i++) {

			messageIDs[i] = "";
		}
	}

	// public Tardy(String dbTardy)
	// {
	// String[] tardy = dbTardy.split(" ");
	// setTime(new Time(tardy[0], tardy[1]));
	// approved = false;
	// }

	public String toString() {
		// Should be in the form
		// "year-month-day hour:minute:second type isApproved"
		return getTime().getDate().toString() + " " + getTime().get24Format()
				+ " " + type + " " + status;
	}

	public Time getTime() {
		return new Time(checkInTime);
	}

	public void setTime(Time time) {
		this.checkInTime = time.toString(24);
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		if (type.equalsIgnoreCase("performance"))
			this.type = "performance";
		else if (type.equalsIgnoreCase("rehearsal"))
			this.type = "rehearsal";
		else
			this.type = "unknown";
	}

	public boolean isRehearsal() {
		return type.equalsIgnoreCase("rehearsal");
	}

	public boolean isPerformance() {
		return type.equalsIgnoreCase("performance");
	}

	// public void setID(long id) {
	// this.id = id;
	// }

	public Long getID() {
		return id;
	}

	public List<Message> getMessages() {
		System.out.println("MESSAGEIDS is " + messageIDs);
		return DatabaseUtil.getMessages(messageIDs);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o.getClass() != this.getClass())
			return false;
		Tardy t = (Tardy) o;
		return t.netID.equals(netID) && t.getTime().compareTo(getTime()) == 0
				&& t.type.equalsIgnoreCase(type);
	}

	/**
	 * 
	 * Adds a new message associated with this Tardy.
	 * 
	 * 
	 * @author Curtis Ullerich
	 * @param m
	 *            the message to be added
	 */
	public void addMessage(Message m) {
		DatabaseUtil.addMessage(m);
		if (currentIndex >= messageIDs.length) {
			messageIDs = Arrays.copyOf(messageIDs, messageIDs.length * 2);
		}
		messageIDs[currentIndex] = new Long(m.getID()).toString();
		currentIndex++;
	}

	/**
	 * 
	 * Checks to see if this Tardy has a new message for a person.
	 * 
	 * @author Curtis Ullerich
	 * @param netId
	 *            the user in question
	 * @return true if this tardy has a new message for this user. False
	 *         otherwise.
	 */

	public boolean hasNewMessageFor(String netId) {
		for (Message m : this.getMessages()) {
			if (!m.readBy(netId)) {
				return true;
			}
		}
		return false;
	}

	public long hash(String netID, Time time) {
//		try {
//			String id = netID + time.toString(24);
//			MessageDigest cript = MessageDigest.getInstance("SHA-1");
//			cript.reset();
//			cript.update(id.getBytes("utf8"));
//			BigInteger bigot = new BigInteger(cript.digest());
//			// Something about things
//			return bigot.longValue();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return 0;
//		}
		int retVal = 17;
		retVal += netID.toString().hashCode() * 23;
		retVal += time.toString(24).hashCode() * 7;
		return retVal;
	}

	/**
	 * 
	 * Returns true if this Tardy was during the given event.
	 * 
	 * @author Curtis Ullerich
	 * @date 4.9.12
	 * @param event
	 * @return
	 */
	public boolean isDuringEvent(Event event) {
		//TODO we should probably test this.
		return (this.getTime().compareTo(event.getStartTime()) >= 0 
				&& this.getTime().compareTo(event.getEndTime())<=0);
		
	}

}
//		}
	}

	/**
	 * 
	 * Returns true if this Tardy was during the given event.
	 * 
	 * @author Curtis Ullerich
	 * @date 4.9.12
	 * @param event
	 * @return
	 */
	public boolean isDuringEvent(Event event) {
		//TODO we should probably test this.
		return (this.getTime().compareTo(event.getStartTime()) >= 0 
				&& this.getTime().compareTo(event.getEndTime())<=0);
		
	}

}
