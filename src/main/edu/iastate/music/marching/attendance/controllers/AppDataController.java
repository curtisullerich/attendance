package edu.iastate.music.marching.attendance.controllers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

public class AppDataController extends AbstractController {

	private DataTrain dataTrain;

	public AppDataController(DataTrain dataTrain) {
		this.dataTrain = dataTrain;
	}

	/**
	 * Always returns an instance of AppData, a default one if none is in the datastore
	 * @return
	 */
	public AppData get() {
		// TODO Caching
		AppData appData = dataTrain.getDataStore().find().type(AppData.class).returnUnique().now();
		
		if(appData == null)
		{
			// No AppData in the data store, so make a new default one
			appData = ModelFactory.newAppData();
			
			//
			// Defaults
			//
			appData.setDirectorRegistered(false);
			
			appData.setTitle("Band Attendance");
			
			appData.setTimeZone(TimeZone.getDefault());
			
			// Default form cutoff is the end of august
			Calendar calendar = Calendar.getInstance(appData.getTimeZone());
			calendar.set(Calendar.MONTH, Calendar.AUGUST);
			calendar.set(Calendar.DATE, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 16);
			calendar.set(Calendar.MINUTE, 35);
			calendar.set(Calendar.MILLISECOND, 0);
			
			calendar.roll(Calendar.MONTH, true);
			calendar.roll(Calendar.MILLISECOND, false);
			appData.setFormSubmissionCutoff(calendar.getTime());
			
			// Defaults to "password"
			appData.setHashedMobilePassword("5BAA61E4C9B93F3F0682250B6CF8331B7EE68FD8");
			
			appData = save(appData);
		}
		
		// We have some app data from the store
		return appData;
	}

	//TODO made this public. Is that okay?
	public AppData save(AppData appData) {
		dataTrain.getDataStore().storeOrUpdate(appData);
		return appData;
	}
	
	public boolean sendBugReportEmail(String severity,
			String message) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		String email = "mbattendance@iastate.edu";
		String msgBody = new StringBuilder()
				.append("Severity: ")
				.append("severity\n")
				.append("Message: \n")
				.append(message)
				.toString();

		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("mbattendance@iastate.edu"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(
					email));

			msg.setSubject("Attendance Bug Report");
			msg.setContent(msgBody, "text/html; charset=UTF-8");

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
