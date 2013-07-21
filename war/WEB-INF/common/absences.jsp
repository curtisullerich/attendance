<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />
	<c:choose>
	<c:when test="${fn:length(absences) > 0}">

		<table class="gray full-width gray-hover" style="table-layout:auto;"><!-- white-space:nowrap;overflow:hidden; -->
			<thead>
				<tr class="dark-title">
					<th>Event</th>
					<th>Type</th>
					<th>Status</th>
					<th>Time In/Out</th>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${absences}" var="absence">

				<c:url var="absence_url_view" value="viewabsence">
					<c:param name="absenceid" value="${absence.id}"/>
					<%--c:param name="redirect" value="${pagetemplate.uri}"/--%>
				</c:url>
				<tr>
					<c:if test="${empty absence.event}">
						<td onclick="window.location='${absence_url_view}'">
							No event.
						</td>
					</c:if>
					<c:if test="${not empty absence.event}">
						<td onclick="window.location='${absence_url_view}'">
							<c:out value="${absence.event.type}" />
							<fmt:formatDate value="${absence.event.start}" pattern="M/d" />
							<fmt:formatDate value="${absence.event.start}" pattern="h:mm a" />
							-
							<fmt:formatDate value="${absence.event.end}" pattern="h:mm a" />
						</td>
					</c:if>

					<td onclick="window.location='${absence_url_view}'">
						${absence.type }
					</td>
					<td onclick="window.location='${absence_url_view}'">
					${absence.status }
					</td>
					<c:choose>
						<c:when test="${(absence.type.tardy) || (absence.type.earlyCheckOut)}">
						<td onclick="window.location='${absence_url_view}'">
							<fmt:formatDate value="${absence.start}" pattern="h:mm a" />
						</td>
						</c:when>
						<c:when test="${(absence.type.absence) && (empty absence.event)}">
						<td onclick="window.location='${absence_url_view}'">
							<fmt:formatDate value="${absence.start}" pattern="M/d" />
							<fmt:formatDate value="${absence.start}" pattern="h:mm a" />
							-
							<fmt:formatDate value="${absence.end}" pattern="h:mm a" />
						</td>
						</c:when>
						<c:when test="${(absence.type.absence) && (not empty absence.event)}">
							<td onclick="window.location='${absence_url_view}'">-</td>
						</c:when>
					</c:choose>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</c:when>
	<c:otherwise>
	<p>No absences, tardies, or early check outs yet!</p>
	</c:otherwise>
	</c:choose>