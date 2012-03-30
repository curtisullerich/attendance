package people;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.persistence.*;
/**
 * 
 * @author Brandon AND ONLY BRANDON!
 *
 */
@Entity(name="Person")
@Inheritance(strategy=InheritanceType.JOINED)
@MappedSuperclass
@DiscriminatorColumn(name="PersonType")
public class Person
{
	//This should be the hashed value of the person's key
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	private String netID;
	//Can take these 2 strings and put them into 1 and delimit
	private String firstName;
	private String lastName;
	private String hashedUnivID;
	
	//Instance variables are pretty obvious
	private String hashedPassword;

	public String toHTML() {
		String ret = "";
		ret += "<p>Name: " + firstName + " " + lastName + "</p>\n";
		ret += "<p>NetID: " + netID + "</p>\n";
		return ret;
	}
	
	public Person(String netID, String password, String firstName, String lastName, String univID)
	{
		this.netID = netID;
		this.hashedPassword = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.hashedUnivID = univID;
		id = hash(netID);
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
		return hashedPassword;
	}

	public void setPassword(String password) {
		this.hashedPassword = Long.toHexString(hash(password)).toUpperCase();
	}

	public String getNetID() {
		return netID;
	}

	public void setNetID(String netID) {
		this.netID = netID;
	}
	
	public String getUnivID(){
		return hashedUnivID;
	}
	
	public void setUnivID(String univID){
		this.hashedUnivID =  Long.toHexString(hash(univID)).toUpperCase();
	}

	public String toString()
	{
		return netID + " " + firstName + " " + lastName + " " + hashedUnivID;
	}
	public boolean isComplete() {return (firstName != null && lastName != null);}
	
	public String getRank(){
		return "|";
	}

	//protected long getId() {return id;}
	public long hash(String netID)
	{
		try {
			MessageDigest cript = MessageDigest.getInstance("SHA-1");
			cript.reset();
			cript.update(netID.getBytes("utf8"));
			BigInteger bigot =  new BigInteger(cript.digest());
			//Something about things
			return bigot.longValue();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}

}
