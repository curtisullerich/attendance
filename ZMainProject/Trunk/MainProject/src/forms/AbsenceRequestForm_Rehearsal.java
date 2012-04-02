package forms;

import java.io.File;

import people.Person;
import time.Date;

public class AbsenceRequestForm_Rehearsal extends AbsenceRequestForm{

	public AbsenceRequestForm_Rehearsal(Person person, Date startDate,
			Date endDate, String reason, boolean isapproved, File f) 
	{
		super(person, startDate, endDate, reason, isapproved, f);
	}

	/**
	 * Overwrite toString method, return the type of form
	 */
	public String toString()
	{
		return "RehearsalAbsenceRequestForm";
	}
}
