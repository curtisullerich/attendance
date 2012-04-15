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
public class AddTardy extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String buttonPressed = req.getParameter("Submit");
		String typeToKill, idToKill, netID, status, type, currentIndex, messageIDs, tardyMonth, tardyDay, tardyYear, tardyHour, tardyMinute, startrdio;
		if (buttonPressed != null) {

			
			netID = req.getParameter("NetID");
			status = req.getParameter("Status");
			type = req.getParameter("Type");
			currentIndex = req.getParameter("currentIndex");
			messageIDs = req.getParameter("messageIDs");
			tardyMonth = req.getParameter("tardyMonth");
			tardyDay = req.getParameter("tardyDay");
			tardyYear = req.getParameter("tardyYear");
			tardyHour = req.getParameter("tardyHour");
			tardyMinute = req.getParameter("tardyMinute");
			startrdio = req.getParameter("startrdio");
			idToKill = req.getParameter("IDtoKill");
			typeToKill = req.getParameter("TypeToKill");
			
			
			User user = DatabaseUtil.getUser(netID);

			int hour = Integer.parseInt(tardyHour);
			int minute = Integer.parseInt(tardyMinute);
			int year = Integer.parseInt(tardyYear);
			int month = Integer.parseInt(tardyMonth);
			int day = Integer.parseInt(tardyDay);
			
			Date date = new Date(year, month, day);
			Time time = new Time((startrdio.equals("AM") ? hour%12 : (hour + 12) % 24), minute, date);//TODO right?
			Tardy t = new Tardy(netID, time, type);
			
			t.setStatus(status);
			t.setCurrentIndex(Integer.parseInt(currentIndex));
			String[] ids = messageIDs.split(" ");
			t.setMessageIDs(ids);
			
			user.addTardy(t);
			Long theId = Long.parseLong(idToKill);

			if (typeToKill.equals("Tardy")) {
				Tardy toKill = DatabaseUtil.getTardyByID(theId);
				if (toKill == null) {
					System.err.println("TOKILL WAS NULL.");
				}
				DatabaseUtil.removeTardy(toKill);
				
			} else if (typeToKill.equals("Absence")) {
				Absence toKill = DatabaseUtil.getAbsenceByID(theId);
				DatabaseUtil.removeAbsence(toKill);
				
			} else {
				//lolidk
			}
			
			
			
			resp.sendRedirect("/JSPPages/Director_attendanceTable.jsp");
		}
		
	}
}
