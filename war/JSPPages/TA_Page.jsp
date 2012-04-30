<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

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
		if (!user.getType().equalsIgnoreCase("TA")) {
			if(user.getType().equalsIgnoreCase("Student"))
				response.sendRedirect("/JSPPages/Student_Page.jsp");
			else if(user.getType().equalsIgnoreCase("Director"))
				response.sendRedirect("/JSPPages/Director_Page.jsp");
			else
				response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<script>
		window.onload = function(){
			if(<%= request.getParameter("successfulSave")%> == "true"){
				alert("TA password successfully changed.");
			}
			else if(<%= request.getParameter("successfulSave")%> == "false"){
				alert("Password update error. TA password not changed.");	
			}
		}
		
		function help(){
			alert("Helpful information about TA page.")
		}
	
	</script>
	<body>
	<!--*********************Page Trail*****************************-->
	
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/TA_Page.jsp" title="TA Page">TA</a>
			
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
			<input type="submit" onClick="window.location = '/JSPPages/TA_Edit_Info.jsp';"  value = "Edit TA Password"/>
			<br/>
			<input type="submit" onClick="window.location = '/JSPPages/TA_Set_Rank.jsp';" value = "Set Ranks"/>
		</p>
	</body>
</html>