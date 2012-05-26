<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
		TODO <br/>
		Link to director/user not admin/user
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Users</h1>
		
		<table class="gray">
			<tr>
				<thead>
				<th>Type</th>
				<th>Netid</th>
				<th>Name</th>
				</thead>
			</tr>
			<c:forEach items="${users}" var="user" varStatus="loopStatus">
				<tr>
					
					<td><c:out value="${user.type}" /></td>
					<td>
						<a href="/admin/user/<c:out value="${user.netID}" />">
							<c:out value="${user.netID}" />
						</a>
					</td>
					<td><c:out value="${user.name}" /></td>
					
				</tr>
			</c:forEach>
		</table>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>