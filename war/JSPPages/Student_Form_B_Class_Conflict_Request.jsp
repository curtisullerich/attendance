<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.TimeZone" %>

<html>
	<head>
		<title>@10Dance</title>
		<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
	
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
			window.onload = function() {
				if (<%= request.getParameter("error") != null%>)
				{
					if(<%= request.getParameter("error")%> == "invalidStartDate")
						alert("The start date was invalid. Form not Submitted");
					else if(<%= request.getParameter("error")%> == "invalidEndDate")
						alert("The end date was invalid. Form not submitted.");
					else if(<%= request.getParameter("error")%> == "endBeforeStart")
						alert("The start date was after the end date. Form not submitted.");
					else if(<%= request.getParameter("error")%> == "invalidStartTime")
						alert("The time was invalid. Form not submitted.");
					else if(<%= request.getParameter("error")%> == "noRadioButton")
						alert("No radio button was selected. Form not submitted.");
					else if(<%= request.getParameter("error")%> == "nullFields")
						alert("Missing field. Form not submitted.");
				}
			}
			
			function help(){
				alert("Helpful information about student page.")
			}
		
			function confirmData()
			{
				//document.getElementById("UniversityID").value;
				if (document.getElementById("dept").value == "")
				{
					//error and return false
					alert("Please enter something for the department field");
					return false;
				}
				if (document.getElementById("course").value == "")
				{
					//error and return false
					alert("Please enter something for the course field");
					return false;
				}
				if (document.getElementById("sect").value == "")
				{
					//error and return false
					alert("Please enter something for the section field");
					return false;
				}
				if (document.getElementById("building").value == "")
				{
					//error and return false
					alert("Please enter something for the building field");
					return false;
				}
				if (document.getElementById("until").checked == 0)
				{
					if (document.getElementById("startingat").checked == 0)
					{
						//error and return false
						if (document.getElementById("completely").checked == 0)
						{
							//error and return false
							alert("Please check either the until, starting at, or completely miss field");
							return false;
						}
		
					}
				}
				
				var monthDays = [0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
				var d = new Date();
				var year = d.getFullYear();
				var startMonth = parseInt(document.getElementById("startMonth").value);
				var startDay = parseInt(document.getElementById("startDay").value);
				var startYear = parseInt(document.getElementById("startYear").value);
				var endMonth = parseInt(document.getElementById("endMonth").value);
				var endDay = parseInt(document.getElementById("endDay").value);
				var endYear = parseInt(document.getElementById("endYear").value);
				
				if (monthDays[startMonth] < startDay)
				{
					alert("Invalid day for the current starting month");
					return false;
				}
		
				if (monthDays[endMonth] < endDay)
				{
					alert("Invalid day for the current end month");
					return false;
				}
				
				if ((startYear > endYear) || (startYear == endYear && ((startMonth > endMonth) || (startMonth == endMonth && startDay > endDay)))) {
					alert("The start date must be before or the same as the end date");
					return false;
				}
				
				return true;
			}
		</script>
	</head>
	<body>
	
	<!--*********************Page Trail*****************************-->

		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Student_Page.jsp" title="Student Page">Student</a>
		>
		<a href="/JSPPages/Student_Form_B_Class_Conflict_Request.jsp" title="Class Conflict Form B">Class Conflict Form B</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	
	<!--*********************info*****************************-->
	
		<h1><%=user.getNetID() + ", " + user.getFirstName() + " " + user.getLastName() %></h1>
		

<!--==================================================================================================================-->		
<div id="right">
	<form method="post" action="/formB" onSubmit="return confirmData();">
		<table style="text-align:left">
		
			<p><h1>Class Conflict Request Form | FORM B</p></h1>
			<tr>
			<p>First Year Members are only allowed one conflict.</p>
			

			<% 	
			int year = user.getYear();
			Calendar date = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
			int month = date.get(Calendar.MONTH) + 1;
			int curYear = date.get(Calendar.YEAR);
			int day = date.get(Calendar.DATE);
			%>
				
			
			<!-- Form -->
			<tr>
				<td><b>Class Conflict</b></td>
			</tr>
			
			<tr>
				<td>Department:</td>
				<td><input name='dept' type='text' id='dept'></td>
			</tr>
			
			<tr>
				<td>Course:</td>
				<td><input name='course' type='text' id='course'></td>
			</tr>
			<tr>
				<td>Section:</td>
				<td><input name='sect' type='text' id='sect'></td>
			</tr>
			<tr>
				<td>Building:</td>
				<td><input name='building' type='text' id='building'></td>
			</tr>
			<tr>
				<td>Starting Date:</td>
				<td>
					<div id='startDate'>
						<input id='startMonth' size='5' type='number' name='startMonth' min='01' max='31' value='<%= month %>'/>
						<input id='startDay' size='5' type='number' name='startDay' min='1' max='31' step='1' value='<%= day %>'/>
						<input id='startYear' size='5' type='number' name='startYear' min='2012' max='2013' step='1' value='<%= curYear %>'/><!-- TODO make this work with current date instead of hard coding -->
					</div>
				</td>
			</tr>
			<tr>
				<td></td>
				<td>Please enter the first day that the class meets.</td>
			</tr>
			
			<tr>
				<td>Ending Date:</td>
				<td>
					<div id='endDate'>
						<input id='endMonth' size='5' type='number' name='endMonth' min='01' max='31' value='<%= month %>'/>
						<input id='endDay' size='5' type='number' name='endDay' min='00' max='59' step='1' value='<%= day %>'/>
						<input id='endYear' size='5' type='number' name='endYear' min='2012' max='2013' step='1' value='<%= curYear %>'/><!-- TODO make this work with current date instead of hard coding -->
					</div>
				</td>
			</tr>
			<tr>
				<td></td>
				<td>Please enter the last day that the class meets.</td>
			</tr>
			
			<tr>
				<td>
					<div style='text-align:right' id='timeSelect'>
						Until<input type='radio' value='Until' name='until' id='until'><br/>
						Starting At<input type='radio' value='Starting At' name='until' id='startingat'><br/>
						Completely Miss<input type='radio' value='Completely' name='until' id='completely'><br/>
					</div>
				</td>
				<td>
					<div id='time'>
						<input id='startHour' size='5' type='number' name='startHour' min='01' max='12' value='4'/>
						<input id='startMinute' size='5' type='number' name='startMinute' min='00' max='59' step='1' value='30'/>
						<input id='startAM' type='radio' name='startrdio' value='AM'>AM</input>
						<input id='startPM' type='radio' name='startrdio' value='PM' checked='1'>PM</input>
					</div>
				</td>
			</tr>
			<tr>
				<td>Comments:</td>
				<td><textarea rows='6' cols='18' name='comments' wrap='physical' id='comments'></textarea></td>
			</tr>

			<tr>
				<td>
					<input type="submit" name="Submit" value="Submit">
					<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Student_Page.jsp'"/>
				</td>
			</tr>
		</table>
	</form>
</div>
<!--==================================================================================================================-->		
		
	</body>

</html>