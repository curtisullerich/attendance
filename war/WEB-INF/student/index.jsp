<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>


	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Student Page</h1>
		<p>
			Current grade: <c:out value="${user.grade.displayName}" /><br/><br/>

			Use the navigation options on the left to manage your attendance.
		</p>
		<jsp:include page="/WEB-INF/common/status.jsp"/>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>