<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="forms.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="attendance.*" %>
<%@ page import="comparators.*" %>


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
	function help(){
		alert("Helpful information about student page.")
	}
	
	function remove(id){
		var con = confirm("Are you sure you want to delete?");
		if (con == true)
		{
			window.location = "/JSPPages/removeForm.jsp?id=" + id	
		}
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
		<a href="/JSPPages/Director_View_Student_Submitted_Forms.jsp?student=<%= studentNetID %>" title="View Student's Submitted Forms">View Submitted Forms</a>
	
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>
		
		<br/>
		<%
			AttendanceReport myReport = student.getAttendanceReport();
			List<Form> forms = myReport.sortFormsDescending(new FormTimeComp());
		%>
		<br/>
		Number of Forms Submitted: <%=forms.size()%>
		<br/>
		<br/>
		<%
		String formHtml = "";
		if (forms.size() != 0)
		{
			for (Form f : forms)
			{
				formHtml += "<tr><td>" + f.getStartTime().getDate() + "</td> <td>" + f.getEndTime().getDate() + "</td> <td>"+ f.getType() 
						+ "</td> <td>" + f.getStatus() + "</td> <td> <button onClick=\"remove('" + f.getID() + "');\">Delete</button</td>";
			}
		}
		
		String insert = "";
		if (!formHtml.equals("")){
			insert =	" 		<div>"
					+"		<table border='1'>"
					+"			<tr><th>Start Date</th><th>End Date</th><th>Type</th><th>Status</th><th> </th>"
					+ formHtml
					+"		</table>"
					+"	</div>";
		}
		else
		{
			insert = "You haven't submitted any forms yet!<br/>";
		}
		
		%>
		
		<%= insert %>
		
		<br/>
		<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Director_Student_View.jsp?student=<%= studentNetID %>'"/>

	</body>

</html>