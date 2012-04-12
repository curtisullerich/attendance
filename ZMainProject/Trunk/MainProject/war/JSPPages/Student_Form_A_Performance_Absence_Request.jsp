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
	</script>
	<body>
	
	<!--*********************Page Trail*****************************-->

		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Student_Page.jsp" title="Student Page">Student</a>
		>
		<a href="/JSPPages/Student_Form_A_Performance_Absence_Request.jsp" title="Performance Absence Form A">Performance Absence Form A</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	

	<!--*********************info*****************************-->
	
		<h1><%=user.getNetID() + ", " + user.getFirstName() + " " + user.getLastName() %></h1>
		

<!--==================================================================================================================-->		


<div id="right">
	
	<table style="text-align:left">
		<form method="post" action="/formA">
			<p><h1>Performance Absence Request Form | FORM A</h1></p>

<p>Note: This form includes all performances through any post-season activity <br/>
ending January 9, 2013, and it must be submitted by 4:30 p.m. on Monday, August<br/>
23, 2012.  Documentation must be submitted to the director for all absences (doctor's note, obituary, wedding program, etc.).<br/></p>

			<tr>
				<td></td>
				<td>
					<b>Absence Details</b>
				</td>
			</tr>

			<tr>
				<td>Date:</td>
				<td><div id='startDate'>
						<input id='startMonth' size='5' type='number' name='StartMonth' min='01' max='12' value='9'>(MM)</input>
						<input id='startDay' size='5' type='number' name='StartDay' min='01' max='31' step='1' value='1'>(DD)</input>
						<input id='startYear' size='5' type='number' name='StartYear' min='2000' max='2999' step='1' value='2012'/>(YYYY)<!-- TODO make this work with current date instead of hard coding -->
					</div>
				</td>
			</tr>
			
			<tr>
				<td>Reasons:</td>
				<td><textarea rows="6" cols="18" name="Reason" wrap="physical"></textarea></td>
			</tr>

			<tr>
				<td><button type="Submit" name = "Submit" type ="Submit">Submit</button></td>
				<td><input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Student_Page.jsp'"/></td>
			</tr>
		</form>
	</table>

<!--==================================================================================================================-->		

	</body>

</html>