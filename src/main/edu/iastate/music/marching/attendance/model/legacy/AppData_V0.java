package edu.iastate.music.marching.attendance.model.legacy;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Version;

@Version(0)
@Entity(kind="edu.iastate.music.marching.attendance.model.AppData", allocateIdsBy=0)
public class AppData_V0 {
	
	/**
	 * Get app data through datatrain:
	 * DataTrain.get().getAppDataController.get()
	 */
	AppData_V0() {
	}

	private boolean directorRegistered;

	private String title;

	private Date formCutoff;

	private List<String> timeWorkedEmails;

	private String hashedMobilePassword;

	private String timeZoneID;

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

	public Date getFormSubmissionCutoff() {
		return this.formCutoff;
	}

	public void setFormSubmissionCutoff(Date formCutoff) {
		this.formCutoff = formCutoff;
	}

	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(this.timeZoneID);
	}
	
	public void setTimeZone(TimeZone timezone) {
		this.timeZoneID = timezone.getID();
	}
}