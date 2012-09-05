package edu.iastate.music.marching.attendance.controllers;

import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
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

import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.AttendanceDatastore;
import edu.iastate.music.marching.attendance.model.DatastoreVersion;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.GsonWithPartials;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.MobileDataUpload;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

@interface FullyExport {
}

public class DataController extends AbstractController {

	private static final int DUMP_FORMAT_VERSION = 2;

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
		msgBody += "Message: \n"
				+ StringEscapeUtils.escapeHtml4(message).replace("\n",
						"\n<br/>");

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

		dump.format_version = DUMP_FORMAT_VERSION;

		dump.absences = dataTrain.getAbsenceController().getAll();

		dump.appData = dataTrain.getAppDataController().get();

		dump.versions = dataTrain.getVersionController().getAll();

		dump.events = dataTrain.getEventController().getAll();

		dump.forms = dataTrain.getFormsController().getAll();

		dump.messages = dataTrain.getMessagingController().getAll();

		dump.mobileData = dataTrain.getMobileDataController().getUploads();

		dump.users = dataTrain.getUsersController().getAll();

		GsonWithPartials.toJson(dump, out);
	}

	public void importJSONDatabaseDump(Reader input) {
		Dump dump = GsonWithPartials.fromJson(input, Dump.class);

		if (dump.format_version < DUMP_FORMAT_VERSION) {
			// Old dump, probably not compatible with current database format
			throw new IllegalStateException(
					"Tried to import out-of-date dump not compatible with current database structure");
		}

		// Insert all things that don't link to other objects first
		importAll(dump.versions);
		importAll(dump.appData);
		importAll(dump.users);
		importAll(dump.events);
		importAll(dump.mobileData);

		// Then things that depend on the previous
		// injecting the previous things first
		importAll(dump.messages);
		importAll(dump.absences);
		importAll(dump.messages);
		importAll(dump.forms);
		
		// Re-import a couple things that probably got messed up by the references
		importAll(dump.users);
		importAll(dump.events);
		inject(dump.messages);
		inject(dump.absences);
		inject(dump.forms);
		importAll(dump.messages);
		importAll(dump.absences);
		importAll(dump.forms);

		// Done!
	}

	private <T> void inject(List<T> src) {
		for (T item : src) {
			Class<?> clazz = item.getClass();

			for (Field field : clazz.getDeclaredFields()) {
				try {
					field.setAccessible(true);

					Class<?> type = field.getType();

					if (Object[].class.equals(type)) {
						Object[] innerValues = (Object[]) field.get(item);
						if (innerValues != null) {
							List<?> innerList = Arrays.asList(innerValues);
							inject(innerList);
							field.set(item, innerList.toArray());
						}
					} else if (User.class.equals(type)) {
						// Try to load user
						User user = (User) field.get(item);
						if (user != null) {
							user = dataTrain.getUsersController().get(
									(user).getId());
							field.set(item, user);
						}
					} else if (Absence.class.equals(type)) {
						// Try to load Absence
						Absence absence = (Absence) field.get(item);
						if (absence != null) {
							absence = dataTrain.getAbsenceController().get(
									absence.getId());
							field.set(item, absence);
						}
					} else if (Event.class.equals(type)) {
						// Try to load Event
						Event event = (Event) field.get(item);
						if (event != null) {
							event = dataTrain.getEventController().get(
									(event).getId());
							field.set(item, event);
						}
					} else if (MessageThread.class.equals(type)) {
						// Try to load MessageThread
						MessageThread thread = (MessageThread) field.get(item);
						if (thread != null) {
							thread = dataTrain.getMessagingController().get(
									(thread).getId());
							field.set(item, thread);
						}
					}

				} catch (IllegalArgumentException e) {
					// Skip field
				} catch (IllegalAccessException e) {
					// Skip field
				}
			}
		}
	}

	private <T> void importAll(T... src) {
		for(T item : src)
		{
			dataTrain.getDataStore().storeOrUpdate(item);
		}
	}

	private <T> void importAll(List<T> src) {
		for(T item : src)
		{
			dataTrain.getDataStore().storeOrUpdate(item);
		}
	}

	public static class Dump {

		public int format_version = -1;

		public List<Absence> absences;
		public AppData appData;
		public List<DatastoreVersion> versions;
		public List<Event> events;
		public List<Form> forms;
		public List<MessageThread> messages;
		public List<MobileDataUpload> mobileData;
		public List<User> users;
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
