package main;
public class Person 
{
	private String netID;
	//Can take these 2 strings and put them into 1 and delimit
	private String firstName;
	private String lastName;
	
	//Instance variables are pretty obvious
	private String password;
	private String major;
	private String position;
	
	
	public Person(String netID, String firstName, String lastName, String password, String major, String position)
	{
		this.setNetID(netID);
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.major = major;
		this.position = position;
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


}
