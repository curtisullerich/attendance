package edu.iastate.music.marching.attendance.model;

import java.io.Serializable;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(detachable="true")
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1421557192976557705L;

	public enum Type {
		Student, TA, Director;
		
		public boolean isStudent()
		{
			return this.equals(Student);
		}
		public boolean isTa()
		{
			return this.equals(TA);
		}
		public boolean isDirector()
		{
			return this.equals(Director);
		}
	}
	
	public enum Section {
		Piccolo,
		Clarinet,
		AltoSax("Alto Sax"),
		TenorSax("Tenor Sax"),
		Trumpet,
		Trombone,
		Mellophone,
		Baritone,
		Sousaphone,
		Guard,
		DrumMajor("Drum Major"),
		Staff,
		Drumline_Cymbals("Drumline: Cymbals"),
		Drumline_Tenors("Drumline: Tenors"),
		Drumline_Snare("Drumline: Snare"),
		Drumline_Bass("Drumline: Bass"),
		Twirler;
		
		private String mDisplayString;
		
		private Section() {
			mDisplayString = this.toString();
		}
		
		private Section(String display_string) {
			mDisplayString = display_string;
		}
		
		public String getDisplayName()
		{
			return mDisplayString;
		}
		
		public String getValue()
		{
			return name();
		}
	}


	/**
	 * Create users through UserController (DataModel.users().create(...)
	 */
	User() {

	}

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private Type type;
	
	@Persistent
	private String netID;
	
	@Persistent
	private byte[] password;
	
	@Persistent
	private byte[] salt;
	
	@Persistent
	private int universityID;
	
	@Persistent(mappedBy = "student")
	private Set<Absence> absences;
	
	@Persistent(mappedBy = "student")
	private Set<Form> forms;
	
	@Persistent
	private Section section;
	
	@Persistent
	private String firstName;
	
	@Persistent
	private String lastName;
	
	@Persistent
	private int year;
	
	@Persistent
	private String major;
	
	public Key getKey() {
        return key;
    }
	
	public void setKey(Key key) {
        this.key = key;
    }

	public String getFirstName() {
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

	public String getName() {
		return this.getFirstName() + " " + this.getLastName();
	}

	public byte[] getPasswordHash() {
		return password;
	}
	
	public void setPasswordHash(byte[] password) {
		this.password = password;
	}
	
	public byte[] getPasswordSalt() {
		return salt;
	}
	
	public void setPasswordSalt(byte[] salt) {
		this.salt = salt;
	}
	
	public String getNetID() {
		return this.netID;
	}

	public void setNetID(String netID) {
		this.netID = netID;
	}
	
	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
	
	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}
	
	public void setUniversityID(int new_universityID) {
		this.universityID = new_universityID;
	}
	
	public int getUniversityID()
	{
		return universityID;
	}

}
