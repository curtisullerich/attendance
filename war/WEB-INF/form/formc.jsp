<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />

<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
		<script>
			function goahead() {
				return true;
			}
		</script>
	</head>

	<body>
	
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Request for Excuse from Rehearsal | Form C</h1>
		
		<p>This form must be turned in no later than three weekdays following the rehearsal absence or tardy.
		Documentation is required for all absences (doctor's note, obituary, wedding program, etc.).</p>
		<!-- TODO:enforce the three-day submission policy. -->
		
		<br/>		
		<form method="post" accept-charset="utf-8">
		
			<dl class="block-layout">

				<dt ><label class="required">Date</label></dt>
				<dd>
					<input autofocus id='Month' size='5' type='number' name='Month' min='01' max='12' placeholder='MM' value='<c:out value="${empty StartMonth ? '' : StartMonth+1}" />' />
					/
					<input id='Day' size='5' type='number' name='Day' min='01' max='31' step='1' placeholder='DD' value='<c:out value="${StartDay}" />' />
					/
					<input id='Year' size='5' type='number' name='Year' min='${year}' max='${year+1}' step='1' placeholder='YYYY' value='<c:out value="${StartYear}" />' />
				</dd>
				<dt><label >Time of leaving or arriving (if not missing the full rehearsal)</label></dt>
				<dd>
					<input size='5' type='number' name='Hour' min='01' max='12' placeholder='HH' value='<c:out value="${FromHour}" />' />
					:
					<input size='5' type='number' name='Minute' min='00' max='59' step='1' placeholder='MM' value='<c:out value="${FromMinute}" />' />
					
					<select name="AMPM">
						<option>AM</option>
						<option selected>PM</option>
					</select>
				</dd>
				<dt><label for="Type" class="required">Type</label></dt>
				<dd>
					<select name="Type">
						<c:forEach items="${types}" var="s" varStatus="loop">
							<option value="<c:out value="${s.value}" />"
								${absence.type==s.value ? 'selected="true"' : ''}
								><c:out value="${s.displayName}" /></option>
						</c:forEach>
					</select>
				</dd>
								
				<dt><label class='required'>Reasons</label></dt>
				<dd>
					<textarea rows="6" cols="50" id="reason" name="Reason" wrap="physical"><c:out value="${Reason}" /></textarea>
					<br/>
					Please be specific and be thorough.
				</dd>
				
			</dl>
			
			<input type="submit" value="Save Info" name="SaveInfo" onclick="return goahead()"/>
		</form>		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>