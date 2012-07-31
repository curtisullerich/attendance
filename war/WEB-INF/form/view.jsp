<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		
		<h1>Form <c:out value="${form.type.value}" /></h1>
		
		<form method="post" accept-charset="utf-8">
			<dl class="block-layout">
				<dt><label></label></dt>
				<dt><label>Status</label></dt>
				<dd>
				<c:choose>
					<c:when test="${auth.user.type.director}">
						<select name = "status" id= "status"}>
							<option value="Approved" ${form.status.displayName eq 'Approved' ? 'selected="true"': ''}>Approved</option>
							<option value="Pending" ${form.status.displayName eq 'Pending' ? 'selected="true"': ''}>Pending</option>
							<option value="Denied" ${form.status.displayName eq 'Denied' ? 'selected="true"': ''}>Denied</option>
						</select>
					</c:when>
					<c:otherwise>
						<input type="text" name="status" id="status" value="${form.status}" disabled readonly>
					</c:otherwise>
				</c:choose>
				<dd>
				
				<c:if test="${form.type.displayName eq 'A'}">
					<dt><label>Start Time</label></dt>
					<dd>
						<fmt:formatDate value="${form.start}" pattern="M/d/yyyy"/>
					</dd>
				</c:if>
				<c:if test="${form.type.displayName eq 'B'}">
					<dt><label>Department</label></dt>
					<dd>${form.dept}</dd>
					
					<dt><label>Course</label></dt>
					<dd>${form.course}</dd>
					
					<dt><label>Section</label></dt>
					<dd>${form.section}</dd>
					
					<dt><label>Building</label></dt>
					<dd>${form.building}</dd>
					
					<dt><label>Start Date</label></dt>
					<dd><fmt:formatDate value="${form.start}" pattern="M/d/yyyy"/></dd>
					
					<dt><label>End Date</label></dt>
					<dd><fmt:formatDate value="${form.end}" pattern="M/d/yyyy"/></dd>
					
					<dt><label>On</label></dt>
					<dd>${day}</dd>
					
					<dt><label>From</label></dt>
					<dd><fmt:formatDate value="${form.start}" pattern="hh:mm a"/></dd>
					
					<dt><label>To</label>
					<dd><fmt:formatDate value="${form.end}" pattern="hh:mm a"/></dd>

					<dt><label>Absence Type</label>
					<dd>${form.absenceType}</dd>
				</c:if>
				<c:if test="${form.type.displayName eq 'C'}">
					<dt><label>Start Time</label></dt>
					<dd>
						<fmt:formatDate value="${form.start}" pattern="M/d/yyyy"/>
					</dd>
					<dt><label>Time of Arriving/Leaving</label></dt>
					<dd>
						<fmt:formatDate value="${form.start}" pattern="hh:mm a"/>
					</dd>
					<dt><label>Absence Type</label>
					<dd>${form.absenceType}</dd>
				</c:if>
				<c:if test="${form.type.displayName eq 'D'}">
					<dt><label>Email To</label></dt>
					<dd>${form.emailTo}</dd>
					
					<dt><label>Approve by this person?</label></dt>
					<dd>${form.emailStatus}</dd>
					
					<dt><label>Total amount of work</label></dt>
					<dd>${form.minutesWorked}</dd>
					
					<dt><label>Start Date</label></dt>	
					<dd>
					<fmt:formatDate value="${form.start}" pattern="M/d/yyyy"/>
					</dd>
				</c:if>
				<dt><label>Details</label>
				<dd>${form.details}<dd>
				
				<c:if test="${auth.user.type.director}">
					<input type="submit" name="submit" id="submit" value="Submit">
					<input type="hidden" name="id" id="formid" value="${form.id}">
				</c:if>
			</dl>
		</form>

		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>