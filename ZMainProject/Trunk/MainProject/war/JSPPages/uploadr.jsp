<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.Parser"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.lang.String" %>

<!-- This should be changed to reflect successful processing when we can -->
<!-- Parser.splat(info) should return true if successful -->

<head>
<title>@10Dance</title>
<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
<script src = "/JSPPages/script.js"></script>
<script src = "/JSPPages/sha.js"></script>
<link rel="stylesheet" type="text/css" href="/MobileApp/FieldAppCSS.css" />
<script>
	window.onload = function(){
		<%String info = request.getParameter("tempForUpload");
			String worked = "success";
			//out.println("value="+info);
			//System.out.println("We got here");
		%>
		<%if (info != null && !info.equalsIgnoreCase(""))
		{%>
			<%worked = Parser.splat(info);%>
			localStorage.setItem("success", "true");
		<%}%>
		
		<%if (info == null || worked.contains("fail"))
		{%>
			localStorage.setItem("success", "false");
			localStorage.setItem("uploadError", "<%= worked %>");
		<%}%>
		
		<%if (info.equalsIgnoreCase(""))
		{%>
			localStorage.setItem("success", "emptyString");	
		<%}%>
		window.location = "/MobileApp/FieldAppMain.html";
	}
	
</script>
</head>
<body>
		<h1><br/>Uploading...</h1>
</body>
