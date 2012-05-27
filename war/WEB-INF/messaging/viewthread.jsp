<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="fmtCurrentDate" value="${now}" pattern="yyyy-MM-dd"/>
<fmt:formatDate var="fmtCurrentYear" value="${now}" pattern="yyyy"/>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Message Thread</h1>
		
		<br/>
		
		<div class="gutter" style="padding:80px;padding-top:0px;">
			<c:choose>
				<c:when test="${auth.user.type.director}">
					<div class="threadresolved">
						<form id="threadresolveform" method="post" accept-charset="utf-8">
							<input id="resolved_radio" name="resolved" value="false" type="radio" onclick="$('#threadresolveform').submit();" ${thread.resolved?'checked="checked"':''} />
							<label for="resolved_radio">Resolved</label>
							
							<input id="unresolved_radio" name="resolved" value="true" type="radio" onclick="$('#threadresolveform').submit();" ${thread.resolved?'':'checked="checked"'} />
							<label for="unresolved_radio">Unresolved</label>
							
							<input type="hidden" value="<c:out value="${thread.id}" />" name="id"/>
							<input type="hidden" value="submit" name="resolveB"/>
						</form>
					</div>
					<br/>
				</c:when>
				<c:when test="${thread.resolved}">
					<p style="font-size:large;"><i>Resolved.</i></p>
				</c:when>
				<c:when test="${!thread.resolved}">
					<p style="font-size:large;"><i>Not Resolved.</i></p>
				</c:when>
			</c:choose>
		
		
		<br/>
		
		<div class="grid-full">
			<c:if test="${!(empty thread.messages)}">
				<div id="newmessagepreview" class="newmessagepreview" onclick="$('#newmessageform').show('fast');$('#newmessagepreview').hide('fast');">
					Click here to send a reply
					<c:if test="${thread.resolved}">
						and <strong>un-resolve</strong> the thread
					</c:if>
				</div>
				<br/>
			</c:if>
		
			<form id="newmessageform" method="post" accept-charset="utf-8" style="display:${(empty thread.messages)?'block':'none'};">
			
				<dl class="block-layout">
					
					<dt><label>New message:</label></dt>
					<dd>
						<textarea rows="6" cols="50" name="Message" wrap="physical"></textarea>
					</dd>
					
				</dl>
				
				<input type="hidden" value="<c:out value="${thread.id}" />" name="id"/>
				
				<input type="submit" value="Add Message" name="Submit"/>
			
			</form>
		
		</div>
		
		<br/>
		<br/>
		
		<c:if test="${empty thread.messages}">
			<strong>No messages yet.</strong>
			<br/>
		</c:if>
			<c:forEach items="${thread.messages}" var="message">
				<div class="grid-9 ${(auth.user eq message.author)?'message-mine':'message-theirs'}">
				<p>
					<strong style="font-size:104%"><c:out value="${message.author.name}" /></strong>
					<br/>
					<c:out value="${message.text}" />
					<br/>
					<span style="float:right;color:#aaa;">
					<fmt:formatDate var="fmtDate" value="${message.timestamp}" pattern="yyyy-MM-dd"/>
					<fmt:formatDate var="fmtYear" value="${message.timestamp}" pattern="yyyy"/>
					<c:choose>
						<c:when test="${(fmtDate eq fmtCurrentDate)}">
							<fmt:formatDate value="${message.timestamp}" pattern="h:mm a" />
						</c:when>
						<c:when test="${!(fmtYear eq fmtCurrentYear)}">
							<fmt:formatDate value="${message.timestamp}" pattern="MM/dd/YY" />
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${message.timestamp}" pattern="MMM d" />
						</c:otherwise>
					</c:choose>
					</span>
				</p>
				</div>
			</c:forEach>
		</div>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>