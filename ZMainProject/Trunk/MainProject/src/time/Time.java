package time;
/**
 * 
 * @author Yifei Zhu
 *
 *This class contain the time format
 *default--24
 *printing--12 
 */
public class Time extends Date
{
	private int hour;
	private int minute;
	private int second;
	
	
	public Time(int year, int month, int day, int hour, int minute, int second) {
		super(year, month, day);
		//time
		this.hour = hour;
		this.minute = minute;
		this.second=second;
	}


	public int getHour() {
		return hour;
	}


	public void setHour(int hour) {
		this.hour = hour;
	}


	public int getMinute() {
		return minute;
	}


	public void setMinute(int minute) {
		this.minute = minute;
	}


	public int getSecond() {
		return second;
	}


	public void setSecond(int second) {
		this.second = second;
	}

	public String get24Format()
	{
		return hour+":"+minute+":"+second;
	}
	public String get12Format()
	{
		if(this.hour>12)
		{
			return (this.hour-12)+":"+this.minute+":"+this.second+"PM";
		}
		else
		{
			return this.get24Format()+"AM";
		}
		
	}
	
	
	
	public String toString(int format) {
		if(format==12)
		{
			return this.toString()+" "+get12Format();
		}
		return this.toString()+" "+get24Format();
	}
	
	
	

	
	

}
