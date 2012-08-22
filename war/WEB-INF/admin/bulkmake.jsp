<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />

<!DOCTYPE html>

<html>
  <head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
    <script>
    	function superconfirm() {
    		var a = confirm('Message 1 of 5: This will wipe the ENTIRE DATABASE. Are you absolutely sure you want this to happen?');
			if (a){
	    		var b = confirm('Message 2 of 5: Really?');
				if (b) {
		    		var c = confirm('Message 3 of 5: It is impossible to undo this.');
					if (c) {
			    		var d = confirm('Message 4 of 5: Positive about this?');
						if (d) {
				    		var e = confirm('Message 5 of 5: Clicking OK will destroy everything, including your user account. Go ahead if you really know what you are doing.');
				    		if (e) {
				    			return true;
				    		}
						}
					}
				}
			}
			return false;
    	}
    
    </script>
  </head>
  <body>
  	<jsp:include page="/WEB-INF/template/header.jsp" />
  	
	<h1><c:out value="${pagetemplate.siteTitle}" /> <fmt:formatDate value="${date}" pattern="yyyy" /></h1>
	<h2>Create all the Students</h2>
	
	<form method="post" accept-charset="utf-8">
		<dl class="block-layout">
			<dt><label class="required" for="Data">All the Data</label></dt>
			<b>Format:</b> first name, last name, primary email, secondary email, type, section, uid, year, major, rank, minutesavailable
			<dd>
				<textarea rows="20" cols="80" name="Data" wrap="physical"></textarea>
			</dd>
		</dl>
		<input type="submit" value="Go" name="Go" onclick="return confirm('This will attempt to create a bunch of fake student. Please only continue if this is a testing version of the application.')" />
	</form>
	<form method="post" accept-charset="utf-8">
	Delete ALL the things!
	<input  type="submit" value="Delete All" name="DeleteAll" onclick="return superconfirm()">
	
	</form>
	<jsp:include page="/WEB-INF/template/footer.jsp" />
</body>
</html>