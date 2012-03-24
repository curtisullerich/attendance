package People;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.persistence.*;
/**
 * 
 * @author Brandon AND ONLY BRANDON!
 *
 */
@Entity(name="Person")
@Inheritance
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
	
	//Instance variables are pretty obvious
	private String password;

	public Person(String netID, String password, String firstName, String lastName)
	{
		this.netID = netID;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		id = hash(netID);
	}
	
	public Person(String netID, String firstName, String lastName, String password, String major, String advisor, String position)
	{
		this.setNetID(netID);
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
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
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getNetID() {
		return netID;
	}

	public void setNetID(String netID) {
		this.netID = netID;
	}

	public String toString()
	{
		return netID + " " + firstName + " " + lastName;
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
			return bigot.longValue();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
	}

}
