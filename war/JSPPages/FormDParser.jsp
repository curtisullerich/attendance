<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="serverLogic.*" %>
<%@ page import="forms.*" %>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>@10Dance</title>
<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">

<!--tried:  http://localhost:8888/JSPPages/FormDParser.jsp?id=1&response=yes -->
<%
	String id = request.getParameter("id");
	String status = ""+request.getParameter("response");
	System.out.println("1"+status);
	if(status.equals("yes"))
	{
		status="Approved";
	}
	else if(status.equals("no"))
	{
		status="Denied";
	}
	else
	{
		status="Pending";
		System.out.println(status);
	}
	
	Form f= DatabaseUtil.getFormByID(Long.parseLong(id));
	f.setEmailStatus(status);
	
	
	DatabaseUtil.refreshForm(f);
	
%>


</head>
	<body>
	<p>Parsing Response...</p>
	</body>
</html>