<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />
  <style>
    table.gray tbody tr.late td {
      background-color: #ffa3a3;
    }
    .divBox {
        width: 100%;
        height: 100%;
    }
    .divBox a {
        width: 100%;
        height: 100%;
        display: block;
        hover:none;
        color: #444;
    }
  </style>
		<c:choose>
			<c:when test="${fn:length(forms) > 0}">
 				<div>
					<p>Number of Forms Submitted: ${fn:length(forms)}</p>
					<br/>
					<table class="sortable gray full-width gray-hover" style="table-auto;"><!-- white-space:nowrap;overflow:hidden; -->
						<thead>
							<tr class="dark-title">
								<th>netID</th>
								<th>Type</th>
								<th>Status</th>
                                <th>Submitted</th>
								<th>About</th>
								<th class="sorttable_nosort"></th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${forms}" var="form">
							<c:url var="form_url_view" value="forms/view">
								<c:param name="id" value="${form.id}"/>
								<%--c:param name="redirect" value="${pagetemplate.uri}"/--%>
							</c:url>
							<c:url var="form_url_remove" value="forms">
								<c:param name="id" value="${form.id}"/>
							</c:url>

 						  <tr <c:if test="${form.late}">class="late"</c:if> id="row_form_<c:out value="${form.id}" />">
							<%--Note that I did this because the last two columns are buttons. --%>
                            <td>
                              <div class="divBox">
                                <a href="${form_url_view}">
                                  ${form.student.id}
                                </a>
                              </div>
                            </td>
                            <td>
                              <div class="divBox">
                                <a href="${form_url_view}">
                                  ${form.type}
                                </a>
                              </div>
                            </td>
                            <td>
                              <div class="divBox">
                                <a href="${form_url_view}">
                                  ${form.status}
                                </a>
                              </div>
                            </td>
                            <td>
                              <div class="divBox">
                                <a href="${form_url_view}">
                                  <fmt:formatDate value="${form.submissionTime}" pattern="M/d h:mm a" />&nbsp;
                                </a>
                              </div>
                            </td>
                            <td>
                              <div class="divBox">
                                <a href="${form_url_view}">
        							<p style="overflow:hidden;">
                                        <c:if test="${form.late}"><b>LATE SUBMISSION.</b> </c:if>
        								<c:choose>
        									<c:when test="${form.type.performanceAbsence}">
        										<fmt:formatDate value="${form.start}" pattern="M/d/yyyy" /> <c:if test="${not empty form.details }"> - ${form.details }</c:if>
        									</c:when>
        									<c:when test ="${form.type.classConflict }">
        										${form.dept} ${form.course } <c:if test="${not empty form.details }"> - ${form.details }</c:if>
        									</c:when>
        									<c:when test ="${form.type.timeWorked }">
        										<fmt:formatDate value="${form.start}" pattern="M/d/yyyy" /> - ${form.minutesWorked } minutes worked <c:if test="${not empty form.details }"> - ${form.details } </c:if>
        									</c:when>
        								</c:choose>
        								</p>
                                    </a>
                                  </div>
								</td>								
								<td>
									<c:choose>
										<c:when test="${auth.user.type.student || auth.user.type.ta}">
											<c:choose>
												<c:when test="${form.status.value eq 'Pending'}">
													<button onclick="deleteForm(${form.id}, 'student')">Delete</button>
												</c:when>
												<c:when test="${form.status ne 'Pending'}">
													<button onclick="alert('You cannot delete a form unless it is pending.');">Delete</button>
												</c:when>
											</c:choose>
										</c:when>
										<c:when test="${auth.user.type.director}">
											<button onclick="deleteForm(${form.id}, 'director')">Delete</button>
										</c:when>
									</c:choose>
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
		<form method="POST" id="deleteForm">
			<input type="hidden" id="deleteFormId" name="removeid" value="" />
		</form>
		<script>
			function deleteForm(id, type) {
				var answer = confirm("Really delete the form?");
				if (answer) {
					$("#deleteFormId").val(id);
					$("#deleteForm").submit();
				}
			}
		</script>		