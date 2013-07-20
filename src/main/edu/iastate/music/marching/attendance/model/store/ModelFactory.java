package edu.iastate.music.marching.attendance.model.store;

import java.util.Date;

import com.google.appengine.api.datastore.Email;
import com.google.code.twig.standard.StandardObjectDatastore;


public class ModelFactory {

	public static Event newEvent(Event.Type type, Date start, Date end) {
		Event e = new Event();
		e.setType(type);
		e.setStart(start);
		e.setEnd(end);
		return e;
	}

	public static User newUser(User.Type type, Email email, String univID) {
		User u = new User();
		u.setType(type);
		u.setPrimaryEmail(email);
		u.setUniversityID(univID);
		return u;
	}

	public static Absence newAbsence(Absence.Type type, User student) {
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

	public static MobileDataUpload newMobileDataUpload(User uploader,
			Date uploadTime, String uploadData) {
		MobileDataUpload upload = new MobileDataUpload();
		upload.setUploader(uploader);
		upload.setTimestamp(uploadTime);
		upload.setData(uploadData);
		return upload;
	}

	public static StandardObjectDatastore newObjectDatastore() {
		return new AttendanceDatastore();
	}

	public static DatastoreVersion newDatastoreVersion(int version) {
		DatastoreVersion v = new DatastoreVersion();
		v.setVersion(version);
		return v;
	}

	public static Object newInstance(Class<?> toType)
			throws InstantiationException, IllegalAccessException {
		return toType.newInstance();
	}

	public static ImportData newImportData() {
		return new ImportData();
	}
}
