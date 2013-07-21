package edu.iastate.music.marching.attendance.model.store;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Activate;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;

import edu.iastate.music.marching.attendance.App.WeekDay;

@Entity(kind = "Form", allocateIdsBy = 0)
public class Form {

	public static enum Status {
		Pending, Approved, Denied;
		public String getValue() {
			return name();
		}

		private Status() {
			mDisplayString = this.toString();
		}

		private String mDisplayString;

		public String getDisplayName() {
			return mDisplayString;
		}

		public boolean isPending() {
			return this == Pending;
		}
	};

	public static enum Type {
		PerformanceAbsence, ClassConflict, TimeWorked;

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

		public boolean isPerformanceAbsence() {
			return this == PerformanceAbsence;
		}

		public boolean isClassConflict() {
			return this == ClassConflict;
		}

		public boolean isTimeWorked() {
			return this == TimeWorked;
		}
	};

	public static final String FIELD_STUDENT = "student";
	public static final String HASHED_ID = "hashedId";

	/**
	 * Create users through FormController
	 * (DataTrain.get().getFormsController().createFormA(...)
	 */
	Form() {
	}

	@Id
	private long id;

	@Index
	@Activate
	private User student;

	@Index
	private Type type;

	private Status status;

	public boolean isApplied() {
		return applied;
	}

	public void setApplied(boolean applied) {
		if (this.applied) {
			throw new IllegalArgumentException(
					"Once a Time Worked Form has been applied, it can never change again.");
		}
		this.applied = applied;
	}

	private boolean applied;

	private Absence.Type absenceType;

	@com.google.code.twig.annotation.Type(Text.class)
	private String details;

	private Date startTime;
	private Date endTime;
	private Date submissionTime;

	// Strings to be used by Class Conflict Form
	private String dept;
	private String course;
	private String section;
	private String building;
	private int day;

	public void setDayOfWeek(WeekDay day) {
		this.day = day.DayOfWeek;
	}

	public WeekDay getDayOfWeek() {
		return WeekDay.valueOf(day);
	}

	public String getDayAsString() {
		String ret = "";
		switch (day) {
		case 1:
			ret = "Sunday";
			break;
		case 2:
			ret = "Monday";
			break;
		case 3:
			ret = "Tuesday";
			break;
		case 4:
			ret = "Wednesday";
			break;
		case 5:
			ret = "Thursday";
			break;
		case 6:
			ret = "Friday";
			break;
		case 7:
			ret = "Saturday";
			break;
		}
		return ret;
	}

	public String getBuilding() {
		return building;
	}

	private int minutesWorked;
	private int minutesToOrFrom;

	public long getId() {
		return id;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public User getStudent() {
		return this.student;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getDetails() {
		return this.details;
	}

	public Interval getInterval() {
		return new Interval(new DateTime(this.startTime), new DateTime(this.endTime));
	}

	public void setInterval(Interval interval) {
		this.startTime = interval.getStart().toDate();
		this.endTime = interval.getEnd().toDate();
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public int getMinutesWorked() {
		return minutesWorked;
	}

	public void setMinutesWorked(int minutesWorked) {
		this.minutesWorked = minutesWorked;
	}

	public Status getStatus() {
		return this.status;
	}

	public String getStatusString() {
		return this.status.toString();
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public void setMinutesToOrFrom(int minutesToOrFrom) {
		this.minutesToOrFrom = minutesToOrFrom;
	}

	public int getMinutesToOrFrom() {
		return this.minutesToOrFrom;
	}

	public Absence.Type getAbsenceType() {
		return absenceType;
	}

	public void setAbsenceType(Absence.Type absenceType) {
		this.absenceType = absenceType;
	}

	public DateTime getSubmissionTime() {
		return new DateTime(submissionTime);
	}

	public void setSubmissionTime(DateTime submissionTime) {
		this.submissionTime = submissionTime.toDate();
	}
}
