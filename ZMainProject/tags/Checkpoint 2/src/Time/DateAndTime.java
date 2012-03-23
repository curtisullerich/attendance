package Time;
/**
 * 
 * @author Yifei Zhu
 *
 *This class contain the time format
 *default--24
 *printing--12 
 */
public class DateAndTime 
{
	private int hour;
	private int minute;
	private int second;
	private int month;
	private int day;
	private int year;
	
	public DateAndTime(int hour, int minute, int second)
	{
		this.hour = hour;
		this.minute = minute;
		this.second=second;
	}
	
	public String pritn12Hours()
	{
		return hour+":"+minute+":"+second;
	}
	
	
	public String print24Hours()
	{
		return ;
		
	}
	
	//comparable
	
	

}
