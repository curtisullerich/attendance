package attendance;

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
 * @author Yifei Zhu, Todd Wegter
 * 
 */

// get and set messages with key field with corresponding times and type

@Entity
public class Absence {

	@Id
	private Long id;

	// The netID for the student this Absence belongs to
	private String netID;

	@Basic
	private String[] messageIDs;

	private String startTime;
	private String endTime;
	private String status;
	// Either Rehearsal or Performance
	private String type;

	private int currentIndex;

	public Absence(String netID, Time startTime, Time endTime, String type) {
		this.netID = netID;
		this.startTime = startTime.toString(24);
		this.endTime = endTime.toString(24);
		this.status = "pending";
		setType(type);
		this.currentIndex = 0;
		this.id = hash(netID, startTime, endTime);
		messageIDs = new String[10];
		for (int i = 0; i < messageIDs.length; i++) {
			messageIDs[i] = "";
		}
	}

	// public Absence(String dbAbsence)
	// {
	// String[] info = dbAbsence.split(" ");
	// Time start = new Time(info[0], info[1]);
	// Time end = new Time (info[0], info[2]);
	// this.setStartTime(start);
	// this.setEndTime(end);
	// this.type = info[3];
	// this.isApproved = Boolean.parseBoolean(info[4]);
	// }

	/**
	 * Returns true if this Absence was during the given event.
	 * 
	 * @author Curtis Ullerich
	 * @date 4/9/12
	 * @param event
	 * @return
	 */
	public boolean isDuringEvent(Event event) {
		// TODO we should probably test this
		return (this.getStartTime().compareTo(event.getStartTime()) == 0 && this
				.getEndTime().compareTo(event.getEndTime()) == 0);
	}

	public String toString() {
		// start Time End Time
		// | |
		// Should be in the form
		// "year-month-day hour:minute:second hour:minute:second type isApproved"
		return getStartTime().getDate().toString() + " "
				+ getStartTime().get24Format() + " "
				+ getEndTime().get24Format() + " " + type + " " + status;
	}

	public Time getStartTime() {
		return new Time(startTime);
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime.toString(24);
	}

	public Time getEndTime() {
		return new Time(endTime);
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime.toString(24);
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

	public String getNetID() {
		return netID;
	}

	public void setNetID(String netID) {
		this.netID = netID;
	}

	public void setType(String type) {
		if (type.equalsIgnoreCase("performance"))
			this.type = "performance";
		else
			this.type = "rehearsal";
	}

	public boolean isRehearsal() {
		return type.equalsIgnoreCase("rehearsal");
	}

	public boolean isPerformance() {
		return type.equalsIgnoreCase("performance");
	}

	public void setID(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	public List<Message> getMessages() {
		return DatabaseUtil.getMessages(messageIDs);
	}

	/**
	 * Adds a new message associated with this Absence.
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

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o.getClass() != this.getClass())
			return false;
		Absence a = (Absence) o;
		return a.netID.equals(netID)
				&& a.getStartTime().compareTo(getStartTime()) == 0
				&& a.getEndTime().compareTo(getEndTime()) == 0
				&& a.type.equalsIgnoreCase(type);
	}

	public long hash(String netID, Time startTime, Time endTime) {
		// try {
		// String id = netID + startTime.toString(24) + endTime.toString(24);
		// MessageDigest cript = MessageDigest.getInstance("SHA-1");
		// cript.reset();
		// cript.update(id.getBytes("utf8"));
		// BigInteger bigot = new BigInteger(cript.digest());
		// // Something about things
		// return bigot.longValue();
		// } catch (Exception e) {
		// e.printStackTrace();
		// return 0;
		int retVal = 17;
		retVal += netID.hashCode() * 13;
		retVal += startTime.toString(24).hashCode() * 7;
		retVal += endTime.toString(24).hashCode() * 23;
		return (long) retVal;
	}

}
