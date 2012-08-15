package edu.iastate.music.marching.attendance.controllers;

import java.io.Reader;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.gson.Gson;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.AttendanceDatastore;
import edu.iastate.music.marching.attendance.model.DatastoreVersion;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.MobileDataUpload;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

public class DataController extends AbstractController {

	private static final int DUMP_FORMAT_VERSION = 1;

	private static final String BUGREPORT_EMAIL_TO = "mbattendance@iastate.edu";
	private static final String BUGREPORT_EMAIL_FROM = "mbattendance@gmail.com";

	private DataTrain dataTrain;

	public DataController(DataTrain dataTrain) {
		this.dataTrain = dataTrain;
	}

	public boolean sendBugReportEmail(User user, String severity, String url,
			String userAgent, boolean mobileSite, String message) {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String msgBody = "Severity: " + StringEscapeUtils.escapeHtml4(severity)
				+ "<br/>\n";

		if (user == null) {
			msgBody += "From: Anonymous";
		} else {
			msgBody += "From: " + user.getName() + " (" + user.getId() + ")";
		}
		msgBody += "<br/>\n";
		msgBody += "Url: " + StringEscapeUtils.escapeHtml4(url) + "<br/>\n";
		msgBody += "User Agent: " + userAgent + "<br/>\n";
		msgBody += "On mobile site: " + new Boolean(mobileSite).toString()
				+ "<br/>\n";
		msgBody += "<br/>\n";
		msgBody += "Message: \n" + StringEscapeUtils.escapeHtml4(message);

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(BUGREPORT_EMAIL_FROM));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					BUGREPORT_EMAIL_TO));

			msg.setSubject("Attendance Bug Report");
			msg.setContent(msgBody, "text/html");

			Transport.send(msg);

			return true;
		} catch (AddressException e) {
			throw new IllegalArgumentException(
					"Internal Error: Could not send Email", e);
		} catch (MessagingException e) {
			throw new IllegalArgumentException(
					"Internal Error: Could not send Email", e);
		}
	}

	public void dumpDatabaseAsJSON(Appendable out) {

		Dump dump = new Dump();

		dump.absences = dataTrain.getAbsenceController().getAll();

		dump.appData = dataTrain.getAppDataController().get();

		dump.events = dataTrain.getEventController().getAll();

		dump.forms = dataTrain.getFormsController().getAll();

		dump.messages = dataTrain.getMessagingController().getAll();

		dump.mobileData = dataTrain.getMobileDataController().getUploads();

		dump.users = dataTrain.getUsersController().getAll();

		getGson().toJson(dump, out);
	}

	public void importJSONDatabaseDump(Reader input) {
		Dump dump = getGson().fromJson(input, Dump.class);

		if (dump.format_version < DUMP_FORMAT_VERSION) {
			// Old dump, probably not compatible with current database format
			throw new IllegalStateException(
					"Tried to import out-of-date dump not compatible with current database structure");
		}
	}

	private class Dump {

		public int format_version;
		public List<Absence> absences;
		public AppData appData;
		public List<Event> events;
		public List<Form> forms;
		public List<MessageThread> messages;
		public List<MobileDataUpload> mobileData;
		public List<User> users;

		public Dump() {
			format_version = DUMP_FORMAT_VERSION;
		}
	}

	private Gson getGson() {
		return new Gson();
	}
	
	
	public void deleteEverthingInTheEntireDatabaseEvenThoughYouCannotUndoThis() {
		this.dataTrain.getDataStore().deleteAll(Absence.class);
		this.dataTrain.getDataStore().deleteAll(AppData.class);
		this.dataTrain.getDataStore().deleteAll(Event.class);
		this.dataTrain.getDataStore().deleteAll(Form.class);
		this.dataTrain.getDataStore().deleteAll(Event.class);
		this.dataTrain.getDataStore().deleteAll(Message.class);
		this.dataTrain.getDataStore().deleteAll(MessageThread.class);
		this.dataTrain.getDataStore().deleteAll(MobileDataUpload.class);
		this.dataTrain.getDataStore().deleteAll(User.class);
		
		this.dataTrain.getMemCache().clear();
	}
}
