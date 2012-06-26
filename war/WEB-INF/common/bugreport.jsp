<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />



<form method='post' action='/public/bugreport'>
	<!-- We could include hidden field and dump any data we want here on page load as well. -->
	<label for="Severity">Severity</label>
	<select name="Severity">
		<option value="critical">Critical (data affected)</option>
		<option value="high">High (functionality affected)</option>
		<option value="medium">Medium (annoyance)</option>
		<option value="low">Low (feature request)</option>		
	</select>	
	<br/>
	<label for="Description">Please let us know as many details about what happened as possible.
	<br/>
	(What you did, what the results were, etc.)</label>
	<br/>
	<textarea rows="6" cols="50" name="Description"></textarea>
	<br/>
	<input type="hidden" name="redirect" value="${pagetemplate.uri}" />
	<input type="submit" value="Submit" name="Submit"/>
</form>