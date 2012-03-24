package time;

/**
 * 
 * @author Yifei Zhu
 * 
 *         This class can be able to -get date, hour, min, second -get or print
 *         time and date in 12 format and 24 format -compare to "date and time"
 *         The default time format is in 24 hours
 * 
 *         default--24 printing--12
 */
public class Time implements Comparable<Time> {
	private int hour;
	private int minute;
	private int second;
	private Date date;
	private String dayOfWeek;

	public Time(int hour, int minute, int second, Date date, String dayOfWeek) {
		// //time
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.date=date;
		this.dayOfWeek=dayOfWeek;
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
	
	

	public Date getDate() {
		return date;
	}

	public void setDate(Date d) {
		this.date = d;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String get24Format() {
		return hour + ":" + minute + ":" + second;
	}

	public String get12Format() {
		if (this.hour > 12) {
			return (this.hour - 12) + ":" + this.minute + ":" + this.second
					+ "PM";
		} else {
			return this.get24Format() + "AM";
		}

	}



	public String toString(int format) {
		if (format == 12) {
			return this.toString() + " " + get12Format();
		}
		return this.toString() + " " + get24Format();
	}

	public int compareTo(Time dt)
	{

		if(dt==null)
			return -1 ; //TODO return -1?
	
		if(date.compareTo(dt.getDate())!=0)
		{
			return date.compareTo(dt.getDate());
		}
		//if date are the same
		
		//hour is different 
		if(this.getHour()!=dt.getHour())
		{
			return this.getHour()-dt.getHour();
		}
		//if also have the 
		else if(this.getMinute()!=dt.getHour())
		{
			return this.getMinute()-dt.getHour();
		}
		else if(this.getSecond()!=dt.getSecond())
		{
			return this.getSecond()-dt.getSecond();
		}
		else 
		{
			return 0;
		}
	}

}
