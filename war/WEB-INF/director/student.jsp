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

		<h1>Student Info for <c:out value="${user.name}" /> (<c:out value="${user.email}" />)</h1>
		
		<c:if test="${not empty error_messages}">
			<p class="notify-msg error">
				<strong>Error:</strong>
				<c:forEach items="${error_messages}" var="error_message">
					<c:out value="${error_message}" />
					<br/>
				</c:forEach>
			</p>
		</c:if>
		
		
		<h2>Forms</h2>
		<jsp:include page="/WEB-INF/common/forms.jsp" />
		
		
		<h2>Absences</h2>
		<jsp:include page="/WEB-INF/common/absences.jsp" />
		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>