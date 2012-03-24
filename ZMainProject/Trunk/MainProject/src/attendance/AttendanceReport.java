package attendance;
import java.util.ArrayList;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import forms.Form;
/**
 * 
 * @author Yifei Zhu
 *
 */
public class AttendanceReport implements Serializable{
	
	List<Absence> absences;
	List<Tardy> tardies;
	private List<Form> forms;


	public AttendanceReport() {
		absences = new LinkedList<Absence>();
		tardies = new LinkedList<Tardy>();
		forms = new ArrayList<Form>();
	}

	public void addAbsence(Absence newAbsence) {
		absences.add(newAbsence);
	}

	public void addTardy(Tardy newTardy) {
		tardies.add(newTardy);
	}
	
	
	public List<Absence> getAbsences() {
		return absences;
	}

	public void setAbsences(List<Absence> absences) {
		this.absences = absences;
	}

	public List<Tardy> getTardies() {
		return tardies;
	}

	public void setTardies(List<Tardy> tardies) {
		this.tardies = tardies;
	}

	public List<Form> getForms() {
		return forms;
	}

	public void setForms(List<Form> forms) {
		this.forms = forms;
	}

	/**
	 * Sort the forms by type, date, and so on
	 * @param f --list of forms
	 */
	public void sort(Form f)
	{
		
		
	}
	
	

}
