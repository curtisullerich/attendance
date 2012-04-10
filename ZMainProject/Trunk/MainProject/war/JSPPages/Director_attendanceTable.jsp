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

	<%
		PriorityQueue<Event> pq = new PriorityQueue<Event>();
		List<Event> events = DatabaseUtil.getAllEvents();
		pq.addAll(events);
		
		String filters = "";
		String table = "<table>";
		
		String headers = "<b><tr>";
		
		for (Event event : pq) {
			String date = event.getDate().toString();
			String color = event.isPerformance() ? "green" : "white";
			headers += "<td bgcolor=\""+color+"\"" + date +"</td>";
			
		}
		
		headers+="</tr></b>";
		table+=headers;
		
		List<User> students = DatabaseUtil.getStudents();
		//TODO does they want to see absences, etc, here if they are excused? Probably not
		for (User student : students) {
			String row = "<tr>";
			for (Event event : pq) {
				row += "<td>"+ student.eventStatus(event) +"</td>";
			}
			row +="</tr>";
		}
	%>
	
</head>
	<body>

		<div style='height: 100%; width: 10%; border: 3px solid black; float: left; overflow:auto'><%= filters %></div>
		<div style='height: 100%; width: 85%; border: 3px solid black; float: left; overflow:auto'><%= table %></div>

	</body>
</html>