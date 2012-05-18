<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>


	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
TODO: link to messaging center (which needs to be implemented)<br/>
new message indication<br/>
current grade<br/>

if this student is staff: set ranks link
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Student Page</h1>
		<p>
			Use the navigation options on the left to manage your attendance.
		</p>
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>