<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<html>
  <head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
    <link rel="stylesheet" type="text/css" href="/css/404.css">
	<title>@10Dance 404!!</title>
	<script src="/js/common.js"></script>
  </head>
  <body>
  	<jsp:include page="/WEB-INF/template/header.jsp" />
   	<div class="errorbox">
   		<div class="heading">
			<h1>You've been 404'd!!</h1>
		</div>
		<br/>
		<div class="whitebox">
			This is not the page you're looking for.
			<br/>
			Unfortunately, we can't find it.
			<br/>
			Try double-checking the URL you entered.
			<br/>
			If you got this error by clicking on a link, contact the developers at <a href="mailto:a10dance@iastate.edu?Subject=@10dance%20404%20Error">a10dance@iastate.edu</a>.
			<br/>
			<br/>
			You can easily find your way to the main
			<br/>
			@10dance page using this fancy-shmancy button:
			<div class="centeralign">
				<input class="button" type="button" id="main" value="Main Home Page" onclick="window.location='/'" />
			</div>
		</div>
		<input class="back" type="button" id="back" value="Back" onclick="goBack()"/>
		<br/>
   </div>
    <jsp:include page="/WEB-INF/template/footer.jsp" />
  </body>
</html>