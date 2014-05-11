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
		
		<h1>Absence info for ${absence.student.name} (${absence.student.netID})</h1>

		<dl class="block-layout">
			<dt>Type</dt>
			<dd>
				${absence.type.displayName}
			</dd>
			
			<dt>Status</dt>
			<dd>
				${absence.status.displayName}
			</dd>

			<c:if test='${not absence.type.absence }'>
				<c:if test='${absence.type.tardy }'>
                  <dt>Check in time</dt>
                </c:if>
                <c:if test='${absence.type.earlyCheckOut }'>
                  <dt>Check out time</dt>
                </c:if>
				<dd><fmt:formatDate value="${absence.start}" pattern="M/d/yyyy 'at' h:mm a"/></dd>
			</c:if>

			<dt>Event</dt>
			<dd>
				Start Time: <fmt:formatDate value="${absence.event.start}" pattern="M/d/yyyy 'at' h:mm a"/>
			</dd>
			<dd>
				End Time: <fmt:formatDate value="${absence.event.end}" pattern="M/d/yyyy 'at' h:mm a"/>
			</dd>		
		</dl>
		<jsp:include page="/WEB-INF/template/footer.jsp" />	
	</body>
</html>	