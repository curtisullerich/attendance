<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>


<META NAME="Yifei Zhu">
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
		<a href="/JSPPages/Student_Form_D_TimeWorked.jsp" title="Time Worked Form D">Time Worked Form D</a>

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
		<form method="post" action="/formD">
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
				<td ><input name="AmountWorked"></input>Hours</td>
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
				<td>Work details :</td>
				<td ><textarea rows="6" cols="18" name="Details" wrap="physical"></textarea></td>
			</tr>

			<tr>
				<td>
					<button type="Submit" name = "Submit" type ="Submit">Submit</button>
					<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Student_Page.jsp'"/>
				</td>
			</tr>
		</form>
	</table>

<!--==================================================================================================================-->		

	</body>

</html>