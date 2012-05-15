<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="date" class="java.util.Date" />

<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
		
		<script type="text/javascript">
		function remove(id){
			if (confirm("Are you sure you want to delete?"))
			{
				window.location = "/JSPPages/remove?id=" + id	
			}
		}
		</script>
		
	</head>

	<body>
	
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>View Forms</h1>
		
		<c:choose>
			<c:when test="${fn:length(forms) eq 0}">
				<div>
					<p>Number of Forms Submitted: ${fn:length(forms)}</p>
					<br/>
					<table border='1'>
						<tr>
							<th>Start Date</th>
							<th>End Date</th>
							<th>Type</th>
							<th>Status</th>
						</tr>
						<c:forEach items="${forms}" var="form">
							<tr id="row_form_<c:out value="${form.id}" />">
								<td><fmt:formatDate value="${form.start}" pattern="mm/dd/yyyy" /></td>
								<td><fmt:formatDate value="${form.end}" pattern="mm/dd/yyyy" /></td>
								<td><c:out value="${form.type}</td>
								<td><c:out value="${form.status}</td>
								<td>
									<button onClick="remove('${form.id}');">Delete</button>
								</td>
								<td>
									<button onClick="window.location='./messages?id=${form.id}'">messages</button>
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