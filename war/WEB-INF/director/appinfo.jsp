<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
        <link rel="stylesheet" media="all" type="text/css" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <link rel="stylesheet" media="all" type="text/css" href="/css/jquery-ui-timepicker-addon.css" />
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
			
			<dt><label for="datetime" class="required">Performance Absence Form Submission Cutoff</label></dt>
            <dd>
              <input type="text" name="datetime" id="datetime" value='<c:out value="${datetime}" />' />
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

        <script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
        <script type="text/javascript" src="/js/jquery-ui-timepicker-addon.js"></script>
        <script>
          $('#datetime').datetimepicker({
        		timeFormat: "h:mm TT"
          });
        </script>
	</body>
</html>