<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Message Thread</h1>
		
		<c:if test="${thread.resolved}">
			<strong>Resolved.</strong>
			<br/>
		</c:if>
		
		<c:if test="${!thread.resolved}">
			<strong>Not Resolved.</strong>
			<br/>
		</c:if>
		
		<c:if test="${empty thread.messages}">
			<strong>No messages yet.</strong>
			<br/>
		</c:if>
		
		<div>
			<c:forEach items="${thread.messages}" var="message">
				<p>
					<c:out value="${message.text}" />
					<br/>
					<strong><c:out value="${message.author.name}" /> (<c:out value="${message.author.netID}" />)</strong>	
				</p>
			</c:forEach>
		</div>
		
		<form method="post" accept-charset="utf-8">
		
			<dl class="block-layout">
				
				<dt><label>Message:</label></dt>
				<dd>
					<textarea rows="6" cols="50" name="Message" wrap="physical"></textarea>
				</dd>
				
			</dl>
			
			<input type="hidden" value="<c:out value="${thread.id}" />" name="Id"/>
			
			<input type="submit" value="Add Message" name="Submit"/>
		
		</form>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>