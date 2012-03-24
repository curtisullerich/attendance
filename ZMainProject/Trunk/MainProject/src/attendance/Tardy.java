package attendance;

import time.Date;
import time.Time;

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
}
