<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
TODO<br/>
columns: last name, first name, section, university ID, events, grade<br/>
    can click on name to view student's page<br/>
    can click on any absence to change anything about it (including deleting it)<br/>
    can click on any event at the top to view stats about it<br/>
    link to stats about the whole semester<br/>
    export grades button
		<jsp:include page="/WEB-INF/template/header.jsp" />


		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>