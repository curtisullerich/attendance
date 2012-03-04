package classes;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**
 * 
 * @author Brandon AND ONLY BRANDON!
 *
 */
@Entity
public class Person
{
	//This should be the hashed value of the person's key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String netID;
	//Can take these 2 strings and put them into 1 and delimit
	private String firstName;
	private String lastName;
	
	//Instance variables are pretty obvious
	private String password;
	private String major;
	private String advisor;
	private String position;
	
	//This is where absences and tardies are stored
	private AttendanceReport report;
	
	public Person(String netID, String password)
	{
		this.netID = netID;
		this.password = password;
		id = hash(netID);
		report = new AttendanceReport();
	}
	
	public Person(String netID, String firstName, String lastName, String password, String major, String advisor, String position)
	{
		this.setNetID(netID);
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.major = major;
		this.advisor = advisor;
		this.position = position;
		id = hash(netID);
		report = new AttendanceReport();
	}
	
	public String getFirstName() 
	{
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getAdvisor() {
		return advisor;
	}

	public void setAdvisor(String advisor) {
		this.advisor = advisor;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getNetID() {
		return netID;
	}

	public void setNetID(String netID) {
		this.netID = netID;
	}

	public String toString()
	{
		return netID + " " + firstName + " " + lastName + " " + major + " " + advisor + " " + position;
	}
	public boolean isComplete() {return (firstName != null && lastName != null);}

	public long hash(String netID)

	
	{
		try {
			MessageDigest cript = MessageDigest.getInstance("SHA-1");
			cript.reset();
			cript.update(netID.getBytes("utf8"));
			BigInteger bigot =  new BigInteger(cript.digest());
			return bigot.longValue();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}
	

	public void addTardy(Tardy newTardy)
	{
		report.addTardy(newTardy);
	}
	
	public void addAbsence(Absence newAbsence)
	{
		report.addAbsence(newAbsence);
	}
}
