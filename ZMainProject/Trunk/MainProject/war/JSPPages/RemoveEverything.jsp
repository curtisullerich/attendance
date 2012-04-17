<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.DatabaseUtil" %>


<html>
	<head>
		<title>@10Dance</title>
	</head>
	
	<%DatabaseUtil.dropATrainOnEm(); %>
	
	<script>
		window.onload = function() {
			window.location = document.referrer;
		}
	</script>
	
	<body>
		<p>Deleting EVERYTHING...</p>	
	</body>
</html>