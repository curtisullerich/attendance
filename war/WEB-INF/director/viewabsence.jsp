<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		
		<h1>Absence Info For: <c:out value="${absence.student.name}" /></h1>
		
		<c:if test="${not empty error_messages}">
			<p class="notify-msg error">
				<strong>Error:</strong>
				<c:forEach items="${error_messages}" var="error_message">
					<c:out value="${error_message}" />
					<br/>
				</c:forEach>
			</p>
		</c:if>
		
		<form action="./viewabsence" method="post" accept-charset="utf-8">
			<d1 class="block-layout">
			<fmt:formatDate var="arst" value="${absence.start}" pattern="yyyy"/> 
			<dt><label></label></dt>
			
			<dt><label for="StartMonth" class="required">Start Time</label></dt>
			<dd>
				<input size='5' type='number' name='StartMonth' min='01' max='12' value='<fmt:formatDate value="${absence.start}" pattern="M"/>'/>
				/
				<input size='5' type='number' name='StartDay' min='01' max='31' value='<fmt:formatDate value="${absence.start}" pattern="d"/>'/>
				/
				<input size='5' type='number' name='StartYear' min='${arst}' max='${arst+1}' value='<fmt:formatDate value="${absence.start}" pattern="yyyy"/>'/>
				at   
				<input size='5' type='number' name='StartHour' min='01' max='12' value='<fmt:formatDate value="${absence.start}" pattern="hh"/>'/>
				:
				<input size='5' type='number' name='StartMinute' min='00' max='59' value='<fmt:formatDate value="${absence.start}" pattern="mm"/>'/>
				
				<select name="StartAMPM">
				<fmt:formatDate var="SAMPM"  value="${absence.start}" pattern="a"/></option>
					<option ${SAMPM eq 'AM' ? 'selected' : ''}>AM</option>
					<option ${SAMPM eq 'AM' ? '' : 'selected'}>PM</option>				
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
			
			<dt><label for="Status" class="required">Status</label></dt>
			<dd>
				<select name="Status">
					<c:forEach items="${status}" var="st" varStatus="loop">
						<option value="<c:out value="${st.value}" />"
							${absence.status==st.value ? 'selected="true"' : ''}
							><c:out value="${st.displayName}" /></option>
					</c:forEach>
				</select>
			</dd>
			<dt><label for="Event" class="required">Event</label></dt>
				<dd>
					Start Time: <input type="text" name="Event" value='<fmt:formatDate value="${absence.event.start}" pattern="MM/dd/yyyy 'at' hh:mm a"/>' disabled readonly/> 
				</dd>
				<dd>
					End Time: <input type="text" name="Event" value='<fmt:formatDate value="${absence.event.end}" pattern="MM/dd/yyyy 'at' hh:mm a"/>' disabled readonly/>
				</dd>		
			</d1>
			
			<input type="hidden" value="${absence.id}" name="absenceid"/>
			<input type="submit" value="Submit Changes" name="SubmitChanges" />
						<input type="button" value="Back" name="Back" onclick="window.location = '/director/attendance'"/>
		</form>
	
	
		<jsp:include page="/WEB-INF/template/footer.jsp" />	
	</body>
</html>	