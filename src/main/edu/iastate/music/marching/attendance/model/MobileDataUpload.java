package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Type;

public class MobileDataUpload {
	
	private User uploader;
	
	private Date timestamp;
	
	// TODO Change to blob
	@Type(Text.class)
	private String data;

	public void setUploader(User uploader) {
		this.uploader = uploader;
	}

	public void setTimestamp(Date uploadTime) {
		this.timestamp = uploadTime;
	}

	public void setData(String uploadData) {
		this.data = uploadData;
	}

}
