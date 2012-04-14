<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="attendance.*" %>
<%@ page import="time.*" %>
<%@ page import="comment.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>

	<body>
		
		
		<!--*********************Page Trail*****************************-->
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Student_Page.jsp" title="Student Page">Student</a>
		>
		<a href="/JSPPages/Student_View_Attendance.jsp" title="View Student Attendance">View Student Attendance</a>
		>
		<a href="/JSPPages/tardyMessages.jsp" title="View Tardy Messages">View Tardy Messages</a>
			
		<%
		String netID = (String) session.getAttribute("user");
		User user = null;

		String table = "";
		if (netID == null || netID.equals("")) 
		{
			response.sendRedirect("/JSPPages/logout.jsp");
			return;
		}
		else
		{
			user = DatabaseUtil.getUser(netID);
			if (!user.getType().equalsIgnoreCase("Student")) {
				if(user.getType().equalsIgnoreCase("TA"))
					response.sendRedirect("/JSPPages/TA_Page.jsp");
				else if(user.getType().equalsIgnoreCase("Director"))
					response.sendRedirect("/JSPPages/Director_Page.jsp");
				else
					response.sendRedirect("/JSPPages/logout.jsp");
			}
		}
		
		boolean hasMessage = true;
		
		user = DatabaseUtil.getUser(netID);
		if ((request.getParameter("id") == null || request.getParameter("id").isEmpty())) {
			hasMessage = false;
		}
		%>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	

		<%
		
		
		if (hasMessage) {

			long id = Long.parseLong((String) request.getParameter("id"));

			Tardy t = DatabaseUtil.getTardyByID(id);
			
			PriorityQueue<Message> p = new PriorityQueue<Message>();

			t.getMessages();
			p.addAll(t.getMessages());
			if (p.isEmpty()) {
				table = "<br/><br/><b>There are currently no messages on this tardy.</b>";
			}
			table += 
					"<table>\n"	
							+ "<tr>"
							+"<td>"	
								+""
							+"</td>"
							+"<td>"

							+"</td>"
						+"</tr>"
						
						+ "<tr>"
						+"<td>"	
						+"			Write a new message:"
						+"</td>"
						+"<td>"
						+"			<form action='/newMessage' method='post'><input name='tardyid' type='text' hidden='true' value='" + id +"'></input> <input type= 'text' name='New Message' id='New Message'/>	<button name='Submit' type='Submit'>Send</button></form>"

						+"</td>"
					+"</tr>";

			
			while (!p.isEmpty()) {
				Message m = p.poll();
				if (m != null) {
					table+= 
					"	<tr>"
					+"		<td>"
					+"			<b>" + (m.readBy(netID) ? "" : "(new) ") + m.getTime().toString(12) + " " + m.getSenderNetID() + "</b>"
					+"		</td>"
					+"		<td>"
					+"			" + m.getContents()
					+"		</td>"
					+"	</tr>";
				}
			}
			table+="</table>";
		} else {
			table = "<br/><br/><b>No tardy selected.</b>";
		}

		%>
	
	
	<%= table %>
	
	
	<h3>
		<!--button-->
		<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages//Student_View_Attendance.jsp'"/>
	</h3>		
	</body>
	
</html>