package edu.iastate.music.marching.attendance.controllers;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringEscapeUtils;

import edu.iastate.music.marching.attendance.model.User;

public class DataController extends AbstractController {

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
			msgBody += "From: " + user.getName() + " " + user.getNetID();
		}
		msgBody += "<br/>\n";
		msgBody += "Url: " + StringEscapeUtils.escapeHtml4(url) + "<br/>\n";
		msgBody += "User Agent: " + userAgent + "<br/>\n";
		msgBody += "On mobile site: " + new Boolean(mobileSite).toString() + "<br/>\n";
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
					"Internal Error: Could not send Email");
		} catch (MessagingException e) {
			throw new IllegalArgumentException(
					"Internal Error: Could not send Email");
		}
	}
}
