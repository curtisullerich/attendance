package edu.iastate.music.marching.attendance.tasks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.joda.time.DateTime;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.User;

public class Export {

	public static class ExportDataSource implements javax.activation.DataSource {

		private final String data;
		private final String name;

		public ExportDataSource(String name, StringWriter dataStream) {
			this.name = name;
			this.data = dataStream.toString();
		}

		@Override
		public String getContentType() {
			return "text/plain";
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(data.getBytes());
		}

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static final Logger LOG = Logger.getLogger(Export.class.getName());

	public static boolean performExport() {

		try {

			DataTrain train = DataTrain.depart();

			String appTitle = train.appData().get().getTitle();
			DateTime exportTime = new DateTime();
			String humanExportTime = DateFormat.getDateTimeInstance().format(
					new Date());
			String filenameExportTime = new SimpleDateFormat("yyMMddHHmmssZ")
					.format(exportTime.toDate());

			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);
			String subject = "Data export for " + appTitle;
			String msgBody = "Attached is a file containing all data for "
					+ appTitle + " exported at " + humanExportTime;
			String from = App.Emails.DATA_EXPORT_SENDER;
			String fileName = "data-export-for-" + appTitle + "-"
					+ filenameExportTime + ".json";
			StringWriter dataStream = new StringWriter();

			train.data().dumpDatabaseAsJSON(dataStream);

			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));

			// Add all directors as recipients
			for (User d : train.users().get(User.Type.Director)) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
						d.getPrimaryEmail().getEmail()));
			}
			msg.setSubject(subject);

			MimeBodyPart messageBodyPart = new MimeBodyPart();

			// fill message
			messageBodyPart.setText(msgBody);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			// Attach file data
			messageBodyPart = new MimeBodyPart();
			DataSource source = new ExportDataSource(fileName, dataStream);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(fileName);
			multipart.addBodyPart(messageBodyPart);

			// Put parts in message
			msg.setContent(multipart);

			Transport.send(msg);

		} catch (AddressException e) {
			LOG.log(Level.SEVERE,
					"Internal Error: Could not send export email", e);
			return false;
		} catch (MessagingException e) {
			LOG.log(Level.SEVERE,
					"Internal Error: Could not send export email", e);
			return false;
		}

		return true;
	}

}
