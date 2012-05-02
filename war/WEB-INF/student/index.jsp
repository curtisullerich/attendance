<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
	
<!--*********************Page Trail*****************************-->
		<div class="trail">
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Student_Page.jsp" title="Student Page">Student</a>
		</div>
		<div class="status">	
		You are logged in as <c:out value="${auth.user.name}" />
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	
		</div>	


<!--*********************info*****************************-->

		<h1>Student Page</h1>

--------------
<p>
		<!--********************* Button *****************************-->
		<input type="submit" id="listForms" onClick="listForms();"  value="Submit a Form"/>
		<input type="submit" onClick="viewForms();"  value="View Submitted Forms"/>
		<p>
			<div id="formsDiv" style="display: none">
				<p><a href="/JSPPages/Student_Form_A_Performance_Absence_Request.jsp">Form A - Performance Absence Request</a></p>
				<p><a href="/JSPPages/Student_Form_B_Class_Conflict_Request.jsp">Form B - Class Conflict Request</a></p>
				<p><a href="/JSPPages/Student_Form_C_Rehearsal_Excuse.jsp">Form C - Request for Excuse from Rehearsal</a></p>
				<p><a href="/JSPPages/Student_Form_D_TimeWorked.jsp">Form D - Time Worked</a></p>
			</div>
		</p>
		<input type ="submit" onClick="window.location = '/JSPPages/Student_View_Attendance.jsp';"  value = "View Attendance">
		<br/>
		<input type ="submit" onClick="window.location = '/student/info.jsp';"  value = "Edit My Information">
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>