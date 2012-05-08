package edu.iastate.music.marching.attendance.util;

import java.util.regex.Pattern;

public class ValidationUtil {
	
	private static final Pattern PATTERN_NAME = Pattern.compile("[\\w\\. -]+");

	public static boolean isValidName(String name) {
		// Names are composed of characters (\w) and dashes
		return PATTERN_NAME.matcher(name).matches();
	}
	
	public static boolean validGoogleUser(
			com.google.appengine.api.users.User google_user) {
		
		if(google_user == null)
			return false;
		
		String[] email_parts = google_user.getEmail().split("@");
		
		if(email_parts.length == 2 && validEmailDomain(email_parts[1]))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean validEmailDomain(String domain) {
		// TODO Auto-generated method stub
		return "iastate.edu".equals(domain);
	}

}
