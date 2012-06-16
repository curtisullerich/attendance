<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />
	<c:if test="${not empty success_message}">
		<p class="notify-msg success">
			<strong>Success:</strong> <c:out value="${success_message}" />
		</p>

		<c:if test="${(not empty redirect_url)}">
			<p class="notify-msg">
				Redirecting you to: <a href="<c:out value="${redirect_url}" />"><c:out value="${redirect_url}" /></a>
				<script type="text/javascript">
				$(document).ready(function() {
					redirectWithDelay(
						"<c:out value="${redirect_url}" />",
						"Redirecting...");
				});
				</script>
			</p>
		</c:if>
	</c:if>
	<c:if test="${not empty error_messages}">
		<p class="notify-msg error">
			<strong>Error:</strong>
			<c:forEach items="${error_messages}" var="error_message">
				<c:out value="${error_message}" />
				<br/>
			</c:forEach>
		</p>
	</c:if>