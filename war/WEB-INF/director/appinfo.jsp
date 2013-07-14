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
	
		<h1>Application Settings for: <c:out value="${appinfo.title}" /></h1>		
		
		<%//Put the form down here so the MobilePassword wasn't getting posted in the form %>
		<form method="post" accept-charset="utf-8">	
		<dl class="block-layout">
<!-- 			<dt><label for="Timezone" class="required">Timezone</label></dt> -->
<!-- 			<dd> -->
<!-- 				<select name="Timezone"> -->
<%-- 					<option selected>${timezone }</option> --%>
				
<!-- 				</select> -->
<!-- 			</dd> -->

			<dt><label class="required" for="Title">Title</label></dt>
			<dd><input type="text" name="Title" value="<c:out value="${appinfo.title}" />" /></dd>
			
			<dt><label for="CronExportEnabled">Backups:</label></dt>
			<dd><label>Daily data backups enabled: <input type="checkbox" name="CronExportEnabled" <c:if test="${appinfo.cronExportEnabled}">checked="checked"</c:if> align="right"></label></dd>
			
			<dt><label for="Month" class="required">Form A Submission Cutoff</label></dt>
			
			<dd>
				<input size='5' type='number' name='Month' min='01' max='12' placeholder='MM' value='<c:out value="${Month}" />' />
				/
				<input size='5' type='number' name='Day' min='01' max='31' step='1' placeholder='DD' value='<c:out value="${Day}" />' />
				/
				${year}
				<br/>at<br/>
				<input size='5' type='number' name='ToHour' min='01' max='12' placeholder='HH' value='<c:out value="${ToHour}" />' />
				:
				<input size='5' type='number' name='ToMinute' min='00' max='59' step='1' placeholder='MM' value='<c:out value="${ToMinute}" />' />
				
				<select name="ToAMPM">
					<option ${ToAMPM eq 'AM' ? 'selected' : ''}>AM</option>
					<option ${ToAMPM eq 'AM' ? '' : 'selected'}>PM</option>
				</select>
				<br/>
				Please enter the <b>last</b> acceptable day/time for submitting Performance Absence Requests.
			</dd>
			
 			<dt><label for="Timezone">Timezone</label></dt>
			<dd>
				<select name="Timezone">
					<c:forEach items="${timezones}" var="zone">
						<option> </option>
						<option ${zone.ID} ${timezone.ID eq zone.ID ? 'selected' : ''}>${zone.ID}</option>
					</c:forEach>
				</select>
			</dd>			

			<dt><label for="StatusMessage">Status Message</label></dt>
			<dd>
				<textarea rows="12" cols="60" name="StatusMessage">${StatusMessage }</textarea>
			</dd>
		</dl>
		<input type="submit" value="Save Info" name="SaveInfo" onclick="preprocessInfo();"/>
		<input id='Year' size='5' type='hidden' name='Year' value='<c:out value="${year}" />' />
		<input type="hidden" id="hiddenEmails" name="hiddenEmails" value="">
		<input type="hidden" id="hashedPass" name="hashedPass" value="">
		<input type="hidden" id="hashedPassConf" name = "hashedPassConf" value ="">
		</form>		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>