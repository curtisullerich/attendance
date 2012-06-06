<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		
		<h1>Make a New Event</h1>
		
		<c:if test="${not empty error_messages}">
			<p class="notify-msg error">
				<strong>Error:</strong>
				<c:forEach items="${error_messages}" var="error_message">
					<c:out value="${error_message}" />
					<br/>
				</c:forEach>
			</p>
		</c:if>

		<form action="./makeevent" method="post" accept-charset="utf-8">
			<dl class="block-layout">
			<dt><label></label></dt>


				<dt><label for="Date" class="required">Date</label></dt>
				<dd>
					<input size='5' type='number' name='Month' min='01' max='12' value='<fmt:formatDate value="${today}" pattern="M"/>'/>
					<input size='5' type='number' name='Day' min='01' max='31' value='<fmt:formatDate value="${today}" pattern="d"/>'/>
					<input size='5' type='number' name='Year' min='${arst}' max='${arst+1}' value='<fmt:formatDate value="${today}" pattern="yyyy"/>'/>
				</dd>
			
				<dt><label for="StartTime" class="required">Start Time</label></dt>
				<dd>
					<input size='5' type='number' name='StartHour' min='01' max='12' value='4'/>
					:
					<input size='5' type='number' name='StartMinute' min='00' max='59' value='30'/>
					
					<select name="StartAMPM">
						<option>AM</option>
						<option selected>PM</option>				
					</select>
				</dd>
				<dt><label for="EndTime" class="required">End Time</label></dt>
				<dd>   
					<input size='5' type='number' name='EndHour' min='01' max='12' value='5'/>
					:
					<input size='5' type='number' name='EndMinute' min='00' max='59' value='50'/>
					
					<select name="EndAMPM">
						<option>AM</option>
						<option selected>PM</option>				
					</select>
				</dd>

				
				<dt><label for="Type" class="required">Type</label></dt>
				<dd>
					<select name="Type">
						<c:forEach items="${types}" var="s" varStatus="loop">
							<option value="<c:out value="${s.value}" />"> <c:out value="${s.displayName}" /> </option>
						</c:forEach>
					</select>
				</dd>
				
			</dl>
			
			<input type="submit" value="Create" name="Create" />
		</form>
	
	
		<jsp:include page="/WEB-INF/template/footer.jsp" />	
	</body>
</html>	