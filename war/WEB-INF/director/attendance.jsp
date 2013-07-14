<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
<head>
<jsp:include page="/WEB-INF/template/head.jsp" />

<style type="text/css">
/* #container 
 { 
 	width:auto; 
 	display:inline-block; 
 	margin-left: 50px; 
 	margin-right: 50px; 
 } 
 #container-inner { 
 	width:auto; 
 	overflow:visible; 
 } 
 #container div.sidebar { 
 	position: fixed; 
 	background-color: white; 
 } 
 #content { 
 	width:auto; 
 	margin-left:210px; 
 } */

td .show-absence-onhover {
	color:#333;
	display:none;
}
td:hover .show-absence-onhover {
	visibility:visible;
}
td:hover .show-absence-onhover a {
	color:#333;
}
td .show-add-onhover {
	color:#aaa;
	visibility:hidden;
}
td:hover .show-add-onhover {
	visibility:visible;
}
td:hover .show-add-onhover a {
	color:#aaa;
}
table.gray tr:nth-child(odd) td.highlight, table.gray tr:nth-child(even) td.highlight {
	background-color: e2e2c5;
}<%// e0e0e0 is another option for the color%>
</style>

</head>
<body>
	<jsp:include page="/WEB-INF/template/header.jsp" />

	<h1>${pagetemplate.title}</h1>
	<br />

<%-- 	<button id="show" onClick="showApproved();"><c:out value="${showApproved ? 'Hide Approved' :'Show Approved' }"/></button> --%>
	<div class="threadresolved">
		<form id="showapprovedform" method="post" accept-charset="utf-8">
			<input id="approved_radio" name="approved" value="true" type="radio" onclick="$('#showapprovedform').submit();" ${showApproved ?'checked="checked"':''} />
			<label for="approved_radio">Show Approved</label>
			
			<input id="unapproved_radio" name="approved" value="false" type="radio" onclick="$('#showapprovedform').submit();" ${showApproved ?'':'checked="checked"'} />
			<label for="unapproved_radio">Hide Approved</label>
			
			<input type="hidden" value="<c:out value="${thread.id}" />" name="id"/>
			<input type="hidden" value="submit" name="approvedB"/>
		</form>
	</div>
	
	<br/><br/>

	<div>
	Click a column header to sort!
	<table class="sortable gray">
		<thead>
			<tr>
				<th>Last Name</th>
				<th>First name</th>
				<th>Section</th>
				<th>University ID</th>
				<th>Grade</th>
				<th>Absences</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${students }" var="student" >
				<tr>
					<td><a target="_blank" href="/director/student?id=${student.id }&newindow=true">${student.lastName }</a></td>
					<td><a target="_blank" href="/director/student?id=${student.id }&newindow=true">${student.firstName }</a></td>
					<td>${student.section.displayName }</td>
					<td>${student.universityID }</td>
					<td>${student.grade.displayName }</td>
					
						<c:set var="cellcontents" value="" />
							
						<c:forEach items="${absenceList[student]}" var="absence" varStatus="loop">
							<c:choose>
								<c:when test="${absence.type.tardy && !absence.status.approved}">
									<c:set var="cellcontents">
										${cellcontents}
										<a target="_blank" href="/director/viewabsence?absenceid=${absence.id }&newindow=true">
                                          ${absence.status} ${absence.type }[<fmt:formatDate value="${absence.datetime}" pattern="M/d"/>]
                                        </a>
                                        <c:if test="${not loop.last}">
                                          <br/>
                                        </c:if>
									</c:set>
								</c:when>
								<c:when test="${absence.type.absence && !absence.status.approved}">
									<c:set var="cellcontents">
										${cellcontents}
										<a target="_blank" href="/director/viewabsence?absenceid=${absence.id }&newindow=true">
                                          ${absence.status} ${absence.type } [<fmt:formatDate value="${absence.datetime}" pattern="M/d"/>]
                                        </a>
                                        <c:if test="${not loop.last}">
                                          <br/>
                                        </c:if>
									</c:set>
								</c:when>
								<c:when test="${absence.type.earlyCheckOut && !absence.status.approved}">
									<c:set var="cellcontents">
										${cellcontents}
										<a target="_blank" href="/director/viewabsence?absenceid=${absence.id }&newindow=true">
                                          ${absence.status} ${absence.type }[<fmt:formatDate value="${absence.datetime}" pattern="M/d"/>]
                                        </a>
                                        <c:if test="${not loop.last}">
                                          <br/>
                                        </c:if>
									</c:set>
								</c:when>
								<c:when test="${absence.status.approved}">
									<c:if test="${showApproved}">
										<c:set var="cellcontents">
											${cellcontents}
											<a target="_blank" href="/director/viewabsence?absenceid=${absence.id }&newindow=true" >
                                              ${absence.status} ${absence.type }[<fmt:formatDate value="${absence.datetime}" pattern="M/d"/>]
                                            </a>
                                            <c:if test="${not loop.last}">
                                              <br/>
                                            </c:if>
										</c:set>
									</c:if>
									<c:if test="${!showApproved}">
										<c:set var="cellcontents">
											${cellcontents}
											<span class="show-absence-onhover">
												<a target="_blank" href="/director/viewabsence?absenceid=${absence.id }&newindow=true" >
                                                  ${absence.status} ${absence.type }[<fmt:formatDate value="${absence.datetime}" pattern="M/d"/>]
                                                </a>
                                                <c:if test="${not loop.last}">
                                                  <br/>
                                                </c:if>
											</span>
										</c:set>
									</c:if>
								</c:when>
								<c:otherwise>								
								</c:otherwise>
							</c:choose>
 						</c:forEach>

						<td class="${(event.type.performance)?'highlight':''}">
							${cellcontents}
						</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>
</html>