package attendance;

import time.Time;
/**
 * 
 * @author Yifei Zhu
 *
 */
public abstract class Event 
{
	private Time startTime;
	private Time endTime;
	
	public Event(Time start, Time end)
	{
		startTime = start;
		endTime = end;
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
