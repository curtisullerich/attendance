package edu.iastate.music.marching.attendance.controllers;

import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
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

		dump.absences = dataTrain.getAbsenceController().getAll();

		dump.appData = dataTrain.getAppDataController().get();

		dump.versions = dataTrain.getVersionController().getAll();

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
		public List<DatastoreVersion> versions;
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
		GsonBuilder gson = new GsonBuilder();
		OuterTypeAdapter adapter = new OuterTypeAdapter();
		gson.registerTypeAdapter(Absence.class, adapter);
		gson.registerTypeAdapter(AppData.class, adapter);
		gson.registerTypeAdapter(DatastoreVersion.class, adapter);
		gson.registerTypeAdapter(Event.class, adapter);
		gson.registerTypeAdapter(Form.class, adapter);
		gson.registerTypeAdapter(Message.class, adapter);
		gson.registerTypeAdapter(MessageThread.class, adapter);
		gson.registerTypeAdapter(MobileDataUpload.class, adapter);
		gson.registerTypeAdapter(User.class, adapter);
		return gson.create();
	}

	private class OuterTypeAdapter implements JsonSerializer<Object>,
			JsonDeserializer<Object> {

		@Override
		public Object deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return getInnerGson().fromJson(json, typeOfT);
		}

		@Override
		public JsonElement serialize(Object src, Type typeOfSrc,
				JsonSerializationContext context) {

			if (src == null) {
				return JsonNull.INSTANCE;
			}

			Class<?> clazz = (Class<?>) typeOfSrc;
			JsonObject object = new JsonObject();

			for (Field field : clazz.getDeclaredFields()) {
				try {
					field.setAccessible(true);

					if (!Modifier.isStatic(field.getModifiers())) {
						object.add(
								field.getName(),
								getInnerGson().toJsonTree(field.get(src),
										field.getType()));
					}
				} catch (IllegalArgumentException e) {
					// Skip field
				} catch (IllegalAccessException e) {
					// Skip field
				}
			}

			return object;
		}
	}

	private class InnerTypeAdapter implements JsonSerializer<Object>,
			JsonDeserializer<Object> {

		@Override
		public Object deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public JsonElement serialize(Object src, Type typeOfSrc,
				JsonSerializationContext context)
				throws IllegalArgumentException {

			if (src == null) {
				return JsonNull.INSTANCE;
			}

			Class<?> clazz = src.getClass();
			String id = null;

			for (Field field : clazz.getDeclaredFields()) {
				try {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Id.class)) {
						if (id != null) {
							throw new IllegalArgumentException(
									"Multiple Id fields on model object not allowed");
						}

						Object value = field.get(src);

						if (value == null) {
							throw new IllegalArgumentException(
									"Null Id field value not allowed");
						}

						id = value.toString();
					}
				} catch (IllegalArgumentException e) {
					// Skip field
				} catch (IllegalAccessException e) {
					// Skip field
				}
			}

			if (id == null) {
				// return getGson().toJsonTree(src, typeOfSrc);
//				return JsonNull.INSTANCE;
				 throw new IllegalArgumentException(
				 "No Id field found in model object of type " +
				 typeOfSrc.toString());
			}

			JsonObject object = new JsonObject();
			object.addProperty("id", id);
			return object;
		}
	}

	private Gson getInnerGson() {
		GsonBuilder gson = new GsonBuilder();
		InnerTypeAdapter adapter = new InnerTypeAdapter();
		gson.registerTypeAdapter(Absence.class, adapter);
		gson.registerTypeAdapter(AppData.class, adapter);
		gson.registerTypeAdapter(DatastoreVersion.class, adapter);
		gson.registerTypeAdapter(Event.class, adapter);
		gson.registerTypeAdapter(Form.class, adapter);
		gson.registerTypeAdapter(Message.class, adapter);
		gson.registerTypeAdapter(MessageThread.class, adapter);
		gson.registerTypeAdapter(MobileDataUpload.class, adapter);
		gson.registerTypeAdapter(User.class, adapter);
		return gson.create();
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
