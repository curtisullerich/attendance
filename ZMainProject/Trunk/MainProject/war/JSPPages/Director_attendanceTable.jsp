<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="attendance.*" %>

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
//		PriorityQueue<Event> pq = new PriorityQueue<Event>();
		List<Event> events = DatabaseUtil.getAllEvents();
//		pq.addAll(events);
		
		String table = "<table border='1'>";
		
		String headers = "<tr><td>netID</td><td>Name</td><td>grade</td><td>Section</td><td>Year</td><td>Major</td><td>Rank</td><td nowrap>University ID</td>";
		
		for (Event event : events) {
			String date = event.getDate().toString();
			String color = event.isPerformance() ? "blue" : "#009900";
			headers += "<td bgcolor='"+color+"' nowrap><a href='/JSPPages/Director_View_Event.jsp?eventID='" +event.getId() +">" + date +"</a></td>";//TODOO this actually gets me the id, right?
		}
		
		headers+="</tr>";
		table+=headers;
		
		List<User> students = DatabaseUtil.getStudents();
		//TODO does they want to see absences, etc, here if they are excused? Probably not
		for (User student : students) {
			String row = "<tr>";
			row += "<td><b><a href='/JSPPages/Director_Student_View.jsp?student="+student.getNetID()+"'>"+student.getNetID()+"</a></b></td>";
			row+= "<td>"+student.getFirstName() + " " + student.getLastName()+"</td>";
			row+= "<td>"+student.getGrade()+"</td>";
			row+= "<td>"+student.getSection()+"</td>";
			row+= "<td>"+student.getYear()+"</td>";
			row+= "<td>"+student.getMajor()+"</td>";
			row+= "<td>"+student.getRank()+"</td>";
			row+= "<td>"+student.getUnivID()+"</td>";
			
			for (Event event : events) {
				String status = student.eventStatus(event);
				row += "<td nowrap>"+ (status.equals("present") ? "" : status) +"</td>";
			}
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