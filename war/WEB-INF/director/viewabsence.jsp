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
		
		<form method="post" accept-charset="utf-8">
			<fmt:formatDate var="arst" value="${absence.start}" pattern="yyyy"/> 
			<dl class="block-layout">
			<dt><label></label></dt>
			
				<dt><label for="StartMonth" class="required">Start Time</label></dt>
				<dd><fmt:formatDate value="${absence.start}" pattern="M/d/yyyy"/>
					<%//hidden because these should never be changed %>
					<input size='5' type='hidden' name='StartMonth' min='01' max='12' value='<fmt:formatDate value="${absence.start}" pattern="M"/>'/>
					<input size='5' type='hidden' name='StartDay' min='01' max='31' value='<fmt:formatDate value="${absence.start}" pattern="d"/>'/>
					<input size='5' type='hidden' name='StartYear' min='${arst}' max='${arst+1}' value='<fmt:formatDate value="${absence.start}" pattern="yyyy"/>'/>
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
			</dl>
			
			<input type="hidden" value="${absence.id}" name="absenceid"/>
			<input type="submit" value="Submit Changes" name="SubmitChanges" />
			<input type="button" value="Cancel" name="Cancel" onclick="window.location = '/director/attendance'"/>
			<c:if test="${not new}">
				<input type="button" value="Messages" name="Message" onclick="window.location = '/director/messages/viewthread?id=${absence.messageThread.id}'"/>
			</c:if>	
			<c:if test="${new }">
				<input type="button" value="Messages" disabled name="Message" title="You can't view messages until you've actually created this absence." onclick="window.location = '/director/messages/viewthread?id=${absence.messageThread.id}'"/>
			</c:if>
		</form>
	
	
		<jsp:include page="/WEB-INF/template/footer.jsp" />	
	</body>
</html>	