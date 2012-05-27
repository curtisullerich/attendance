package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Activate;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;

import edu.iastate.music.marching.attendance.model.Absence.Status;

public class Form {

	public static enum Status {
		Pending, Approved, Denied;
		public String getValue() {
			return name();
		}
	};

	public static enum Type {
		A, B, C, D;

		public boolean isA() {
			return this == A;
		}

		public boolean isB() {
			return this == B;
		}

		public boolean isC() {
			return this == C;
		}

		public boolean isD() {
			return this == D;
		}
	};

	public static final String FIELD_STUDENT = "student";

	/**
	 * Create users through FormController
	 * (DataTrain.get().getFormsController().createFormA(...)
	 */
	Form() {
	}

	@Id
	private long id;

	/**
	 * Owning student
	 * 
	 */
	private User owner;

	@Index
	private User student;

	@Index
	private Type type;

	private Status status;

	private Status emailStatus;

	@Activate(1)
	private MessageThread messages;

	@com.google.code.twig.annotation.Type(Text.class)
	private String details;

	private Date startTime;
	private Date endTime;

	// Strings to be used by Form B
	private String dept;
	private String course;
	private String section;
	private String building;

	public String getBuilding() {
		return building;
	}

	// String to be used by Form D
	private String emailTo;
	private int hoursWorked;

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
		this.owner = student;
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
	
	public Date getStart() {
		return this.startTime;
	}

	public void setStart(Date startDate) {
		this.startTime = startDate;
	}

	public Date getEnd() {
		return this.endTime;
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

	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public int getHoursWorked() {
		return hoursWorked;
	}

	public void setHoursWorked(int hoursWorked) {
		this.hoursWorked = hoursWorked;
	}

	public void setEnd(Date endDate) {
		this.endTime = endDate;
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

	public MessageThread getMessageThread() {
		return this.messages;
	}

	public void setMessageThread(MessageThread messageThread) {
		this.messages = messageThread;
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

	public Status getEmailStatus() {
		return emailStatus;
	}

	public void setEmailStatus(Status emailStatus) {
		this.emailStatus = emailStatus;
	}

}
