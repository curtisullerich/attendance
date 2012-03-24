<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.Parser"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.lang.String" %>

<!--  <script>
	var str = document.getElementById("tempForUpload").innerHTML;
</script>  -->

<%
	String info = request.getParameter("tempForUpload");
	//String info= "<script> document.writeln(str)</script>";
	out.println("value="+info);
  	//Parser.splat(info);
%>

<!-- This should be changed to reflect successful processing when we can -->
<!-- Parser.splat(info) should return true if successful -->

<script>
	if (<%=info != null%>)
	{
		localStorage.setItem("success", true);
	}
	else
	{
		localStorage.setItem("success", false);
	}
</script>
