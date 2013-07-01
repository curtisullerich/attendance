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
		<p>Unanchored absences are absences, tardies, or early checkouts that are not associated with an event in the database. You need to either anchor or delete all of these to make sure grades are being calculated properly.</p>
		<p>Seeing a lot of unanchored items from the same day? You're probably missing an event. <a href="./makeevent">Create one.</a></p>
		<c:if test="${not empty absences }">
		<form method="post" accept-charset="utf-8">
		<table class="gray full-width gray-hover">
			<thead>
				<tr class="dark-title">
					<th>Event</th>
					<th>netID</th>
					<th>Type</th>
					<th>Status</th>
					<th>Time of Arrival/Leaving</th>
				</tr>
			</thead>
			<c:forEach items="${absences}" var="absence" varStatus="i">
					<c:if test="${empty absence.event}">
						<tr>

						<td>
							<select name=${"\"EventID"}${i.count}${"\""}>
								<option value="">Choose one:</option>
								<c:forEach items="${events}" var="event">
									<option value="${event.id}"> ${event.type} on <fmt:formatDate value="${event.date}" pattern="MMMMM d, yyyy" /></option>
								</c:forEach>
							</select>
						</td>
						<td>
							${absence.student.id}
						</td>

					<c:if test="${not empty absence.event}">
						<td>
							<c:out value="${absence.event.type}" />
							<fmt:formatDate value="${absence.event.start}" pattern="M/d/yy" />
							<fmt:formatDate value="${absence.event.start}" pattern="h:mm a" />
							-
							<fmt:formatDate value="${absence.event.end}" pattern="h:mm a" />
						</td>
					</c:if>

					<td onclick="window.location='/director/viewabsence?absenceid=${absence.id}'">
						<input type="hidden" name=${"\"AbsenceID"}${i.count}${"\""} value="${absence.id}"/>
						${absence.type}
					</td>
					<td onclick="window.location='/director/viewabsence?absenceid=${absence.id}'">${absence.status}</td>
					<c:choose>
						<c:when test="${(absence.type.tardy) || (absence.type.earlyCheckOut)}">
						<td onclick="window.location='/director/viewabsence?absenceid=${absence.id}'">
							<fmt:formatDate value="${absence.start}" pattern="M/d/yy" />
							<fmt:formatDate value="${absence.start}" pattern="hh:mm a" />
						</td>
						</c:when>
						<c:when test="${(absence.type.absence) && (empty absence.event)}">
						<td onclick="window.location='/director/viewabsence?absenceid=${absence.id}'">
							<fmt:formatDate value="${absence.start}" pattern="M/d/yy" />
							<fmt:formatDate value="${absence.start}" pattern="h:mm a" />
							-
							<fmt:formatDate value="${absence.end}" pattern="h:mm a" />
						</td>
						</c:when>
						<c:when test="${(absence.type.absence) && (not empty absence.event)}">
							<td onclick="window.location='/director/viewabsence?absenceid=${absence.id}'">
								-
							</td>
						</c:when>
					</c:choose>
				</tr>
					</c:if>
					</c:forEach>
		</table>
		<br/>
		<input type="hidden" value="${fn:length(absences)}" name="UnanchoredCount"/>
		<input type="submit" value="Anchor to the selected events" name="Submit"/>
		</form>
		</c:if>
		<c:if test="${empty absences }">
			<strong>There are no un-anchored absences! (That's a good thing.)</strong>
		</c:if>
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>