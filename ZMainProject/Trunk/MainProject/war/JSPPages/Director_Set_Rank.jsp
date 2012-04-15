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

<script>
function confirmData()
{
	
	
	
	
} 

</script>

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
		String table = "<table id=table1 border='1'>";
		
		String headers ="<tr><h2>"+"Set Students Rank:";
		headers+="</h2>";
		headers+="</tr>";
		headers+= "<tr><td>Name</td><td>NetID</td><td> Section </td><td>Ranks</td>";

		headers+="</tr>";
		table+=headers;
		
		int i=0;
		for(User s : students)
		{
			
			String row="<tr>";
			row+="<td>"+ s.getLastName() + " "+s.getFirstName()+"</td>";
			row+="<td>"+s.getNetID()+"</td>";
			row+="<td>"+ s.getSection()+"</td>";
			
			row+="<td>"+ "<input name ='Rank"+i+"' id ='Rank"+i+"'>"+"</input>"+"</td>";
			row+="<td style='display: none'> <input id ='netID"+i+"'  name = 'netID"+i+"' value='"+s.getNetID()+"'></input></td>";

			table+=row;
			i++;
		}
		
		table+="</table>";
	%>
<%

<form action="/register" method="post"
onSubmit="return confirmAndHashData();" accept-charset="utf-8">
<table>
	<tr>
		<td><label for="FirstName">First Name</label></td>
		<td><input type="text" name="FirstName" id="FirstName" /></td>
	</tr>

	<tr>
		<td><label for="LastName">Last Name</label></td>
		<td><input type="text" name="LastName" id="LastName" /></td>
	</tr>

	<tr>
		<td><label for="NetID">NetID</label></td>
		<td><input type="text" name="NetID" id="NetID" /></td>
	</tr>

	<tr>
		<td><label for="UniversityID">UniversityID</label></td>
		<td><input type="text" name="UniversityID" id="UniversityID" /></td>
	</tr>


</table>
<input type="submit" value="Submit" name="Submit" />
</form>
%>	
	

	<%//for filters
		String filters = "";
	%>
		




</head>
<body>
		
	<div style='height: 100%; width: 100%; float: left; overflow:auto'>
	<form method ="post"  action="/setRank" onsubmit="return confirmData();">
		<%= table %>
		<button type="submit" name = "Submit" >Submit</button>		
	</form>

	</div>
	
</body>
</html>