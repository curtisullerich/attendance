<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Attendance for <c:out value="${user.name}" /></h1>
		
		<h2>Absences</h2>
		<table border='1'>
			<tr>
				<th>Event</th>
				<th>Type</th>
				<th>Status</th>
				<th>Time of Arrival/Leaving</th>
				<th>Messages</th>
			</tr>
			<c:forEach items="${absences}" var="absence">
				<tr>
					<c:if test="${empty absence.event}">
						<td>
							No event.
						</td>
					</c:if>
					<c:if test="${not empty absence.event}">
						<td>
							<c:out value="${absence.event.type}" />
							<fmt:formatDate value="${absence.event.start}" pattern="MM/dd/yyyy hh:mm a" />
							-
							<fmt:formatDate value="${absence.event.end}" pattern="hh:mm a" />
						</td>
					</c:if>

					<td>${absence.type}</td>
					<td>${absence.status}</td>

					<c:if test="${absence.type.Tardy or absence.type.EarlyCheckOut}">
						<td>
							<fmt:formatDate value="${absence.start}" pattern="hh:mm a" />
						</td>
					</c:if>
					<c:if test="${not empty absence.event}">
						<td>
							-
						</td>
					</c:if>
					<td>
						<c:if test="${absence.messageThread.resolved}">
							<strong>
								<a href="/student/messages/viewthread?id=${absence.messageThread.id}">messages</a>
							</strong>
						</c:if>
						
						<c:if test="${!absence.messageThread.resolved}">
							<a href="/student/messages/viewthread?id=${absence.messageThread.id}">messages</a>
						</c:if>
						<!-- Messages button. Make it bold if there's an unresolved thread. -->
					</td>
					
				</tr>
			</c:forEach>
		</table>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>