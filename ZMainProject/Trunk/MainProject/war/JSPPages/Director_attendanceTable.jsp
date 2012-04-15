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
			if(user.getType().equalsIgnoreCase("TA"))
				response.sendRedirect("/JSPPages/TA_Page.jsp");
			else if(user.getType().equalsIgnoreCase("Student"))
				response.sendRedirect("/JSPPages/Student_Page.jsp");
			else
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
			String color = event.isPerformance() ? "#FFDDFF" : "#FFFFDD";
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
				String info = student.eventAttendanceItem(event);
				String itemType=info.split(",")[0];
				String itemID = info.split(",")[1];
				if (status.equals("present")) {
					itemType=status;
				}
				
				row += "<td nowrap onClick=\"window.location='/JSPPages/editAttendanceItem.jsp?itemType="+itemType+"&itemID="+itemID+"'\">"+ (status.equals("present") ? "" : status) +"</td>";
			}
			row+= "<td>"+student.getGrade()+"</td>";
			row +="</tr>";
			table+=row;
		}
		table+="</table>";
	%>
	
</head>
	<body>
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Director_Page.jsp" title="Director Page">Director</a>
		>
		<a href="/JSPPages/Director_attendanceTable.jsp" title="View Class Attendance">View Class Attendance</a>
			
		You are logged in as the Director (<%= user.getFirstName() + " " + user.getLastName() %>)
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	
	
		<br/>
		<br/>
<%= table %>
		<!-- div style='height: 100%; width: 10%; border: 3px solid black; float: left; overflow:auto'></div>
		<div style='height: 100%; width: 85%; border: 3px solid black; float: left; overflow:auto'></div-->
		
		<br/>
		<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Director_Page.jsp'"/>

	</body>
</html>