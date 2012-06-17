<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />
<html>
  <head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
	<script src="/js/common.js"></script>
  </head>
  <body>
  	<jsp:include page="/WEB-INF/template/header.jsp" />
  	
  	<h1>Unauthorized</h1>
  	
  	<p>You are not authorized to view this page.</p>
  	
   	<div>

		<div>
			Try double-checking the URL you entered.
			<br/>
			If you got this error by clicking on a link,
			<br/>
			contact the developers at <a href="mailto:mbattendance@iastate.edu?Subject=@10dance%20404%20Error">mbattendance@iastate.edu</a>.
		</div>
		<br/><br/>
		<img src="/img/404Cy.png" height="250" alt="404 Cy" />
   </div>
    <jsp:include page="/WEB-INF/template/footer.jsp" />
  </body>
</html>