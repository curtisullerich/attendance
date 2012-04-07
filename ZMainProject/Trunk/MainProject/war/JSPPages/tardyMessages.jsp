<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="attendance.*" %>
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
	
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
	}

	User user = DatabaseUtil.getUser(netID);
	
	long id = Long.parseLong((String) request.getParameter("id"));
	Tardy t = DatabaseUtil.getTardyByID(id);
	%>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>




		<%
		String table = 
		"<table>\n"	;
		
		PriorityQueue<Message> p = new PriorityQueue<Message>(t.getMessages());
		
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
		
		%>
	
	
	
	
	
	<h3>
		<!--button-->
		<button type="Back">Back</button>
	</h3>		
	</body>
	
</html>