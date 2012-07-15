<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Administrative Data Migration Copy</h1>
		
		<br/>
		
		<p>
			Proposing copying all data from version: <strong>v<c:out value="${version}" /></strong> <br/>
			into the current version (v<c:out value="${ObjectDatastoreVersion}" />).
		</p>
		
		<p>
			Note that copying all data could result in duplicates or other very broken things, please be sure before you do this.
		</p>
		
		<br/>
		
		<p>
			Is this okay?
		</p>
		
		<form action="data_migrate" method="POST">
			<input type="submit" value="Confirm Migration" title="Copy this data to current application version" />
			<input type="hidden" name="version" value="<c:out value="${version}" />"/>
		</form>
		
		<br/>
		
		<p>
			<a href="data">Cancel</a>
		</p>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>