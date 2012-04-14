package servlets;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

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
			
			int maxForms = 1;
			if(current.getYear() > 1)
				maxForms = 2;
			
			List<Form> existingForms = DatabaseUtil.getForms("FormB", netID);
			if (existingForms.size() >= maxForms) {
				resp.sendRedirect("/JSPPages/Student_Page.jsp?formSubmitted='false'");
				return;
			}

			
			String dept1, course1, sect1, building1, comment1, startrdio1;
			int startMonth1, startDay1, startYear1, endMonth1, endDay1, endYear1, startHour1, startMinute1;
			startMonth1 = startDay1 = startYear1 = endMonth1 = endDay1 = endYear1 = startHour1 = startMinute1 = 0;
			String type1;
			
			// netID = req.getParameter("NetID");
			dept1 = req.getParameter("dept");
			course1 = req.getParameter("course");
			sect1 = req.getParameter("sect");
			building1 = req.getParameter("building");
			type1 = req.getParameter("until");
			startrdio1 = req.getParameter("startrdio");
			
			if (type1 == null) {
				resp.sendRedirect("/JSPPages/Student_Form_B_Class_Conflict_Request.jsp?error='noRadioButton'");
				return;
			}
			
			// Doesn't have to be there
			comment1 = req.getParameter("comments");
			
			
			if (dept1 != null && course1 != null && sect1 != null && building1 != null && req.getParameter("startDay") != null
					&& req.getParameter("startMonth") != null && req.getParameter("startYear") != null && req.getParameter("endDay") != null
					&& req.getParameter("endMonth") != null && req.getParameter("endYear") != null && req.getParameter("startHour") != null
					&& req.getParameter("startMinute") != null && startrdio1 != null && type1 != null && dept1 != "" && course1 != "" && sect1 != "" && building1 != "" && req.getParameter("startDay") != ""
					&& req.getParameter("startMonth") != "" && req.getParameter("startYear") != "" && req.getParameter("endDay") != ""
					&& req.getParameter("endMonth") != "" && req.getParameter("endYear") != "" && req.getParameter("startHour") != "" 
					&& req.getParameter("startMinute") != "" && startrdio1 != "" && type1 != "") {
				
						startMonth1 = Integer.parseInt(req.getParameter("startMonth"));
						startDay1 = Integer.parseInt(req.getParameter("startDay"));
						startYear1 = Integer.parseInt(req.getParameter("startYear"));
	
						endMonth1 = Integer.parseInt(req.getParameter("endMonth"));
						endDay1 = Integer.parseInt(req.getParameter("endDay"));
						endYear1 = Integer.parseInt(req.getParameter("endYear"));
	
						startHour1 = Integer.parseInt(req.getParameter("startHour"));
						startMinute1 = Integer.parseInt(req.getParameter("startMinute"));

				if (!isValidateDate(startMonth1, startDay1, startYear1)) {
					resp.sendRedirect("/JSPPages/Student_Form_B_Class_Conflict_Request.jsp?error='invalidStartDate'");
					return;
				}
				if (!isValidateDate(endMonth1, endDay1, endYear1)) {
					resp.sendRedirect("/JSPPages/Student_Form_B_Class_Conflict_Request.jsp?error='invalidEndDate'");
					return;
				}
				
				if ((startYear1 > endYear1) || (startYear1 == endYear1 && ((startMonth1 > endMonth1) || (startMonth1 == endMonth1 && startDay1 > endDay1)))) {
					resp.sendRedirect("/JSPPages/Student_Form_B_Class_Conflict_Request.jsp?error='endBeforeStart'");
					return;
				}
				
				if (!isValidateTime(startHour1, startMinute1)) {
					resp.sendRedirect("/JSPPages/Student_Form_B_Class_Conflict_Request.jsp?error='invalidStartTime '");
					return;
				}
				if (startHour1 < 12 && startrdio1.equalsIgnoreCase("PM"))
					startHour1+=12;
				if (startHour1 == 12 && startrdio1.equalsIgnoreCase("AM"))
					startHour1=0;
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
					resp.sendRedirect("/JSPPages/Student_Form_B_Class_Conflict_Request.jsp?error='noRadioButton'");
					return;
				}

				Form form = new Form(current.getNetID(), comment1, start, end, dept1,
						course1, sect1, building1, "FormB");
				DatabaseUtil.addForm(form);

				resp.sendRedirect("/JSPPages/Student_Page.jsp?formSubmitted='true'");
				return;
			}
			//Outside the big if that checks if all the fields had stuff in them
			resp.sendRedirect("/JSPPages/Student_Form_B_Class_Conflict_Request.jsp?error='nullFields'");
			return;
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
