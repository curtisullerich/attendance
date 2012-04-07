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
	
	
	<!--TODO: need to connected to specific page-->
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Student_Page.jsp" title="PageTrail_Student">Student</a>
			>
			<a href="/JSPPages/Student_View_Attendance.jsp" title="PageTrail_ViewAttendance">View Attendance</a>
			>
			<a href="/JSPPages/tardyMessages.jsp" title="PageTrail_tardyMessages">Tardy Messages</a>
			
	<%
	String netID = (String) session.getAttribute("user");
	String table = "";
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
	}

	boolean hasMessage = true;
	User user = DatabaseUtil.getUser(netID);
	if ((request.getParameter("id") == null || request.getParameter("id").isEmpty())) {
		hasMessage = false;
	}
	%>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

		<%
		
		
		if (hasMessage) {
			
			long id = Long.parseLong((String) request.getParameter("id"));
			Tardy t = DatabaseUtil.getTardyByID(id);
			PriorityQueue<Message> p = new PriorityQueue<Message>(t.getMessages());

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
						+"			<input type= 'text' name='New Message' id='New Message'/>"

						+"</td>"
					+"</tr></table>";
			
			
			while (!p.isEmpty()) {
				Message m = p.poll();
				if (m != null) {
					table+= 
					"	<tr>"
					+"		<td>"
					+"			<b>" + (m.readBy(netID) ? "" : "(new) ") + m.getTime() + " " + m.getSenderNetID() + "</b>"
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
		<button type="Back">Back</button>
	</h3>		
	</body>
	
</html>