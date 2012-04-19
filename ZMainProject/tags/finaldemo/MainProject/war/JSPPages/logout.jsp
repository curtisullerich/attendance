<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
		<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
	</head>
	<%
	session.removeAttribute("user");
	%>
	<script>
	
		window.onload = function(){
			window.location = "/";
		}
	</script>
	<body>
	<p>Logging out...</p>
	</body>
</html>