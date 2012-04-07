package attendance;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import serverLogic.DatabaseUtil;
import forms.Form;

/**
 * 
 * @author Brandon Maxwell, Todd Wegter, Yifei Zhu
 * 
 */

@Entity
public class AttendanceReport {

	@Id
	// creates id for entry
	private Long id;

	private String netID; // netID of corresponding student

	public AttendanceReport(String netID) {
		this.netID = netID;
		this.id = hash(netID);
	}

	public void addAbsence(Absence newAbsence) {
		DatabaseUtil.addAbsence(newAbsence);
	}

	public void addTardy(Tardy newTardy) {
		DatabaseUtil.addTardy(newTardy);
	}

	public List<Absence> getAbsences() {
		return DatabaseUtil.getAbsences(netID);
	}

	public List<Tardy> getTardies() {
		return DatabaseUtil.getTardies(netID);
	}

	public List<Form> getForms() {
		return DatabaseUtil.getForms(netID);
	}

	/**
	 * Sort the forms by given Comparator (type, date, and so on) in descending
	 * order
	 * 
	 * @param comp
	 *            - Comparator for comparing the forms
	 */
	public List<Form> sortFormsDescending(Comparator<Form> comp) {
		List<Form> forms = getForms();
		for (int i = 0; i < forms.size() - 1; i++) {
			for (int j = i + 1; j < forms.size(); j++) {
				if (comp.compare(forms.get(i), forms.get(j)) < 0) { // larger
																	// first
					Form temp = forms.get(i);
					forms.set(i, forms.get(j));
					forms.set(j, temp);
				}
			}
		}
		return forms;
	}

	/**
	 * Sort the forms by given Comparator (type, date, and so on) in ascending
	 * order
	 * 
	 * @param comp
	 *            - Comparator for comparing the forms
	 */
	public List<Form> sortFormsAscending(final Comparator<Form> comp) {
		return sortFormsDescending(new Comparator<Form>() {

			@Override
			public int compare(Form o1, Form o2) {
				return -comp.compare(o1, o2);
			}

		});
	}
	
	public long hash(String netID) {
		try {
			MessageDigest cript = MessageDigest.getInstance("SHA-1");
			cript.reset();
			cript.update(netID.getBytes("utf8"));
			BigInteger bigot = new BigInteger(cript.digest());
			// Something about things
			return bigot.longValue();

		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
