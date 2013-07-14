package edu.iastate.music.marching.attendance.model.store;

import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Version;


@Version(AttendanceDatastore.VERSION)
@Entity(kind = "MobileDataUpload", allocateIdsBy = 0)
public class MobileDataUpload {

	public static final String FIELD_UPLOADER = "uploader";

	public MobileDataUpload() {
	}

	private User uploader;

	private Date timestamp;

	@Type(Text.class)
	private String data;
	
	@Type(Text.class)
	private String error;

	public void setUploader(User uploader) {
		this.uploader = uploader;
	}
	
	public User getUploader() {
		return this.uploader;
	}

	public void setTimestamp(Date uploadTime) {
		this.timestamp = uploadTime;
	}
	
	public Date getTimestamp() {
		return this.timestamp;
	}

	public void setData(String uploadData) {
		this.data = uploadData;
	}

	public String getData() {
		return this.data;
	}

	public void setErrorMessage(String message) {
		this.error = message;
	}
	
	public String getErrorMessage() {
		return this.error;
	}
}
