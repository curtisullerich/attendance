<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Attendance for <c:out value="${user.name}" /></h1>
		
		<h2>Absences</h2>

		<jsp:include page="/WEB-INF/common/absences.jsp" />
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>