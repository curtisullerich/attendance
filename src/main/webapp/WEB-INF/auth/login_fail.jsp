<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />


<html>
  <head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
  </head>
  <body>
  	<jsp:include page="/WEB-INF/template/header.jsp" />
    <h1>ISU Varsity Marching Band / Fall <fmt:formatDate value="${date}" pattern="yyyy" /></h1>
    <h2>Login Failed</h2>
	
	You must login as an iastate.edu email
    
    <jsp:include page="/WEB-INF/template/footer.jsp" />
  </body>
</html>