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
		
		<h2>Backup / Restore</h2>
		<p>
			<a href="export/data.json">Export entire current database</a> (Useful as a backup)<br/>
			<form class="grid-10">
			<fieldset>
				<label for="file">Import data: </label>
				<input id="file" name="file" type="file" accept="application/json" />
				<br/>
				Expects a file previously exported from above link.
				<br/>
				<input type="submit" value="Import" style="float:right" />
				</fieldset>
			</form>
			<br style="clear:both" />
		</p>
		
		<br/>
		
		<h2>Database Version</h2>
		<p>
			Application code uses database version: v<c:out value="${ObjectDatastoreVersion}" /><br/>
			The following versions are available:<br/>
			<ul>
			<c:forEach items="${AppData}" var="SingleAppData" varStatus="loopStatus">
				<li>
					<c:choose>
						<c:when test="${SingleAppData.datastoreVersion == ObjectDatastoreVersion}">
							Current version:
						</c:when>
						<c:when test="${SingleAppData.datastoreVersion > ObjectDatastoreVersion}">
							Future version:
						</c:when>
						<c:when test="${SingleAppData.datastoreVersion < ObjectDatastoreVersion}">
							Past version:
						</c:when>
					</c:choose>
					v<c:out value="${SingleAppData.datastoreVersion}" />
				
					<ul>
					<c:choose>
						<c:when test="${SingleAppData.datastoreVersion < ObjectDatastoreVersion}">
							<li>
								<form action="data_migrate" method="GET" style="display:inline">
									<input type="submit" value="Migrate" title="Copy this data to current application version" />
									<input type="hidden" name="version" value="<c:out value="${SingleAppData.datastoreVersion}" />"/>
								</form>
							</li>
							<li>
								<form action="data_delete" method="POST" style="display:inline">
									<input type="submit" value="Delete" title="Delete this data" />
									<input type="hidden" name="version" value="<c:out value="${SingleAppData.datastoreVersion}" />"/>
								</form>
							</li>
						</c:when>
					</c:choose>
					</ul>
				</li>
			</c:forEach>
			</ul>
		</p>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>