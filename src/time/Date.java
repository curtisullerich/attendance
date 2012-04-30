package time;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * 
 * @author Yifei Zhu
 *
 */
public class Date implements Comparable<Date>
{
	private int year;
	private int month;
	private int day;
	
	public Date(){
		//default construct creates current date   
		Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
		this.month = date.get(Calendar.MONTH)+1;
		this.year = date.get(Calendar.YEAR);
		this.day = date.get(Calendar.DATE); 
	}
	
	public Date(int year, int month, int day)
	{
		setYear(year);
		setMonth(month);
		setDay(day);
	}
	
	//Constructor to change from dataBase string to an Object
	public Date(String dbDate)
	{
		String[] date = dbDate.split("-");
		year = Integer.parseInt(date[0]);
		month = Integer.parseInt(date[1]);
		day = Integer.parseInt(date[2]);
	}
	

	public int getYear() {
		return year;
	}

	public void setYear(int year)
	{
		if(year<2000 || year>2099)
		{
			throw new IllegalArgumentException("Year must be > 2000 and <2099");
		}
		this.year=year;
	}
	
	//Years that are divisible by four are leap years, 
	//with the exception of years that are divisible by 100, 
	//which are not leap years, and with the final exception of years divisible by 400, which are.
	public boolean isLeapYear() {
		//determine leap year
		if((year%4==0 && year%100 !=0)|| year%400==0)
			return true;
		return false;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		if(month<=0 || month>12)
		{
			throw new IllegalArgumentException("Invalid Input: Month");
		}
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		if(day<0||day>31)
			throw new IllegalArgumentException("Day cant <0 or >31");
		if(getMonth()==2)
		{
			if(isLeapYear() && day<=29)
			{
				this.day=day;
			}
			else if(!isLeapYear()&&day<=28)
			{
				this.day=day;
			}
			else
			{
				throw new IllegalArgumentException("Feb. should be < 28 || < 29");
			}
		}
		else if((getMonth()==4|| getMonth()==6|| getMonth()==9|| getMonth()==11)&&(day==31))
		{
			throw new IllegalArgumentException(getMonth()+"cant be == 31");
		}
		else
		{
			this.day = day;
		}
	}
	
	@Override
	public int compareTo(Date o) {
		if(o==null)
		{
			throw new NullPointerException("date is null");
		}
		//if d==this
		if(this.year==o.year && this.month==o.month)
			return this.day-o.day;
		
		else if(this.year==o.year )
				return this.month-o.month;
		
		return this.year-o.year;
	}
	
	public String toString()
	{
		return year+"-"+addZero(month)+"-"+addZero(day);
	}
	
	/**
	 * Add a zero in front of num that <10
	 * @param num --the number
	 * @return --return modified num
	 */
	private String addZero(int num)
	{
		if(num<10)
			return "0"+num;
		return ""+num;
	}
	
	
	/**
	 * this is a helper method to get special integer for the month
	 * more detail see http://www.terra.es/personal2/grimmer/#Monthcodes
	 * @return
	 */
	private int getMonthIn()
	{
		switch(this.month)
		{
		case 1:
			return 6;
		case 2:
			return 2;
		case 3:
			return 2;
		case 4:
			return 5;
		case 5:
			return 0;
		case 6:
			return 3;
		case 7:
			return 5;
		case 8:
			return 1;
		case 9:
			return 4;
		case 10:
			return 6;
		case 11:
			return 2;
		case 12:
			return 4;
		}
		throw new IllegalArgumentException("Invalid Month");
	}
	
	private String getMyDayOfWeek(int num)
	{
		switch(num)
		{
		case 0:
			return "Sunday";
		case 1:
			return "Monday";
		case 2:
			return "Tuesday";
		case 3:
			return "Wednesday";
		case 4:
			return "Thursday";
		case 5:
			return "Friday";
		case 6:
			return "Saturday";
		
		}
		return ""; //TODO
	}
	
	/**
	 * calculate the dayOfYear from 2000-2099
	 * http://www.terra.es/personal2/grimmer/
	 * 
	 * Example date July 13th, 2004
	1.Take the last 2 digits of the year and add a quarter onto itself. (04 + 1 = 5)
	2.Get the corresponding code for the month. (January = 6, February = 2, March = 2, etc. See month codes for details). July = 5
	Take the day. (=13)
	Add the numbers together (5 + 5 + 13 = 23)
	Take away 7 (or multiples of 7) until a number from 1-7 is left. (23 - 21 =2)
	This number corresponds to the day of the week. (1 = Monday, 2 = Tuesday, etc.) In this case 2 = Tuesday
	 * @return
	 */
	public String getDayOfWeek()
	{
		//Take the last 2 digits of the year and add a quarter onto itself
		int lastTwoNum = (int)((this.year%200)*(1+0.25));
		int monthCode = getMonthIn();
		int addAll = lastTwoNum+monthCode+this.day;
		
		//take away 7
		int result=addAll%7;
		return getMyDayOfWeek(result);
	}
	
	public boolean equals(Object o)
	{
		if (o == null || o.getClass() != this.getClass()) return false;
		Date d = (Date) o;
		return this.compareTo(d) == 0;
	}

	
	
}
