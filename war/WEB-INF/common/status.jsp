<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />
<c:if test="${ not empty StatusMessage}">		
	<br/>
	<div style="border:2px solid black; word-wrap: break-word">
		<div style="border:10px solid white;">
			<h3>Status Message</h3>
			<p>
			${StatusMessage }
			</p>
		</div>
	</div>
</c:if>