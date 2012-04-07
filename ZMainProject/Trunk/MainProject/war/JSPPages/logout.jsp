<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
	<%
	session.removeAttribute("user");
	%>
	<script>window.location = "/";</script>
	</head>
	<p>Logging out...</p>
</html>