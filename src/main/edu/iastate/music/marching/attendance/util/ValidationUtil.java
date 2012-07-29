package edu.iastate.music.marching.attendance.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.User;

public class ValidationUtil {

	private static final Pattern PATTERN_NAME = Pattern
			.compile("^[\\w\\. -]+$");

	private static final Pattern PATTERN_EMAIL = Pattern
			.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$");

	private static final int MAX_STRING_LENGTH = 500;

	private static final int MAX_TEXT_LENGTH = 50000;

	public static boolean isValidName(String name) {
		// Names are composed of characters (\w) and dashes
		return PATTERN_NAME.matcher(name).matches();
	}

	public static boolean validPrimaryEmail(Email email, DataTrain train) {

		if (email == null | email.getEmail() == null)
			return false;

		if (!PATTERN_EMAIL.matcher(email.getEmail()).matches())
			return false;

		String[] email_parts = email.getEmail().split("@");

		if (email_parts.length == 2 && validEmailDomain(email_parts[1], train)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validSecondaryEmail(Email email, DataTrain train) {

		// Null or empty emails are okay for the secondary
		if (email == null || email.getEmail() == null
				|| email.getEmail().equals(""))
			return true;

		return PATTERN_EMAIL.matcher(email.getEmail()).matches();
	}

	@Deprecated
	public static boolean validGoogleUser(
			com.google.appengine.api.users.User google_user, DataTrain train) {

		if (google_user == null)
			return false;

		String[] email_parts = google_user.getEmail().split("@");

		if (email_parts.length == 2 && validEmailDomain(email_parts[1], train)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean validEmailDomain(String domain, DataTrain train) {
		// TODO https://github.com/curtisullerich/attendance/issues/122
		//This should be an application setting
		AppData app = train.getAppDataController().get();
		return "iastate.edu".equals(domain);
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

	public static boolean isPost(HttpServletRequest req) {
		return "POST".equals(req.getMethod());
	}

	public static boolean isValidFormDEmail(String email, AppData appData) {
		return appData.getTimeWorkedEmails().contains(email);
	}

	/**
	 * Validation check for form C submission date
	 * 
	 * @param date
	 * @return
	 */
	public static boolean dateIsAtLeastThreeWeekdaysFresh(Date date,
			TimeZone timezone) {
		int weekdays = 3;

		Date to = date;
		Date now = Calendar.getInstance(timezone).getTime();
		return dateIsWeekdaysFrom(now, to, weekdays, timezone);
	}

	/**
	 * Compares dates to see if the second date is no more than the given number
	 * of weekdays after the first
	 * 
	 * @param from
	 *            Start date
	 * @param to
	 *            To date
	 * @param days
	 *            maximum difference in days
	 * @return True if from.date - to.date <= days and from < to
	 */
	public static boolean dateIsWeekdaysFrom(Date from, Date to, int days,
			TimeZone timezone) {
		Calendar today = Calendar.getInstance(timezone);
		today.setTime(from);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			today.roll(Calendar.DATE, 2);
		} else if (today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			today.roll(Calendar.DATE, 1);
		}
		// now if it was a weekend, it's Monday for the comparison

		Calendar other = Calendar.getInstance(timezone);
		other.setTime(to);
		other.set(Calendar.HOUR_OF_DAY, 0);
		other.set(Calendar.MINUTE, 0);
		other.set(Calendar.SECOND, 0);
		other.set(Calendar.MILLISECOND, 0);

		other.roll(Calendar.DATE, days);

		if (other.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			other.roll(Calendar.DATE, 2);
		} else if (other.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			other.roll(Calendar.DATE, 1);
		}
		// now if it was a weekend, it's Monday for the comparison

		// now we should just be comparing weekend-agnostic DAYS.
		return other.compareTo(today) >= 0;
	}

	public static boolean isValidUniversityID(String uId) {
		if (uId.length() != 9) {
			return false;
		}
		else {
			try {
				Integer.parseInt(uId);
				return true;
			}
			catch (NumberFormatException e) {
				return false;
			}
		}
	}

	public static boolean isValidMajor(String major) {
		return isValidText(major, false);
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

	public static boolean isValidYear(int year) {
		boolean ret = false;
		for (int i = 1; i <= 10; ++i) {
			if (year == i) {
				ret = true;
			}
		}
		return ret;
	}

}
