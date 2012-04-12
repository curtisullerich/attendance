<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

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
	<script>
		window.onload = function(){
			if(<%= request.getParameter("successfulSave")%> == "true"){
				alert("User info successfully edited.");
			}
			else if(<%= request.getParameter("successfulSave")%> == "false"){
				alert("Info update error. User info not changed.");	
			}
		}
	
		function listForms() {
			var div = document.getElementById("formsDiv")
			if (div.style.display == "none") {
				div.style.display = "block";
				document.getElementById("listForms").value = "Select a Form Below";
			} else {
				div.style.display = "none";
				document.getElementById("listForms").value = "Submit a Form";
			}
		}
		
		function help(){
			alert("Helpful information about student page.")
		}
	</script>
	<body>
<!--*********************Page Trail*****************************-->
	
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Student_Page.jsp" title="Student Page">Student</a>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>		


<!--*********************info*****************************-->

	<!--*********************Student Info*****************************-->	
		<br/><br/>
		<div>
		<table>
			<tr><td>NetID:</td> <td><%= user.getNetID() %></td></tr>
		</table>
		</div>
----------------------------------
<p>
		<!--********************* Button *****************************-->
		<input type="submit" id="listForms" onClick="listForms();"  value="Submit a Form"/>
		<input type="submit" onClick="viewForms();"  value="View Submitted Forms"/>
			<div id="formsDiv" style="display: none">
				<p><a href="/JSPPages/Student_Form_A_Performance_Absence_Request.jsp">Form A - Performance Absence Request</a></p>
				<p><a href="/JSPPages/Student_Form_B_Class_Conflict_Request.jsp">Form B - Class Conflict Request</a></p>
				<p><a href="/JSPPages/Student_Form_C_Rehearsal_Excuse.jsp">Form C - Request for Excuse from Rehearsal</a></p>
				<p><a href="/JSPPages/Student_Form_D_TimeWorked.jsp">Form D - Time Worked</a></p>
			</div>
		<input type ="submit" onClick="window.location = '/JSPPages/Student_View_Attendance.jsp';"  value = "View Attendance">
		<br/>
		<input type ="submit" onClick="window.location = '/JSPPages/Student_Edit_Info.jsp';"  value = "Edit My Information">
	</body>

</html>