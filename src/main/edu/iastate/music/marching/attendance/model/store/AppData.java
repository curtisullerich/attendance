package edu.iastate.music.marching.attendance.model.store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.appengine.api.datastore.Text;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Id;
import com.google.code.twig.annotation.Type;

@Entity(kind = "AppData", allocateIdsBy = 0)
public class AppData implements Serializable {
	
	private static final long serialVersionUID = 6536920800348300644L;

	/**
	 * Get app data through datatrain:
	 * DataTrain.get().getAppDataController.get()
	 */
	AppData() {
	}
	
	private String title;
	
	private Date performanceAbsenceFormCutoff;
	
	private Date classConflictFormCutoff;
	
	private Date timeWorkedFormCutoff;
	
	private String timeZoneID;
	
	@Type(Text.class)
	private String statusMessage;

	@Id
	private int datastoreVersion;

	private boolean isCronExportEnabled;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPerformanceAbsenceFormCutoff() {
		return this.performanceAbsenceFormCutoff;
	}

	public void setPerformanceAbsenceFormCutoff(Date formCutoff) {
		this.performanceAbsenceFormCutoff = formCutoff;
	}

	public TimeZone getTimeZone() {
		return TimeZone.getTimeZone(this.timeZoneID);
	}

	public void setTimeZone(TimeZone timezone) {
		this.timeZoneID = timezone.getID();
	}

	public int getDatastoreVersion() {
		return this.datastoreVersion;
	}

	public void setDatastoreVersion(int version) {
		this.datastoreVersion = version;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage() {
		return this.statusMessage;
	}

	public List<TimeZone> getTimezoneOptions() {
		List<TimeZone> opts = new ArrayList<TimeZone>();
		opts.add(TimeZone.getTimeZone("CST"));
		return opts;
	}

	public boolean isCronExportEnabled() {
		return this.isCronExportEnabled;
	}

	public void setCronExportEnabled(boolean isEnabled) {
		this.isCronExportEnabled = isEnabled;
	}

	public Date getClassConflictFormCutoff() {
		return classConflictFormCutoff;
	}

	public void setClassConflictFormCutoff(Date classConflictFormCutoff) {
		this.classConflictFormCutoff = classConflictFormCutoff;
	}

	public Date getTimeWorkedFormCutoff() {
		return timeWorkedFormCutoff;
	}

	public void setTimeWorkedFormCutoff(Date timeWorkedFormCutoff) {
		this.timeWorkedFormCutoff = timeWorkedFormCutoff;
	}
}
