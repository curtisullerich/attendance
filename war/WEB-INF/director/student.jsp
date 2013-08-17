<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />
<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
		<script type="text/javascript">
//this will need to be uncommented when we remove the report bug div		
// 		$(document).ready(function()
// 			{	
// 			  //hide the all of the element with class msg_body
// 			  $(".msg_body").hide();
// 			  //toggle the componenet with class msg_body
// 			  $(".msg_head").click(function()
// 			  {
// 			    $(this).next(".msg_body").slideToggle(200);
// 			  });
// 			});
		</script>
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Student Info for <c:out value="${user.name}" /> (<c:out value="${user.id}" />)</h1>

		Click to expand categories.
		<%--Note that this is a near-duplicate of /student/info.jsp --%>
		<div class="msg_list">
			<h2 class="msg_head">Student Information </h2>
			<div class="msg_body">
			
			<form method="post" action="/director/studentinfo" accept-charset="utf-8">
				<dl class="block-layout">
					<dt><label class="required" for="FirstName">First Name</label></dt>
					<dd><input type="text" name="FirstName" value="<c:out value="${user.firstName}" />" /></dd>
					
					<dt><label class="required" for="LastName">Last Name</label></dt>
					<dd><input type="text" name="LastName" value="<c:out value="${user.lastName}" />" /></dd>

                    <dt><label for="Grade">Grade</label></dt>
                    <dd>
                      <input type="text" value="<c:out value="${user.grade}"/>" disabled readonly/>
                    </dd>
          
					<dt><label for="NetID">NetID</label></dt>
					<dd>
						<input type="text" value="<c:out value="${user.id}"/>" disabled readonly/>
					</dd>
						<input type="hidden" name="NetID" value="<c:out value="${user.id}"/>"/>				
						<input type="hidden" name="id" value="<c:out value="${user.id}"/>"/>				
	
					<dt><label for="UniversityID">University ID</label></dt>
					<dd><input type="text" name="UniversityID" value="<c:out value="${user.universityID}" />" disabled readonly/></dd>
					
					<dt><label for="MinutesAvailable">Minutes Available from Time Worked Form Submissions</label></dt>
					<dd><input type="number" name="MinutesAvailable" value="<c:out value="${user.minutesAvailable}" />"/></dd>
						
					
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
					<dt><label for="Rank">Rank</label></dt>
					<dd><input type="text" name="Rank" value="<c:out value="${user.rank}" />" /></dd>
					
				</dl>
		
				<input type="submit" value="Save Info" name="SaveInfo"/>
				
			</form>
		</div>
		<h2 class="msg_head">Forms (${fn:length(forms)})</h2>
		<div class="msg_body">
			<jsp:include page="/WEB-INF/common/forms.jsp" />
		</div>
		
		<h2 class="msg_head">Absences (${fn:length(absences)})</h2>
		<div class="msg_body">
			<jsp:include page="/WEB-INF/common/absences.jsp" />
		</div>
		<h2 class="msg_head">Delete</h2>
		<div class="msg_body">
			<form method="post" action="/director/deletestudent" accept-charset="utf-8">
				<input type="hidden" name="deleteid" value="${user.id }"/>
				<input type="submit" value="Delete this student" onclick="return confirm('Do you really want to delete this student?')"/>
			</form>
		</div>		
		</div>
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>