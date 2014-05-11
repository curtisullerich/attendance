package edu.iastate.music.marching.attendance.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.User;

public class ValidationUtil {

	private static final Pattern PATTERN_NAME = Pattern
			.compile("^[\\w\\. -]+$");

	private static final Pattern PATTERN_EMAIL = Pattern
			.compile("^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})$");

	private static final Pattern PATTERN_GTEMPEMAIL = Pattern
			.compile("^([a-zA-Z0-9._%+-]+)%([a-zA-Z0-9.-]+)@gtempaccount.com$");

	private static final int MAX_TEXT_LENGTH = 50000;

	public static boolean isPost(HttpServletRequest req) {
		return "POST".equals(req.getMethod());
	}

	public static boolean isUniqueId(String id, Email primary) {
		return DataTrain.depart().users().isUniqueId(id, primary);
	}

	public static boolean isUniqueSecondaryEmail(Email checkEmail,
			Email excludedUserPrimaryEmail, DataTrain train) {
		return train.users().isUniqueSecondaryEmail(checkEmail,
				excludedUserPrimaryEmail);
	}

	public static boolean isValidMajor(String major) {
		return isValidText(major, false);
	}

	// private static boolean validEmailDomain(String domain, DataTrain train) {

	// AppData app = train.getAppDataController().get();

	// }

	public static boolean isValidName(String name) {
		// Names are composed of characters (\w) and dashes
		return PATTERN_NAME.matcher(name).matches();
	}

	public static boolean isValidRank(String rank) {
		return true;
	}

	public static boolean isValidSection(User.Section section) {
		boolean ret = false;
		for (User.Section sect : User.Section.values()) {
			if (section == sect) {
				ret = true;
			}
		}
		return ret;
	}

	public static boolean isValidText(String text, boolean canBeEmpty) {

		if (text == null) {
			return false;
		}

		if ("".equals(text.trim())) {
			return canBeEmpty;
		}

		if (text.length() > MAX_TEXT_LENGTH) {
			return false;
		}

		return true;
	}

	public static boolean isValidUniversityID(String uId) {
		if (uId.length() != 9) {
			return false;
		} else {
			try {
				Integer.parseInt(uId);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
	}

	public static boolean isValidYear(int year) {
		boolean ret = false;
		for (int i = 1; i <= 10; ++i) {
			if (year == i) {
				ret = true;
			}
		}
		return ret;
	}

	public static boolean isValidPrimaryEmail(Email email, DataTrain train) {

		// TODO https://github.com/curtisullerich/attendance/issues/122
		// This should be an application setting
		String domain = App.DOMAIN;

		if (null == email)
			return true;

		if ("".equals(email.getEmail()))
			return false;

		if (null == email.getEmail())
			return false;

		Matcher emailMatcher = PATTERN_EMAIL.matcher(email.getEmail());

		// Check for valid email
		if (!emailMatcher.matches()) {
			return false;
		}

		// Allow address@domain email's
		if (domain.equals(emailMatcher.group(2))) {
			return true;
		}

		// Allow address%40domain@gtempaccount.com email's
		Matcher gTempMatcher = PATTERN_GTEMPEMAIL.matcher(email.getEmail());
		if (gTempMatcher.matches() && domain.equals(gTempMatcher.group(2))) {
			return true;
		}

		return false;
	}

	public static boolean isValidSecondaryEmail(Email email, DataTrain train) {

		// Null or empty emails are okay for the secondary
		if (email == null)
			return true;

		if (email.getEmail() == null || email.getEmail().equals(""))
			return false;

		return PATTERN_EMAIL.matcher(email.getEmail()).matches();
	}

}
