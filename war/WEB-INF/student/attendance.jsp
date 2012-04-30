<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="attendance.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>

<html>
	<head>
		<title>@10Dance</title>
		<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
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
			if(user.getType().equalsIgnoreCase("TA"))
				response.sendRedirect("/JSPPages/TA_Page.jsp");
			else if(user.getType().equalsIgnoreCase("Director"))
				response.sendRedirect("/JSPPages/Director_Page.jsp");
			else
				response.sendRedirect("/JSPPages/logout.jsp");
		}
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
		<a href="/JSPPages/Student_Page.jsp" title="Student Page">Student</a>
		>
		<a href="/JSPPages/Student_View_Attendance.jsp" title="View Student Attendance">View Student Attendance</a>
	
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>

	<!--*********************info*****************************-->
	<br/>
	Current grade: <%= user.getGrade() %>
	
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
					+ "</td><td><button onClick='sendMeToMyTardyMessages("
					+t.getID()
					+");'>Messages</button></td><td>" 
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
						"<tr><td>" 
						+ a.getStartTime().getDate()+ " " + a.getStartTime().get12Format()
						+"</td> <td>"
						+a.getType()
						+"</td><td>x</td><td></td><td>"
						+(a.getStatus()) 
						+ "</td><td><button onClick='sendMeToMyAbsenceMessages("
						+a.getID() 
						+");'>Messages</button></td><td>"
						+ ("") 
						+ "</td></tr>";
					
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
		<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Student_Page.jsp'"/>
	</h3>		
	</body>
	
</html>