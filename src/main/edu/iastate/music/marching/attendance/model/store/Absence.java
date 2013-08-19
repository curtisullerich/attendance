package edu.iastate.music.marching.attendance.model.store;

import java.util.Date;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.ReadableInterval;

import com.google.code.twig.annotation.Activate;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Store;

@Entity(kind = "Absence", allocateIdsBy = 0)
public class Absence {

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

		public boolean isApproved() {
			return this == Approved;
		}

		public boolean isDenied() {
			return this == Denied;
		}

		public boolean isPending() {
			return this == Pending;
		}
	}

	public enum Type {
		Absence, Tardy, EarlyCheckOut;

		private String mDisplayString;

		private Type() {
			mDisplayString = this.toString();
		}

		private Type(String display_string) {
			mDisplayString = display_string;
		}

		public String getDisplayName() {
			return mDisplayString;
		}

		public String getValue() {
			return name();
		}

		public boolean isAbsence() {
			return Absence.equals(this);
		}

		public boolean isEarlyCheckOut() {
			return EarlyCheckOut.equals(this);
		}

		public boolean isTardy() {
			return Tardy.equals(this);
		}
	};

	public static final String FIELD_EVENT = "event";
	public static final String FIELD_STUDENT = "student";
	public static final String FIELD_TYPE = "type";

	@Id
	private long id;

	private Type type;

	private Status status;

	@Index
	@Activate
	private Event event;

	@Index
	@Activate
	private User student;

	private Date start;

	private Date end;

	@Store(false)
	private DateTime cachedCheckout;

	@Store(false)
	private DateTime cachedCheckin;
	@Store(false)
	private Interval cachedInterval;

	/**
	 * Create absence through AbsenceController (DataModel.absence().create(...)
	 */
	Absence() {
	}

	public static boolean isContainedIn(Absence a, ReadableInterval i) {
		if (a.getType() == null || i == null)
			return false;

		Chronology chron = i.getChronology();

		switch (a.getType()) {
		case Absence:
			return i.contains(a.getInterval(chron));
		case EarlyCheckOut:
			DateTime checkout = a.getCheckout(chron);
			return i.contains(checkout);
		case Tardy:
			DateTime checkin = a.getCheckin(chron);
			return i.contains(checkin);
		default:
			throw new UnsupportedOperationException();
		}
	}

	public DateTime getCheckin(DateTimeZone zone) {
		if (getType().isTardy())
			return new DateTime(this.start, zone);
		else
			throw new IllegalStateException("Intervals only valid for absences");
	}

	public DateTime getCheckin(Chronology chron) {
		if (getType().isTardy())
			return new DateTime(this.start, chron);
		else
			throw new IllegalStateException("Intervals only valid for absences");
	}

	public DateTime getCheckout(DateTimeZone zone) {
		if (getType().isEarlyCheckOut())
			return new DateTime(this.start, zone);
		else
			throw new IllegalStateException(
					"Checkouts only valid for early checkouts");
	}

	public DateTime getCheckout(Chronology chron) {
		if (getType().isEarlyCheckOut())
			return new DateTime(this.start, chron);
		else
			throw new IllegalStateException("Intervals only valid for absences");
	}

	@Deprecated
	public Date getDatetime() {
		return start;
	}

	@Deprecated
	public Date getEnd() {
		return end;
	}

	public Event getEvent() {
		return event;
	}

	public long getId() {
		return id;
	}

	/**
	 * Time-span of an absence
	 */
	public Interval getInterval(DateTimeZone zone) {
		if (getType().isAbsence()) {
			return new Interval(new DateTime(this.start, zone), new DateTime(
					this.end, zone));

		} else
			throw new IllegalStateException("Intervals only valid for absences");
	}

	/**
	 * Time-span of an absence
	 */
	public Interval getInterval(Chronology chron) {
		if (getType().isAbsence()) {
			return new Interval(new DateTime(this.start, chron), new DateTime(
					this.end, chron));

		} else
			throw new IllegalStateException("Intervals only valid for absences");
	}

	@Deprecated
	public Date getStart() {
		return start;
	}

	public Status getStatus() {
		return this.status;
	}

	public User getStudent() {
		return student;
	}

	public Type getType() {
		return this.type;
	}

	public void setCheckin(DateTime datetime) {
		if (getType().isTardy())
			this.start = datetime.toDate();
		else
			throw new IllegalStateException("Check-in times only valid for tardies");

	}

	public void setCheckout(DateTime datetime) {
		if (getType().isEarlyCheckOut())
			this.start = datetime.toDate();
		else
			throw new IllegalStateException("Checkout times only valid for early check outs");

	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setInterval(Interval interval) {
		if (getType().isAbsence()) {
			this.end = interval.getEnd().toDate();
			this.start = interval.getStart().toDate();
		} else
			throw new IllegalStateException("Intervals only valid for absences");

	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public void setType(Type type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return student.toString() + " Start: " + start.toString() + " End: "
				+ end.toString();
	}
}
