package forms;

import java.io.File;

import people.Person;
import time.Date;

public class RehearsalAbsenceRequestForm extends AbsenceRequestForm{

	public RehearsalAbsenceRequestForm(Person person, Date startDate,
			Date endDate, String reason, boolean isapproved, File f,
			String typeAbsence) {
		super(person, startDate, endDate, reason, isapproved, f, typeAbsence);
	}

}
