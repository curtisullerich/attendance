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
   	<div>
<!--    		<div> -->
<!-- 			<h1>404!</h1> -->
<!-- 		</div> -->
<!-- 		<br/> -->
		<div>
			This is not the page you're looking for.
			<br/>
			Unfortunately, we can't find it.
			<br/>
			Try double-checking the URL you entered.
			<br/>
			If you got this error by clicking on a link,
			<br/>
			contact the developers at <a href="mailto:mbattendance@iastate.edu?Subject=@10dance%20404%20Error">mbattendance@iastate.edu</a>.
		</div>
		<br/>
		<img src="/img/404Cy.png" height="250" alt="404 Cy" />
   </div>
    <jsp:include page="/WEB-INF/template/footer.jsp" />
  </body>
</html>