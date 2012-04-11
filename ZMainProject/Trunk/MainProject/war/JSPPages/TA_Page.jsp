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
		if (!user.getType().equalsIgnoreCase("TA")) {
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
	</script>
	<body>
	<!--*********************Page Trail*****************************-->
	
		<a href="/" title="PageTrail_Home">Home</a> 
		>
		<a href="/JSPPages/TA_Page.jsp" title="PageTrail_TA">TA</a>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

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
			<input type ="submit" onClick="window.location = '/JSPPages/TA_Edit_Info.jsp';"  value = "Edit TA Password">
		</p>
	</body>
</html>