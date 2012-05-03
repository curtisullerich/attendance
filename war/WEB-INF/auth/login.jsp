<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="date" class="java.util.Date" />


<html>
  <head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
  </head>
  <body>
  	<jsp:include page="/WEB-INF/template/header.jsp" />
    <h1>ISU Varsity Marching Band / Fall <fmt:formatDate value="${date}" pattern="yyyy" /></h1>
    <h2>Login</h2>
    <form action="/auth/login" method="post" accept-charset="utf-8">
    	<dl class="block-layout">
    		<dt><label></label></dt>
			<dd>
				<c:if test="${not empty errors}">
				    <ul class="errors">
				    	<c:forEach items="${errors}" var="e" varStatus="loop">
				    		<li><c:out value="${e}" /></li>
				    	</c:forEach>
				    </ul>
				</c:if>
			</dd>
    		<dt><label></label></dt>
    		<dd>
	    		<fieldset>
	    			<dl class="block-layout" style="width:150pt">
		    			<dt>
			    			<label for="NetID">NetID</label>
			    		</dt>
			    		<dd>
			    			<input type="text" name="NetID" value="<c:out value="${NetID}"/>" style="width:100%;padding-right:74px"/>
			    			<div style="float: right;position: relative;right: 4px;top: -22px;color: grey;">@iastate.edu</div>
			    		</dd>
			    		<dt>
			    			<label for="Password">Password</label>
			    		</dt>
			    		<dd>
			    			<input style="width:100%" type="Password" name="Password" />
			    		</dd>
		    		</dl>
	    		</fieldset>
	    	</dd>
    		<dt><label></label></dt>
			<dd>
				<dl class="inline-layout">
					<dt><label></label></dt>
					<dd><input type="submit" name="login" value="Login"></dd>
					<dt><label></label></dt>
					<dd><input type="button" name="register" value="Register" onClick="window.location='/auth/register'; returnfalse;"></dd>
					<dt><label></label></dt>
					<dd style="float:right"><input type="submit" name="resetpassword" value="Reset Password"></dd>
				</dl>
			</dd>
    	</dl>
			
	</form>	
    
    <jsp:include page="/WEB-INF/template/footer.jsp" />
  </body>
</html>