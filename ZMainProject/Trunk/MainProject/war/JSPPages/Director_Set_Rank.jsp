<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="forms.*" %>
 
 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Direcor:Set Rank</title>

<form method="post" action="/setRank" onsubmit=""
	<%
	String netID = (String) session.getAttribute("user");
	User user = null;
	
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
		return;
	}
	else
	{
		user = DatabaseUtil.getUser(netID);
		if (!user.getType().equalsIgnoreCase("Director")) {
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>


<!-- Set Rank Table -->
	<%//for table
		List<User> students = DatabaseUtil.getStudents();
		String table = "<table border='1'>";
		
		String headers ="<tr><h2>"+"Set Students Rank:";
		headers+="</h2>";
		headers+="</tr>";
		headers+= "<tr><td>Name</td><td>NetID</td><td> Section </td><td>Ranks</td>";

		headers+="</tr>";
		table+=headers;
		
		for(User s : students)
		{
			String row="<tr>";
			row+="<td>"+ s.getLastName() + " "+s.getFirstName()+"</td>";
			row+="<td>"+ s.getNetID()+"</td>";
			row+="<td>"+ s.getSection()+"</td>";
			
			row+="<td>"+ "<input name='Rank' id ='Rank'>"+"</input>"+"</td>";
			table+=row;
		}
		
		table+="</table>";
	%>
	
	<button type="Submit" name = "Submit" type ="Submit">Submit</button>
	

	<%//for filters
		String filters = "";
	%>
		




</head>
<body>
		
	<div style='height: 100%; width: 100%; float: left; overflow:auto'><%= table %></div>

</body>
</html>