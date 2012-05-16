package edu.iastate.music.marching.attendance.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AppData {
	/**
	 * Get app data through datatrain: DataTrain.get().getAppDataController.get()
	 */
	AppData() {
		// Defaults
		title = "Band Attendance";
		
		// TODO Set hashed mobile password to some default
		
		
		// Default form cutoff is the end of august
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, Calendar.AUGUST);
		calendar.set(Calendar.DATE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 16);
		calendar.set(Calendar.MINUTE, 35);
		calendar.set(Calendar.MILLISECOND, 0);
		
		calendar.roll(Calendar.MONTH, true);
		calendar.roll(Calendar.MILLISECOND, false);
		formCutoff = calendar.getTime();
		
	}
	
	private boolean directorRegistered;

	private String title;
	
	private Date formCutoff;
	
	private List<String> timeWorkedEmails;
	
	private String hashedMobilePassword;

	public void setTimeWorkedEmails(List<String> timeWorkedEmails) {
		this.timeWorkedEmails = timeWorkedEmails;
	}
	
	public List<String> getTimeWorkedEmails() {
		return this.timeWorkedEmails;
	}

	public String getHashedMobilePassword() {
		return hashedMobilePassword;
	}
	
	public Date getFormSubmissionCutoff()
	{
		return formCutoff;
	}
}
