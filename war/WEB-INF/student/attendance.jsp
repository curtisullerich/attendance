<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
	
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Attendance for <c:out value="${user.netID}" /></h1>
		
		<h2>Absences</h2>
		<table>
			<tr>
				<th>Type</th>
				<th>Event</th>
			</tr>
			<c:forEach items="${absences}" var="absence">
				<tr>
					<td>${absence.type}</td>
					<c:if test=${empty absence.event}">
						No event. Absence from:
						<td>
							<fmt:formatDate value="${absence.start}" pattern="mm/dd/yyyy hh:mm" />
							-
							<fmt:formatDate value="${absence.end}" pattern="hh:mm" />
						</td>
					</c:if>
					<c:if test=${not empty absence.event}">
						<td>
							<fmt:formatDate value="${absence.event.start}" pattern="mm/dd/yyyy hh:mm" />
							-
							<fmt:formatDate value="${absence.event.end}" pattern="hh:mm" />
						</td>
					</c:if>
					
				</tr>
			</c:forEach>
		</table>
		
		
		<h2>Forms</h2>
		<table>
			<tr>
				<th>Start Date</th>
				<th>End Date</th>
				<th>Type</th>
				<th>Status</th>
			</tr>
			<c:forEach items="${forms}" var="form">
				<tr>
					<td><fmt:formatDate value="${form.start}" pattern="mm/dd/yyyy" /></td>
					<td><fmt:formatDate value="${form.end}" pattern="mm/dd/yyyy" /></td>
					<td>${form.type}</td>
					<td>${form.status}</td>
					<td>
						<button onClick="window.location='./messages?id=${form.id}'">messages</button>
					</td>
				</tr>
			</c:forEach>
		</table>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>