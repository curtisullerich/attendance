<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

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
	<body>
<!--*********************Page Trail*****************************-->

	
	<!--TODO: need to connected to specific page-->
	<!--<h1>-->
		<li>
			<a href="http://www.iastate.edu" title="PageTrail_Home">Home</a> 
			>
			<a href="http://www.iastate.edu" title="PageTrail_Director">Director</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_ViewAndEditStudentList">View Class Reports and Absence Forms</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_ViewStudent">View Student</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_ViewIndividualReport">View Individual Report</a>
		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>

	<!--</h1>-->
<!--*********************info*****************************-->

	<!--*********************select..*****************************-->
	<a> Bar Graph <a/> </br>
	<a> Pie Chart <a/></br>
	<a> Line Graph <a/></br>
	<a> Absence List <a/></br>
	<a> Absence Forms<a/></br>
	
	</br>
	
	<!--*********************Student Table*****************************-->	
	NetID, Student name
	</br>
	<div>
		<table border="1">
			<tr><th>Date</th><th>Present</th><th>Absent</th><th>Tardy</th><th>Excused</th><th>Reason</th></tr>
			<tr><td>8/21</td><td><input type="checkbox" name="present"checked /></td><td><input type="checkbox" name="present" /></td><td><input type="checkbox" name="present" /></td><td><input type="checkbox" name="present" /></td><td>N/A</tr>
			<tr><td>8/22</td><td><input type="checkbox" name="present" /></td><td><input type="checkbox" name="present" checked /></td><td><input type="checkbox" name="present"  /></td><td><input type="checkbox" name="present"checked /></td><td>Crazy Teacher</tr>
			<tr><td>8/25</td><td><input type="checkbox" name="present" /></td><td><input type="checkbox" name="present" checked /></td><td><input type="checkbox" name="present"  /></td><td><input type="checkbox" name="present"checked /></td><td></tr>
		</table>
	</div>
	<button type="Save">Save Changes</button>
	
		<!--********************* Button *****************************-->	

	<h3>
		<!--button-->
		<button type="Back">Back</button>
	</h3>		
	</body>
	
</html>