<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="fmtCurrentDate" value="${now}" pattern="yyyy-MM-dd"/>
<fmt:formatDate var="fmtCurrentYear" value="${now}" pattern="yyyy"/>

		<div class="">
			<table class="sortable gray full-width gray-hover" style="table-layout:fixed;"><!-- white-space:nowrap;overflow:hidden; -->
				<colgroup>
					<col width="18%" />
					<col width="32%" />
					<col width="40%" />
					<col width="10%" />
				</colgroup>
				<thead>
				<tr class="dark-title">
					<th>Participants</th>
					<th>Subject</th>
					<th class="sorttable_nosort" colspan=2>Latest Message</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach items="${threads}" var="thread" varStatus="loopCount">
					<tr onclick="window.open('messages/viewthread?id=<c:out value="${thread.id}"/>')">
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
							<c:if test="${not empty thread.formParent}">
								<c:if test="${thread.formParent.type.displayName eq 'A'}">
									<p>${thread.formParent.status} Form A - <fmt:formatDate value="${thread.formParent.start}" pattern="M/d/yy"/></p>
								</c:if>
								<c:if test="${thread.formParent.type.displayName eq 'B'}">
									<p>${thread.formParent.status} Form B - ${thread.formParent.absenceType eq 'EarlyCheckOut' ? "ECO" : thread.formParent.absenceType}. ${thread.formParent.dayAsString}, <fmt:formatDate value="${thread.formParent.start}" pattern="h:mm"/>-<fmt:formatDate value="${thread.formParent.end}" pattern="h:mm"/></p>									
								</c:if>
								<c:if test="${thread.formParent.type.displayName eq 'C'}">
									<p>${thread.formParent.status} Form C - ${thread.formParent.absenceType eq 'EarlyCheckOut' ? "ECO" : thread.formParent.absenceType}. <fmt:formatDate value="${thread.formParent.start}" pattern="M/d/yy - h:mm a"/></p>
								</c:if>
								<c:if test="${thread.formParent.type.displayName eq 'D'}">
									<p>${thread.formParent.status} Form D - <fmt:formatDate value="${thread.formParent.start}" pattern="M/d/yy"/>. ${thread.formParent.minutesWorked} mins for ${thread.formParent.emailTo}</p>
								</c:if>
							</c:if>
							<c:if test="${not empty thread.absenceParent}">
								<p>${thread.absenceParent.status} ${thread.absenceParent.type } - <fmt:formatDate value="${thread.absenceParent.start}" pattern="M/d/yy"/></p>
							</c:if>
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
								<fmt:formatDate var="fmtDate" value="${thread.messages[0].timestamp}" pattern="yyyy-M-d"/>
								<fmt:formatDate var="fmtYear" value="${thread.messages[0].timestamp}" pattern="yyyy"/>
								<c:choose>
									<c:when test="${(fmtDate eq fmtCurrentDate)}">
										<fmt:formatDate value="${thread.messages[0].timestamp}" pattern="h:mm a" />
									</c:when>
									<c:when test="${!(fmtYear eq fmtCurrentYear)}">
										<fmt:formatDate value="${thread.messages[0].timestamp}" pattern="M/d/YY" />
									</c:when>
									<c:otherwise>
										<fmt:formatDate value="${thread.messages[0].timestamp}" pattern="MMM d" />
									</c:otherwise>
								</c:choose>
								<c:if test="${empty thread.messages }">
									-
								</c:if>
							</p>
							<c:if test="${!thread.resolved}"></strong></c:if>
						</td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
