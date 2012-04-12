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
System.out.println("tardyMessage.jsp: 1");
	String table = "";
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
	}
System.out.println("tardyMessage.jsp: 2");

	boolean hasMessage = true;
	User user = DatabaseUtil.getUser(netID);
	if ((request.getParameter("id") == null || request.getParameter("id").isEmpty())) {
		hasMessage = false;
System.out.println("tardyMessage.jsp: 4");//TODO remove debugging SOPs
	}
System.out.println("tardyMessage.jsp: 5");
	%>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

		<%
		
		System.out.println("tardyMessage.jsp: 6");
		
		if (hasMessage) {
			System.out.println("tardyMessage.jsp: 7");

			System.out.println("the long string is " + request.getParameter("id"));
			long id = Long.parseLong((String) request.getParameter("id"));
			System.out.println("the long long is   " + id);
			System.out.println("tardyMessage.jsp: 7.5");

			Tardy t = DatabaseUtil.getTardyByID(id);
			
			System.out.println("tardyMessage.jsp: 7.6");
			PriorityQueue<Message> p = new PriorityQueue<Message>();
			System.out.println("tardyMessage.jsp: 7.7");

			t.getMessages();
			System.out.println("tardyMessage.jsp: 8");
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
					System.out.println("tardyMessage.jsp: 9");

			
			while (!p.isEmpty()) {
				System.out.println("tardyMessage.jsp: 10");
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
			System.out.println("tardyMessage.jsp: 11");
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