<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="forms.*" %>



<html>

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
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>


<!-- FORM A -->
	<%//for table
	//FROM A *********************************************************************
		List <Form> forms = DatabaseUtil.getAllForms();
		
		String table = "<table border='1'>";
		
		String headers ="<tr><h2>"+"form A";
		headers+="</h2>";
		headers+="</tr>";
		headers+= "<tr><td>netID</td><td>Name</td><td>Type of Form</td><td>Start Date</td><td>Reason</td><td>Status</td><td>Delete</td>";

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
	
	<%
	//FROM B *********************************************************************
	String table1 = "<table border='1'>";
	forms = DatabaseUtil.getAllForms();
	String headers1 ="<tr><h2>"+"form B";
	headers1+="</h2>";
	headers1+="</tr>";
	headers1+= "<tr><td>netID</td><td>Name</td><td>Type of Form</td><td>Department</td><td>Course</td><td>Section</td><td>Building</td><td>Start Date</td><td>End Date</td><td>Until</td><td>Comment</td><td>View</td>";

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
			row+="<td>"+f.getReason()+"</td>";//comment
			row += "<td><b><a href='/JSPPages/View_Student_Form.jsp?viewForm="+f.getID()+"'>"+"View"+"</a></b></td>";

			
			row +="</tr>";
			table1+=row;
		}
	}
	
	table1+="</table>";
	%>
	
		<%//for table
	//FROM C *********************************************************************
		
		String table2 = "<table border='1'>";
		
		String headers2 ="<tr><h2>"+"form C";
		headers2+="</h2>";
		headers2+="</tr>";
		headers2+= "<tr><td>netID</td><td>Name</td><td>Type of Form</td><td>Start Date</td><td>Reason</td>";

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
		
			row+="<td>"+f.getReason()+"</td>";
			row += "<td><b><a href='/JSPPages/View_Student_FormAC.jsp?viewForm="+f.getID()+"'>"+"View"+"</a></b></td>";

			row +="</tr>";
			table2+=row;
			}
		}
		table2+="</table>";
	%>

	<%//for table
	//FROM D *********************************************************************
		
		String table3 = "<table border='1'>";
		
		String headers3 ="<tr><h2>"+"form D";
		headers3+="</h2>";
		headers3+="</tr>";
		headers3+= "<tr><td>netID</td><td>Name</td><td>Type of Form</td><td>Email To</td><td>Total Hours</td><td>Start Date</td><td>Reason</td>";

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
			row+="<td>"+f.getReason()+"</td>";
			row += "<td><b><a href='/JSPPages/View_Student_FormD.jsp?viewForm="+f.getID()+"'>"+"View"+"</a></b></td>";

			row +="</tr>";
			table3+=row;
			}
		}
		table3+="</table>";
	%>

	
	<%//for filters
		String filters = "";
	%>
	
</head>
	<body>
		<div style='height: 100%; width: 100%; float: left; overflow:auto'><%= table %><%= table1 %><%= table2 %><%= table3 %></div>
	</body>
</html>


