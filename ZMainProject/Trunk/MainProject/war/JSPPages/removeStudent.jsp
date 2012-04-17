<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	
	<% 
		String studentNetID = request.getParameter("student");
		DatabaseUtil.removeUser(DatabaseUtil.getUser(studentNetID));
		DatabaseUtil.removeAllAbsencesFor(studentNetID);
		DatabaseUtil.removeAllTardiesFor(studentNetID);
		DatabaseUtil.removeAllFormsFor(studentNetID);
		DatabaseUtil.removeMyMessages(studentNetID);
		DatabaseUtil.removeReport(DatabaseUtil.getAttendanceReport(studentNetID));
	%>
	
	<script>
		window.onload = function() {
			window.location = "/JSPPages/Director_attendanceTable.jsp";
		}
	</script>
	
	<body>
		<p>Deleting the student...</p>	
	</body>
</html>