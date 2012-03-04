package classes;

public class Absence 
{	
	private Date date;
	private Time startTime;
	private Time endTime;
	
	public Absence(Date date, Time start, Time end)
	{
		this.date = date;
		startTime = start;
		endTime = end;
	}
}
