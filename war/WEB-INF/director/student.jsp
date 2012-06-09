<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />
<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Student Info for <c:out value="${user.name}" /> (<c:out value="${user.id}" />)</h1>
		
		<form method="post" action="/director/delete" accept-charset="utf-8">
			<input type="hidden" name="deleteid" value="${user.id }"/>
			<input type="submit" value="Delete this student" onclick="confirm('Do you really want to delete this student?')"/>
		</form>

		<%//Note that this is a near-duplicate of /student/info.jsp %>
		<form method="post" action="/director/studentinfo" accept-charset="utf-8">
			<dl class="block-layout">
				<dt><label class="required" for="FirstName">First Name</label></dt>
				<dd><input type="text" name="FirstName" value="<c:out value="${user.firstName}" />" /></dd>
				
				<dt><label class="required" for="LastName">Last Name</label></dt>
				<dd><input type="text" name="LastName" value="<c:out value="${user.lastName}" />" /></dd>
				
				<dt><label for="NetID">NetID</label></dt>
				<dd>
					<input type="text" name="NetID" value="<c:out value="${user.netID}"/>" disabled readonly/>
				</dd>
				
				<dt><label for="UniversityID">University ID</label></dt>
				<dd><input type="text" name="UniversityID" value="<c:out value="${user.universityID}" />" disabled readonly/></dd>
				
				<dt><label for="Section">Section</label></dt>
				<dd>
					<select name="Section" id="Section">
						<option value="">(Select One)</option>
						<c:forEach items="${sections}" var="s" varStatus="loop">
							<option value="<c:out value="${s.value}" />"
								${user.section==s.value ? 'selected="true"' : ''}>
								<c:out value="${s.displayName}" /></option>
						</c:forEach>
					</select>
				</dd>
				
				<dt><label for="Year">Years in band</label></dt>
				<dd>
					<select name="Year" id="Year"  >
						<option value="">(Choose)</option>
						<c:forEach var="i" begin="1" end="10" step="1" varStatus="loop">
							<option ${user.year==i ? 'selected="true"' : ''}><c:out value="${i}" /></option>
						</c:forEach>
					</select>
				</dd>
				
				<dt><label for="Major">Major</label></dt>
				<dd><input type="text" name="Major" value="<c:out value="${user.major}" />" /></dd>
			</dl>
	
			<input type="submit" value="Save Info" name="SaveInfo"/>
			
		</form>
		
		<br/>
		<c:if test="${not empty error_messages}">
			<p class="notify-msg error">
				<strong>Error:</strong>
				<c:forEach items="${error_messages}" var="error_message">
					<c:out value="${error_message}" />
					<br/>
				</c:forEach>
			</p>
		</c:if>
		
		
		<h2>Forms</h2>
		<jsp:include page="/WEB-INF/common/forms.jsp" />
		<hr/>
		
		<h2>Absences</h2>
		<jsp:include page="/WEB-INF/common/absences.jsp" />
		<hr/>
		<h2>Messages</h2>
		<jsp:include page="/WEB-INF/common/threadtable.jsp"/>		
		<hr/>
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>