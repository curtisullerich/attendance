package Time;
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
	
	public Date(int year, int month, int day)
	{
		this.year=year;
		this.day=day;
		this.month=month;
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


	
	
}
