<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Message Inbox</h1>

		<div>
			<table>
				<td>
					<th></th>
					<th>Participants</th>
					<th>About</th>
					<th></th>
				</td>
					<c:forEach items="${threads}" var="thread">
						<c:if test="${!thread.resolved }"><b></c:if>
						<td>
							<tr><fmt:formatDate value="${thread.mostRecent.timestamp}" pattern="yyyy/MM/dd 'at' HH:mm:ss"/></tr>
							<tr>
							<c:forEach items="${thread.participants } var="participant">
								< c:out value="${participant.name }"/>  
							</c:forEach>
							</tr>
							<tr>
								<!-- c:out value="${//TODO need to insert info about the event here }"/-->
							</tr>
							<tr><a href="/messages/viewthread?id=<c:out value="${thread.id}" />"><c:out value="${message.text}" /></a></tr>
						</td>
						<c:if test="${!thread.resolved }"></b></c:if>
					</c:forEach>
			</table>
		</div>
		
		
		<c:if test="${thread.resolved}">
			<h2>Resolved.</h2>
			<%//the button that allows the director to mark a message as unresolved or resolved. ONLY VISIBLE TO DIRECTORS. %>
			<c:if test="${auth.user.type.director}">
				<form>
					<input type="text" hidden="true" value="true" name="resolved"/>
					<input type="submit" value="Un-resolve issue" name='resolveB'/>
				</form>
			</c:if>
			<br/>
		</c:if>
		
		<c:if test="${!thread.resolved}">
			<h2>Not Resolved.</h2>
			<c:if test="${auth.user.type.director}">
				<form>
					<input type="text" hidden="true" value="false" name="resolved"/>
					<input type="submit" value="Resolve issue" name='resolveB'/>
				</form>
			</c:if>
			<br/>
		</c:if>
		
		<c:if test="${empty thread.messages}">
			<strong>No messages yet.</strong>
			<br/>
		</c:if>
		

		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>