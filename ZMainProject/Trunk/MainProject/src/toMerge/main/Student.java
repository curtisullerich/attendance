package toMerge.main;
/**
 * 
 * @author: Todd Wegter, Curtis Ullerich, Brandon Maxwell, and Yifei Zhu
 *
 */
public class Student extends Person
{
	//Need a way to store the absences -> actual dates
	//AttendanceRecord class?
		
	//This field can be used to determine the grade without having to
	//iterate thru all the absences and count them
	private int numAbsences;
	
	//students should be the only people with advisors
	private String advisor;
	public Student(String netID, String firstName, String lastName,
			String password, String major, String advisor, String position) {
		super(netID, firstName, lastName, password, major, position);
		this.advisor = advisor;
		numAbsences = 0;
	}
	
	public String getAdvisor() {
		return advisor;
	}

	public void setAdvisor(String advisor) {
		this.advisor = advisor;
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
