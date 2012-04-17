package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import people.User;
import serverLogic.DatabaseUtil;
import time.Date;
import time.Time;
import attendance.Absence;
import attendance.Tardy;


@SuppressWarnings("serial")
public class AddAbsence extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String buttonPressed = req.getParameter("Submit");
		String typeToKill, idToKill, netID, status, type, currentIndex, messageIDs, startMonth, startDay, startHour, startYear, startMinute, startrdio, endMonth, endDay, endYear, endHour, endMinute, endrdio;
		if (buttonPressed != null) {

			
			netID = req.getParameter("NetID");
			status = req.getParameter("Status");
			type = req.getParameter("Type");
			currentIndex = req.getParameter("currentIndex");
			messageIDs = req.getParameter("messageIDs");
			startMonth = req.getParameter("startMonth");
			startDay = req.getParameter("startDay");
			startYear = req.getParameter("startYear");
			startHour = req.getParameter("startHour");
			startMinute = req.getParameter("startMinute");
			startrdio = req.getParameter("startrdio");
			endMonth = req.getParameter("endMonth");
			endDay = req.getParameter("endDay");
			endYear = req.getParameter("endYear");
			endHour = req.getParameter("endHour");
			endMinute = req.getParameter("endMinute");
			endrdio = req.getParameter("endrdio");
			idToKill = req.getParameter("IDtoKill");
			typeToKill = req.getParameter("TypeToKill");
			
			
			User user = DatabaseUtil.getUser(netID);

			int istartHour = Integer.parseInt(startHour);
			int istartMinute = Integer.parseInt(startMinute);
			int istartYear = Integer.parseInt(startYear);
			int istartMonth = Integer.parseInt(startMonth);
			int istartDay = Integer.parseInt(startDay);
			
			int iendHour = Integer.parseInt(endHour);
			int iendMinute = Integer.parseInt(endMinute);
			int iendYear = Integer.parseInt(endYear);
			int iendMonth = Integer.parseInt(endMonth);
			int iendDay = Integer.parseInt(endDay);
			
			Date startDate = new Date(istartYear, istartMonth, istartDay);
			Time startTime = new Time(istartHour, istartMinute, startDate);//TODO right?

			Date endDate = new Date(iendYear, iendMonth, iendDay);
			Time endTime = new Time(iendHour, iendMinute, endDate);//TODO right?

			Absence a = new Absence(netID, startTime, endTime, type);
			
			if (!status.equals("")) {
				a.setStatus(status);
				a.setCurrentIndex(Integer.parseInt(currentIndex));
				String[] ids = messageIDs.split(" ");
				a.setMessageIDs(ids);				
			}
			
			user.addAbsence(a);

			if (typeToKill.equals("Tardy")) {
				Long theId = Long.parseLong(idToKill);
				Tardy toKill = DatabaseUtil.getTardyByID(theId);
				if (toKill == null) {
					System.err.println("TOKILL WAS NULL.");
				}
				DatabaseUtil.removeTardy(toKill);
				
			} else if (typeToKill.equals("Absence")) {
				Long theId = Long.parseLong(idToKill);
				Absence toKill = DatabaseUtil.getAbsenceByID(theId);
				DatabaseUtil.removeAbsence(toKill);
				
			} else {
				//lolidk
				System.err.println("wrong type to kill\"" + typeToKill + "\"ended up in AddTardy");
			}
			
			resp.sendRedirect("/JSPPages/Director_attendanceTable.jsp");
		}
		
	}
}
