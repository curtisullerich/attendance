<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="forms.*" %>

<html>
	<head>
		<title>@10Dance</title>
		<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
	</head>
	<%
	Form f = DatabaseUtil.getFormByID(new Long(request.getParameter("id")));
	f.setStatus("denied");
	DatabaseUtil.refreshForm(f);//TODO THIS MIGHT BREAK THINGS
	%>
	<script>
	
		window.onload = function(){
			window.location = document.referrer;
		}
	</script>
	<body>
	<p>Denying Form...</p>
	</body>
</html>