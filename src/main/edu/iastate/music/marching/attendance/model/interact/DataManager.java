package edu.iastate.music.marching.attendance.model.interact;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringEscapeUtils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.common.collect.Lists;

import edu.iastate.music.marching.attendance.Configuration;
import edu.iastate.music.marching.attendance.model.GsonWithPartials;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.AppData;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.ImportData;
import edu.iastate.music.marching.attendance.model.store.MobileDataUpload;
import edu.iastate.music.marching.attendance.model.store.User;

public class DataManager extends AbstractManager {

	public static class Dump {

		public int format_version = -1;

		public List<Absence> absences;
		public AppData appData;
		public List<Event> events;
		public List<Form> forms;
		public List<MobileDataUpload> mobileData;
		public List<User> users;
	}

	private static final int DUMP_FORMAT_VERSION = 2;

	private static final Logger LOG = Logger.getLogger(DataManager.class
			.getName());

	private DataTrain dataTrain;

	public DataManager(DataTrain dataTrain) {
		this.dataTrain = dataTrain;
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

	public void dumpDatabaseAsJSON(Appendable out) {

		Dump dump = new Dump();

		dump.format_version = DUMP_FORMAT_VERSION;

		dump.absences = dataTrain.getAbsenceManager().getAll();

		dump.appData = dataTrain.getAppDataManager().get();

		dump.events = dataTrain.getEventManager().getAll();

		dump.forms = dataTrain.getFormsManager().getAll();

		dump.mobileData = dataTrain.getMobileDataManager().getUploads();

		dump.users = dataTrain.getUsersManager().getAll();

		GsonWithPartials.toJson(dump, out);
	}

	public ImportData getImportData(long id) {

		return dataTrain.getDataStore().load(
				KeyFactory.createKey(dataTrain.getDataStore()
						.getConfiguration().typeToKind(ImportData.class), id));
	}

	private <T> void importAll(Class<T> clazz, List<T> instances) {
		if (instances == null)
			return;

		LOG.info("Importing " + instances.size() + " " + clazz.getSimpleName()
				+ " objects");

		try {
			dataTrain.getDataStore().storeAll(instances);
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "Encountered error during import", ex);
		}
	}

	public void importJSONDatabaseDump(String string) {

		Dump dump = GsonWithPartials.fromJson(new StringReader(string),
				Dump.class);

		if (dump.format_version < DUMP_FORMAT_VERSION) {
			// Old dump, probably not compatible with current database format
			throw new IllegalStateException(
					"Tried to import out-of-DateTime dump not compatible with current database structure");
		}

		// Yup
		deleteEverthingInTheEntireDatabaseEvenThoughYouCannotUndoThis();

		// Insert all things that don't link to other objects first

		importAll(AppData.class, Lists.newArrayList(dump.appData));
		importAll(User.class, dump.users);
		importAll(Event.class, dump.events);

		// Then things that depend on the previous
		// injecting the previous things first
		inject(Absence.class, dump.absences);
		inject(Form.class, dump.forms);
		inject(MobileDataUpload.class, dump.mobileData);

		// Final set of imports, with injected references
		importAll(Absence.class, dump.absences);
		importAll(Form.class, dump.forms);
		importAll(MobileDataUpload.class, dump.mobileData);
		// Done!
	}

	private <T> void inject(Class<?> clazz, List<T> src) {
		if (src == null)
			return;

		LOG.info("Injecting " + src.size() + " " + clazz.getSimpleName()
				+ " objects");

		Field[] fields = clazz.getDeclaredFields();

		for (T item : src) {
			for (Field field : fields) {
				try {
					field.setAccessible(true);

					Class<?> type = field.getType();

					if (Object[].class.equals(type)) {
						Object[] innerValues = (Object[]) field.get(item);

						if (innerValues != null && innerValues.length > 0) {
							List<?> innerList = Arrays.asList(innerValues);
							Class<?> innerType = innerList.get(0).getClass();
							inject(innerType, innerList);
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

	public void removeImportData(ImportData importData) {
		dataTrain.getDataStore().delete(importData);
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
			msg.setFrom(new InternetAddress(
					Configuration.Emails.BUGREPORT_EMAIL_FROM));
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

	public Key storeImportData(ImportData iData) {
		return dataTrain.getDataStore().store(iData);
	}
}
