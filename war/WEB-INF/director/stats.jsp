<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
	
		TODO<br/>
		implement reporting of average grades once grades are implemented
	
		<jsp:include page="/WEB-INF/template/header.jsp" />
		
		<h1>${pagetemplate.title}</h1>
		
		<b>Current as of
			<fmt:setTimeZone value="America/Chicago" />
			<fmt:formatDate value="${date}" pattern="h:mm a" /> 
			<fmt:formatDate value="${date}" pattern="M/dd/yyyy" />
		</b>
		<br/>
		<br/>
		
		<table style="font-size:small" cellspacing=10 width=450>
			<tr>
				<td>
					Number of Students: 
				</td>
				<td>
					<c:out value="${numStudents}" />
				</td>
			</tr>
			<tr>
				<td>
					Number of Events: 
				</td>
				<td>
					<c:out value="${numEvents}" />
				</td>
			</tr>
			<tr>
				<td>
					Average Present Students (including those tardy and leaving early): 
				</td>
				<td>
					<c:out value="${avgPresentStudents}" />
				</td>
			</tr>
			<tr>
				<td>
					Average Students Present for Whole Rehearsal: 
				</td>
				<td>
					<c:out value="${avgPresentStudentsWR}" />
				</td>
			</tr>
			<tr>
				<td>
					Average Tardy Students: 
				</td>
				<td>
					<c:out value="${avgTardyStudents}" />
				</td>
			</tr>
			<tr>
				<td>
					Average Students Who Leave Early: 
				</td>
				<td>
					<c:out value="${avgLeaveEarly}" />
				</td>
			</tr>
			<tr>
				<td>
					Average Absent Students: 
				</td>
				<td>
					<c:out value="${avgAbsentStudents}" />
				</td>
			</tr>
			<tr>
				<td>
				</td>
				<td>
				</td>
			</tr>
			<tr>
				<td>
					Average Grade: 
				</td>
				<td>
					<!-- <c:out value="${avgGrade}" /> -->
					F--
				</td>
			</tr>
		</table>
		
		<br/>
		Please be aware that these statistics will be made inaccurate by Un-Anchored items.
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>