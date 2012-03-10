package toMerge.PersonPackage;

/**
 * 
 * @author Brandon AND ONLY BRANDON!
 *
 */
public class Student extends Person
{
	//Need a way to store the absences -> actual dates
	//AttendanceRecord class?
		
	//This field can be used to determine the grade without having to
	//iterate thru all the absences and count them
	private int numAbsences = 0;

	public Student(String netID, String firstName, String lastName,
			String password, String major, String advisor, String position) {
		super(netID, firstName, lastName, password, major, advisor, position);
		// TODO Auto-generated constructor stub
	}
	
	public int getNumAbsences() {
		return numAbsences;
	}

	public void setNumAbsences(int numAbsences) {
		this.numAbsences = numAbsences;
	}
	
	public void addNumAbsences()
	{
		this.numAbsences++;
	}

}
