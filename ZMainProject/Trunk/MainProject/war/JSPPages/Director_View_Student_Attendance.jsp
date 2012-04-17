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
		if (!user.getType().equalsIgnoreCase("Director")) {
			if(user.getType().equalsIgnoreCase("TA"))
				response.sendRedirect("/JSPPages/TA_Page.jsp");
			else if(user.getType().equalsIgnoreCase("Student"))
				response.sendRedirect("/JSPPages/Student_Page.jsp");
			else
				response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<%
	String studentNetID = request.getParameter("student");//TODO send this parameter
	if (studentNetID == null || studentNetID.equals("")) {
		System.err.println("There was a null or empty student param sent to the director view student page.");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
	}
	User student = DatabaseUtil.getUser(studentNetID);
	if (student == null) {
		System.err.println("The director tried to view a null student");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
	}
	
	%>
	<script>
		function sendMeToMyTardyMessages(id) {
			window.location = "/JSPPages/ViewMessages.jsp?parentType=Tardy&parentID=" + id;
		}
		
		function sendMeToMyAbsenceMessages(id) {
			window.location = "/JSPPages/ViewMessages.jsp?parentType=Absence&parentID=" + id;
		}
		
		function help(){
			alert("Helpful information about student page.")
		}
	</script>

	<body>
<!--*********************Page Trail*****************************-->



		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Director_Page.jsp" title="Director Page">Director</a>
		>
		<a href="/JSPPages/Director_attendanceTable.jsp" title="View Class Attendance">View Class Attendance</a>
		>
		<a href="/JSPPages/Director_Student_View.jsp?student=<%= studentNetID %>" title="View Individual Student">View Individual Student</a>
		>
		<a href="/JSPPages/Director_View_Student_Attendance.jsp?student=<%= studentNetID %>" title="View Student's Attendance">View Attendance</a>
	
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>

	<!--*********************info*****************************-->
	<br/>
	Current grade: <%= student.getGrade() %>
	
	<!--*********************Student Table*****************************-->	
	<br/>
	<br/>
 			
			<%
			System.out.println("Director_View_Student_Attendance: getting tardies");

			List<Tardy> tardies = student.getTardies();
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
					+"</td>"
					+"<td><button onClick='sendMeToMyTardyMessages("
					+t.getID()
					+");'>Messages</button></td></tr>";
					System.out.println("The id that is sent as the param is" + t.getID());
				}
			}
			
			System.out.println("Student_View_Attendance: getting absences");

			List<Absence> absences = student.getAbsences();
			String absenceHtml = "";
			if (absences != null) {
				for (Absence a : absences) {
					absenceHtml += 
						"<tr><td>" 
						+ a.getStartTime().getDate()+ " " + a.getStartTime().get12Format()
						+"</td> <td>"
						+a.getType()
						+"</td><td>x</td><td></td><td>"
						+(a.getStatus())  
						+"</td>"
						+"<td><button onClick='sendMeToMyAbsenceMessages("
						+a.getID() 
						+");'>Messages</button></td></tr>";
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
					+"			<tr><th>Date</th><th>Type</th><th>Absent</th><th>Tardy</th><th>Excused</th></tr>"
								
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
		<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Director_Student_View.jsp?student=<%= studentNetID %>'"/>
	</h3>		
	</body>
	
</html>