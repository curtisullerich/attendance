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


<!-- FORM A -->
	<%//for table
	//FROM A *********************************************************************
		List <Form> forms = DatabaseUtil.getAllForms();
		
		String table = "<table border='1'>";
		
		String headers ="<tr><h2>"+"Set Students Rank:";
		headers+="</h2>";
		headers+="</tr>";
		headers+= "<tr><td>Name</td><td> Section </td><td>Ranks</td>";

		headers+="</tr>";
		table+=headers;
				
		for(Form f : forms)
		{
			if(f.getType().equals("FormA"))
			{
				String row="<tr>";
				//row+="<td>"+ f.getNetID()+"</td>";
				row += "<td><b><a href='/JSPPages/Director_Student_View.jsp?student="+f.getNetID()+"'>"+f.getNetID()+"</a></b></td>";
				row+="<td>"+DatabaseUtil.getUser(f.getNetID()).getFirstName()+" "+DatabaseUtil.getUser(f.getNetID()).getLastName()+"</td>";
				row+="<td>"+ f.getType()+"</td>";
				row+="<td>"+ f.getStartTime().toString(12)+"</td>";
				row+="<td>"+f.getReason()+"</td>";
				row+="<td>"+f.getStatus()+"</td>";
				row += "<td><b><a href='/JSPPages/View_Student_FormAC.jsp?viewForm="+f.getID()+"'>"+"View"+"</a></b></td>";

				row +="</tr>";
				table+=row;
			}
		}
		table+="</table>";
	%>

	<%//for filters
		String filters = "";
	%>
		




</head>
<body>
		<div style='height: 100%; width: 100%; float: left; overflow:auto'><%= table %></div>

</body>
</html>