<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="attendance.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="forms.*" %>
<%@ page import="comparators.*" %>

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
		<a href="/JSPPages/Student_Page.jsp" title="Student Page">Student</a>
		>
		<a href="/JSPPages/Student_View_Submitted_Forms.jsp" title="View Submitted Forms">View Submitted Forms</a>
	
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>
		
		<br/>
		<%
			AttendanceReport myReport = user.getAttendanceReport();
			List<Form> forms = myReport.sortFormsDescending(new FormTimeComp());
		%>
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
			insert = "You haven't submitted any forms yet!";
		}
		
		%>
		
		<%= insert %>
		
		
		
	</body>

</html>