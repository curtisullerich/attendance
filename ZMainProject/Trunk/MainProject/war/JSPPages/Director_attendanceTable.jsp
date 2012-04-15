<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="attendance.*" %>
<%@ page import="comparators.*" %>

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
		
		String headers = "<tr><td>Last Name</td><td>First Name</td><td>Section</td><td nowrap>University ID</td>";
		
		for (Event event : events) {
			String date = event.getDate().toString();
			String color = event.isPerformance() ? "#FFFFAA" : "#FFAAFF";
			headers += "<td bgcolor='"+color+"' nowrap><a href='/JSPPages/statusDistributionGraph.jsp?id=" +event.getId() +"&type=Event'>" + date +"</a></td>";//TODOO this actually gets me the id, right?
		}
		headers+="<td>Grade</td>";
		headers+="</tr>";
		table+=headers;
		
		List<User> students = DatabaseUtil.getStudents();
		
		PriorityQueue<User> pq = new PriorityQueue<User>(350, new UserComparator());
		pq.addAll(students);
		//TODO does they want to see absences, etc, here if they are excused? Probably not

		while (!pq.isEmpty()) {
			User student = pq.poll();
			String row = "<tr>";
			row += "<td><b><a href='/JSPPages/Director_Student_View.jsp?student="+student.getNetID()+"'>"+student.getLastName()+"</a></b></td>";
			row += "<td><b><a href='/JSPPages/Director_Student_View.jsp?student="+student.getNetID()+"'>"+student.getFirstName()+"</a></b></td>";
			row+= "<td>"+student.getSection()+"</td>";
			row+= "<td>"+student.getUnivID()+"</td>"; 

			for (Event event : events) {
				String status = student.eventStatus(event);
				
				String attendanceID = student.eventAttendanceItem(event);
				String itemType = attendanceID.split(",")[0];
				String itemID = attendanceID.split(",")[1];				
				
				row += "<td nowrap onClick=\"window.open('/JSPPages/editAttendanceItem.jsp?type="+itemType+"&id="+itemID+"','Edit Item','width=500,height=800,titlebar=no')\">"+ (status.equals("present") ? "" : status) +"</td>";

			}
			row+= "<td>"+student.getGrade()+"</td>";
			row +="</tr>";
			table+=row;
		}
		table+="</table>";
	%>
	
</head>
	<body>
	
	<%= table %>
		<!--div style='height: 100%; width: 10%; border: 3px solid black; float: left; overflow:auto'></div>
		<div style='height: 100%; width: 85%; border: 3px solid black; float: left; overflow:auto'></div-->


	</body>
</html>