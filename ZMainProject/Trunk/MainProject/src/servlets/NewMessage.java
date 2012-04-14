package servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import people.User;
import serverLogic.DatabaseUtil;
import attendance.Absence;
import attendance.Tardy;

import comment.Message;

@SuppressWarnings("serial")
public class NewMessage extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String buttonPressed = req.getParameter("Submit");
		String parentID, message, myNetID, recipient, parentType;
		if (buttonPressed != null) {

			// add message from the current user, to either the director or to
			// the student who's netID is on the tardy

			recipient = "";
			// Grab all the data from the fields
			parentID = req.getParameter("parentID");
			parentType = req.getParameter("parentType");
			message = req.getParameter("New Message");
			myNetID = (String) req.getSession().getAttribute("user");
			User user = DatabaseUtil.getUser(myNetID);
			long id = Long.parseLong(parentID);
			if (user.getType().equals("Student")) {
				recipient = DatabaseUtil.getDirector().getNetID();
			} else if (user.getType().equals("Director")) {
				//we'll be getting it below then. recipient = tardy.getNetID();
			} else {
				System.err.println("Some, a TA sent a message");
			}
			if (recipient.equals("")) {
				System.err.println("Recipient was empty");
			}
			Message theMessage = new Message(myNetID, recipient, message, parentID, parentType);
			if (parentType.equals("Tardy")) {
				Tardy tardy = DatabaseUtil.getTardyByID(id);
				tardy.addMessage(theMessage);
				DatabaseUtil.addTardy(tardy);
				recipient = tardy.getNetID();
			} else if (parentType.equals("Absence")) {
				Absence absence = DatabaseUtil.getAbsenceByID(id);
				absence.addMessage(theMessage);
				DatabaseUtil.addAbsence(absence);
				recipient=absence.getNetID();
			} else if (parentType.equals("EarlyCheckOut")) {
//				EarlyCheckOut eoc = DatabaseUtil.getEarlyCheckOutByID(id);
//				eoc.addMessage(theMessage);
//				DatabaseUtil.addEarlyCheckOut(eoc);
//				eoc.getNetID();
			} else if (parentType.equals("FormA")) {
				//TODO
			} else if (parentType.equals("FormB")) {
				
			} else if (parentType.equals("FormC")) {
				
			} else if (parentType.equals("FormD")) {
				
			} else {
				System.err.println("And, we have a type of message that we didn't account for.");
			}
			theMessage.setRecipientNetID(recipient);
			DatabaseUtil.addMessage(theMessage);//because we modified it after adding to the database
			resp.sendRedirect("/JSPPages/" + parentType + "Messages.jsp?id=" + parentID);
		}
		

		
	}
}
