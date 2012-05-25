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
				<tr><th>Last Name</th><th>First name</th><th>Section</th><th>University ID</th>
				//all events
				<c:forEach items="${events}" var="event">
					<th><fmt:formatDate value="${event}" pattern="M/dd/yyyy"/></th>
				</c:forEach>				
				<th>Grade</th>
				<c:forEach items="${student }" var="student">
				//now we need one list per student. Iterate through the list of events and grab the info about their attendance on that date.
				<tr>
				//basic info
				<td>${student.firstName }</td><td>${student.lastName }</td><td>${student.section }</td><td>${student.universityID }</td>
				//event attendances
					//this is going to be tricky in jstl....
				//grade
				<td>${student.grade }</td>
				</tr>
				</c:forEach>			
			
			
			
			
			</table>

		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>