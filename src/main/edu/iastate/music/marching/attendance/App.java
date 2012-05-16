package edu.iastate.music.marching.attendance;

import java.util.List;
import java.util.TimeZone;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.User;

public class App {

	private static final String TITLE = "ISUCF'V'MB Attendance";

	public static String getTitle() {
		return TITLE;
	}

	public static boolean isDirectorRegistered() {
		// TODO Caching
		List<User> directors = DataTrain.getAndStartTrain()
				.getUsersController().get(User.Type.Director);
		return directors != null && directors.size() > 0;
	}

	public static String getHashedMobilePassword() {
		return getAppData().getHashedMobilePassword();
	}

	public static TimeZone getTimeZone() {
		// TODO Auto-generated method stub
		return TimeZone.getDefault();
	}

	public static List<String> getTimeWorkedEmails() {
		return getAppData().getTimeWorkedEmails();
	}

	private static AppData getAppData() {
		// TODO Caching
		return DataTrain.getAndStartTrain()
				.getAppDataController().get();
	}

}
