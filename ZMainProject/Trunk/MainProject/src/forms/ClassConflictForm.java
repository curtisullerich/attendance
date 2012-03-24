package forms;

import people.Person;
import time.Date;

/**
 * @author Yifei Zhu
 * 
 */
public class ClassConflictForm extends Form{

	private Course course;
	
	public ClassConflictForm(Person person, Date startDate, Date endDate,
			String reason, boolean isapproved, Course c) {
		super(person, startDate, endDate, reason, isapproved);
		
		this.setCourse(c);
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	
	
}
