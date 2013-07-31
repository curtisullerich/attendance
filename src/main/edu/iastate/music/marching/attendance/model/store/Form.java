package edu.iastate.music.marching.attendance.model.store;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.LocalTime;

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

		public boolean isClassConflict() {
			return this == ClassConflict;
		}

		public boolean isPerformanceAbsence() {
			return this == PerformanceAbsence;
		}

		public boolean isTimeWorked() {
			return this == TimeWorked;
		}
	};

	public static final String FIELD_STUDENT = "student";
	public static final String HASHED_ID = "hashedId";

	@Id
	private long id;

	@Index
	@Activate
	private User student;

	@Index
	private Type type;

	private Status status;

	private boolean applied;

	private Absence.Type absenceType;

	@com.google.code.twig.annotation.Type(Text.class)
	private String details;

	private Date startDate;
	private Date endDate;
	private Date submissionTime;

	private String startTime;
	private String endTime;

	// Strings to be used by Class Conflict Form
	private String dept;
	private String course;
	private String section;

	private String building;
	private int day;
	private boolean late;
	private int minutesWorked;
	private int minutesToOrFrom;

	/**
	 * Create users through FormController
	 * (DataTrain.get().getFormsController().createFormA(...)
	 */
	Form() {
	}

	public Absence.Type getAbsenceType() {
		return absenceType;
	}

	public String getBuilding() {
		return building;
	}

	public String getCourse() {
		return course;
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

	public WeekDay getDayOfWeek() {
		return WeekDay.valueOf(day);
	}

	public String getDept() {
		return dept;
	}

	public String getDetails() {
		return this.details;
	}

	@Deprecated
	public Date getEnd() {
		return endDate;
	}

	public long getId() {
		return id;
	}

	public Interval getInterval(DateTimeZone zone) {
		return new Interval(new DateTime(this.startDate, zone), new DateTime(
				this.endDate, zone));
	}

	public int getMinutesToOrFrom() {
		return this.minutesToOrFrom;
	}

	public int getMinutesWorked() {
		return minutesWorked;
	}

	public String getSection() {
		return section;
	}

	@Deprecated
	public Date getStart() {
		return startDate;
	}

	public Status getStatus() {
		return this.status;
	}

	public String getStatusString() {
		return this.status.toString();
	}

	public User getStudent() {
		return this.student;
	}

	public DateTime getSubmissionDateTime() {
		return new DateTime(submissionTime);
	}

	@Deprecated
	public Date getSubmissionTime() {
		return submissionTime;
	}

	public Type getType() {
		return this.type;
	}

	public boolean isApplied() {
		return applied;
	}

	public boolean isLate() {
		return late;
	}

	public void setAbsenceType(Absence.Type absenceType) {
		this.absenceType = absenceType;
	}

	public void setApplied(boolean applied) {
		if (this.applied) {
			throw new IllegalArgumentException(
					"Once a Time Worked Form has been applied, it can never change again.");
		}
		this.applied = applied;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public void setDayOfWeek(WeekDay day) {
		this.day = day.DayOfWeek;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public void setInterval(Interval interval) {
		this.startDate = interval.getStart().toDate();
		this.endDate = interval.getEnd().toDate();
	}

	public void setLate(boolean late) {
		this.late = late;
	}

	public void setMinutesToOrFrom(int minutesToOrFrom) {
		this.minutesToOrFrom = minutesToOrFrom;
	}

	public void setMinutesWorked(int minutesWorked) {
		this.minutesWorked = minutesWorked;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setStudent(User student) {
		this.student = student;
	}

	public void setSubmissionTime(DateTime submissionTime) {
		this.submissionTime = submissionTime.toDate();
	}

	public void setType(Type type) {
		this.type = type;
	}

	public LocalTime getStartTime() {
		if (getType() != Type.ClassConflict)
			throw new UnsupportedOperationException();
		else
			return LocalTime.parse(this.startTime);
	}

	public LocalTime getEndTime() {
		if (getType() != Type.ClassConflict)
			throw new UnsupportedOperationException();
		else
			return LocalTime.parse(this.endTime);
	}

	public void setStartTime(LocalTime startTime) {
		if (getType() != Type.ClassConflict)
			throw new UnsupportedOperationException();
		else
			this.startTime = startTime.toString();
	}

	public void setEndTime(LocalTime endTime) {
		if (getType() != Type.ClassConflict)
			throw new UnsupportedOperationException();
		else
			this.endTime = endTime.toString();
	}
}
