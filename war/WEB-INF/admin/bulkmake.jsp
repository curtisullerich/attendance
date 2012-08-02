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
	<h2>Create all the Students</h2>
	
	<form method="post" accept-charset="utf-8">
		<dl class="block-layout">
			<dt><label class="required" for="Data">All the Data</label></dt>
			<b>Format:</b> first name, last name, primary email, secondary email, type, section, uid, year, major, rank, minutesavailable
			<dd>
				<textarea rows="20" cols="80" name="Data" wrap="physical"></textarea>
			</dd>
		</dl>
		<input type="submit" value="Go" name="Go" />
	</form>
	<form method="post" accept-charset="utf-8">
	Delete ALL the things!
	<input  type="submit" value="Delete All" name="DeleteAll">
	
	</form>
	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>
</html>