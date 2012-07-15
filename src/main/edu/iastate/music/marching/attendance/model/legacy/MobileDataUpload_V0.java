package edu.iastate.music.marching.attendance.model.legacy;

import java.util.Date;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Type;
import com.google.code.twig.annotation.Version;

@Version(0)
@Entity(kind="edu.iastate.music.marching.attendance.model.MobileDataUpload", allocateIdsBy=0)
public class MobileDataUpload_V0 {
	
	MobileDataUpload_V0() {
		
	}
	
	private User_V0 uploader;
	
	private Date timestamp;
	
	// TODO Change to blob
	@Type(Text.class)
	private String data;

	public void setUploader(User_V0 uploader) {
		this.uploader = uploader;
	}

	public void setTimestamp(Date uploadTime) {
		this.timestamp = uploadTime;
	}

	public void setData(String uploadData) {
		this.data = uploadData;
	}

}
