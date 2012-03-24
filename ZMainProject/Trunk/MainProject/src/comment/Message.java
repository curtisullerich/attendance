package comment;

import time.Date;

public class Message {
	private String fromSomebody;
	private String toSomebody;
	private boolean isRead;
	private String content;
	private Date date;
	//TODO 
	
	public Message(String from, String to, String content, boolean isRead, Date d)
	{
		this.fromSomebody=from;
		this.toSomebody=to;
		this.content=content;
		this.isRead=isRead;
		this.date=d;
		
	}
	
	public String getFromSomebody() {
		return fromSomebody;
	}
	public void setFromSomebody(String fromSomebody) {
		this.fromSomebody = fromSomebody;
	}
	public String getToSomebody() {
		return toSomebody;
	}
	public void setToSomebody(String toSomebody) {
		this.toSomebody = toSomebody;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	

}
