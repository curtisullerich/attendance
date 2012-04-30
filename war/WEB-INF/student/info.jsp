<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
	
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
	
	<!--*********************Page Trail*****************************-->

	
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Student_Page.jsp" title="Student Page">Student</a>
		>
		<a href="/JSPPages/Student_Edit_Info.jsp" title="Edit Student Info Page">Edit Student Info</a>

		You are logged in as <c:out value="${auth.user.name}" />
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/auth/logout'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	
	<!--*********************info*****************************-->
	
		<h1><c:out value="${user.netID}" /></h1>
		<form action="/student/info" method="post" accept-charset="utf-8">
		
		<dl class="block-layout">
			<dt><label class="required" for="FirstName">First Name</label></dt>
			<dd><input type="text" name="FirstName" value="<c:out value="${user.firstName}" />" /></dd>
			
			<dt><label class="required" for="LastName">Last Name</label></dt>
			<dd><input type="text" name="LastName" value="<c:out value="${user.lastName}" />" /></dd>
			
			<dt><label for="NetID">NetID</label></dt>
			<dd>
				<input type="text" name="NetID" value="<c:out value="${user.netID}" />" />
				<c:if test="${not empty NetID_error}">
					<ul class="errors">
						<li><c:out value="${NetID_error}" /></li>
					</ul>
				</c:if>
			</dd>
			
			<dt><label for="UniversityID">University ID</label></dt>
			<dd><input type="text" name="UniversityID" value="<c:out value="${user.universityID>0?user.universityID:''}" />" /></dd>
			
			<dt><label for="Section">Section</label></dt>
			<dd>
				<select name="Section" id="Section">
					<option value="">(Select One)</option>
					<c:forEach items="${sections}" var="s" varStatus="loop">
				        <option value="<c:out value="${s.value}" />"
				        	${user.section==s.value ? 'selected="true"' : ''}
				        	><c:out value="${s.displayName}" /></option>
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
	
					<tr>
						<td><label for="Current Password">Current Password</label></td>
						<td><input type= "password" name="Current Password" id="Current Password" /></td>	
					</tr>

					<tr>
						<td><label for="Password">New Password</label></td>
						<td><input type= "password" name="Password" id="Password" /></td>	
					</tr>
					
					<tr>
						<td><label for="Password2">Re-Enter New Password</label></td>
						<td><input type= "password" name="Password2" id="Password2"/></td>	

					</tr>
					
					<tr>
						<td><input type="password" name="Hashed Password"
								id="Hashed Password" style="display: none" /></td>
					</tr>	


				</table>
				
				<p>New Passwords are Optional</p>
				
				<input type="submit" value="Save Info" name="SaveInfo"/>
				<input type="button" value="Back" name="Back" onclick="window.location = '/student'"/>
		</form>		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>