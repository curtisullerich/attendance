package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Activate;
import com.google.code.twig.annotation.Child;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Index;
import com.google.code.twig.annotation.Parent;

import edu.iastate.music.marching.attendance.model.Absence.Status;

public class Form {

	public static enum Status {
		Pending, Approved, Denied
	};

	public static enum Type {
		A, B, C, D
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
	@Parent
	private User owner;

	@Index
	private User student;

	@Index
	private Type type;

	private Status status;

	private Status emailStatus;

	@Child
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

	// String to be used by Form D
	private String emailTo;
	private int hoursWorked;
	
	public long getId()
	{
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
	
	public Date getStart() {
		return this.startTime;
	}

	public void setStart(Date startDate) {
		this.startTime = startDate;
	}
	
	public Date getEnd() {
		return this.endTime;
	}

	public void setEnd(Date endDate) {
		this.endTime = endDate;
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

	public void setBuilding(String building) {
		this.building = building;
	}

}
