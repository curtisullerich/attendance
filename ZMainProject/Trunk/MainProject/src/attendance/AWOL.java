package attendance;
import time.Time;

/**
 * 
 * @author Yifei Zhu
 *
 */
public abstract class AWOL 
{
	private Time startTime;
	private Time endtTime;
	private boolean isApproved;
	
	public AWOL (Time s, Time e)
	{
		this.startTime=s;
		this.endtTime=e;
		this.isApproved=false;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndtTime() {
		return endtTime;
	}

	public void setEndtTime(Time endtTime) {
		this.endtTime = endtTime;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}
	
}
