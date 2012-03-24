package forms;

import java.io.File;

import people.Person;
import time.Date;

public class AbsenceRequestForm_Rehearsal extends AbsenceRequestForm{

	public AbsenceRequestForm_Rehearsal(Person person, Date startDate,
			Date endDate, String reason, boolean isapproved, File f,
			String typeAbsence) {
		super(person, startDate, endDate, reason, isapproved, f, typeAbsence);
	}

}
