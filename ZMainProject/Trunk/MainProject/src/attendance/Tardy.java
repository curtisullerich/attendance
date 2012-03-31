package attendance;

import time.Time;
/**
 *
 * @author Yifei Zhu
 *
 */
public class Tardy extends AWOL{

	public Tardy(Time s, Time e) {
		super(s, e);
	}
	
	public Tardy(String dbTardy)
	{
		String[] tardy = dbTardy.split(" ");
		setStartTime(new Time(tardy[0], tardy[1]));
	}
	
	public String toString()
	{
		//Since tardies only have 1 time im going to forget about the endtime
		//Should be in the form "year-month-day hour:minute:second"
		return getStartTime().getDate().toString() + " " + getStartTime().get24Format();
	}
	
	
}
