package comment;
import people.Person;
import time.Time;
/**
 * 
 * @author Yifei Zhu
 *
 */
public class Message {
	private Person fromSomebody;
	private boolean isRead;
	private String content;
	private Time time;
	
	public Message(Person from, String content, boolean isRead, Time d)
	{
		this.fromSomebody=from;
		this.content=content;
		this.isRead=isRead;
		this.time=d;
	}
	
	public Person getFromSomebody() {
		return fromSomebody;
	}
	public void setFromSomebody(Person fromSomebody) {
		this.fromSomebody = fromSomebody;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Time getDate() {
		return time;
	}
	public void setDate(Time time) {
		this.time = time;
	}
	
	

}
