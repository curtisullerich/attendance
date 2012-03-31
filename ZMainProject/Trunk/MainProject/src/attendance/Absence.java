package attendance;
import time.*;
/**
 * 
 * @author Yifei Zhu
 *
 */
public class Absence extends AWOL{

	public Absence(Time s, Time e) {
		super(s, e);
	}

	public Absence(String dbAbsence)
	{	
		String[] info = dbAbsence.split(" ");
		Time start = new Time(info[0], info[1]);
		Time end = new Time (info[0], info[2]);
		this.setStartTime(start);
		this.setEndtTime(end);
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
