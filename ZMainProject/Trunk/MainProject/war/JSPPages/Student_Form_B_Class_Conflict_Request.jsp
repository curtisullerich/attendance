<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
		
		<script>
		window.onload = function() {
			if (<%= request.getParameter("error") != null%>)
			{
				alert("An error occurred in the fields");	
			}
		}
		</script>
		
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
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<script>
		function help(){
			alert("Helpful information about student page.")
		}
	</script>
	
<script>
	function confirmData()
	{

		//document.getElementById("UniversityID").value;
		if (document.getElementById("dept1").value == "")
		{
			//error and return false
			alert("Please enter something for the department field");
			return false;
		}
		if (document.getElementById("course1").value == "")
		{
			//error and return false
			alert("Please enter something for the course field");
			return false;
		}
		if (document.getElementById("sect1").value == "")
		{
			//error and return false
			alert("Please enter something for the section field");
			return false;
		}
		if (document.getElementById("building1").value == "")
		{
			//error and return false
			alert("Please enter something for the building field");
			return false;
		}
		if (document.getElementById("until1").checked == 0)
		{
			if (document.getElementById("startingat1").checked == 0)
			{
				//error and return false
				if (document.getElementById("completely1").checked == 0)
				{
					//error and return false
					alert("Please check either the until, starting at, or completely miss field");
					return false;
				}

			}
		}
		if (document.getElementById("comments1").value == "")
		{
			//error and return false
			alert("Please enter something for the comments field");
			return false;
		}
		if (<%= user.getYear() > 1%>)
		{
			if (document.getElementById("dept2").value == "")
			{
				//error and return false
				alert("Please enter something for the department field");
				return false;
			}
			if (document.getElementById("course2").value == "")
			{
				//error and return false
				alert("Please enter something for the course field");
				return false;
			}
			if (document.getElementById("sect2").value == "")
			{
				//error and return false
				alert("Please enter something for the section field");
				return false;
			}
			if (document.getElementById("building2").value == "")
			{
				//error and return false
				alert("Please enter something for the building field");
				return false;
			}
			if (document.getElementById("until2").checked == 0)
			{
				if (document.getElementById("startingat2").checked == 0)
				{
					//error and return false
					if (document.getElementById("completely2").checked == 0)
					{
						//error and return false
						alert("Please check either the until, starting at, or completely miss field");
						return false;
					}

				}
			}
			if (document.getElementById("comments2").value == "")
			{
				//error and return false
				alert("Please enter something for the comments field");
				return false;
			}
			var monthDays = [0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
			var d = new Date();
			var year = d.getFullYear();
			if (monthDays[document.getElementById("startMonth2").value] != document.getElementById("startDay2"));
			{
				alert("Invalid day for the current starting month");
				return false;
			}

			if (monthDays[document.getElementById("endMonth2").value] != document.getElementById("endDay2"));
			{
				alert("Invalid day for the current end month");
				return false;
			}
			
		}
		
		var monthDays = [0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
		var d = new Date();
		var year = d.getFullYear();
		if (monthDays[document.getElementById("startMonth1").value] != document.getElementById("startDay1"));
		{
			alert("Invalid day for the current starting month");
			return false;
		}

		if (monthDays[document.getElementById("endMonth1").value] != document.getElementById("endDay1"));
		{
			alert("Invalid day for the current end month");
			return false;
		}

		return true;
	}

</script>
	<body>
	
	<!--*********************Page Trail*****************************-->

		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Student_Page.jsp" title="Student Page">Student</a>
		>
		<a href="/JSPPages/Student_Class_Conflict_Form.jsp" title="Class Conflict Form B">Class Conflict Form B</a>

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
		<form method="post" action="/formB">
			<p><h1>Class Conflict Request Form | FORM B</p></h1>
			<tr>
			<p>First Year Members are only allowed one conflict.</p>
			

<% int year = user.getYear();
	int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
	int curYear = Calendar.getInstance().get(Calendar.YEAR);
	int day = Calendar.getInstance().get(Calendar.DATE);
	
int max = 1;
if (year > 1) max++;
String formHtml = "";
for (int i = 1; i <= max; i++) {
	formHtml +=	"	<!-- Form -->"
			+"	<tr>"
			+"		<td><b>Class Conflict #" + i + "</b>"
			+"	</td>"
			+"	</tr>"
			+""
			+"	<tr>"
			+"		<td>Department:</td>"
			+"		<td><input name='dept" + i + "' type='text'></td>"
			+"	</tr>"
			+""
			+"	<tr>"
			+"		<td>Course:</td>"
			+"		<td><input name='course" + i + "' type='text'></td>"
			+"	</tr>"
			+"	<tr>"
			+"		<td>Section:</td>"
			+"		<td><input name='sect" + i + "' type='text'></td>"
			+"	</tr>"
			+"	<tr>"
			+"		<td>Building:</td>"
			+"		<td><input name='building" + i + "' type='text'></td>"
			+"	</tr>"
			+"	<tr>"
			+"		<td>Starting Date:</td>"
			+"		<td><div id='startDate" + i + "'>"
			+"				<input id='startMonth" + i + "' size='5' type='number' name='startMonth" + i + "' min='01' max='31' value='" + month+ "'/>"
			+"				<input id='startDay" + i + "' size='5' type='number' name='startDay" + i + "' min='1' max='31' step='1' value='" + day + "'/>"
			+"				<input id='startYear" + i + "' size='5' type='number' name='startYear" + i + "' min='2012' max='2013' step='1' value='" + curYear + "'/><!-- TODO make this work with current date instead of hard coding -->"
			+"			</div>"
			+"		</td>"
			+"	</tr>"
			+"	"
			+"	<tr>"
			+"		<td>Ending Date:</td>"
			+"		<td><div id='endDate" + i + "'>"
			+"				<input id='endMonth" + i + "' size='5' type='number' name='endMonth" + i + "' min='01' max='31' value='" + month + "'/>"
			+"				<input id='endDay" + i + "' size='5' type='number' name='endDay" + i + "' min='00' max='59' step='1' value='" + day + "'/>"
			+"				<input id='endYear" + i + "' size='5' type='number' name='endYear" + i + "' min='2012' max='2013' step='1' value='" + curYear + "'/><!-- TODO make this work with current date instead of hard coding -->"
			+"			</div>"
			+"		</td>"
			+"	</tr>"
			+""
			+"	<tr>"
			+"		<td>"
			+"			<div style='text-align:right' id='timeSelect" + i + "'>"
			+"				Until<input type='radio' value='Until' name='until" + i + "'><br/>"
			+"				Starting At<input type='radio' value='Starting At' name='until" + i + "'><br/>"
			+"				Completely Miss<input type='radio' value='Completely' name='until" + i + "'><br/>"
			+"			</div>"
			+"		</td>"
			+"		<td>"
			+"			<div id='time" + i + "'>"
			+"				<input id='startHour" + i + "' size='5' type='number' name='startHour" + i + "' min='01' max='12' value='4'/>"
			+"				<input id='startMinute" + i + "' size='5' type='number' name='startMinute" + i + "' min='00' max='59' step='1' value='30'/>"
			+"				<input id='startAM" + i + "' type='radio' name='startrdio" + i + "' value='AM'>AM</input>"
			+"				<input id='startPM" + i + "' type='radio' name='startrdio" + i + "' value='PM' checked='1'>PM</input>"
			+"			</div>"
			+"		</td>"
			+"	</tr>"
			+""
			+"	<tr>"
			+"		<td>Comments:</td>"
			+"		<td><textarea rows='6' cols='18' name='comments" + i + "' wrap='physical'></textarea></td>"
			+"	</tr>"
			+"<!-- /form -->";
		
}

%>

<%= formHtml %>

			<tr>
				<td>
					<input type="submit" name="Submit" value="Submit">
					<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Student_Page.jsp'"/>
				</td>
			</tr>
		</form>
	</table>
	</form>
</div>
<!--==================================================================================================================-->		
		
	</body>

</html>