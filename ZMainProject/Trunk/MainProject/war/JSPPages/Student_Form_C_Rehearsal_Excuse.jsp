<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	<%
	String netID = (String) session.getAttribute("user"); 
	
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
	}

	User user = DatabaseUtil.getUser(netID);
	%>

	<body>
	
	<!--*********************Page Trail*****************************-->

			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Student_Page.jsp" title="PageTrail_Student">Student</a>
			>
			<a href="/JSPPages/Student_Form_C_Rehearsal_Excuse.jsp" >Rehearsal Excuse Form</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

	<!--*********************info*****************************-->
	
		<h1><%=user.getNetID() + ", " + user.getFirstName() + " " + user.getLastName() %></h1>
		

<!--==================================================================================================================-->		

<div id="right">
	
	<table style="text-align:right">
		<form method="post" action="/formC">
			<p><h1>Request for Excuse from Rehearsal | FORM C</p></h1><br>
			<tr>
			<p>Note: This form must be turned in no later than three weekdays following the rehearsal absence or tardy.  Documentation is required for all absences (doctor's note, obituary, wedding program, etc.).</p>
			<p> </p><br>

			<tr>
				<td><b>Absence Details</b></p>

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
				<td>
					<button type="Submit" name = "Submit" type ="Submit">Submit</button>
				</td>
			</tr>
		</form>
	</table>

	</form>
</div>

<!--==================================================================================================================-->		
		
<br/>
		<button type="Back">Back</button>
	</body>

</html>