<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="people.*" %>


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
	DatabaseUtil.dropATrainOnEm(); 
	%>
	
	<script>
		window.onload = function() {
			window.location = document.referrer;
		}
	</script>
	
	<body>
		<p>Deleting EVERYTHING...</p>	
	</body>
</html>