<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="forms.*" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	<%
	Form f = DatabaseUtil.getFormByID(new Long(request.getParameter("id")));
	f.setStatus("approved");
	DatabaseUtil.refreshForm(f);//TODO THIS MIGHT BREAK THINGS
	%>
	<script>
	
		window.onload = function(){
			window.location = document.referrer;
		}
	</script>
	<body>
	<p>Approving Form...</p>
	</body>
</html>