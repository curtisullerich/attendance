<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />

<!DOCTYPE html>

<html>
  <head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
  </head>
  <body>
  	<jsp:include page="/WEB-INF/template/header.jsp" />
	
  	
	<h1>${pagetemplate.title} <fmt:formatDate value="${date}" pattern="yyyy" /></h1>
	<h2>Registration</h2>
	
	<p>
	You will need to login using your NetID and password to access the attendance system.
	</p>
	
	<p>
	Click below to begin. After logging in you will be directed to a form to finish the registration process.
	</p>
	
	<br/>
	
	<center>
	<p>
	<a style="font-size:large" href="/auth/login">Login with NetID &amp; Register</a>
	</p>
	</center>
	
	<br/>
	<br/>
	
	<p>
	<i>Note that if you are currently logged in to a gmail account, you will first need to <a href="/auth/logout">log out of it</a>
	so that you can login with your NetID and access the attendance system.</i>
	</p>
	
	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>

</html>