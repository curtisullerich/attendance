<%
response.setStatus(301);
response.setHeader("Location", "auth");
response.setHeader("Connection", "close");
%>