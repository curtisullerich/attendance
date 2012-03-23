package formsPackage;

import Time.Date;
import Time.DateAndTime;

public class Tardy 
{
	//This date corresponds to a particular event date
	private Date date;
	
	//These times correspond to a particular event time
	private DateAndTime startTime;
	private DateAndTime endTime;
	
	public Tardy (Date date, DateAndTime start, DateAndTime end)
	{
		this.date = date;
		startTime = start;
		endTime = end;
	}
}
