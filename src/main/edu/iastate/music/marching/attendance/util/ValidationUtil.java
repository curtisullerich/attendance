package edu.iastate.music.marching.attendance.util;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.Email;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.AppData;
import edu.iastate.music.marching.attendance.model.store.User;

public class ValidationUtil {

	private static final Pattern PATTERN_NAME = Pattern
			.compile("^[\\w\\. -]+$");

	private static final Pattern PATTERN_EMAIL = Pattern
			.compile("^([a-zA-Z0-9._%+-]+)@([a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4})$");

	private static final Pattern PATTERN_GTEMPEMAIL = Pattern
			.compile("^([a-zA-Z0-9._%+-]+)%40([a-zA-Z0-9.-]+)@gtempaccount.com$");

	private static final int MAX_STRING_LENGTH = 500;

	private static final int MAX_TEXT_LENGTH = 50000;

	public static boolean isValidName(String name) {
		// Names are composed of characters (\w) and dashes
		return PATTERN_NAME.matcher(name).matches();
	}

	public static boolean validPrimaryEmail(Email email, DataTrain train) {
		
		// TODO https://github.com/curtisullerich/attendance/issues/122
		// This should be an application setting
		String domain = "iastate.edu";

		if (email == null | email.getEmail() == null)
			return false;
		
		Matcher emailMatcher = PATTERN_EMAIL.matcher(email.getEmail());

		// Check for valid email
		if (!emailMatcher.matches())
		{
			return false;
		}
		
		// Allow address@domain email's
		if(domain.equals(emailMatcher.group(2)))
		{
			return true;
		}
		
		// Allow address%40domain@gtempaccount.com email's
		Matcher gTempMatcher = PATTERN_GTEMPEMAIL.matcher(email.getEmail());
		if(gTempMatcher.matches() && domain.equals(gTempMatcher.group(2)))
		{
			return true;
		}

		return false;
	}

	public static boolean validSecondaryEmail(Email email, DataTrain train) {

		// Null or empty emails are okay for the secondary
		if (email == null || email.getEmail() == null
				|| email.getEmail().equals(""))
			return true;

		return PATTERN_EMAIL.matcher(email.getEmail()).matches();
	}

	public static boolean isUniqueSecondaryEmail(Email checkEmail,
			Email primary, DataTrain train) {
		return train.getUsersManager().isUniqueSecondaryEmail(checkEmail,
				primary);

	}

	//private static boolean validEmailDomain(String domain, DataTrain train) {
		
	//	AppData app = train.getAppDataController().get();
		
		
	//}

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

	/**
	 * Validation check for form C submission date. Verifies that it is no more
	 * than three weekdays since the absence.
	 * 
	 * @param absenceTime
	 * @return
	 */
	public static boolean canStillSubmitFormC(Date absenceTime,
			Date submissionTime, TimeZone timezone) {

		Calendar absence = Calendar.getInstance(timezone);
		Calendar submission = Calendar.getInstance(timezone);
		submission.setTime(submissionTime);
		absence.setTime(absenceTime);
		absence.set(Calendar.HOUR_OF_DAY, 0);
		absence.set(Calendar.MINUTE, 0);
		absence.set(Calendar.SECOND, 0);
		absence.set(Calendar.MILLISECOND, 0);
		submission.set(Calendar.HOUR_OF_DAY, 0);
		submission.set(Calendar.MINUTE, 0);
		submission.set(Calendar.SECOND, 0);
		submission.set(Calendar.MILLISECOND, 0);

		for (int i = 0; i < 3; i++) {
			submission.add(Calendar.DATE, -1);
			if (submission.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				submission.add(Calendar.DATE, -1);
			}
			if (submission.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				submission.add(Calendar.DATE, -2);
			}
		}

		if (submission.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			submission.add(Calendar.DATE, -1);
		}
		if (submission.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			submission.add(Calendar.DATE, -2);
		}

		if (absence.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			absence.add(Calendar.DATE, -1);
		}
		if (absence.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			absence.add(Calendar.DATE, -2);
		}

		return !absence.before(submission);
	}

	/**
	 * Compares dates to see if the second date is no more than the given number
	 * of weekdays after the first
	 * 
	 * @param start
	 *            Start date
	 * @param end
	 *            To date
	 * @param days
	 *            maximum difference in days
	 * @return True if from.date - to.date <= days and from < to
	 */
	@Deprecated
	public static boolean isLessThanWeekdaysAfter(Date start, Date end,
			int days, TimeZone timezone) {

		if (days < 0) {
			throw new IllegalArgumentException(
					"Don't support negative day counts.");
		}

		Calendar startCal = Calendar.getInstance(timezone);
		startCal.setTime(start);
		startCal.set(Calendar.HOUR_OF_DAY, 0);
		startCal.set(Calendar.MINUTE, 0);
		startCal.set(Calendar.SECOND, 0);
		startCal.set(Calendar.MILLISECOND, 0);

		if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			startCal.add(Calendar.DATE, -1);
		} else if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			startCal.add(Calendar.DATE, -2);
		}
		// now if it was a weekend, it's Friday for the comparison

		Calendar endCal = Calendar.getInstance(timezone);
		endCal.setTime(end);
		endCal.set(Calendar.HOUR_OF_DAY, 0);
		endCal.set(Calendar.MINUTE, 0);
		endCal.set(Calendar.SECOND, 0);
		endCal.set(Calendar.MILLISECOND, 0);

		if (endCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			endCal.add(Calendar.DATE, -1);
		} else if (endCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			endCal.add(Calendar.DATE, -2);
		}
		// now if it was a weekend, it's Friday for the comparison

		for (int day = 0; day < Math.abs(days); day++) {
			endCal.add(Calendar.DATE, -1);

			if (endCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				endCal.add(Calendar.DATE, 0);
			} else if (endCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				endCal.add(Calendar.DATE, -1);
			}
			// now if it was a weekend, it's Friday for the comparison
		}

		// now we should just be comparing weekend-agnostic WEEK DAYS.
		return endCal.compareTo(startCal) < 0;
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

	public static boolean isUniqueId(String id, Email primary) {
		return DataTrain.getAndStartTrain().getUsersManager()
				.isUniqueId(id, primary);
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
