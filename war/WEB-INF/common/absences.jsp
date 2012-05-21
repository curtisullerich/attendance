<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
						<c:if test="${absence.messageThread.resolved}">
							<strong>
								<a href="/student/messages/viewthread?id=${absence.messageThread.id}">Unread messages</a>
							</strong>
						</c:if>
						
						<c:if test="${!absence.messageThread.resolved}">
							<a href="/student/messages/viewthread?id=${absence.messageThread.id}">No new messages</a>
						</c:if>
						<!-- Messages button. Make it bold if there's an unresolved thread. -->
					</td>
					
				</tr>
			</c:forEach>
		</table>