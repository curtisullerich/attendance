<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:useBean id="date" class="java.util.Date" />

<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
		
		<script type="text/javascript">
		function remove(id){
			if (confirm("Are you sure you want to delete?"))
			{
				$('#loading').fadeIn();
				window.location = "/student/forms?removeid=" + id;
			}
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
			<p>
				Create a new form:
				<a href="/student/forms/forma" title="Performance Absence Request">Form A</a>
				| <a href="/student/forms/formb" title="Class Conflict Request">Form B</a>
				| <a href="/student/forms/formc" title="Request for Excuse from Rehearsal">Form C</a>
				| <a href="/student/forms/formd" title="Time Worked">Form D</a>
			</p>
		</c:if>
		
		<c:choose>
			<c:when test="${fn:length(forms) > 0}">
				<div>
					<p>Number of Forms Submitted: ${fn:length(forms)}</p>
					<br/>
					<table border='1'>
						<tr>
							<th>Start Date</th>
							<th>End Date</th>
							<th>Type</th>
							<th>Status</th>
							<th>Delete</th>
							<th>Messages</th>
						</tr>
						<c:forEach items="${forms}" var="form">
							<tr id="row_form_<c:out value="${form.id}" />">
								<td><fmt:formatDate value="${form.start}" pattern="mm/dd/yyyy" /></td>
								<td><fmt:formatDate value="${form.end}" pattern="mm/dd/yyyy" /></td>
								<td>${form.type}</td>
								<td>${form.status}</td>
								<td>
									<button onClick="remove('${form.id}');">Delete</button>
								</td>
								<td>
									<button onClick="window.location='./messages/viewthread?id=${form.id}'">messages</button>
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