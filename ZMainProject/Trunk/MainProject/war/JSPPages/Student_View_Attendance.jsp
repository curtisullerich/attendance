<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="attendance.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	<%
	String netID = (String) session.getAttribute("user");
	User user = null;
	
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
		return;
	}
	else
	{
		user = DatabaseUtil.getUser(netID);
		if (!user.getType().equalsIgnoreCase("Student")) {
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<script>
		function sendMeToMyTardyMessages(id) {
			window.location = "/JSPPages/tardyMessages.jsp?id=" + id;
		}
		function sendMeToMyAbsenceMessages(id) {
			window.location = "/JSPPages/absenceMessages.jsp?id=" + id;
		}
	</script>

	<body>
<!--*********************Page Trail*****************************-->
	
	
	<!--TODO: need to connected to specific page-->
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Student_Page.jsp" title="PageTrail_Student">Student</a>
			>
			<a href="/JSPPages/Student_View_Attendance.jsp" title="PageTrail_ViewAttendance">View Attendance</a>
	
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

	<!--*********************info*****************************-->
	<br/>
	Current grade: <%= user.getLetterGrade() %>
	
	<!--*********************Student Table*****************************-->	
	<br/>
	<br/>
 			
			<%
			System.out.println("Student_View_Attendance: getting tardies");

			List<Tardy> tardies = user.getTardies();
			String tardyHtml = "";
			if (tardies != null) {
 				for (Tardy t : tardies) {
					tardyHtml +=
					"<tr><td>"
					+t.getTime().getDate() + " " +t.getTime().get12Format()
					+"</td> <td>"
					+t.getType()
					+"</td><td></td><td>x</td><td>" 
					+(t.getStatus()) 
					+ "</td><td><button onClick=\"sendMeToMyTardyMessages('"
					+t.getID()
					+"');\">Messages</button></td><td>" 
					+ (t.hasNewMessageFor(netID) ? "x" : "") 
					+"</td></tr>";
					System.out.println("The id that is sent as the param is" + t.getID());
				}
			}
			
			System.out.println("Student_View_Attendance: getting absences");

			List<Absence> absences = user.getAbsences();
			String absenceHtml = "";
			if (absences != null) {
				for (Absence a : absences) {
					absenceHtml += 
						"<tr><td>" + a.getStartTime().getDate()+"</td> <td>"+a.getType()+"</td><td>x</td><td></td><td>" +(a.getStatus()) + "</td><td><button onClick='sendMeToMyAbsenceMessages("+a.getID() +");'>Messages</button></td><td>" + ("") + "</td></tr>";
					absenceHtml +=
					"<tr><td>"
					+a.getStartTime().getDate()
					+"</td> <td>"
					+a.getType()
					+"</td><td></td><td>x</td><td>" 
					+(a.getStatus()) 
					+ "</td><td><button onClick='sendMeToMyTardyMessages("
					+a.getID() 
					+");'>Messages</button></td><td>" 
					+ (a.hasNewMessageFor(netID) ? "x" : "") 
					+"</td></tr>";
				}
			}			
			%>

			<%
			String html = tardyHtml +absenceHtml;
			String insert = "";
			System.out.println("Student_View_Attendance: building html");

			if (!html.equals("")){
					insert = 
					"		<div>"
					+"		<table border='1'>"
					+"			<tr><th>Date</th><th>Type</th><th>Absent</th><th>Tardy</th><th>Excused</th><th> </th><th>New message?</th></tr>"
								
								+tardyHtml + absenceHtml
								
					+"		</table>"
					+"	</div>";
			} else {
				
				insert= "You don't have any tardies or absences! Hooray!";
			}
			%>

			<%= insert %>
			
	
	<h3>
		<!--button-->
		<button type="Back">Back</button>
	</h3>		
	</body>
	
</html>