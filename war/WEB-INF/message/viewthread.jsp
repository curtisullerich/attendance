<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		
		<h1>Messages for <c:out value="${user.name}" /></h1>
		
		<%//the button that allows the director to mark a message as unresolved or resolved. ONLY VISIBLE TO DIRECTORS. %>
		<c:if test="${thread.resolved==true}">
			<h2>Resolved</h2>

			<c:if test="${auth.user.type.director}">
				<form>
					<input type="text" hidden="true" value="true" name="resolved"/>
					<input type="submit" value="Un-resolve issue" name='resolveB'/>
				</form>
			</c:if>
		</c:if>
		
		<c:if test="${thread.resolved==false}">
			<h2>Not Resolved</h2>

			<c:if test="${auth.user.type.director}">
				<form>
					<input type="text" hidden="true" value="false" name="resolved"/>
					<input type="submit" value="Resolve issue" name='resolve'/>
				</form>
			</c:if>
		</c:if>
		
		<table border='1'>
			<tr>
				<td>Write a new message:</td>
				<td><form action='/message' method='post'><input name='text' id='text'/><button name='submit' type='submit'></button></form></td>
			</tr>
			<c:forEach items="${thread.messages}" var="message">
				<tr>
					<td><fmt:formatDate value="${message.timestamp}" pattern="M/dd/yyyy" />
					<c:out value="${message.author.name}"/></td>
					<td><c:out value="${message.text}"/></td>
				</tr>
			</c:forEach>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
		</table>
	</body>
</html>
