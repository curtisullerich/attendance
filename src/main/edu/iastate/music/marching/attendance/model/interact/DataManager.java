package edu.iastate.music.marching.attendance.model.interact;

import java.io.Reader;
import java.lang.reflect.Field;
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

import edu.iastate.music.marching.attendance.Configuration;
import edu.iastate.music.marching.attendance.model.GsonWithPartials;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.AppData;
import edu.iastate.music.marching.attendance.model.store.DatastoreVersion;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.MobileDataUpload;
import edu.iastate.music.marching.attendance.model.store.User;

@interface FullyExport {
}

public class DataManager extends AbstractManager {

	private static final int DUMP_FORMAT_VERSION = 2;

	private DataTrain dataTrain;

	public DataManager(DataTrain dataTrain) {
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
			msg.setFrom(new InternetAddress(Configuration.Emails.BUGREPORT_EMAIL_FROM));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					Configuration.Emails.BUGREPORT_EMAIL_TO));

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

		dump.absences = dataTrain.getAbsenceManager().getAll();

		dump.appData = dataTrain.getAppDataManager().get();

		dump.versions = dataTrain.getVersionManager().getAll();

		dump.events = dataTrain.getEventManager().getAll();

		dump.forms = dataTrain.getFormsManager().getAll();

		dump.mobileData = dataTrain.getMobileDataManager().getUploads();

		dump.users = dataTrain.getUsersManager().getAll();

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
		importAll(dump.absences);
		importAll(dump.forms);
		
		// Re-import a couple things that probably got messed up by the references
		importAll(dump.users);
		importAll(dump.events);
		inject(dump.absences);
		inject(dump.forms);
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
							user = dataTrain.getUsersManager().get(
									(user).getId());
							field.set(item, user);
						}
					} else if (Absence.class.equals(type)) {
						// Try to load Absence
						Absence absence = (Absence) field.get(item);
						if (absence != null) {
							absence = dataTrain.getAbsenceManager().get(
									absence.getId());
							field.set(item, absence);
						}
					} else if (Event.class.equals(type)) {
						// Try to load Event
						Event event = (Event) field.get(item);
						if (event != null) {
							event = dataTrain.getEventManager().get(
									(event).getId());
							field.set(item, event);
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
		public List<MobileDataUpload> mobileData;
		public List<User> users;
	}

	public void deleteEverthingInTheEntireDatabaseEvenThoughYouCannotUndoThis() {
		this.dataTrain.getDataStore().deleteAll(Absence.class);
		this.dataTrain.getDataStore().deleteAll(AppData.class);
		this.dataTrain.getDataStore().deleteAll(Event.class);
		this.dataTrain.getDataStore().deleteAll(Form.class);
		this.dataTrain.getDataStore().deleteAll(Event.class);
		this.dataTrain.getDataStore().deleteAll(MobileDataUpload.class);
		this.dataTrain.getDataStore().deleteAll(User.class);

		this.dataTrain.getMemCache().clear();
	}
}
