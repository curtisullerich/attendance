package servlets;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.*;

import forms.Form;

import people.User;

import serverLogic.DatabaseUtil;
import time.Time;
import time.Date;

public class FormB extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		// Get startrdio to do the correct time
		if (req.getParameter("Submit") != null) {
			// Account for it being possible to have 2 different class conflicts

			String netID = (String) req.getSession().getAttribute("user");
			User current = DatabaseUtil.getUser(netID);

			String dept1, course1, sect1, building1, comment1;
			int startMonth1, startDay1, startYear1, endMonth1, endDay1, endYear1, startHour1, startMinute1;
			startMonth1 = startDay1 = startYear1 = endMonth1 = endDay1 = endYear1 = startHour1 = startMinute1 = 0;
			String type1;
			// netID = req.getParameter("NetID");
			dept1 = req.getParameter("dept1");
			course1 = req.getParameter("course1");
			sect1 = req.getParameter("sect1");
			building1 = req.getParameter("building1");
			type1 = req.getParameter("until1");
			// Doesn't have to be there
			comment1 = req.getParameter("comments1");
			if (dept1 != null && course1 != null && sect1 != null
					&& building1 != null
					&& req.getParameter("startDay1") != null
					&& req.getParameter("startMonth1") != null
					&& req.getParameter("startYear1") != null
					&& req.getParameter("endDay1") != null
					&& req.getParameter("endMonth1") != null
					&& req.getParameter("endYear1") != null
					&& req.getParameter("startHour1") != null
					&& req.getParameter("startMinute1") != null
					&& type1 != null) {
				try {
					startMonth1 = Integer.parseInt(req
							.getParameter("startMonth1"));
					startDay1 = Integer.parseInt(req.getParameter("startDay1"));
					startYear1 = Integer.parseInt(req
							.getParameter("startYear1"));

					endMonth1 = Integer.parseInt(req.getParameter("endMonth1"));
					endDay1 = Integer.parseInt(req.getParameter("endDay1"));
					endYear1 = Integer.parseInt(req.getParameter("endYear1"));

					startHour1 = Integer.parseInt(req
							.getParameter("startHour1"));
					startMinute1 = Integer.parseInt(req
							.getParameter("startMinute1"));
				}

				catch (NumberFormatException e) {
					resp.sendRedirect("/JSPPages/invalidInputs");
				}

				if (!isValidateDate(startMonth1, startDay1, startYear1)) {
					resp.sendRedirect("/JSPPages/invalidInputs");
				}
				if (!isValidateDate(endMonth1, endDay1, endYear1)) {
					resp.sendRedirect("/JSPPages/invalidInputs");
				}
				if (!isValidateTime(startHour1, startMinute1)) {
					resp.sendRedirect("/JSPPages/invalidInputs");
				}
				Time start = null;
				Time end = null;
				if (type1.equalsIgnoreCase("Until")) {
					// 12:01 AM
					start = new Time(0, 1, new Date(startYear1, startMonth1,
							startDay1));
					end = new Time(startHour1, startMinute1, new Date(endYear1,
							endMonth1, endDay1));
				} else if (type1.equalsIgnoreCase("Starting At")) {
					start = new Time(startHour1, startMinute1, new Date(
							startYear1, startMonth1, startDay1));
					end = new Time(23, 59, new Date(endYear1, endMonth1,
							endDay1));
				} else if (type1.equalsIgnoreCase("Completely")) {
					start = new Time(0, 1, new Date(endYear1, endMonth1,
							endDay1));
					end = new Time(23, 59, new Date(endYear1, endMonth1,
							endDay1));
				} else {
					resp.sendRedirect("/JSPPages/invalidInputs");
				}

				Form form = new Form(current.getNetID(), comment1, start, end,
						"FormB");
				DatabaseUtil.addForm(form);

				if (current.getYear() > 1) {
					// Do it all again for the second one
					String dept2, course2, sect2, building2, comment2, type2;
					int startMonth2, startDay2, startYear2, endMonth2, endDay2, endYear2, startHour2, startMinute2;
					startMonth2 = startDay2 = startYear2 = endMonth2 = endDay2 = endYear2 = startHour2 = startMinute2 = 0;
					dept2 = req.getParameter("dept2");
					course2 = req.getParameter("course2");
					sect2 = req.getParameter("sect2");
					building2 = req.getParameter("building2");
					type2 = req.getParameter("until2");
					// Doesn't have to be there
					comment2 = req.getParameter("comments2");
					if (dept2 != null && course2 != null && sect2 != null
							&& building2 != null
							&& req.getParameter("startDay2") != null
							&& req.getParameter("startMonth2") != null
							&& req.getParameter("startYear2") != null
							&& req.getParameter("endDay2") != null
							&& req.getParameter("endMonth2") != null
							&& req.getParameter("endYear2") != null
							&& req.getParameter("startHour2") != null
							&& req.getParameter("startMinute2") != null
							&& type2 != null) {
						try {
							startMonth2 = Integer.parseInt(req
									.getParameter("startMonth2"));
							startDay2 = Integer.parseInt(req
									.getParameter("startDay2"));
							startYear2 = Integer.parseInt(req
									.getParameter("startYear2"));

							endMonth2 = Integer.parseInt(req
									.getParameter("endMonth2"));
							endDay2 = Integer.parseInt(req
									.getParameter("endDay2"));
							endYear2 = Integer.parseInt(req
									.getParameter("endYear2"));

							startHour2 = Integer.parseInt(req
									.getParameter("startHour2"));
							startMinute2 = Integer.parseInt(req
									.getParameter("startMinute2"));
						}

						catch (NumberFormatException e) {
							resp.sendRedirect("/JSPPages/invalidInputs");
						}

						if (!isValidateDate(startMonth2, startDay2, startYear2)) {
							resp.sendRedirect("/JSPPages/invalidInputs");
						}
						if (!isValidateDate(endMonth2, endDay2, endYear2)) {
							resp.sendRedirect("/JSPPages/invalidInputs");
						}
						if (!isValidateTime(startHour2, startMinute2)) {
							resp.sendRedirect("/JSPPages/invalidInputs");
						}
						Time start2 = null;
						Time end2 = null;
						if (type2.equalsIgnoreCase("Until")) {
							// 12:01 AM
							start2 = new Time(0, 1, new Date(startYear2,
									startMonth2, startDay2));
							end2 = new Time(startHour2, startMinute2, new Date(
									endYear2, endMonth2, endDay2));
						} else if (type2.equalsIgnoreCase("Starting At")) {
							start2 = new Time(
									startHour2,
									startMinute2,
									new Date(startYear2, startMonth2, startDay2));
							end2 = new Time(23, 59, new Date(endYear2,
									endMonth2, endDay2));
						} else if (type2.equalsIgnoreCase("Completely")) {
							start2 = new Time(0, 1, new Date(endYear2,
									endMonth2, endDay2));
							end2 = new Time(23, 59, new Date(endYear2,
									endMonth2, endDay2));
						} else {
							resp.sendRedirect("/JSPPages/invalidInputs");
						}

						Form form2 = new Form(current.getNetID(), comment2,
								start2, end2, "FormB");
						DatabaseUtil.addForm(form2);
					}

					resp.sendRedirect("/JSPPages/Student_Page.jsp");
				}
			}
		}
	}

	private boolean isValidateDate(int month, int day, int year) {
		int monthDays[] = { 0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (month <= 0 || month > 12)
			return false;
		int thisYear = Calendar.getInstance().get(Calendar.YEAR);
		if (year > thisYear + 1 || year < thisYear)
			return false;
		if (day > monthDays[month])
			return false;
		return true;
	}

	private boolean isValidateTime(int hour, int minute) {
		return hour <= 12 && minute <= 59;
	}

}
