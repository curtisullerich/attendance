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
			>
			<a href="http://www.iastate.edu" title="PageTrail_ViewIndicidualReport">View Indicidual Report</a>
		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>

	<!--</h1>-->
<!--*********************tab*****************************-->
	<a> Bar Graph <a/> </br>
	<a> Pie Chart <a/></br>
	<a> Line Graph <a/></br>
	<a> Absence List <a/></br>
	<a> Absence Forms<a/></br>
<!--*********************tab*****************************-->
	
	</br>
	<a> NetID, Student Name<a/></br>
	
	
	Sort By:
	<select>
	  <option>Month</option>
	  <option>Week</option>
	  <option>Day</option>
	</select>
	</br>
	
	Show:
	<select>
	  <option>Jan.</option>
	  <option>Feb.</option>
	  <option>March</option>
	  <option>....</option>
	</select>
	
	</br>
	</br>
	Today's Date: (TODO: add calendar)
	<!--TODO : add calendar-->
	
	<h3>
		<div class="demo" id="canvaspie" width="600" height="200"></div>
		<div class="demo" id="canvasline" width="600" height="200"></div>
		<div class="demo" id="canvasbar" width="600" height="200"></div>
	</h3>
	
	<h3>
		<table bgColor=red height=10 width=200
		 cellSpacing=0 cellPadding=0 border= 0>
	 <TR><TD></TD></TR>
		</table>
		<table bgColor=green
		 height=10 width=100
		 cellSpacing=0 cellPadding=0 border= 0>
		 <TR><TD></TD></TR>
		</table>
	</h3>
	
	
	<h3>
		<input type="submit" value="Back" name="Back"/>
	</h3>
	</body>
</html>
	