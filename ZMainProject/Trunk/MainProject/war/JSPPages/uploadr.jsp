<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.Parser"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.lang.String" %>

<!-- This should be changed to reflect successful processing when we can -->
<!-- Parser.splat(info) should return true if successful -->
<script src = "jspScript.js"></script>
<script>
	if(validateTA()) 
	{
		<%
			String info = request.getParameter("tempForUpload");
			//out.println("value="+info);
			Parser.splat(info);
			//System.out.println("We got here");
		%>
		<%if (info != null && !info.equalsIgnoreCase(""))
		{%>
			localStorage.setItem("success", true);
		<%}%>
		
		<%if (info == null)
		{%>
			localStorage.setItem("success", false);
		<%}%>
		
		<%if (info.equalsIgnoreCase(""))
		{%>
			localStorage.setItem("success", "emptyString");	
		<%}%>
	}
	window.location = "/MobileApp/FieldAppMain.html";
</script>
