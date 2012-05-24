<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
	<jsp:include page="/WEB-INF/template/head.jsp" />
</head>
<body>
	<jsp:include page="/WEB-INF/template/header.jsp" />

	<h1>Edit your information</h1>
	<form action="/director/info" method="post" accept-charset="utf-8">

		<dl class="block-layout">
			<dt><label class="required" for="FirstName">First Name</label></dt>
			<dd><input type="text" name="FirstName" value="<c:out value="${user.firstName}" />" /></dd>
			
			<dt><label class="required" for="LastName">Last Name</label></dt>
			<dd><input type="text" name="LastName" value="<c:out value="${user.lastName}" />" /></dd>
			
			<dt><label for="NetID">NetID</label></dt>
			<dd>
				<input type="text" name="NetID" value="<c:out value="${user.netID}"/>" disabled readonly/>
			</dd>
			
			<dt><label for="UniversityID">University ID</label></dt>
			<dd><input type="text" name="UniversityID" value="<c:out value="${user.universityID}" />" disabled readonly/></dd>
			
		</dl>

		<input type="submit" value="Save Info" name="SaveInfo"/>
		<input type="button" value="Back" name="Back" onclick="window.location = '/director'"/>
	</form>		
	
	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>
</html>