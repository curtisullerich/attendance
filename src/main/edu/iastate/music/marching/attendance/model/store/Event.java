package edu.iastate.music.marching.attendance.model.store;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;

@Entity(kind = "Event", allocateIdsBy = 0)
public class Event {

	public static final String FIELD_START = "start";
	public static final String FIELD_END = "end";
	public static final String FIELD_TYPE = "type";

	/**
	 * No-args constructor for datastore
	 */
	Event() {

	}

	@Id
	private long id;

	@Index
	private Date start;

	@Index
	private Date end;

	private Type type;

	public long getId() {
		return this.id;
	}

	public Interval getInterval() {
		return new Interval(new DateTime(start), new DateTime(end));
	}

	public void setInterval(Interval interval) {
		this.start = interval.getStart().toDate();
		this.end = interval.getEnd().toDate();
	}

	public enum Type {
		Rehearsal, Performance;
		private String mDisplayString;

		private Type() {
			mDisplayString = this.toString();
		}

		private Type(String display_string) {
			mDisplayString = display_string;
		}

		public boolean isRehearsal() {
			return Rehearsal.equals(this);
		}

		public boolean isPerformance() {
			return Performance.equals(this);
		}

		public String getDisplayName() {
			return mDisplayString;
		}

		public String getValue() {
			return name();
		}

	}

	public void setType(Event.Type type) {
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public String toString() {
		return "Start: " + start.toString() + " End: " + end.toString();
	}
}
