<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Administrative Data Export/Import</h1>
		
		<br/>
		
		<h2>Database Version</h2>
		<p>
			Application code uses database version: v<c:out value="${ObjectDatastoreVersion}" /><br/>
			The following versions are available:<br/>
			<ul>
			<c:forEach items="${AppData}" var="SingleAppData" varStatus="loopStatus">
				<c:choose>
					<c:when test="${SingleAppData.datastoreVersion == ObjectDatastoreVersion}">
						<li>
							Current version: v<c:out value="${SingleAppData.datastoreVersion}" />
						</li>
					</c:when>
					<c:when test="${SingleAppData.datastoreVersion > ObjectDatastoreVersion}">
						<li style="color:red">
							Future version: v<c:out value="${SingleAppData.datastoreVersion}" />
						</li>
					</c:when>
					<c:when test="${SingleAppData.datastoreVersion < ObjectDatastoreVersion}">
						<li>
							Past version: v<c:out value="${SingleAppData.datastoreVersion}" />
							<ul>
								<li>
									<form action="data_migrate" method="GET" style="display:inline">
										<input type="submit" name="submit" value="Migrate" title="Copy this data to current application version" />
										<input type="hidden" name="version" value="<c:out value="${SingleAppData.datastoreVersion}" />"/>
									</form>
								</li>
								<li>
									<form action="data_delete" method="POST" style="display:inline">
										<input type="submit" name="submit" value="Delete" title="Delete this data" />
										<input type="hidden" name="version" value="<c:out value="${SingleAppData.datastoreVersion}" />"/>
									</form>
								</li>
							</ul>
						</li>
					</c:when>
				</c:choose>
			</c:forEach>
			</ul>
		</p>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>