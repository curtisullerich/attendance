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
	
		<h1>Time Worked | Form D</h1>
				
		<form method="post" accept-charset="utf-8">
		
			<dl class="block-layout">
	
				<dt><label class="required">Who needs to verify?</label></dt>
				<label>If the person for whom your worked off time is not listed, put his or her contact<br/>
				 information in the details box below and select the director here.</label>
				<dd>
					<select name="Email" autofocus>
						<option value="">(Select)</option>
						<c:forEach items="${verifiers}" var="verifier">
							<option><c:out value="${verifier}" /></option>
						</c:forEach>
					</select>
				</dd>
				
				<dt><label for="AmountWorked" class="required">Total amount of work</label></dt>
				<label>If you worked on more than one day, just select one of them here and make note of it below.</label>
				<dd>
					<input size='5' type='number' name='AmountWorked' value='<c:out value="${AmountWorked}" />' style="width:64pt" />
					<label for="AmountWorked">Minutes</label>
				</dd>
				
				<dt><label for="StartMonth" class="required">Date</label></dt>
				<dd>
					<input id='startMonth' size='5' type='number' name='StartMonth' min='01' max='12' placeholder='MM' value='<c:out value="${empty StartMonth ? '' : StartMonth+1}" />' />
					/
					<input id='startDay' size='5' type='number' name='StartDay' min='01' max='31' step='1' placeholder='DD' value='<c:out value="${StartDay}" />' />
					/
					<input id='startYear' size='5' type='number' name='StartYear' min='${year}' max='${year+1}' step='1' placeholder='YYYY' value='<c:out value="${StartYear}" />' />
				</dd>
				
				<dt><label class='required' for="Details">Work Details</label></dt>
				<dd>
					<textarea rows="6" cols="50" name="Details" wrap="physical"><c:out value="${Details}" /></textarea>
					<br/>
					Please be specific and be thorough.
				</dd>
				
			</dl>
			
			<input type="submit" value="Submit" name="Submit"/>
		</form>		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>