package edu.iastate.music.marching.attendance.model;

import java.util.Calendar;
import java.util.Date;

import com.google.code.twig.annotation.AnnotationObjectDatastore;

public class ModelFactory {

	public static Event newEvent(Event.Type type, Date start, Date end) {
		Event e = new Event();
		e.setType(type);
		e.setStart(start);
		e.setEnd(end);
		return e;
	}

	public static User newUser(User.Type type, com.google.appengine.api.users.User google_user, String netID, int univID) {
		User u = new User();
		u.setType(type);
		u.setGoogleUser(google_user);
		u.setNetID(netID);
		u.setUniversityID(univID);
		return u;
	}

	public static Absence newAbsence(
			Absence.Type type, User student) {
		Absence a = new Absence();
		a.setType(type);
		a.setStudent(student);
		return a;
	}

	public static AppData newAppData() {
		return new AppData();
	}
	
	public static Form newForm(Form.Type type, User student) {
		Form form = new Form();
		form.setType(type);
		form.setStudent(student);
		form.setBuilding("");
		return form;
	}

	public static MobileDataUpload newMobileDataUpload(User uploader, Date uploadTime, String uploadData) {
		MobileDataUpload upload = new MobileDataUpload();
		upload.setUploader(uploader);
		upload.setTimestamp(uploadTime);
		upload.setData(uploadData);
		return upload;
	}

	public static MessageThread newMessageThread() {
		MessageThread m = new MessageThread();
		
		// Defaults
		// TODO: Move defaults to controller
		m.setResolved(false);
		
		return m;
	}
	
	public static AnnotationObjectDatastore newObjectDatastore() {
		return new ObjectDatastore();
	}

	public static Message newMessage(User sender, String message) {
		Message m = new Message();
		m.setAuthor(sender);
		m.setText(message);
		m.setTimestamp(Calendar.getInstance().getTime());
		return m;
	}
}
