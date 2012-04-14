package time;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
//	private int second;
	private Date date;


	public Time() {
		//gets current 24hr time and date
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		this.hour = date.get(Calendar.HOUR_OF_DAY);
		this.minute = date.get(Calendar.MINUTE);
        this.date = new Date();
	}

	
	public Time(int hour, int minute, Date date) {
		//time
		setHour(hour);
		setMinute(minute);
//		setSecond(second);
		this.date=date;
	}
	//Used to convert from the string in the database to the actual object
	public Time(String dbDate, String dbTime)
	{
		//String in the form 
		this.date = new Date(dbDate);
		String[] time = dbTime.split(":");
		this.hour = Integer.parseInt(time[0]);
		this.minute = Integer.parseInt(time[1]);
//		this.second = Integer.parseInt(time[2]);
	}
	
	public Time(String dbTimeAndDate)
	{
		String[] timeAndDate = dbTimeAndDate.split(" ");
		this.date = new Date(timeAndDate[0]);
		String[] time = timeAndDate[1].split(":");
		this.hour = Integer.parseInt(time[0]);
		this.minute = Integer.parseInt(time[1]);		
	}
	

	public int getHour() {
		return hour;
	}

	//range: 1-24
	public void setHour(int hour) {
		if(hour<0 || hour> 23)
			throw new IllegalArgumentException("Hour must be >=0 && <=23");
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}
	//range: 0-59
	public void setMinute(int minute) {
		if(minute<0 || minute>59)
			throw new IllegalArgumentException("Min must be < 0 || >59");
		this.minute = minute;
	}

//	public int getSecond() {
//		return second;
//	}

//	public void setSecond(int second) {
//		if(second<0 || second>59)
//			throw new IllegalArgumentException("Sec must be < 0 || >59");
//		this.second = second;
//	}
	

	public Date getDate() {
		return date;
	}

	public void setDate(Date d) {
		this.date = d;
	}


	public String get24Format() {
		return addZero(this.getHour())+":"+addZero(this.minute)/*+":"+addZero(this.getSecond())*/;
	}

	public String get12Format() {
		if (this.hour > 12) {
			return  addZero(this.getHour()-12)+":"+addZero(this.minute)/*+":"+addZero(this.getSecond())*/
					+ "PM";
		} 
		else if(this.hour == 0){
			return  "12:"+addZero(this.minute)/*+":"+addZero(this.getSecond())*/
					+ "AM";
		}
		else {
			return this.get24Format() + "AM";
		}

	}

	private String addZero(int num)
	{
		if(num<10)
			return "0"+num;
		return ""+num;
	}


	public String toString(int format) {
		if (format == 12) {
			return date.toString() + " " + get12Format();
		}
		return date.toString() + " " + get24Format();
	}

	public int compareTo(Time dt)
	{

		if(dt==null)
			throw new NullPointerException("Time is null");
	
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
		else if(this.getMinute()!=dt.getMinute())
		{
			return this.getMinute()-dt.getMinute();
		}
//		else if(this.getSecond()!=dt.getSecond())
//		{
//			return this.getSecond()-dt.getSecond();
//		}
		else 
		{
			return 0;
		}
	}

}
