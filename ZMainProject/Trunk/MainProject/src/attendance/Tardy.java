package attendance;

import time.Date;
import time.Time;
/**
 *
 * @author Yifei Zhu
 *
 */
public class Tardy {
	// This date corresponds to a particular event date
	private Date date;

	// These times correspond to a particular event time
	private Time startTime;
	private Time endTime;

	public Tardy(Date date, Time start, Time end) {
		this.date = date;
		startTime = start;
		endTime = end;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}
	
	
	
}
