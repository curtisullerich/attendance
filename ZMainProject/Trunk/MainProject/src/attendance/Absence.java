package attendance;

import time.Time;

import time.Date;

public class Absence {
	private Date date;
	private Time startTime;
	private Time endTime;
	private boolean isApproved = false;

	public Absence(Date date, time.Time start, time.Time end) {
		this.date = date;
		startTime = start;
		endTime = end;
	}
}
