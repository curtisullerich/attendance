<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />
<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
		<script type="text/javascript" src="/js/sha.js"></script>
	
		<script type="text/javascript">
		function addTimeWorkedEmail() {
			var email = $('#TimeWorkedEmail').val();
			if(isValidEmailAddress(email))
				$('#TimeWorkedEmails').append('<option>'+email+'</option>');
			else
				alert("Invalid email to add");
		}
		function deleteTimeWorkedEmails() {
			var selected = $("#TimeWorkedEmails option:selected");
			selected.map(function(){
		      $(this).remove();
		    });
		}
		
		function preprocessInfo(){
			var opts = document.getElementById('TimeWorkedEmails').options;
			var emails = "";
			for (var i = 0; i < opts.length; i++)
			{
				emails += opts[i].value + "delimit";
			}
			$('input[name=hiddenEmails]').val(emails);
			var pass = $('#MobilePassword').val();
			if (pass != "")
				$('input[name=hashedPass]').val(Sha1.hash(pass, true));		
			var passConf = $('#MobilePasswordConf').val();
			if (passConf != "")
				$('input[name=hashedPassConf]').val(Sha1.hash(passConf, true));
		}
		</script>
	</head>

	<body>		
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Application Settings for: <c:out value="${appinfo.title}" /></h1>
		
		<c:if test="${not empty error_messages}">
			<p class="notify-msg error">
				<strong>Error:</strong>
				<c:forEach items="${error_messages}" var="error_message">
					<c:out value="${error_message}" />
					<br/>
				</c:forEach>
			</p>
		</c:if>
		
		
		<dl class="block-layout">
			
			<dt><label class="required" for="Title">Title</label></dt>
			<dd><input type="text" name="Title" value="<c:out value="${appinfo.title}" />" /></dd>
			
			<dt><label for="MobilePassword">New Mobile App Password</label></dt>
			<dd>
				<input type="password" id="MobilePassword" name="MobilePassword" />
				<br/>
				Leave blank to not change the password.
			</dd>
			<dt><label for="MobilPasswordConfirm">Confirm New Mobile App Password</label></dt>
			<dd>
				<input type="password" id="MobilePasswordConf" name = "MobilePasswordConf" />
				<br/>
			</dd>	
		</d1>
		
		<%//Put the form down here so the MobilePassword wasn't getting posted in the form %>
		<form action="./appinfo" method="post" accept-charset="utf-8">	
		<dl class="block-layout">
			<dt><label for="Month" class="required">Form A Submission Cutoff:</label></dt>
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
			
			<dt><label for="TimeWorkedEmails">Valid Time Worked Emails for Form D</label></dt>
			<dd>
				<select id="TimeWorkedEmails" name="TimeWorkedEmails" size="10">
<%-- 					<c:forEach items="${daysOfWeek}" var="day"> --%>
<%-- 						<option ${DayOfWeek eq day ? 'selected' : ''}>${day}</option> --%>
<%-- 					</c:forEach> --%>
					<c:forEach items="${emails}" var="email">
						<option ${email }>${email}</option>
					</c:forEach>
				</select>
				<input type="button" value="Remove Selected" name="Back" onclick="deleteTimeWorkedEmails()"/>
				<br/>
				<input type="email" id="TimeWorkedEmail" />
				<input type="button" value="Add Email" onclick="addTimeWorkedEmail()"/>
				<br/>
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