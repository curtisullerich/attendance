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
			<h2>Resolved.</h2>
			<%//the button that allows the director to mark a message as unresolved or resolved. ONLY VISIBLE TO DIRECTORS. %>
			<c:if test="${auth.user.type.director}">
				<form>
					<input type="hidden" value="<c:out value="${thread.id}" />" name="id"/>
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
					<input type="hidden" value="<c:out value="${thread.id}" />" name="id"/>
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
		
		<div>
			<c:forEach items="${thread.messages}" var="message">
				<p>
					At <fmt:formatDate value="${message.timestamp}" pattern="M/dd/yyyy" /> 
					<strong><c:out value="${message.author.name}" /> (<c:out value="${message.author.netID}" />)</strong>
					said:
					<c:out value="${message.text}" />
					<br/>
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
			
			<input type="hidden" value="<c:out value="${thread.id}" />" name="id"/>
			
			<input type="submit" value="Add Message" name="Submit"/>
		
		</form>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>