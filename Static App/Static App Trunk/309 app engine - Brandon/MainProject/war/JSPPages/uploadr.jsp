<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.Parser"%>
<%@ page import="java.util.ArrayList"%>


<script>
	<%ArrayList<String> strs = new ArrayList<String>();%>
	<%String[] str = new String[1000]; %>
	for (var i = 0; i < localStorage.length; i++) 
	{
		<%str[%> =i <%]%> = localStorage.key(i);
<%-- 		<% strs.add(%> localStorage.key(i) <%);%> --%>
		
		//note that I intend to use the key, NOT the value here. Values are just what we print.
	}




</script>