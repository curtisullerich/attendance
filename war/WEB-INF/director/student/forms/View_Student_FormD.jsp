<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="forms.*" %>

<html>
	<head>
		<title>@10Dance</title>
		<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
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
			if(user.getType().equalsIgnoreCase("TA"))
				response.sendRedirect("/JSPPages/TA_Page.jsp");
			else if(user.getType().equalsIgnoreCase("Student"))
				response.sendRedirect("/JSPPages/Student_Page.jsp");
			else
				response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
<!-- 	-------------------------------             -->
	<%
	//String studentNetID = request.getParameter("form");//TODO send this parameter
	String formStr = request.getParameter("viewForm");
	
	
	if (formStr == null || formStr.equals("")) {
		System.err.println("There was a null or empty student param sent to the director view student page.");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
	}
	//User student = DatabaseUtil.getUser(studentNetID);
	Form form = DatabaseUtil.getFormByID(Long.parseLong(formStr));
	if (formStr == null) {
		System.err.println("The director tried to view a null student");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
	}
	
	%>
	
<!-- 	CHECK BELOW -->
	
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
				<td>Form Type:</td>
				<td><%=form.getType() %></td>
			</tr>
			<tr>					
				<td>Email to:</td>
				<td><%=form.getEmail() %></td>
			</tr>
			<tr>					
				<td>TotalHours:</td>
				<td><%=form.getHours() %></td>
			</tr>
			<tr>
				<td>Start Time:</td>
				<td><%=form.getStartTime().toString(12) %></td>	
			</tr>

			<tr>
				<td>Comment:</td>
				<td><%=form.getReason() %></td>
			</tr>
					
		</table>
			<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Student_Page.jsp'"/>
	
</body>
</html>