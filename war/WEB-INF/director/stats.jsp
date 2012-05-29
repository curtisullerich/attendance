<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		
		<h1>${pagetemplate.title}</h1>
		
		<b>Current as of
			<fmt:setTimeZone value="America/Chicago" />
			<fmt:formatDate value="${date}" pattern="h:mm a" /> 
			<fmt:formatDate value="${date}" pattern="M/dd/yyyy" />
		</b>
		<br/>
		<br/>
		
		<table class="gray"><!-- style="font-size:small" cellspacing=10 width=450-->
			<tr>
				<th>Statistics</th>
				<th></th>
			</tr>
			<tr>
				<td>
					Number of students
				</td>
				<td>
					<c:out value="${numStudents}" />
				</td>
			</tr>
			<tr>
				<td>
					Number of events
				</td>
				<td>
					<c:out value="${numEvents}" />
				</td>
			</tr>
			<tr>
				<td>
					Average present students (including those tardy and leaving early)
				</td>
				<td>
					<c:out value="${avgPresentStudents}" />
				</td>
			</tr>
			<tr>
				<td>
					Average students present for whole rehearsal: 
				</td>
				<td>
					<c:out value="${avgPresentStudentsWR}" />
				</td>
			</tr>
			<tr>
				<td>
					Average tardy students
				</td>
				<td>
					<c:out value="${avgTardyStudents}" />
				</td>
			</tr>
			<tr>
				<td>
					Average students who leave early
				</td>
				<td>
					<c:out value="${avgLeaveEarly}" />
				</td>
			</tr>
			<tr>
				<td>
					Average absent students
				</td>
				<td>
					<c:out value="${avgAbsentStudents}" />
				</td>
			</tr>
<!-- 			<tr> -->
<!-- 				<td> -->
<!-- 					Average grade -->
<!-- 				</td> -->
<!-- 				<td> -->
<%-- 					<c:out value="${avgGrade}" /> --%>
<!-- 				</td> -->
<!-- 			</tr> -->
		</table>
		
		<br/>
		Please be aware that these statistics will be made inaccurate by un-anchored items.<br/>
		Would other statistics be useful? <script>document.write('<a href="mailto:'+ ["mbattendance", "iastate.edu"].join('@') +'">'+ ["Let", "know"].join(' us ') +'</a>')</script><noscript>mbattendance (at) iastate (dot) edu</noscript>.
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>