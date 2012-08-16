<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="fmtCurrentDate" value="${now}" pattern="yyyy-MM-dd"/>
<fmt:formatDate var="fmtCurrentYear" value="${now}" pattern="yyyy"/>

<html>
<head>
<jsp:include page="/WEB-INF/template/head.jsp" />
<script type="text/javascript">
<!--
   function newMessage() {
   
		var threadid = $("#newmessageform input[name=id]").val();
		var name = $("#newmessageform input[name=name]").val();
		var text = $("#newmessageform textarea").val();
		
		if(!text)
		{
			alert("Please enter a message");
			return false;
		}
		
		$.post(window.location, {'id':threadid, 'Message':text});
		
		var currentDate = new Date();
		var hour = currentDate.getHours();
		var minute = currentDate.getMinutes();
		
		var date;
		if (hour%12 == 0)
			date = "12";
		else
			date = hour.toString();
		
		if (minute < 10)
			date += ":0" + minute;
		else
			date += ":" + minute;
		
		if (hour >= 12)
			date += " PM";
		else
			date += " AM";
		
		
		var message = $("<div class='grid-12 message-mine'>"
		+ "<p>"
		+ '<strong style="font-size:104%">'
		+ name
		+ "</strong>"
		+ "<br/>"
		+ text
		+ "<br/>"
		+ '<span style="float:right;color:#aaa;">'
		+ date
		+ '</span>'
		+ '</p>'
		+ '</div>');
		message.hide();
		message.prependTo( "#message_thread" );
		message.show('fast');
		
		
		$("#newmessageform textarea").val("");
		
		
		return false;
   }
   
	function changeResolved()
	{
		var resolved = $('#threadresolveform input:radio[name=resolved][checked=checked]').val();
		var threadid = $("#threadresolveform input[name=id]").val();

		$.post(window.location, {'id':threadid, 'resolved':resolved});
		
		if(resolved == "false")
			$("#newmessagepreview").html("Click here to send a reply and <strong>un-resolve</strong> the thread");
		else
			$("#newmessagepreview").text("Click here to send a reply");
		
		return false;
	}
 // -->
</script>
</head>

	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h2>Message Thread</h2>
		<c:if test="${not empty thread.formParent}">
			<c:if test="${auth.user.type.director}">
				<a href='/director/forms/view?id=${thread.formParent.id}'>Form ${thread.formParent.type}</a> submitted by ${thread.formParent.student.firstName } ${thread.formParent.student.lastName} (<a href='/director/student?id=${thread.formParent.student.netID}'>${thread.formParent.student.netID}</a>)<br/>
			</c:if>
			<c:if test="${not auth.user.type.director}">
				<a href='/student/forms/view?id=${thread.formParent.id}'>Form ${thread.formParent.type}</a> submitted by ${thread.formParent.student.firstName } ${thread.formParent.student.lastName} (${thread.formParent.student.netID})<br/>
			</c:if>
			

			<c:if test="${thread.formParent.type.displayName eq 'A'}">
				<%--TODO redirect form? --%>
				Date of performance: <fmt:formatDate value="${thread.formParent.start}" pattern="M/d/yy"/><br/>
			</c:if>
			<c:if test="${thread.formParent.type.displayName eq 'B'}">
					Department: ${thread.formParent.dept}<br/>
					Course: ${thread.formParent.course }<br/>
					Section: ${thread.formParent.section}<br/>
					Building: ${thread.formParent.building}<br/>
					Start Date: <fmt:formatDate value="${thread.formParent.start}" pattern="M/d/yyyy"/><br/>
					End Date: <fmt:formatDate value="${thread.formParent.end}" pattern="M/d/yyyy"/><br/>
					On ${thread.formParent.dayAsString} from <fmt:formatDate value="${thread.formParent.start}" pattern="h:mm a"/> to <fmt:formatDate value="${thread.formParent.end}" pattern="h:mm a"/><br/>
					Type: ${thread.formParent.absenceType}<br/>
			</c:if>
			<c:if test="${thread.formParent.type.displayName eq 'C'}">
				 Type: ${thread.formParent.absenceType}<br/>
				 Date of rehearsal: <fmt:formatDate value="${thread.formParent.start}" pattern="M/d/yy"/> <br/>
	  			 <c:if test="${thread.formParent.absenceType eq 'Tardy'}">
					 Time of arriving: <fmt:formatDate value="${thread.formParent.start}" pattern="h:mm a"/><br/>
				 </c:if>
	  			 <c:if test="${thread.formParent.absenceType eq 'EarlyCheckOut'}">
					 Time of leaving: <fmt:formatDate value="${thread.formParent.start}" pattern="h:mm a"/><br/>
				 </c:if>
			</c:if>
			<c:if test="${thread.formParent.type.displayName eq 'D'}">
				Date: <fmt:formatDate value="${thread.formParent.start}" pattern="M/d/yy"/><br/>
				${thread.formParent.minutesWorked} ${thread.formParent.emailStatus} minutes worked for ${thread.formParent.emailTo}<br/>
			</c:if>
			Approved by director? - ${thread.formParent.status }<br/>
		</c:if>
		<c:if test="${not empty thread.absenceParent}">

			<c:if test="${auth.user.type.director}">
				${thread.absenceParent.status} <a href='/director/viewabsence?absenceid=${thread.absenceParent.id}'> ${thread.absenceParent.type } </a> for ${thread.absenceParent.student.firstName } ${thread.absenceParent.student.lastName} (<a href='/director/student?id=${thread.absenceParent.student.netID}'>${thread.absenceParent.student.netID}</a>)<br/>
			</c:if>
			<c:if test="${not auth.user.type.director}">
				${thread.absenceParent.status} ${thread.absenceParent.type} for ${thread.absenceParent.student.firstName } ${thread.absenceParent.student.lastName} (${thread.absenceParent.student.netID})<br/>
				<c:if test="${not empty thread.absenceParent.event}">
					Event details: <c:out value="${thread.absenceParent.event.type}" /> on 
					<fmt:formatDate value="${thread.absenceParent.event.start}" pattern="M/d/yy" /> from 
					<fmt:formatDate value="${thread.absenceParent.event.start}" pattern="h:mm a" /> to 
					<fmt:formatDate value="${thread.absenceParent.event.end}" pattern="h:mm a" />
<%-- 					<c:choose> --%>
<%-- 						<c:when test="${(thread.absenceParent.type.tardy) || (thread.absenceParent.type.earlyCheckOut)}"> --%>
<%-- 							<fmt:formatDate value="${thread.absenceParent.start}" pattern="h:mm a" /> --%>
<%-- 						</c:when> --%>
<%-- 						<c:when test="${(thread.absenceParent.type.absence) && (empty thread.absenceParent.event)}"> --%>
<%-- 							<fmt:formatDate value="${thread.absenceParent.start}" pattern="M/d" /> --%>
<%-- 							<fmt:formatDate value="${thread.absenceParent.start}" pattern="h:mm a" /> --%>
<!-- 							- -->
<%-- 							<fmt:formatDate value="${thread.absenceParent.end}" pattern="h:mm a" /> --%>
<%-- 						</c:when> --%>
<%-- 					</c:choose>		 --%>
				</c:if>
			</c:if>
			<br/>
			<c:if test="${thread.absenceParent.type.displayName eq 'Tardy'}">
			Time of arrival: <fmt:formatDate value="${thread.absenceParent.start}" pattern="h:mm a"/><br/>
			</c:if>
			<c:if test="${thread.formParent.type.displayName eq 'EarlyCheckOut'}">
			Time of leaving: <fmt:formatDate value="${thread.absenceParent.start}" pattern="h:mm a"/><br/>
			</c:if>
			<c:if test="${thread.formParent.type.displayName eq 'Absence'}">
			</c:if>
		</c:if>
		<br/>
		
		<div class="gutter" style="padding:0px 20px;">
			<c:choose>
				<c:when test="${auth.user.type.director}">
					<div class="threadresolved">
						<form id="threadresolveform" method="post" accept-charset="utf-8" onsubmit="return changeResolved();">
							<input id="resolved_radio" name="resolved" value="false" type="radio" onclick="$('#threadresolveform').submit();" ${thread.resolved?'checked="checked"':''} />
							<label for="resolved_radio">Resolved</label>
							
							<input id="unresolved_radio" name="resolved" value="true" type="radio" onclick="$('#threadresolveform').submit();" ${thread.resolved?'':'checked="checked"'} />
							<label for="unresolved_radio">Unresolved</label>
							
							<input type="hidden" value="<c:out value="${thread.id}" />" name="id"/>
							<input type="hidden" value="submit" name="resolveB"/>
						</form>
					</div>
					<br/>
				</c:when>
				<c:when test="${thread.resolved}">
					<p style="font-size:large;"><i>Resolved.</i></p>
				</c:when>
				<c:when test="${!thread.resolved}">
					<p style="font-size:large;"><i>Not Resolved.</i></p>
				</c:when>
			</c:choose>
		<br/>
		
		<div class="grid-full">
			<c:if test="${!(empty thread.messages)}">
				<div id="newmessagepreview" class="newmessagepreview" onclick="$('#newmessageform').show('fast');$('#newmessagepreview').hide('fast');$('#newmessageform textarea').focus();">
					Click here to send a reply
					<c:if test="${thread.resolved}">
						and <strong>un-resolve</strong> the thread
					</c:if>
				</div>
				<br/>
			</c:if>
		
			<form id="newmessageform" method="post" accept-charset="utf-8" style="display:${(empty thread.messages)?'block':'none'};"
				onsubmit="return newMessage();">
			
				<dl class="block-layout">
					
					<dt><label>New message:</label></dt>
					<dd>
						<textarea rows="6" cols="50" name="Message" wrap="physical" autofocus></textarea>
					</dd>
					
				</dl>
				
				<input type="hidden" value="<c:out value="${thread.id}" />" name="Id"/>
				
				<input type="hidden" value="<c:out value="${auth.user.name}" />" name="name"/>
				
				<input type="submit" value="Add Message" name="Submit"/>
			</form>
		</div>
		<br/>
		<br/>
		<c:if test="${empty thread.messages}">
			<strong>No messages yet.</strong>
			<br/>
		</c:if>
		<div id="message_thread">
			<c:forEach items="${thread.messages}" var="message">
				<div class="grid-12 ${(auth.user eq message.author)?'message-mine':'message-theirs'}">
				<p>
					<strong style="font-size:104%"><c:out value="${message.author.name}" /></strong>
					<br/>
					<c:out value="${message.text}" />
					<br/>
					<span style="float:right;color:#aaa;">
					<fmt:formatDate var="fmtDate" value="${message.timestamp}" pattern="yyyy-MM-dd"/>
					<fmt:formatDate var="fmtYear" value="${message.timestamp}" pattern="yyyy"/>
					<c:choose>
						<c:when test="${(fmtDate eq fmtCurrentDate)}">
							<fmt:formatDate value="${message.timestamp}" pattern="h:mm a" />
						</c:when>
						<c:when test="${!(fmtYear eq fmtCurrentYear)}">
							<fmt:formatDate value="${message.timestamp}" pattern="MM/dd/YY" />
						</c:when>
						<c:otherwise>
							<fmt:formatDate value="${message.timestamp}" pattern="MMM d" />
						</c:otherwise>
					</c:choose>
					</span>
				</p>
				</div>
			</c:forEach>
		</div>
		</div>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>