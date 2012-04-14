<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import = "attendance.*" %>

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

	<%//for table
		List<Event> events = DatabaseUtil.getAllEvents();
		List <Form> forms = DatabaseUtil.getAllForms();
		
		String table = "<table border='1'>";
		
		String headers = "<tr><td>netID</td><td>Name</td><td>Type of Form</td><td>Start Date</td><td>Department</td><td>Course</td><td>Section</td><td>Building</td><td>End Date</td><td>Until</td><td>Reason</td>";
	
	
	//	for (Event event : events) {
		//for(Form f: forms){
	//		String date = event.getDate().toString();
	//		String color = event.isPerformance() ? "blue" : "#009900";
	//		headers += "<td bgcolor='"+color+"' nowrap><a href='/JSPPages/Director_View_Event.jsp?eventID='" +event.getId() +">" + date +"</a></td>";//TODOO this actually gets me the id, right?
	//	}
		
		headers+="</tr>";
		table+=headers;
				
		for(Form f : forms)
		{
			String row="<tr>";
			//row+="<td>"+ f.getNetID()+"</td>";
			row += "<td><b><a href='/JSPPages/Director_Student_View.jsp?student="+f.getNetID()+"'>"+f.getNetID()+"</a></b></td>";
			
			row+="<td>"+DatabaseUtil.getUser(f.getNetID()).getFirstName()+" "+DatabaseUtil.getUser(f.getNetID()).getLastName()+"</td>";
			row+="<td>"+ f.getType()+"</td>";
			row+="<td>"+ f.getStartTime().toString(12)+"</td>";
			if(f.getType().equals("B"))
			{
				//TODO:
			}
			else
			{
			
				row+="<td>"+"-"+"</td>";
				row+="<td>"+"-"+"</td>";
				row+="<td>"+"-"+"</td>";
				row+="<td>"+"-"+"</td>";
				row+="<td>"+"-"+"</td>";
				row+="<td>"+"-"+"</td>";
				row+="<td>"+"-"+"</td>";
				
			}
			row+="<td>"+f.getReason()+"</td>";
			row +="</tr>";
			table+=row;
		}
		
		table+="</table>";
	%>
	<%//for filters
		String filters = "";
		
	%>
	
</head>
	<body>

		<div style='height: 100%; width: 10%; border: 3px solid black; float: left; overflow:auto'><%= filters %></div>
		<div style='height: 100%; width: 85%; border: 3px solid black; float: left; overflow:auto'><%= table %></div>

	</body>
</html>


