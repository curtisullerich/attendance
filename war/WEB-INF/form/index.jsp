<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />
 
<jsp:useBean id="date" class="java.util.Date" />

<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Forms</h1>
		
		<c:if test="${auth.user.type.student or auth.user.type.ta}">
			<p>
				Create a new form:
				<a href="/student/forms/forma" title="Performance Absence Request">Form A</a>
				| <a href="/student/forms/formb" title="Class Conflict Request">Form B</a>
				| <a href="/student/forms/formd" title="Time Worked">Form D</a>
			</p>
		</c:if>
		
		<jsp:include page="/WEB-INF/common/forms.jsp" />
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>