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
  	Parser.splat(info);
%>

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
