<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>${pagetemplate.title}</h1>
		<br/>
		<jsp:include page="/WEB-INF/common/absences.jsp" />
			
			<table>
				<!-- start headers -->
				<tr><th>Last Name</th><th>First name</th><th>Section</th><th>University ID</th>
					<!-- all events. TODO will need to link this -->
					<c:forEach items="${events}" var="event">
						<th><fmt:formatDate value="${event}" pattern="M/dd/yyyy"/></th>
					</c:forEach>				
				<th>Grade</th>
				<!-- headers are now done -->
				<%System.out.println("in jsp"); %>
				<c:forEach items="${students }" var="student">
					//now we need one list per student. Iterate through the list of events and grab the info about their attendance on that date.
					<%System.out.println("inside loop a"); %>
					<tr>
						//basic info
						<td>${student.firstName }</td><td>${student.lastName }</td><td>${student.section }</td><td>${student.universityID }</td>
						//event attendances
						<td>
							<c:forEach items="${absenceMap[student][event] }" var="absence">
								<%System.out.println("inside loop b"); %>
								<c:choose>
									<c:when test="${absence.type.tardy}">
										${absence.status } ${absence.type }: ${absence.datetime }<br/>
									</c:when>
									<c:when test="${absence.type.absence}">
										${absence.status } ${absence.type } <br/>
									</c:when>
									<c:when test="${absence.type.earlyCheckOut}">
										${absence.status } ${absence.type }: ${absence.datetime }<br/>
									</c:when>
								</c:choose>
							</c:forEach>
						</td>
						//grade
						<td>${student.grade }</td>
					</tr>
				</c:forEach>			
			
			
			
			
			</table>

		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>