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
			<a href="/JSPPages/Student_Form_A_Performance_Absence_Request.jsp" title="PageTrail_AbsenceApprovalForm_Class_Conflict">Performance Absence Request</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

	<!--*********************info*****************************-->
	
		<h1><%=user.getNetID() + ", " + user.getFirstName() + " " + user.getLastName() %></h1>
		

<!--==================================================================================================================-->		


<div id="right">
	
	<table style="text-align:right">
		<form method="post" action="/formA">
			<p><h1>Performance Absence Request Form | FORM A</h1></p>

<p>Note: This form includes all performances through any post-season activity <br/>
ending January 9, 2013, and it must be submitted by 4:30 p.m. on Monday, August<br/>
23, 2012.  Documentation is required for all absences (doctor's note, obituary, wedding program, etc.).<br/></p>

			<tr>
				<td></td>
				<td>
					<b>Absence Details</b>
				</td>
			</tr>

			<tr>
				<td>Date:</td>
				<td><div id='startDate'>
						<input id='startMonth' size='5' type='number' name='startMonth' min='01' max='31' value='9'/>
						<input id='startDay' size='5' type='number' name='startDay' min='00' max='59' step='1' value='1'/>
						<input id='startYear' size='5' type='number' name='startYear' min='2012' max='2013' step='1' value='2012'/><!-- TODO make this work with current date instead of hard coding -->
					</div>
				</td>
			</tr>
			
			<tr>
				<td>Reasons:</td>
				<td><textarea rows="6" cols="18" name="comments" wrap="physical"></textarea></td>
			</tr>

			<tr>
				<td></td>
				<td><input type="submit" value="Submit"></td>
			</tr>
		</form>
	</table>

<!--==================================================================================================================-->		

<br/>
		<button type="Back">Back</button>
	</body>

</html>