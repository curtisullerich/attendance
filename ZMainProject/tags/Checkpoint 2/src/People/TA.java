package People;

import java.io.File;


/**
 * 
 * @author Yifei Zhu
 *
 */

@Entity(name="TA")
@DiscriminatorValue("TA")
public class TA extends Person{

	public TA(String netID, String password, String firstName, String lastName) {
		super(netID, password, firstName, lastName);
		// TODO Auto-generated constructor stub
	}
	

	

}
