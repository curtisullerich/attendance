package people;

import java.io.File;

import javax.persistence.*;


/**
 * 
 * @author Yifei Zhu
 *
 */

@Entity(name="TA")
@DiscriminatorValue("TA")
public class TA extends Person{

	public TA(String netID, String password, String firstName, String lastName, String univID) {
		super(netID, password, firstName, lastName, univID);
		// TODO Auto-generated constructor stub
	}
	

	

}
