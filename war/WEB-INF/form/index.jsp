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
					<table class="gray full-width gray-hover" style="table-layout:fixed;white-space:nowrap;overflow:hidden;">
						<colgroup>
							<col width="12%" />
							<col width="12%" />
							<col width="51%" />
							<col width="10%" />
							<col width="15%" />
						</colgroup>
						<thead>
							<tr class="dark-title">
								<th>Type</th>
								<th>Status</th>
								<th>About</th>
								<th></th>
								<th></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${forms}" var="form">
							<tr id="row_form_<c:out value="${form.id}" />">
						<tr>
							<%//Note that I did this because the last two columns are buttons. %>
							<td onclick="window.location='form/viewform?id=<c:out value="${form.id}"/>'">${form.type}</td>
							<td onclick="window.location='form/viewform?id=<c:out value="${form.id}"/>'">${form.status}</td>
							<td onclick="window.location='form/viewform?id=<c:out value="${form.id}"/>'">
							<p style="overflow:hidden;">								
								<c:choose>
									<c:when test="${form.type.a || form.type.c}">							
										<fmt:formatDate value="${form.start}" pattern="M/d/yyyy" /> <c:if test="${not empty form.details }"> - ${form.details }</c:if>
									</c:when>
									<c:when test ="${form.type.b }">
										${form.dept} ${form.course } <c:if test="${not empty form.details }"> - ${form.details }</c:if>
									</c:when>
									<c:when test ="${form.type.d }">
										<fmt:formatDate value="${form.start}" pattern="M/d/yyyy" /> - ${form.hoursWorked } hours worked for ${form.emailTo } <c:if test="${not empty form.details }"> - ${form.details } </c:if>
									</c:when>
								</c:choose>
								</p>
								</td>								
								<!-- Make the delete button and messages link. -->
								<td>
									<c:choose>
										<c:when test="${auth.user.type.student || auth.user.type.ta}">
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
						</tbody>
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