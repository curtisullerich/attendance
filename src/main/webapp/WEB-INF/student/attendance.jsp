<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Attendance for <c:out value="${user.name}" /></h1>
		
		<c:if test="${fn:length(absences) > 0}">
			<h2>Absences</h2>
		</c:if>

		<jsp:include page="/WEB-INF/common/absences.jsp" />
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>