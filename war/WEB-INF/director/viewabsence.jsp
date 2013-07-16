<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
        <link rel="stylesheet" media="all" type="text/css" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <link rel="stylesheet" media="all" type="text/css" href="/css/jquery-ui-timepicker-addon.css" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		
		<h1>Absence Info For: <c:out value="${absence.student.name}" /> (<a href='/director/student?id=${absence.student.netID}'>${absence.student.netID}</a>)</h1>

		<form method="post" accept-charset="utf-8">
			<fmt:formatDate var="arst" value="${absence.start}" pattern="yyyy"/> 
			<dl class="block-layout">
			<dt><label></label></dt>
			
				<dt><label for="StartMonth" class="required">Check in/out time</label></dt>
				Note that this time is irrelevant if this is not a tardy or early checkout. This field is here so you can change it to a tardy or early checkout if desired.
				<dd><fmt:formatDate value="${absence.start}" pattern="M/d/yyyy"/>
					<%//hidden because these should never be changed %>
                    <input type="hidden" name="date" id="date" value='<c:out value="${date}" />' />
                    <input type="text" name="time" id="time" value='<c:out value="${time}" />' />
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
					Start Time: <input type="text" name="Event" value='<fmt:formatDate value="${absence.event.start}" pattern="MM/dd/yyyy 'at' h:mm a"/>' disabled readonly/> 
				</dd>
				<dd>
					End Time: <input type="text" name="Event" value='<fmt:formatDate value="${absence.event.end}" pattern="MM/dd/yyyy 'at' h:mm a"/>' disabled readonly/>
				</dd>		
			</dl>
			
			<input type="hidden" value="${absence.id}" name="absenceid"/>
			<input type="hidden" value="${absence.event.id}" name="eventid"/>
			<input type="submit" value="Submit" name="SubmitChanges" />
			<%--input type="button" value="Cancel" name="Cancel" onclick="window.location = '/director/attendance'"/--%>
		</form>
	
		<form method="post" accept-charset="utf-8">
			<c:if test="${not new }">
				<input type="submit" value="Delete Absence" name="delete"/>
			</c:if> 
		</form>

		<br/>
		<hr/>
		<br/>
	
		<jsp:include page="/WEB-INF/template/footer.jsp" />	
        <script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
        <script type="text/javascript" src="/js/jquery-ui-timepicker-addon.js"></script>
        <script>
        $('#time').timepicker({
            timeFormat: "h:mm TT"
          });
        $('#date').datepicker({
          });
        </script>
	</body>
</html>	