package attendance;

import time.Date;
import time.Time;

public class Event 
{
	private Date date;
	private Time startTime;
	private Time endTime;
	
	public Event(Date date, Time start, Time end)
	{
		this.date = date;
		startTime = start;
		endTime = end;
	}
}