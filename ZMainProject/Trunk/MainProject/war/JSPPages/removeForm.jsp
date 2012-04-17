<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.DatabaseUtil" %>


<html>
	<head>
		<title>@10Dance</title>
		<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
	</head>
	<%
	DatabaseUtil.removeForm(DatabaseUtil.getFormByID(new Long(request.getParameter("id"))));
	%>
	<script>
	
		window.onload = function(){
			window.location = document.referrer;
		}
	</script>
	<body>
	<p>Deleting Form...</p>
	</body>
</html>