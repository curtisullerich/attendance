package time;
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
//	private String dayOfWeek;
	
	public Date(int year, int month, int day)
	{
		this.year=year;
		this.day=day;
		this.month=month;
//		this.dayOfWeek="";
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}
	
	@Override
	public int compareTo(Date o) {
		if(o==null)
			return -1 ; //TODO;

		//if d==this
		if(this.year==o.year && this.month==o.month && this.day==o.day)
			return 0;
		
		//if d>this
		if(this.year-o.year>0)
		{
			return 1;
		}
		else if(this.month-o.month>0)
		{
			return 1;
		}
		else if(this.day-o.day>0)
		{
			return 1;
		}
		
		//if d < this
		return -1;
	}
	
	public String toString()
	{
		return year+"-"+month+"-"+day;
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
		return 0; //??
	}
	
	private String getMyDayOfWeek(int num)
	{
		switch(num)
		{
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
		case 7:
			return "Sunday";
		}
		return ""; //TODO
	}
	
	/**
	 * calculate the dayOfYear from 2000-2099
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

	
	
}
