package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import people.User;
import serverLogic.DatabaseUtil;
import attendance.Tardy;

import comment.Message;

@SuppressWarnings("serial")
public class NewMessage extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String buttonPressed = req.getParameter("Submit");
		String tardyID, message, myNetID, recipient;
		if (buttonPressed != null) {

			// add message from the current user, to either the director or to
			// the student who's netID is on the tardy

			recipient = "";
			// Grab all the data from the fields
			tardyID = req.getParameter("tardyid");
			message = req.getParameter("New Message");
			myNetID = (String) req.getSession().getAttribute("user");
			User user = DatabaseUtil.getUser(myNetID);
			long id = Long.parseLong(tardyID);
			Tardy tardy = DatabaseUtil.getTardyByID(id);
			if (user.getType().equals("Student")) {
				recipient = DatabaseUtil.getDirector().getNetID();
			} else if (user.getType().equals("Director")) {
				recipient = tardy.getNetID();
			} else {
				System.err.println("Some, a TA sent a message");
			}
			if (recipient.equals("")) {
				System.err.println("Recipient was empty");
			}
			Message theMessage = new Message(myNetID, recipient, message);
			tardy.addMessage(theMessage);
			DatabaseUtil.addTardy(tardy);
			resp.sendRedirect("/JSPPages/tardyMessages.jsp?id=" + tardyID);
		}
		

		
	}
}
