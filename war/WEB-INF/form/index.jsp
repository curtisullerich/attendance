<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
 <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />
 
<jsp:useBean id="date" class="java.util.Date" />

<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
		<script>
			function remove(url) {
				if (confirm("Are you sure you want to delete?"))
				{
					$('#loading').fadeIn();
					window.location = "/"+url;
				}
			}
			function noRemove() {
				alert("You can only delete pending forms.");
			}
		</script>
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>View Forms</h1>
		
		<c:if test="${not empty success_message}">
			<p class="notify-msg success">
				<strong>Success:</strong> <c:out value="${success_message}" />
			</p>
		</c:if>
		
		<c:if test="${auth.user.type.student or auth.user.type.ta}">
			<!-- p>
				Create a new form:
				<a href="/student/forms/forma" title="Performance Absence Request">Form A</a>
				| <a href="/student/forms/formb" title="Class Conflict Request">Form B</a>
				| <a href="/student/forms/formc" title="Request for Excuse from Rehearsal">Form C</a>
				| <a href="/student/forms/formd" title="Time Worked">Form D</a>
			</p-->
		</c:if>
		
		<c:choose>
			<c:when test="${fn:length(forms) > 0}">
				<div>
					<p>Number of Forms Submitted: ${fn:length(forms)}</p>
					<br/>
					<table class="gray full-width">
						<tr>
							<thead>
							<th>Start Date</th>
							<th>End Date</th>
							<th>Type</th>
							<th>Status</th>
							<th>Delete</th>
							<th>Messages</th>
							</thead>
						</tr>
						<c:forEach items="${forms}" var="form">
							<tr id="row_form_<c:out value="${form.id}" />">
								<td><fmt:formatDate value="${form.start}" pattern="M/d/yyyy" /></td>
								<td><fmt:formatDate value="${form.end}" pattern="M/d/yyyy" /></td>
								<td>${form.type}</td>
								<td>${form.status}</td>
								<td>
									<c:choose>
										<c:when test="${auth.user.type.student}">
											<c:choose>
												<c:when test="${form.status.value eq 'Pending'}">
													<button onClick="remove('student/forms?removeid=${form.id}');">Delete</button>
												</c:when>
												<c:when test="${form.status ne 'Pending'}">
													<button onClick="noRemove();">Delete</button>
												</c:when>
											</c:choose>
										</c:when>
										<c:when test="${auth.user.type.director}">
											<button onClick="remove('director/forms?removeid=${form.id}');">Delete</button>
										</c:when>
									</c:choose>
								</td>
								<td>
									<c:if test="${!form.messageThread.resolved}">
										<strong>
											<c:choose>
												<c:when test="${auth.user.type.director}">
													<a href="/director/messages/viewthread?id=${form.messageThread.id}">Messages(${fn:length(form.messageThread.messages)})</a>
												</c:when>
												<c:when test="${auth.user.type.student || auth.user.type.ta}">
													<a href="/student/messages/viewthread?id=${form.messageThread.id}">Messages(${fn:length(form.messageThread.messages)})</a>
												</c:when>
											</c:choose>
										</strong>
									</c:if>
									
									<c:if test="${form.messageThread.resolved}">
										<c:choose>
											<c:when test="${auth.user.type.director}">
												<a href="/director/messages/viewthread?id=${form.messageThread.id}">Messages(${fn:length(form.messageThread.messages)})</a>
											</c:when>
											<c:when test="${auth.user.type.student || auth.user.type.ta}">
												<a href="/student/messages/viewthread?id=${form.messageThread.id}">Messages(${fn:length(form.messageThread.messages)})</a>
											</c:when>
										</c:choose>
									</c:if>
									<!-- Messages button. Make it bold if there's an unresolved thread. -->
								</td>
							</tr>	
						</c:forEach>
					</table>
				</div>
			</c:when>
			<c:otherwise>
				<p>No forms submitted yet!</p>
			</c:otherwise>
		</c:choose>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>