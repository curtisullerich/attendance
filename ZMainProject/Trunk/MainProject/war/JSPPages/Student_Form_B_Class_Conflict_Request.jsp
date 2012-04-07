<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="java.util.Calendar" %>
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
			<a href="/JSPPages/Student_Class_Conflict_Form.jsp" title="PageTrail_AbsenceApprovalForm_Class_Conflict">Class Conflict Form</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

	<!--*********************info*****************************-->
	
		<h1><%=user.getNetID() + ", " + user.getFirstName() + " " + user.getLastName() %></h1>
		

<!--==================================================================================================================-->		
<div id="right">
	
	<table style="text-align:right">
		<form method="post" action="/formB">
			<p><h1>Class Conflict Request Form | FORM B</p></h1><br>
			<tr>
			<p>First Year Members are only allowed one conflict.</p>
			<p>  </p><br>

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
			+"		<td><input name='dept' type='text'></td>"
			+"	</tr>"
			+""
			+"	<tr>"
			+"		<td>Course:</td>"
			+"		<td><input name='course' type='text'></td>"
			+"	</tr>"
			+"	<tr>"
			+"		<td>Section:</td>"
			+"		<td><input name='sect' type='text'></td>"
			+"	</tr>"
			+"	<tr>"
			+"		<td>Building:</td>"
			+"		<td><input name='building' type='text'></td>"
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
			+"			<div id='timeSelect" + i + "'>"
			+"				Until<input type='radio' value='Until' name='until'><br/>"
			+"				Starting At<input type='radio' value='Starting At' name='until'><br/>"
			+"				Completely Miss<input type='radio' value='Completely' name='until'><br/>"
			+"			</div>"
			+"		</td>"
			+"		<td>"
			+"			<div id='time" + i + "'>"
			+"				<input id='startHour' size='5' type='number' name='startHour' min='01' max='12' value='4'/>"
			+"				<input id='startMinute' size='5' type='number' name='startMinute' min='00' max='59' step='1' value='30'/>"
			+"				<input id='startAM' type='radio' name='startrdio' value='AM'>AM</input>"
			+"				<input id='startPM' type='radio' name='startrdio' value='PM' checked='1'>PM</input>"
			+"			</div>"
			+"		</td>"
			+"	</tr>"
			+""
			+"	<tr>"
			+"		<td>Comments:</td>"
			+"		<td><textarea rows='6' cols='18' name='comments' wrap='physical'></textarea></td>"
			+"	</tr>"
			+"<!-- /form -->";
		
}

%>

<%= formHtml %>

			<tr>
				<td></td>
				<td><input type="submit" value="Submit"></td>
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