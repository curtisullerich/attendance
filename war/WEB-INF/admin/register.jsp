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
	
  	
	<h1><c:out value="${pagetemplate.siteTitle}" /> <fmt:formatDate value="${date}" pattern="yyyy" /></h1>
	<h2>Register a director</h2>
	
	<form method="post" accept-charset="utf-8">
		<dl class="block-layout">
			<dt><label></label></dt>
				
			<dt><label class="required" for="NetID">School Email</label></dt>
			<dd>
				<input type="text" name="NetID" value="<c:out value="${NetID}" />"/>
			</dd>
			
			<dt><label class="required" for="FirstName">First Name</label></dt>
			<dd><input type="text" name="FirstName" value="<c:out value="${FirstName}" />" /></dd>
			
			<dt><label class="required" for="LastName">Last Name</label></dt>
			<dd><input type="text" name="LastName" value="<c:out value="${LastName}" />" /></dd>
			
			<dt><label for="SecondEmail">Login Email</label></dt>
			<dd>
				<input type="email" name="SecondEmail" value="<c:out value="${SecondEmail}" />" />
				<br/>
				Enter the Google email (yourname@gmail.com) you wish to use to login to the application.
			</dd>
		</dl>

		<input type="submit" value="Register" name="Register" />
		<input type="button" value="Back" name="Back" onclick="window.location='/admin'"/>
	</form>
	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>

</html>