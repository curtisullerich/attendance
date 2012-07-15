package edu.iastate.music.marching.attendance.model.legacy;

import java.util.Date;

import com.google.code.twig.annotation.Activate;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Version;

@Version(0)
@Entity(kind="edu.iastate.music.marching.attendance.model.Absence", allocateIdsBy=10)
public class Absence_V0 {

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
	Absence_V0() {
	}

	@Id
	private long id;

	private Type type;

	private Status status;

	@Activate(1)
	@Index
	private Event_V0 event;

	@Activate(1)
	@Index
	private User_V0 student;

	@Activate(1)
	private MessageThread_V0 messages;

	private Date start;

	private Date end;

	public long getId() {
		return id;
	}

	public User_V0 getStudent() {
		return student;
	}

	public void setStudent(User_V0 student) {
		this.student = student;
	}

	public Event_V0 getEvent() {
		return event;
	}

	public void setEvent(Event_V0 event) {
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

	public MessageThread_V0 getMessageThread() {
		return this.messages;
	}

	public void setMessageThread(MessageThread_V0 messageThread) {
		this.messages = messageThread;
	}
}
