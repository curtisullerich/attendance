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
		if (!user.getType().equalsIgnoreCase("Director")) {
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<%
	String studentNetID = request.getParameter("student");//TODO send this parameter
	if (studentNetID == null || studentNetID.equals("")) {
		System.err.println("There was a null or empty student param sent to the director view student page.");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
	}
	User student = DatabaseUtil.getUser(studentNetID);
	if (student == null) {
		System.err.println("The director tried to view a null student");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
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
				div.style.display = "inline";
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
		<br/>
		<br/>
		<table>
			<tr>					
				<td>First Name:</td>
				<td><%=student.getFirstName() %></td>
			</tr>
			<tr>
				<td>Last Name:</td>
				<td><%=student.getLastName()%></td>	
			</tr>

			<tr>
				<td>University ID:</td>
				<td><%=student.getUnivID() %></td>
			</tr>
					
			<!--Instrument-->
			<tr>
				<td>Section:</td>
				
				<td>
					<%= student.getSection() %>
				</td>
			</tr>
			<tr>
				<td>Years in band:</td>
				<td></td>
			</tr>
			<tr>
				<td>Major:</td>
				<td><%=student.getMajor() %></td>	
			</tr>


		</table>
				
----------------------------------
		<p>
		<!--********************* Button *****************************-->
		<input type="submit" onClick="window.location='/JSPPages/Director_View_Student_Submitted_Forms.jsp?student=<%= student.getNetID()%>';"  value="View Submitted Forms"/>
		<br/>
		<input type="submit" onClick="window.location='/JSPPages/Director_View_Student_Attendance.jsp?student=<%= student.getNetID()%>';"  value="View Attendance">
		<br/>
		<input type="submit" onClick="window.location='/JSPPages/Director_Edit_Student_Info.jsp?student=<%= student.getNetID()%>';"  value="Edit Student's Information">
		<br/>
		<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Student_Page.jsp'"/>
	</body>

</html>