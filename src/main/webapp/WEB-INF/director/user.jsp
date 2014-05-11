<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>User: <c:out value="${user.netID}" /></h1>
		<form method="post" accept-charset="utf-8">
		
		<dl class="block-layout">
			<dt><label for="Type">Type</label></dt>
			<dd>
				<select name="Type" id="Type">
					<c:forEach items="${types}" var="t" varStatus="loop">
				        <option value="<c:out value="${t}" />"
				        	${user.type==t ? 'selected="true"' : ''}
				        	><c:out value="${t}" /></option>
				    </c:forEach>
				</select>
			</dd>
			
			<dt><label class="required" for="FirstName">First Name</label></dt>
			<dd><input type="text" name="FirstName" value="<c:out value="${user.firstName}" />" /></dd>
			
			<dt><label class="required" for="LastName">Last Name</label></dt>
			<dd><input type="text" name="LastName" value="<c:out value="${user.lastName}" />" /></dd>
			
			<dt><label for="NetID">NetID</label></dt>
			<dd>
				<input type="text" name="NetID" value="<c:out value="${user.netID}"/>" disabled readonly/>
				<!-- Since setting a field to disabled seems to prevent it from being posted, added this hidden field to post netid -->
				<input type="hidden" name="NetID" value="<c:out value="${user.netID}"/>"/>
			</dd>

		</dl>
	

				<input type="submit" value="Save Info" name="SaveInfo"/>
				<input type="button" value="Back" name="Back" onclick="window.location = '/admin/users'"/>
		</form>		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>