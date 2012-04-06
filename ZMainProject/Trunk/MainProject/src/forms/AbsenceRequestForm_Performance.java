package forms;

import java.io.File;
import java.util.List;

import time.Date;

public class AbsenceRequestForm_Performance extends AbsenceRequestForm {

	public AbsenceRequestForm_Performance(Person person, Date startDate,
			Date endDate, String reason, boolean isapproved, File f) 
	{
		super(person, startDate, endDate, reason, isapproved, f);
	}
	/**
	 * Overwrite toString method, return the type of form
	 */
	public String toString()
	{
		return "PerformanceAbsenceRequestForm";
	}

}
