package time;

public class Absence 
{	
	private Date date;
	private DateAndTime startTime;
	private DateAndTime endTime;
	private boolean isApproved = false;
	
	public Absence(Date date, DateAndTime start, DateAndTime end)
	{
		this.date = date;
		startTime = start;
		endTime = end;
	}
}
