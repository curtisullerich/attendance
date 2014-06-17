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
		
			<form class="grid-8" method="POST" action="/admin/backup" enctype="multipart/form-data">
				<fieldset>
					<legend>Data Backup:</legend>
					<p>Backup all data currently in the system by sending it via email to the directors.<p/>
					<p>Note: May take several minutes for the backup to complete.</p>
					<input type="submit" value="Backup" style="float:right" />
				</fieldset>
			</form>
			
			<form class="grid-8" method="POST" action="/admin/restore" enctype="multipart/form-data">
				<fieldset>
					<legend>Data Restore:</legend>
					<p>Import a previous backup file.</p>
					<label for="file">Data file: </label>
					<input name="file" type="file" accept="application/json" />
					<br/><br/>
					<p><strong>WARNING:</strong> Will overwrite all data currently in the system</p>
					<input type="submit" value="Restore" style="float:right" />
				</fieldset>
			</form>
			<form class="grid-8" method="POST" action="/admin/refresh" enctype="multipart/form-data">
				<fieldset>
					<legend>Refresh Datastore:</legend>
					<p>This is a tool for the developers to trigger a full refresh of properties (like grades) 
					in the case of calculation changes in the code.</p>
					<input type="submit" value="Refresh" style="float:right" />
				</fieldset>
			</form>
			<br style="clear:both" />
		</p>
		
		<br/>
		
		<h2>Database Version</h2>
		<p>
			Application code uses database version: v<c:out value="${ObjectDatastoreVersion}" />
		</p>
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>