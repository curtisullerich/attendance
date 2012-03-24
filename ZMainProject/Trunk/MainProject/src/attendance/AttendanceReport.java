package attendance;

import java.util.LinkedList;
import java.util.List;

public class AttendanceReport {
	List<Absence> absences;
	List<Tardy> tardies;

	public AttendanceReport() {
		absences = new LinkedList<Absence>();
		tardies = new LinkedList<Tardy>();
	}

	public void addAbsence(Absence newAbsence) {
		absences.add(newAbsence);
	}

	public void addTardy(Tardy newTardy) {
		tardies.add(newTardy);
	}

}