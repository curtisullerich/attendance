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
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<body>
	
	<!--*********************Page Trail*****************************-->

			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Student_Page.jsp" title="PageTrail_Student">Student</a>
			>
			<a href="/JSPPages/Student_Form_D_TimeWorked.jsp" title="TimeWork">Form D</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

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
				<td ><input name="AmountWorked"></input></td>
			</tr>

			<tr>
				<td>Date (MM/DD/YYYY):</td>
				<td ><div id='startDate'>
						<input id='startMonth' size='5' type='number' name='Month' min='01' max='12' value='9'></input>
						<input id='startDay' size='5' type='number' name='Day' min='01' max='31' step='1' value='1'></input>
						<input id='startYear' size='5' type='number' name='Year' min='2000' max='2999' step='1' value='2012'/><!-- TODO make this work with current date instead of hard coding -->
					</div>
				</td>
			</tr>
			
			<tr>
				<td>Work details :</td>
				<td ><textarea rows="6" cols="18" name="Details" wrap="physical"></textarea></td>
			</tr>

			<tr>
			<td></td>
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