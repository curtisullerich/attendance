<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />

<!DOCTYPE html>

<html>
<head>
  <jsp:include page="/WEB-INF/template/head.jsp" />
</head>
<body>
  <jsp:include page="/WEB-INF/template/header.jsp" />
  
    
  <h1>${pagetemplate.title} <fmt:formatDate value="${date}" pattern="yyyy"/></h1>
  <h2>Welcome</h2>
  
  <p>This is the attendance system for the Iowa State University Cyclone Football 'Varsity' Marching Band.
  This was made by Daniel Stiner, Curtis Ullerich, Brandon Maxwell, and Todd Wegter.
  If you're interested in how this was developed, visit the project's <a href="http://www.github.com/curtisullerich/attendance">page on Github</a>.
  </p>

  <h2>Registering and Logging In</h2>
  <p>This system requires that you have a valid Google account on the iastate domain (your standard CyMail). 
    Any ISU student account created within the last four years falls into this category. Staff accounts, and older
    accounts may not work. Login will redirect you to Google's sign in page. Just login with your full ISU email address
    and normal password. If you have already registered, you will then be logged in. If you haven't, you will see the 
    registration page.
    </p>
  <center>
    <p>
      <a style="font-size:large" href="/auth/login">Login with NetID and Register</a>
    </p>
  </center>

  <p>
    <i>Note that if you are currently logged in to a gmail account, you will first need to <a href="/auth/logout">log out of it</a>
    so that you can login with your NetID and access the attendance system.</i>
  </p>
  
  <jsp:include page="/WEB-INF/template/footer.jsp" />
</body>

</html>