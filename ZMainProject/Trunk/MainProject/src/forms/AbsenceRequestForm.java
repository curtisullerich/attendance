package forms;

import java.io.File;
import java.util.List;

import people.Person;
import time.Date;

/**
 * @author Yifei Zhu
 *
 */
public class AbsenceRequestForm extends Form {

	private String additionalInfo;
	private File file;
	
	public AbsenceRequestForm(Person person, Date startDate, Date endDate,
			String reason, boolean isapproved, File f) {
		super(person, startDate, endDate, reason, isapproved);
		additionalInfo="";
		this.file=f;
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
	
	
	
	

}
