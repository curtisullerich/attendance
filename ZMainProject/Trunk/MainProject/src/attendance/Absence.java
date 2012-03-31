package attendance;
import time.Time;
/**
 * 
 * @author Yifei Zhu
 *
 */
public class Absence extends AWOL{

	public Absence(Time s, Time e) {
		super(s, e);
	}

	public String toString()
	{
		//                                        start Time              End Time
		//                                             |                      |        
		//Should be in the form "year-month-day hour:minute:second hour:minute:second"
		return getStartTime().getDate().toString() + " " + getStartTime().get24Format() 
				+ " " + getEndtTime().get24Format();
	}
}
