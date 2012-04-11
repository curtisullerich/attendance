<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

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
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<body>
<!--*********************Page Trail*****************************-->

	
	<!--TODO: need to connected to specific page-->
	<!--<h1>-->
		<li>
			<a href="http://www.iastate.edu" title="PageTrail_Home">Home</a> 
			>
			<a href="http://www.iastate.edu" title="PageTrail_Director">Director</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_ViewAndEditStudentList">View and Edit Student List</a>
		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>

	<!--</h1>-->
<!--*********************info*****************************-->

	<!--*********************Title*****************************-->
	<h4>
		<select>
			<option>All Student</option>
			<option>Selected Student</option>
			<option>Unselected Student</option>
		</select>
		select
		<a href="http://www.iastate.edu" title="All">All</a>
		<a href="http://www.iastate.edu" title="None">None</a>
	</h4>
	
	<!--*********************Student Table*****************************-->	

	<div>
		<table border="1">
			<tr><th>Name</th><th>Position</th><th>Email</th><th>Major</th><th>Advisor</th><th>Advisor Email</th> </tr>
			<tr><td>Giaccomo Guizzoni</td> <td>TA</td><td>a@iastate.edu</td><td>English</td><td>Any</td><td>a@iastate.edu</td></tr>
			<tr><td>Guido Jack</td> <td>Sax</td><td>a@iastate.edu</td><td>English</td><td>Any</td><td>a@iastate.edu</td></tr>
			<tr><td>Marco Button Tuttofare</td> <td>Trombone</td><td>a@iastate.edu</td><td>English</td><td>Any</td><td>a@iastate.edu</td></tr>
			<tr><td>Mariah Maclachian</td> <td>Drums</td><td>a@iastate.edu</td><td>English</td><td>Any</td><td>a@iastate.edu</td></tr>

		</table>
	</div>

		<!--********************* Button *****************************-->	

	<h3>
		<!--first line-->
		<button type="AddStudent">Add Student</button>
		<button type="ViewStudent">View Student(s)</button>
		<button type="EmailStudent">Email Student(s)</button>
		<button type="DeleteStudent">Delete Student(s)</button>
		</br>
		<!--second line-->
		<button type="Back">Back</button>
		<button type="ViewClassReportAndAbsenceForms">View Class Report and Absence Forms</button>
		<button type="ViewIndividualReportsAndAbsenceForms">View Individual Reports and Absence Forms</button>
	</h3>		
	</body>
	
</html>