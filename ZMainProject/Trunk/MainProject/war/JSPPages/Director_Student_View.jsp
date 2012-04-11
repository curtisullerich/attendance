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
			>
			<a href="http://www.iastate.edu" title="PageTrail_ViewStudent">View Student</a>
			
		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>

	<!--</h1>-->
<!--*********************info*****************************-->
	
	
	<!--*********************Student Info*****************************-->	
		</br>
		<div>
		<table>
			<tr><td>Name:</td> <td>FirstName, LastName</td></tr>
			<tr><td>NetID:</td> <td>aaaa</td></tr>
			<tr><td>Position:</td> <td>xxxxx</td></tr>
			<tr><td>Major:</td> <td>CPR E</td></tr>
			<tr><td>Email:</td> <td>helloworld@iastate.edu</td></tr>
			<tr><td>Attendance:</td> <td>100/100</td></tr>
			<tr><td>Grade:</td> <td>A</td></tr>
		
			<tr><td> </td> </tr>
			<tr><td> </td> </tr>
			<tr><td> </td> </tr>
		
			<tr><td>Advisor:</td> <td>FIRSTn LASTn</td></tr>
			<tr><td>Advisor Email:</td> <td>advisor@iastate.edu</td></tr>		
		</table>
		</div>
	<h2>----------------------------------<h2>
		<!--*********************Student MailBox*****************************-->	
	<div>
		<table>
			<td><label for="Search">Search:</label></td>
			<td><input type= "Search" name="Search" id="Search"/></td>	
		</table>
		</div>
		
		<!--********************* Button *****************************-->	

	<h3>
		<button type="PersonalMessage">Personal Message</button> </br>
		<button type="ViewIndividualReportAndAbsenceForms">View Individual Report and Absence Forms</button></br>
		<button type="EditStudentInfo">Edit Student Info</button></br>
		<button type="EditStudentAttendance">Edit Student Attendance</button></br>
		<button type="button">Prev</button>
		<button type="button">Next</button>
		
		
	</h3>
					
	</body>

</html>