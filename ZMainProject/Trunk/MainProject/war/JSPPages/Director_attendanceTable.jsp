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
//		if (!user.getType().equalsIgnoreCase("Director")) {
//			response.sendRedirect("/JSPPages/logout.jsp");
//		}
	}
	%>

	<%//for table
//		PriorityQueue<Event> pq = new PriorityQueue<Event>();
		List<Event> events = DatabaseUtil.getAllEvents();
//		pq.addAll(events);
		
		String table = "<table border='100'>";
		
		String headers = "<tr><td>netID</td>";
		
		for (Event event : events) {
			String date = event.getDate().toString();
			String color = event.isPerformance() ? "blue" : "#009900";
			headers += "<td bgcolor='"+color+"' >" + date +"</td>";
		}
		
		headers+="</tr>";
		table+=headers;
		
		List<User> students = DatabaseUtil.getStudents();
		//TODO does they want to see absences, etc, here if they are excused? Probably not
		for (User student : students) {
			String row = "<tr>";
			row += "<td><b>"+student.getNetID()+"</b></td>";
			for (Event event : events) {
				row += "<td>"+ student.eventStatus(event) +"</td>";
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