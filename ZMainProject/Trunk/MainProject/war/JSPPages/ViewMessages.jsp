<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="attendance.*" %>
<%@ page import="time.*" %>
<%@ page import="comment.*" %>
<%@ page import="forms.*" %>
<%@ page import="serverLogic.*" %>
<%@ page import="java.util.*" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>

	<body>
		<!--*********************Page Trail*****************************-->
			
		<%
		String netID = (String) session.getAttribute("user");
		User user = null;
		System.err.println("ViewMessages: 1");
		String table = "";
		String pagetrail = "";
		if (netID == null || netID.equals("")) 
		{
			response.sendRedirect("/JSPPages/logout.jsp");
			System.err.println("ViewMessages: 2");
			return;
		}
		else
		{
			user = DatabaseUtil.getUser(netID);
			if(user.getType().equalsIgnoreCase("TA")) {
				response.sendRedirect("/JSPPages/TA_Page.jsp");
			} else if (user.getType().equalsIgnoreCase("Director")){
				pagetrail = "<a href='/JSPPages/logout.jsp' title='Logout and Return to Login Screen'>Home</a> "
						+"				>"
						+"				<a href='/JSPPages/Director_Page.jsp' title='Director Page'>Director</a>"
						+"				>"
						+"				<a href='/JSPPages/Director_View_Student_Attendance.jsp' title='View a Student's Attendance'>View a Student's Attendance</a>"
						+"				>"
						+"				<a href='/JSPPages/ViewMessages.jsp' title='View Messages'>View Messages</a>";
			} else if (user.getType().equalsIgnoreCase("Student")){
				
				pagetrail = "<a href='/JSPPages/logout.jsp' title='Logout and Return to Login Screen'>Home</a> "
						+"				>"
						+"				<a href='/JSPPages/Student_Page.jsp' title='Student Page'>Student</a>"
						+"				>"
						+"				<a href='/JSPPages/Student_View_Attendance.jsp' title='View Student Attendance'>View Student Attendance</a>"
						+"				>"
						+"				<a href='/JSPPages/ViewMessages.jsp' title='View Messages'>View Messages</a>";

			} else {
				response.sendRedirect("/JSPPages/logout.jsp");
				
			}
				System.err.println("ViewMessages: 3");
						//TODO if this is a Student, check that the ID matches an item that belongs to him or her. If this is a director, allow anything.
		}
		
		%>
		<%= pagetrail %>
		<%
		
		
		boolean hasMessage = true;
		
		user = DatabaseUtil.getUser(netID);

		String idstring = request.getParameter("parentID");
		long parentID = Long.parseLong(idstring);
		String parentType = request.getParameter("parentType");
		if (idstring == null || idstring.equals("")) {
			hasMessage = false;
		} 
		System.err.println("ViewMessages: 4");

		List<Message> messages = new ArrayList<Message>();
		if (parentType == null) {
			System.out.println("parenttype was null");
		}
		if (parentType.equals("Tardy")) {
			System.err.println("ViewMessages: 4.5");
			System.out.println(parentID);
			Tardy tardy = DatabaseUtil.getTardyByID(parentID);
			if (tardy == null) {
				System.out.println("tardy was null");
			}
			messages.addAll(tardy.getMessages());
			System.err.println("ViewMessages: 5");
		} else if (parentType.equals("Absence")) {
			System.err.println("ViewMessages: 5.5");
			System.out.println(parentID);
			Absence absence = DatabaseUtil.getAbsenceByID(parentID);
			if (absence == null) {
				System.out.println("absence was null");
			}
			messages.addAll(absence.getMessages());
			System.err.println("ViewMessages: 6");
		} else if (parentType.equals("EarlyCheckOut")) {
//			EarlyCheckOut eoc = DatabaseUtil.getEarlyCheckOutByID(id);
		} else if (parentType.equals("FormA")) {
			System.err.println("ViewMessages: 6.1");
			System.out.println(parentID);
			Form form  = DatabaseUtil.getFormByID(parentID);
			if (form == null) {
				System.out.println("form was null");
			}
			messages.addAll(form.getMessages());
			System.err.println("ViewMessages: 6.2");
		} else if (parentType.equals("FormB")) {
			System.err.println("ViewMessages: 6.1");
			System.out.println(parentID);
			Form form  = DatabaseUtil.getFormByID(parentID);
			if (form == null) {
				System.out.println("form was null");
			}
			messages.addAll(form.getMessages());
			System.err.println("ViewMessages: 6.2");
			
		} else if (parentType.equals("FormC")) {
			System.err.println("ViewMessages: 6.1");
			System.out.println(parentID);
			Form form  = DatabaseUtil.getFormByID(parentID);
			if (form == null) {
				System.out.println("form was null");
			}
			messages.addAll(form.getMessages());
			System.err.println("ViewMessages: 6.2");
			
		} else if (parentType.equals("FormD")) {
			System.err.println("ViewMessages: 6.1");
			System.out.println(parentID);
			Form form  = DatabaseUtil.getFormByID(parentID);
			if (form == null) {
				System.out.println("form was null");
			}
			messages.addAll(form.getMessages());
			System.err.println("ViewMessages: 6.2");
			
		} else {
			System.err.println("And, we have a type of message that we didn't account for.");
		}
		System.err.println("ViewMessages: 5");

		%>
			
		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	

		<%
		
		
		if (hasMessage) {
			System.err.println("ViewMessages: 6");

			PriorityQueue<Message> p = new PriorityQueue<Message>();

			p.addAll(messages);
			if (p.isEmpty()) {
				table = "<br/><br/><b>There are currently no messages on this item.</b>";
			}
			table += 

					"<table >"	
					+"<br/><br/>Write a new message: "
					+""
					+"<form action='/newMessage' method='post'><input name='parentID' type='text' hidden='true' value='" + parentID +"'></input> <input type= 'text' name='New Message' id='New Message'/><input hidden='true' type= 'text' name='parentType' id='parentType' value='"+ parentType+"'/>	<button name='Submit' type='Submit'>Send</button></form>"
					+"<p>";
// 						+ "<tr>"
// 							+"<td>Write a new message:</td>"
// 							+"<td>"
// 							+"			<form action='/newMessage' method='post'><input name='tardyid' type='text' hidden='true' value='" + id +"'></input> <input type= 'text' name='New Message' id='New Message'/>	<button name='Submit' type='Submit'>Send</button></form>"
// 							+"</td>"
// 						+"</tr>";

		System.err.println("ViewMessages: 7");
			
			while (!p.isEmpty()) {
				Message m = p.poll();
				if (m != null) {
					table+= 
					"	<tr>";
					
					if (!m.readBy(netID)) {
						table += "			<td bgcolor='#F5F5DC'><b>" + m.getTime().toString(12) + "</b> from <b>" + m.getSenderNetID() + ":</b>";
					} else {
						table += "			<td><b>" + m.getTime().toString(12) + "</b> from <b>" + m.getSenderNetID() + ":</b>";
					}
					
					table+=
					"		</td>"
					+"		<td >"
					+"			" + m.getContents()
					+"		</td>"
					+"	</tr>";
					m.setReadBy(netID);
					DatabaseUtil.refreshMessage(m);
					
				}
			}
			table+="</table>";
		} else {
			table = "<br/><b>No item selected.</b><br/>";
		}
		System.err.println("ViewMessages: 8");

		%>
		
	<%
		String itemInfo = "";
		itemInfo += HTMLUtil.messagePageHeader(parentType, parentID);
		
	%>	
	<%= itemInfo %>
	<%= table %>
	
	
		<!--button-->
		<br/>
		<input type="button" value="Back" name="Back" onclick="window.location = document.referrer"/>
	</body>
	
</html>