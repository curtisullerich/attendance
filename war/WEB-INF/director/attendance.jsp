<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
<head>
<jsp:include page="/WEB-INF/template/head.jsp" />
</head>
<body>
	<jsp:include page="/WEB-INF/template/header.jsp" />

	<h1>${pagetemplate.title}</h1>
	<br />
		<c:if test="${not empty error_messages}">
			<p class="notify-msg error">
				<strong>Error:</strong>
				<c:forEach items="${error_messages}" var="error_message">
					<c:out value="${error_message}" />
					<br/>
				</c:forEach>
			</p>
		</c:if>

	<table class="gray full-width">
		<!-- start headers -->
		<thead>
			<tr>
				<th>Last Name</th>
				<th>First name</th>
				<th>Section</th>
				<th>University ID</th>
				<!-- all events. TODO will need to link this -->
				<c:forEach items="${events}" var="event">
					<th><c:out value="${event.type}" /><br />
					<fmt:formatDate value="${event.date}" pattern="M/dd/yyyy" /></th>
				</c:forEach>
				<th>Grade</th>
				<!-- headers are now done -->
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${students }" var="student">
				<tr>
					<td>${student.firstName }</td>
					<td>${student.lastName }</td>
					<td>${student.section }</td>
					<td>${student.universityID }</td>
					<c:forEach items="${events}" var="event">
						<td><c:forEach items="${absenceMap[student][event] }"
								var="absence">
								<c:choose>
									<c:when test="${absence.type.tardy}">
										<a href="/director/viewabsence?absenceid=${absence.id }">${absence.status
											} ${absence.type }<!-- : ${absence.datetime }--></a>
										<br />
									</c:when>
									<c:when test="${absence.type.absence}">
										<a href="/director/viewabsence?absenceid=${absence.id }">${absence.status
											} ${absence.type } </a>
										<br />
									</c:when>
									<c:when test="${absence.type.earlyCheckOut}">
										<a href="/director/viewabsence?absenceid=${absence.id }">${absence.status
											} ${absence.type }<!-- : ${absence.datetime }--></a>
										<br />
									</c:when>
								</c:choose>
							</c:forEach></td>
					</c:forEach>
					<td>${student.grade.displayName }</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>
</html>