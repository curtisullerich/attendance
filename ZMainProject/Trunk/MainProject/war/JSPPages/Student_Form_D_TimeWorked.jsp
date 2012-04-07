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
			<a href="/JSPPages/TimeWork.jsp" title="TimeWork">Student_Form_D_TimeWorked.jsp</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

	<!--*********************info*****************************-->
	
		<h1><%=user.getNetID() + ", " + user.getFirstName() + " " + user.getLastName() %></h1>
		

<!--==================================================================================================================-->		


<div id="right">
	
	<table style="text-align:right">
		<form method="post" action="/formD">
			<p><h1>Time Work Form | FORM D</h1></p>


			<tr>
				<td>Email Address:</td>
				<td><input name="Email"></input></td>
			</tr>

			<tr>
				<td>Total Amount of Work:</td>
				<td><input name="AmountWorked"></input></td>
			</tr>

			<tr>
				<td>Date:</td>
				<td><div id='startDate'>
						<input id='startMonth' size='5' type='number' name='Month' min='01' max='12' value='9'>(MM)</input>
						<input id='startDay' size='5' type='number' name='tDay' min='01' max='31' step='1' value='1'>(DD)</input>
						<input id='startYear' size='5' type='number' name='Year' min='2000' max='2999' step='1' value='2012'/>(YYYY)<!-- TODO make this work with current date instead of hard coding -->
					</div>
				</td>
			</tr>
			
			<tr>
				<td>Work Detail :</td>
				<td><textarea rows="6" cols="18" name="Detail" wrap="physical"></textarea></td>
			</tr>

			<tr>
				<td>
					<button type="Submit" name = "Submit" type ="Submit">Submit</button>
				</td>
			</tr>
		</form>
	</table>

<!--==================================================================================================================-->		

<br/>
		<button type="Back" name = "Back" type ="Back">Back</button>
	</body>

</html>