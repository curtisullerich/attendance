package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import com.google.code.twig.annotation.Activate;
import com.google.code.twig.annotation.Child;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Parent;

public class Absence {

	public enum Type {
		Absence, Tardy, EarlyCheckOut;

		public boolean isAbsence() {
			return Absence.equals(this);
		}

		public boolean isTardy() {
			return Tardy.equals(this);
		}

		public boolean isEarlyCheckOut() {
			return EarlyCheckOut.equals(this);
		}
	}

	public static enum Status {
		Pending, Approved, Denied
	};

	public static final String FIELD_EVENT = "event";
	public static final String FIELD_STUDENT = "student";

	/**
	 * Create absence through AbsenceController (DataModel.absence().create(...)
	 */
	Absence() {
	}

	@Id
	private long id;

	private Type type;

	private Status status;

	@Parent
	@Activate(1)
	@Index
	private Event event;

	@Activate(0)
	@Index
	private User student;

	@Child
	@Activate(1)
	private MessageThread messages;

	private Date start;

	private Date end;

	public long getId() {
		return id;
	}

	public User getStudent() {
		return student;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getDatetime() {
		return this.start;
	}

	public void setDatetime(Date time) {
		this.start = time;
	}

	public Date getStart() {
		return this.start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return this.end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Status getStatus() {
		return this.status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public MessageThread getMessageThread() {
		return this.messages;
	}

	public void setMessageThread(MessageThread messageThread) {
		this.messages = messageThread;
	}

}
