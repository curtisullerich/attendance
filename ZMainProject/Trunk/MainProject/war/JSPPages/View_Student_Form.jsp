<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="forms.*" %>

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
<!-- 	-------------------------------             -->
	<%
	//String studentNetID = request.getParameter("form");//TODO send this parameter
	String formStr = request.getParameter("currentForm");
	
	
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
		<br/>
		<br/>
		<table>
			<tr>					
				<td>First Name:</td>
				<td><%=form.getReason() %></td>
			</tr>
			<tr>
				<td>Last Name:</td>
<%-- 				<td><%=student.getLastName()%></td>	 --%>
			</tr>

			<tr>
				<td>University ID:</td>
<%-- 				<td><%=student.getUnivID() %></td> --%>
			</tr>
					
			<!--Instrument-->
			<tr>
				<td>Section:</td>
				
				<td>
<%-- 					<%= student.getSection() %> --%>
				</td>
			</tr>
			<tr>
				<td>Years in band:</td>
				<td></td>
			</tr>
			<tr>
				<td>Major:</td>
<%-- 				<td><%=student.getMajor() %></td>	 --%>
			</tr>


		</table>
</body>
</html>