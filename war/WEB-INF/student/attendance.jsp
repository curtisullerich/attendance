<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
TODO: view message thread button<br/>
new message indication<br/>
columns in the table: date, type (e.g. performance absence), status (pending, etc)

		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Attendance for <c:out value="${user.name}" /></h1>
		
		<h2>Absences</h2>
		<table border='1'>
			<tr>
				<th>Event</th><!-- Should include date, start time, and end time! -->
				<th>Type</th>
				<th>Status</th>
				<th>Time of Arrival/Leaving</th>
			</tr>
			<c:forEach items="${absences}" var="absence">
				<tr>
					<c:if test="${empty absence.event}">
						No event. Absence from:
						<td>
							<fmt:formatDate value="${absence.start}" pattern="mm/dd/yyyy hh:mm" />
							-
							<fmt:formatDate value="${absence.end}" pattern="hh:mm" />
						</td>
					</c:if>

					<td>${absence.type}</td>
					<td>${absence.status}</td>

					<c:if test="${not empty absence.event}">
						<td>
							<fmt:formatDate value="${absence.event.start}" pattern="mm/dd/yyyy hh:mm" />
							-
							<fmt:formatDate value="${absence.event.end}" pattern="hh:mm" />
						</td>
					</c:if>
					<td>
						<c:if test="${absence.messageThread.resolved}">
							<b>
						</c:if>
						<a href="/message/viewthread?id=${absence.messageThread.id}">messages</a>
						<c:if test="${absence.messageThread.resolved}">
							</b>
						</c:if>
						<!-- Messages button. Make it bold if there's an unresolved thread. -->
					</td>
					
				</tr>
			</c:forEach>
		</table>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>