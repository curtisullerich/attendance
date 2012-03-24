package forms;

import people.Person;
import time.Date;

/**
 * 
 * @author Yifei Zhu
 */
public class Course {
	//Course Info
	private String department;
	private String courseName;
	private String section;
	private String building;
	private String comments;
	//Date
	private Date sartDate;
	private Date endDate;
	
	//TODO: not sure about mon--fri  
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public String getBuilding() {
		return building;
	}
	public void setBuilding(String building) {
		this.building = building;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Date getSartDate() {
		return sartDate;
	}
	public void setSartDate(Date sartDate) {
		this.sartDate = sartDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	

	

}