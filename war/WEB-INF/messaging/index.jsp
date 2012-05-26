<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setTimeZone value="America/Chicago" />

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="fmtCurrentDate" value="${now}" pattern="yyyy-MM-dd"/>
<fmt:formatDate var="fmtCurrentYear" value="${now}" pattern="yyyy"/>

<!DOCTYPE html>
<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		<h1>Message Inbox</h1>
		<br/>
		<div class="">
			<table class="gray full-width gray-hover" style="table-layout:fixed;white-space:nowrap;overflow:hidden;">
				<colgroup>
					<col width="12%" />
					<col width="15%" />
					<col width="23%" />
					<col width="40%" />
					<col width="10%" />
				</colgroup>
				<thead>
				<tr class="dark-title">
					<th>Resolved?</th>
					<th>Participants</th>
					<th>Subject</th>
					<th colspan=2>Latest Message</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach items="${threads}" var="thread" varStatus="loopCount">
					<tr onclick="window.location='messages/viewthread?id=<c:out value="${thread.id}"/>'">
						<td>
							<p>
								<c:if test="${thread.resolved}">Resolved</c:if>
								<c:if test="${!thread.resolved}"><strong>Unresolved</strong></c:if>
							</p>
						</td>
						<td>
							<c:if test="${!thread.resolved}"><strong></c:if>
							<p>
								<c:forEach items="${thread.participants}" var="participant" varStatus="">
									<c:out value="${participant.name}"/>
									<br/>
								</c:forEach>
							</p>
							<c:if test="${!thread.resolved}"></strong></c:if>
						</td>
						<td>
							<c:if test="${!thread.resolved}"><strong></c:if>
							<p>event / form info</p>
							<c:if test="${!thread.resolved}"></strong></c:if>
						</td>
						<td>
							<c:if test="${!thread.resolved}"><strong></c:if>
							<p style="overflow:hidden;">
								<c:out value="${thread.messages[0].text}"/>
							</p>
							<c:if test="${!thread.resolved}"></strong></c:if>
						</td>
						<td style="text-align:right;">
							<c:if test="${!thread.resolved}"><strong></c:if>
							<p>
								<fmt:formatDate var="fmtDate" value="${thread.messages[0].timestamp}" pattern="yyyy-MM-dd"/>
								<fmt:formatDate var="fmtYear" value="${thread.messages[0].timestamp}" pattern="yyyy"/>
								<c:choose>
									<c:when test="${(fmtDate eq fmtCurrentDate)}">
										<fmt:formatDate value="${thread.messages[0].timestamp}" pattern="h:mm a" />
									</c:when>
									<c:when test="${!(fmtYear eq fmtCurrentYear)}">
										<fmt:formatDate value="${thread.messages[0].timestamp}" pattern="MM/dd/YY" />
									</c:when>
									<c:otherwise>
										<fmt:formatDate value="${thread.messages[0].timestamp}" pattern="MMM d" />
									</c:otherwise>
								</c:choose>
							</p>
							<c:if test="${!thread.resolved}"></strong></c:if>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>

		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>