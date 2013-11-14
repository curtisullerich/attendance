<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>


	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Student Page</h1>
		<p>
			<b>Current grade:</b> <c:out value="${user.grade.displayName}" /><br/>
			<b>Total (unexcused) minutes missed:</b> <c:out value="${user.minutesMissed}" /><br/>
			<b>Total minutes granted via TimeWorked forms:</b> <c:out value="${user.minutesAvailable}" /><br/>
			If you notice any inconsistencies with this (i.e. numbers aren't adding up like they should) email the developers at <a href="mailto:mbattendance@iastate.edu">mbattendance@iastate.edu</a>.<br/>
			<br/>
			Use the navigation options on the left to manage your attendance.
		</p>
		<jsp:include page="/WEB-INF/common/status.jsp"/>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>