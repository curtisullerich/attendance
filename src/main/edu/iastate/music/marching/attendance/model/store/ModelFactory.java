package edu.iastate.music.marching.attendance.model.store;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.google.appengine.api.datastore.Email;
import com.google.code.twig.standard.StandardObjectDatastore;

public class ModelFactory {

	public static Absence newAbsence(Absence.Type type, User student) {
		Absence a = new Absence();
		a.setType(type);
		a.setStudent(student);
		return a;
	}

	public static AppData newAppData() {
		return new AppData();
	}

	public static Event newEvent(Event.Type type, Interval interval) {
		Event e = new Event();
		e.setType(type);
		e.setInterval(interval);
		return e;
	}

	public static Form newForm(Form.Type type, User student) {
		Form form = new Form();
		form.setType(type);
		form.setStudent(student);
		form.setBuilding("");
		return form;
	}

	public static ImportData newImportData() {
		return new ImportData();
	}

	public static Object newInstance(Class<?> toType)
			throws InstantiationException, IllegalAccessException {
		return toType.newInstance();
	}

	public static MobileDataUpload newMobileDataUpload(User uploader,
			DateTime uploadTime, String uploadData) {
		MobileDataUpload upload = new MobileDataUpload();
		upload.setUploader(uploader);
		upload.setTimestamp(uploadTime);
		upload.setData(uploadData);
		return upload;
	}

	public static StandardObjectDatastore newObjectDatastore() {
		return new AttendanceDatastore();
	}

	public static User newUser(User.Type type, Email email, String univID) {
		User u = new User();
		u.setType(type);
		u.setPrimaryEmail(email);
		u.setUniversityID(univID);
		return u;
	}
}
