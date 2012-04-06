<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>

	<body>
<!--*********************Page Trail*****************************-->

	
	<!--TODO: need to connected to specific page-->
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Student_Page.jsp" title="PageTrail_Student">Student</a>
			>
			<a href="/JSPPages/Student_View_Attendance_List" title="PageTrail_ViewAttendance">View Attendance</a>
	<%
	String netID = (String) session.getAttribute("user"); 
	
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
	}

	User user = DatabaseUtil.getUser(netID);
	%>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

	<!--*********************info*****************************-->
	<br/>
	Current grade: <%= user.getLetterGrade() %>
	
	<!--*********************Student Table*****************************-->	
	<br/>
	<br/>
	<div>
		<table border="1">
			<tr><th>Date</th><th>Type</th><th>Absent</th><th>Tardy</th><th>Excused</th><th></th></tr>
			<%=user.getAttendanceHtml()%>
			<tr><td>8/21</td><td>  </td><td>  </td><td>  </td><td>  </td><td><button onClick="sendMeToMyMessages();">Messages</button></tr>
			<tr><td>8/22</td><td>  </td><td>  </td><td>  </td><td>  </td><td><button onClick="sendMeToMyMessages();">Messages</button></tr>
			<tr><td>8/25</td><td>  </td><td>  </td><td>  </td><td>  </td><td><button onClick="sendMeToMyMessages();">Messages</button></tr>
		</table>
	</div>
	<button type="Save">Save Changes</button>
	
		<!--********************* Button *****************************-->	

	<h3>
		<!--button-->
		<button type="Back">Back</button>
	</h3>		
	</body>
	
</html>