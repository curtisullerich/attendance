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
public class DeleteItem extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String buttonPressed = req.getParameter("Submit");
		String typeToKill, idToKill;
		if (buttonPressed != null) {

			idToKill = req.getParameter("IDtoKill");
			typeToKill = req.getParameter("TypeToKill");
			
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
				System.err.println("wrong type to kill\"" + typeToKill + "\"ended up in AddTardy");
			}
			
			resp.sendRedirect("/JSPPages/Director_attendanceTable.jsp");
		}
		
	}
}
