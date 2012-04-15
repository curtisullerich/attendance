<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="forms.*" %>
<%@ page import="comparators.*" %>
 
<html>
	<head>
		<title>@10Dance</title>
	</head>
	
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
		if (!user.getType().equalsIgnoreCase("TA")) {
			if(user.getType().equalsIgnoreCase("Student"))
				response.sendRedirect("/JSPPages/Student_Page.jsp");
			else if(user.getType().equalsIgnoreCase("Director"))
				response.sendRedirect("/JSPPages/Director_Page.jsp");
			else
				response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	
	<script>
	
	function help(){
		alert("Helpful information about TA page.")
	}
	
	function confirmData()
	{
		
		
		
		
	} 
	</script>
	
	<!-- Set Rank Table -->
		<%
		//SORT THIS
		
			List<User> students = DatabaseUtil.getStudents();
			User.sortUsersDescending(new StudentSectionComp(), students);
			String table = "<table id=table1 border='1'>";
			
			String headers ="<tr><h2>"+"Set Students Rank:";
			headers+="</h2>";
			headers+="</tr>";
			headers+= "<tr><td>Last Name</td><td>First Name</td><td>NetID</td><td> Section </td><td>Ranks</td>";
	
			headers+="</tr>";
			table+=headers;
			
			int i=0;
			for(User s : students)
			{
				String row="<tr>";
				row+="<td>"+ s.getLastName()+"</td>";
				row+= "<td>"+s.getFirstName()+"</td>";
				row+="<td>"+s.getNetID()+"</td>";
				row+="<td>"+ s.getSection()+"</td>";
				
				row+="<td>"+ "<input name ='Rank"+i+"' id ='Rank"+i+"'>"+"</input>"+"</td>";
				row+="<td style='display: none'> <input id ='netID"+i+"'  name = 'netID"+i+"' value='"+s.getNetID()+"'></input></td>";
	
				table+=row;
				i++;
			}
			table+="</table>";
		%>
	</head>
	<body>
		
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/TA_Page.jsp" title="TA Page">TA</a>
		>
		<a href="/JSPPages/TA_Set_Rank.jsp" title="Set Student Ranks">Set Ranks</a>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	
			
		<div style='height: 100%; width: 100%; float: left; overflow:auto'>
		<form method ="post"  action="/setRank" onsubmit="return confirmData();">
			<%= table %>
			<br/>
			<button type="submit" name = "Submit" >Submit</button>	
			<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/TA_Page.jsp'"/>	
		</form>
	
		</div>
		
	</body>
</html>