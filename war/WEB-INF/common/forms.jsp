<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

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
							<td onclick="window.location='form/view?id=<c:out value="${form.id}"/>'">${form.type}</td>
							<td onclick="window.location='form/view?id=<c:out value="${form.id}"/>'">${form.status}</td>
							<td onclick="window.location='form/view?id=<c:out value="${form.id}"/>'">
							<p style="overflow:hidden;">								
								<c:choose>
									<c:when test="${form.type.a || form.type.c}">							
										<fmt:formatDate value="${form.start}" pattern="M/d/yyyy" /> <c:if test="${not empty form.details }"> - ${form.details }</c:if>
									</c:when>
									<c:when test ="${form.type.b }">
										${form.dept} ${form.course } <c:if test="${not empty form.details }"> - ${form.details }</c:if>
									</c:when>
									<c:when test ="${form.type.d }">
										<fmt:formatDate value="${form.start}" pattern="M/d/yyyy" /> - ${form.minutesWorked } hours worked for ${form.emailTo } <c:if test="${not empty form.details }"> - ${form.details } </c:if>
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
													<button onClick="window.location='/student/forms?removeid=${form.id}'">Delete</button>
												</c:when>
												<c:when test="${form.status ne 'Pending'}">
													<button onClick="alert('You can't delete a form unless it's pending.);">Delete</button>
												</c:when>
											</c:choose>
										</c:when>
										<c:when test="${auth.user.type.director}">
											<button onClick="window.location='/director/forms?removeid=${form.id}'">Delete</button>
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