<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />

<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
	
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Request for Excuse from Rehearsal | Form C</h1>
		
		<p>This form must be turned in no later than three weekdays following the rehearsal absence or tardy.
		Documentation is required for all absences (doctor's note, obituary, wedding program, etc.).</p>
		<!-- TODO:enforce the three-day submission policy. -->
		
		<br/>
		
		<c:if test="${not empty error_messages}">
			<p class="notify-msg error">
				<strong>Error:</strong>
				<c:forEach items="${error_messages}" var="error_message">
					<c:out value="${error_message}" />
					<br/>
				</c:forEach>
			</p>
		</c:if>
		
		<form action="./formc" method="post" accept-charset="utf-8">
		
			<dl class="block-layout">

				<dt><label>Date:</label></dt>
				<dd>
					<input autofocus id='startMonth' size='5' type='number' name='StartMonth' min='01' max='12' placeholder='MM' value='<c:out value="${empty StartMonth ? '' : StartMonth+1}" />' />
					/
					<input id='startDay' size='5' type='number' name='StartDay' min='01' max='31' step='1' placeholder='DD' value='<c:out value="${StartDay}" />' />
					/
					<input id='startYear' size='5' type='number' name='StartYear' min='${year}' max='${year+1}' step='1' placeholder='YYYY' value='<c:out value="${StartYear}" />' />
				</dd>
				
				<dt><label>Reasons:</label></dt>
				<dd>
					<textarea rows="6" cols="50" id="reason" name="Reason" wrap="physical"><c:out value="${Reason}" /></textarea>
					<br/>
					Please be specific and be thorough.
				</dd>
				
			</dl>
			
			<input type="submit" value="Save Info" name="SaveInfo"/>
		</form>		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>