package edu.iastate.music.marching.attendance.model;

import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Version;

@Version(AttendanceDatastore.VERSION)
@Entity(kind="MobileDataUpload", allocateIdsBy=0)
public class MobileDataUpload {

	public MobileDataUpload() {
	}

	private User uploader;

	private Date timestamp;

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
