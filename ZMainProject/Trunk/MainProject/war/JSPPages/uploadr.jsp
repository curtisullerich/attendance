<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.Parser"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.lang.String" %>

<% 
	String info = request.getParameter("tempForUpload");
	Parser.splat(info);
%>
