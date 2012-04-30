<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="forms.*" %>



<html>
	<title>@10Dance</title>
	<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
<head>
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
			if(user.getType().equalsIgnoreCase("TA"))
				response.sendRedirect("/JSPPages/TA_Page.jsp");
			else if(user.getType().equalsIgnoreCase("Student"))
				response.sendRedirect("/JSPPages/Student_Page.jsp");
			else
				response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
<!--*********************Page Trail*****************************-->
	
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Director_Page.jsp" title="PageTrail_director">Director</a>
			>
			<a href="/JSPPages/Director_View_AllForms.jsp">View All Forms</a>
			
		You are logged in as the Director (<%= user.getFirstName() + " " + user.getLastName()%>)
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	

</head>
	<body>

<h1>View All Submitted Forms</h1>
<!-- FORM A -->
	<%//for table
	//FORM A *********************************************************************
		List <Form> forms = DatabaseUtil.getAllForms();
		
		String table = "<table border='1'>";
		
		String headers ="<tr><h2>"+"Form A";
		headers+="</h2>";
		headers+="</tr>";
		headers+= "<tr><th>netID</th><th>Name</th><th>Type of Form</th><th>Start Date</th><th>Reason</th><th>Status</th>";

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
				row+="<td><button onClick=\"window.location='/JSPPages/removeForm.jsp?id="+f.getID()+"'\">delete</button></td>";//delete
				row+="<td><button onClick=\"window.location='/JSPPages/approveForm.jsp?id="+f.getID()+"'\">approve</button></td>";//approve
				row+="<td><button onClick=\"window.location='/JSPPages/denyForm.jsp?id="+f.getID()+"'\">deny</button></td>";//deny
				row += "<td><button onClick=\"window.location='/JSPPages/ViewMessages.jsp?parentType="+f.getType()+"&parentID="+f.getID()+"'\" value='messages(number)'>messages</button></td>";//messages
				row +="</tr>";
				table+=row;
			}
		}
		table+="</table>";
	%>
	
	<%
	//FORM B *********************************************************************
	String table1 = "<table border='1'>";
	forms = DatabaseUtil.getAllForms();
	String headers1 ="<tr><h2>"+"Form B";
	headers1+="</h2>";
	headers1+="</tr>";
	headers1+= "<tr><th>netID</th><th>Name</th><th>Type of Form</th><th>Department</th><th>Course</th><th>Section</th><th>Building</th><th>Start Date</th><th>End Date</th><th>Until</th><th>Status</th><th>Comment</th>";

	headers1+="</tr>";
	table1+=headers1;
			
	for(Form f : forms)
	{
		if(f.getType().equals("FormB"))
		{
			String row="<tr>";
			//row+="<td>"+ f.getNetID()+"</td>";
			row += "<td><b><a href='/JSPPages/Director_Student_View.jsp?student="+f.getNetID()+"'>"+f.getNetID()+"</a></b></td>";
			
			row+="<td>"+DatabaseUtil.getUser(f.getNetID()).getFirstName()+" "+DatabaseUtil.getUser(f.getNetID()).getLastName()+"</td>";
			row+="<td>"+ f.getType()+"</td>";
			
			row+="<td>"+f.getDeptartment()+"</td>";//department
			row+="<td>"+f.getCourse()+"</td>";//course
			row+="<td>"+f.getSection()+"</td>";//section
			row+="<td>"+f.getBuilding()+"</td>";//building
			row+="<td>"+f.getStartTime().toString(12)+"</td>";//start time
			row+="<td>"+f.getEndTime().toString(12)+"</td>";//end time
			row+="<td>"+f.durationToString()+"</td>";//until + time							//TODO
			row+="<td>"+f.getStatus()+"</td>";
			row+="<td>"+f.getReason()+"</td>";//comment
			row+="<td><button onClick=\"window.location='/JSPPages/removeForm.jsp?id="+f.getID()+"'\">delete</button></td>";//delete
			row+="<td><button onClick=\"window.location='/JSPPages/approveForm.jsp?id="+f.getID()+"'\">approve</button></td>";//approve
			row+="<td><button onClick=\"window.location='/JSPPages/denyForm.jsp?id="+f.getID()+"'\">deny</button></td>";//deny
			row += "<td><button onClick=\"window.location='/JSPPages/ViewMessages.jsp?parentType=Form&parentID="+f.getID()+"'\" value='messages(number)'>messages</button></td>";//messages

			
			row +="</tr>";
			table1+=row;
		}
	}
	
	table1+="</table>";
	%>
	
		<%//for table
	//FORM C *********************************************************************
		
		String table2 = "<table border='1'>";
		
		String headers2 ="<tr><h2>"+"Form C";
		headers2+="</h2>";
		headers2+="</tr>";
		headers2+= "<tr><th>netID</th><th>Name</th><th>Type of Form</th><th>Start Date</th><th>Status</th><th>Reason</th>";

		headers2+="</tr>";
		table2+=headers2;
				
		for(Form f : forms)
		{
			if(f.getType().equals("FormC"))
			{
				
			String row="<tr>";
			//row+="<td>"+ f.getNetID()+"</td>";
			row += "<td><b><a href='/JSPPages/Director_Student_View.jsp?student="+f.getNetID()+"'>"+f.getNetID()+"</a></b></td>";
			row+="<td>"+DatabaseUtil.getUser(f.getNetID()).getFirstName()+" "+DatabaseUtil.getUser(f.getNetID()).getLastName()+"</td>";
			row+="<td>"+ f.getType()+"</td>";
			row+="<td>"+ f.getStartTime().toString(12)+"</td>";
		
			row+="<td>"+f.getStatus()+"</td>";
			row+="<td>"+f.getReason()+"</td>";
			row+="<td><button onClick=\"window.location='/JSPPages/removeForm.jsp?id="+f.getID()+"'\">delete</button></td>";//delete
			row+="<td><button onClick=\"window.location='/JSPPages/approveForm.jsp?id="+f.getID()+"'\">approve</button></td>";//approve
			row+="<td><button onClick=\"window.location='/JSPPages/denyForm.jsp?id="+f.getID()+"'\">deny</button></td>";//deny
			row += "<td><button onClick=\"window.location='/JSPPages/ViewMessages.jsp?parentType=Form&parentID="+f.getID()+"'\" value='messages(number)'>messages</button></td>";//messages

			row +="</tr>";
			table2+=row;
			}
		}
		table2+="</table>";
	%>

	<%//for table
	//FORM D *********************************************************************
		
		String table3 = "<table border='1'>";
		
		String headers3 ="<tr><h2>"+"Form D";
		headers3+="</h2>";
		headers3+="</tr>";
		headers3+= "<tr><th>netID</th><th>Name</th><th>Type of Form</th><th>Email To</th><th>Total Hours</th><th>Start Date</th><th>Status</th><th>Comment</th>";

		headers3+="</tr>";
		table3+=headers3;
				
		for(Form f : forms)
		{
			if(f.getType().equals("FormD"))
			{
				
			String row="<tr>";
			//row+="<td>"+ f.getNetID()+"</td>";
			row += "<td><b><a href='/JSPPages/Director_Student_View.jsp?student="+f.getNetID()+"'>"+f.getNetID()+"</a></b></td>";
			row+="<td>"+DatabaseUtil.getUser(f.getNetID()).getFirstName()+" "+DatabaseUtil.getUser(f.getNetID()).getLastName()+"</td>";
			row+="<td>"+ f.getType()+"</td>";
			row+="<td>"+ f.getEmail()+"</td>";
			row+="<td>"+ f.getHours()+"</td>";
			
			row+="<td>"+ f.getStartTime().toString(12)+"</td>";
			row+="<td>"+f.getStatus()+"</td>";
			row+="<td>"+f.getReason()+"</td>";
			row+="<td><button onClick=\"window.location='/JSPPages/removeForm.jsp?id="+f.getID()+"'\">delete</button></td>";//delete
			row+="<td><button onClick=\"window.location='/JSPPages/approveForm.jsp?id="+f.getID()+"'\">approve</button></td>";//approve
			row+="<td><button onClick=\"window.location='/JSPPages/denyForm.jsp?id="+f.getID()+"'\">deny</button></td>";//deny
			row += "<td><button onClick=\"window.location='/JSPPages/ViewMessages.jsp?parentType=Form&parentID="+f.getID()+"'\" value='messages(number)'>messages</button></td>";//messages

			row +="</tr>";
			table3+=row;
			}
		}
		table3+="</table>";
		

	%>

	
	<%//for filters
		String filters = "";
	%>
		<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Director_Page.jsp'"/>
	
		<div style='height: 100%; width: 100%; float: left; overflow:auto'><%= table %><%= table1 %><%= table2 %><%= table3 %></div>

	</body>
</html>


