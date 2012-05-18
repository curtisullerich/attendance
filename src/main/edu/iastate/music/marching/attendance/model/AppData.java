package edu.iastate.music.marching.attendance.model;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.iastate.music.marching.attendance.App;

public class AppData {
	/**
	 * Get app data through datatrain:
	 * DataTrain.get().getAppDataController.get()
	 */
	AppData() {
	}

	private boolean directorRegistered;

	private String title;

	private Date formCutoff;

	private List<String> timeWorkedEmails;

	private String hashedMobilePassword;

	public boolean isDirectorRegistered() {
		return this.directorRegistered;
	}

	public void setDirectorRegistered(boolean directorRegistered) {
		this.directorRegistered = directorRegistered;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getTimeWorkedEmails() {
		return this.timeWorkedEmails;
	}

	public void setTimeWorkedEmails(List<String> timeWorkedEmails) {
		this.timeWorkedEmails = timeWorkedEmails;
	}

	public String getHashedMobilePassword() {
		return hashedMobilePassword;
	}

	public void setHashedMobilePassword(String hashedMobilePassword) {
		this.hashedMobilePassword = hashedMobilePassword;
	}

	public Calendar getFormSubmissionCutoff() {
		Calendar calendar = Calendar.getInstance(App.getTimeZone());
		calendar.setTime(this.formCutoff);
		return calendar;
	}

	public void setFormSubmissionCutoff(Date formCutoff) {
		this.formCutoff = formCutoff;
	}
}
