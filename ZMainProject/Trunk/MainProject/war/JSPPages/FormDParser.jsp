<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="serverLogic.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Parser Form D</title>


<%
	String id = request.getParameter("id");
	String status = request.getParameter("response");
	if(status.equals("Yes"))
	{
		status="Approved";
	}
	else if(status.equals("No"))
	{
		status="Denied";
	}
	else
	{
		status="Pending";
	}
	DatabaseUtil.getFormByID(Long.parseLong(id)).setEmailStatus(status);
	
	DatabaseUtil.refreshForm(DatabaseUtil.getFormByID(Long.parseLong(id)));
	
%>


</head>
<body>

</body>
</html>