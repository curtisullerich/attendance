<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.util.TimeZone" %>

<META NAME="Yifei Zhu">
<html>
	<head>
		<title>@10Dance</title>
	
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
					if(<%= request.getParameter("error")%> == "invalidDate")
						alert("The start date was invalid. Form not Submitted");
					else if(<%= request.getParameter("error")%> == "invalidDetails")
						alert("No details were supplied. Form not submitted.")
					else if(<%= request.getParameter("error")%> == "invalidAmountWorked")
						alert("The amount worked was invalid. Form not submitted.");
					else if(<%= request.getParameter("error")%> == "nullFields")
						alert("Missing field. Form not submitted.");
				}
			}
		
			function help(){
				alert("Helpful information about student page.")
			}
			
			function confirmData()
			{
				
				if (isNaN(parseInt(document.getElementById("AmountWorked").value)) || document.getElementById("Details").value == "")
				{	
					alert(isNaN(parseInt(document.getElementById("AmountWorked").value)) ? "You must provide an amount of time worked as a number." : "You must provide details for your time worked.");
					return false;
				}
				var monthDays = [0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
				var d = new Date();
				var year = d.getFullYear();
				
				if (monthDays[document.getElementById("startMonth").value] < document.getElementById("startDay").value)
				{
					alert("Invalid day for the current starting month");
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
		<a href="/JSPPages/Student_Form_D_TimeWorked.jsp" title="Time Worked Form D">Time Worked Form D</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>
		
		<!--*********************info*****************************-->
	
		<h1><%=user.getNetID() + ", " + user.getFirstName() + " " + user.getLastName() %></h1>
		

		<!--==================================================================================================================-->		

		<%
		int month = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago")).get(Calendar.MONTH) + 1;
		//No idea why the day is one too many
		int day = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago")).get(Calendar.DATE);
		int year = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago")).get(Calendar.YEAR);
		%>
		<div id="right">
		
			<table style="text-align:left">
				<form method="post" action="/formD" onsubmit="return confirmData();">
					<p><h1>Time Worked Form | FORM D</h1></p>
		
		
					<tr>
						<td>Who needs to verify?:</td>
						<td><select name="Email" id="Email">
								<option>wstaub@iastate.edu</option>
								<option>smyth@iastate.edu</option>
								<option>tkrock@iastate.edu</option>
								<option>lgarrett@iastate.edu</option>
								<option>mmasteller@iastate.edu</option>
								<option>ebliven@iastate.edu</option>
								
						</select></td>
					</tr>
					<tr>
						<td>Total amount of work:</td>
						<td ><input id="AmountWorked" name="AmountWorked"></input>Hours</td>
					</tr>
		
					<tr>
						<td>Date:</td>
						<td><div id='startDate'>
								<input id='startMonth' size='5' type='number' name='StartMonth' min='01' max='12' value='<%=month%>'>(MM)</input>
								<input id='startDay' size='5' type='number' name='StartDay' min='01' max='31' step='1' value='<%=day%>'>(DD)</input>
								<input id='startYear' size='5' type='number' name='StartYear' min='<%=year%>' max='<%=year+1%>' step='1' value='<%=year%>'/>(YYYY)<!-- TODO make this work with current date instead of hard coding -->
							</div>
						</td>
					</tr>
					
					<tr>
						<td>Work details :</td>
						<td ><textarea rows="6" cols="18" id="Details" name="Details" wrap="physical"></textarea></td>
					</tr>
		
					<tr>
						<td>
							<button type="Submit" name = "Submit" type ="Submit">Submit</button>
							<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Student_Page.jsp'"/>
						</td>
					</tr>
				</form>
			</table>
		</div>
	</body>
</html>