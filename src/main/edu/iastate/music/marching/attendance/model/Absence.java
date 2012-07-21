package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Version;

@Version(AttendanceDatastore.VERSION)
@Entity(kind="Absence", allocateIdsBy=0)
public class Absence {

	public enum Type {
		Absence, Tardy, EarlyCheckOut;

		private String mDisplayString;

		private Type() {
			mDisplayString = this.toString();
		}

		private Type(String display_string) {
			mDisplayString = display_string;
		}

		public boolean isAbsence() {
			return Absence.equals(this);
		}

		public boolean isTardy() {
			return Tardy.equals(this);
		}

		public boolean isEarlyCheckOut() {
			return EarlyCheckOut.equals(this);
		}

		public String getDisplayName() {
			return mDisplayString;
		}

		public String getValue() {
			return name();
		}
	}

	public static enum Status {
		Pending, Approved, Denied;

		private String mDisplayString;

		private Status() {
			mDisplayString = this.toString();
		}

		public String getDisplayName() {
			return mDisplayString;
		}

		public String getValue() {
			return name();
		}

		public boolean isPending() {
			return this == Pending;
		}

		public boolean isApproved() {
			return this == Approved;
		}

		public boolean isDenied() {
			return this == Denied;
		}
	};

	public static final String FIELD_EVENT = "event";
	public static final String FIELD_STUDENT = "student";
	public static final String FIELD_TYPE = "type";

	/**
	 * Create absence through AbsenceController (DataModel.absence().create(...)
	 */
	Absence() {
	}

	@Id
	private long id;

	private Type type;

	private Status status;

	@Index
	private Event event;

	@Index
	private User student;

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

	/**
	 * The time of a tardy or earlycheckout.
	 * 
	 * @return time of a tardy or earlycheckout.
	 */
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
