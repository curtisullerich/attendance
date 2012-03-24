package forms;

import java.io.File;

import people.Person;
import time.Date;

/**
 * @author Yifei Zhu
 *
 */
public class AbsenceRequestForm extends Form {

	private String additionalInfo;
	private File file;
	private String typeOfAbsence; //TODO :better structure 
	
	public AbsenceRequestForm(Person person, Date startDate, Date endDate,
			String reason, boolean isapproved, File f,String typeAbsence) {
		super(person, startDate, endDate, reason, isapproved);
		additionalInfo="";
		this.file=f;
		this.typeOfAbsence=typeAbsence;
		//TODO : file in constructor 
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getTypeOfAbsence() {
		return typeOfAbsence;
	}

	public void setTypeOfAbsence(String typeOfAbsence) {
		this.typeOfAbsence = typeOfAbsence;
	}
	
	
	

}
