<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<%
	DatabaseUtil.removeForm(DatabaseUtil.getFormByID(new Long(request.getParameter("id"))));
%>
<script>
	window.history.back;
</script>