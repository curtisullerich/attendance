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
			if(user.getType().equalsIgnoreCase("TA"))
				response.sendRedirect("/JSPPages/TA_Page.jsp");
			else if(user.getType().equalsIgnoreCase("Student"))
				response.sendRedirect("/JSPPages/Student_Page.jsp");
			else
				response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	
	<% 
		List<Event> events = DatabaseUtil.getAllEvents();
		List<Absence> absences = DatabaseUtil.getAllAbsences();
		List<Tardy> tardies = DatabaseUtil.getAllTardies();
		
		ListIterator<Tardy> tardyIter = tardies.listIterator();
		ListIterator<Absence> absenceIter = absences.listIterator();
		
		for (int i = 0; i < tardies.size(); i++)
		{
			Tardy cur = tardies.get(i);
			for (int j = 0; j < events.size(); j++)
			if (cur.isDuringEvent(events.get(j)))
			{
				tardies.remove(cur);
				i--;
			}
		}
		
		for (int i = 0; i < absences.size(); i++)
		{
			Absence cur = absences.get(i);
			for (int j = 0; j < events.size(); j++)
			if (cur.isDuringEvent(events.get(j)))
			{
				absences.remove(cur);
				i--;
			}
		}
		
		String table = "<table border='1'>";
		String headers = "<tr><td>NetID</td><td>Date</td><td>Start Time</td><td>End Time</td> <td>Type</td>";
		table += headers;
		
		for (Absence a: absences)
		{
			String row = "<tr>";
			//TODO send this to Director_View_Student_attendance
			row += "<td><b><a href='/JSPPages/Director_View_Student_Attendance.jsp?student=" + a.getNetID()+"'>"+a.getNetID()+"</a></b></td>";
			row += "<td>"+a.getStartTime().getDate().toString()+"</td>";
			row += "<td>"+a.getStartTime().get12Format()+"</td>";
			row += "<td>"+a.getEndTime().get12Format() +"</td>";
			row += "<td>"+a.getType() +"</td>";
			row += "</tr>";
			table += row;
		}
		
		for (Tardy t: tardies)
		{
			String row = "<tr>";
			//TODO send this to Director_View_Student_attendance
			row += "<td><b><a href='/JSPPages/Director_Student_View.jsp?student=" + t.getNetID()+"'>"+t.getNetID()+"</a></b></td>";
			row += "<td>" +t.getTime().getDate().toString()+"</td>";
			row += "<td>"+t.getTime().get12Format()+"</td>";
			row += "<td>"+"None" +"</td>";
			row += "<td>"+t.getType() +"</td>";
			row += "</tr>";
			table += row;
		}
		if (tardies.size() == 0 && absences.size() == 0)
		{
			table = "Yay! There are no unanchored tardies or absences!";
		}
	
	%>
	</head>
	
	
	<body>
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Director_Page.jsp" title="Director Page">Director</a>
		>			
		You are logged in as the Director (<%= user.getFirstName() + " " + user.getLastName() %>)
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	
		
		<br/>
		<br/>
<%= table %>
	
		<br/>
		<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Director_Page.jsp'"/>
	</body>

</html>