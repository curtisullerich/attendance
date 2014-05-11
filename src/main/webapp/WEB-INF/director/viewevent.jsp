<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		
		<h1>Event</h1>
			<dl class="block-layout">
			<dt><label></label></dt>

				<dt><label for="StartTime">Started at</label></dt>
				<dd>
					<fmt:formatDate value="${event.start}" pattern="h:mm a 'on' M/d/yyyy"/>
				</dd>

				<dt><label for="EndTime">Ended at</label></dt>
				<dd>
					<fmt:formatDate value="${event.end}" pattern="h:mm a 'on' M/d/yyyy"/>
				</dd>
				<dt><label for="Type">Type</label></dt>
				<dd>
					${event.type}
				</dd>
				<dt>${absent } student${(absent eq 1) ? ' was':'s were' } absent</dt>
				<dt>${tardy } student${(tardy eq 1) ? ' was':'s were' } tardy</dt>
				<dt>${earlyout } student${(earlyout eq 1) ? '':'s' } checked out early</dt>
				
			</dl>
	<form method="post" action="/director/deleteevent" accept-charset="utf-8">
		<input type="hidden" value="${event.id }" name="id"/>
		<input type="submit" value="Delete this event" onClick="return confirm('Really delete?')" />
		Also remove all absences linked to this event?<input type="checkbox" name="RemoveAnchored" value="true"/>
	</form>
	<br/>
	
		<jsp:include page="/WEB-INF/template/footer.jsp" />	
	</body>
</html>	