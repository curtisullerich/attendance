<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>${pagetemplate.title}</h1>
		<br/>

		<p>Seeing a lot of unanchored items from the same day? You're probably missing an event. <a href="/director/makeevent">Create one.</a></p>
		<table class="gray full-width">
			<tr>
				<thead>
				<th>Event</th>
				<th>Type</th>
				<th>Status</th>
				<th>Time of Arrival/Leaving</th>
				<th>Messages</th>
				</thead>
			</tr>
			<c:forEach items="${absences}" var="absence">
					<c:if test="${empty absence.event}">
						<tr>

						<td>
							<select name="EventID">
								<option value="">Choose one:</option>
								<c:forEach items="${events}" varStatus="status" var="event">
									<%//the purpose of the varStatus is to put an integer as the value to be sent
									//to the server so we can just to a parseInt instead of an iteration to get the day%>
									<option value="${event.id}"> ${event.type} on <fmt:formatDate value="${event.date}" pattern="MMMMM d, yyyy" /></option>
								</c:forEach>
							</select>
						</td>

					<c:if test="${not empty absence.event}">
						<td>
							<c:out value="${absence.event.type}" />
							<fmt:formatDate value="${absence.event.start}" pattern="M/dd/yyyy" />
							<fmt:formatDate value="${absence.event.start}" pattern="h:mm a" />
							-
							<fmt:formatDate value="${absence.event.end}" pattern="h:mm a" />
						</td>
					</c:if>

					<td>${absence.type}</td>
					<td>${absence.status}</td>
					<c:choose>
						<c:when test="${(absence.type.tardy) || (absence.type.earlyCheckOut)}">
						<td>
							<fmt:formatDate value="${absence.start}" pattern="hh:mm a" />
						</td>
						</c:when>
						<c:when test="${(absence.type.absence) && (empty absence.event)}">
						<td>
							<fmt:formatDate value="${absence.start}" pattern="M/dd/yyyy" />
							<fmt:formatDate value="${absence.start}" pattern="h:mm a" />
							-
							<fmt:formatDate value="${absence.end}" pattern="h:mm a" />
						</td>
						</c:when>
						<c:when test="${(absence.type.absence) && (not empty absence.event)}">
							<td>
								-
							</td>
						</c:when>
					</c:choose>
					<td>

						<c:if test="${!absence.messageThread.resolved}">
							<strong>
								<c:choose>
									<c:when test="${auth.user.type.director}">
										<a href="/director/messages/viewthread?id=${absence.messageThread.id}">Messages(${fn:length(absence.messageThread.messages)})</a>
									</c:when>
									<c:when test="${auth.user.type.student}">
										<a href="/student/messages/viewthread?id=${absence.messageThread.id}">Messages(${fn:length(absence.messageThread.messages)})</a>
									</c:when>
								</c:choose>
							</strong>
						</c:if>
						
						<c:if test="${absence.messageThread.resolved}">
							<c:choose>
								<c:when test="${auth.user.type.director}">
									<a href="/director/messages/viewthread?id=${absence.messageThread.id}">Messages(${fn:length(absence.messageThread.messages)})</a>
								</c:when>
								<c:when test="${auth.user.type.student}">
									<a href="/student/messages/viewthread?id=${absence.messageThread.id}">Messages(${fn:length(absence.messageThread.messages)})</a>
								</c:when>
							</c:choose>
						</c:if>
						<!-- Messages button. Make it bold if there's an unresolved thread. -->
					</td>
					
				</tr>
					</c:if>
					</c:forEach>
		</table>

		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>