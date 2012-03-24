package time;

public class Event 
{
	private Date date;
	private DateAndTime startTime;
	private DateAndTime endTime;
	
	public Event(Date date, DateAndTime start, DateAndTime end)
	{
		this.date = date;
		startTime = start;
		endTime = end;
	}
}
