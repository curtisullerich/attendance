<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="attendance.*" %>
<%@ page import="java.util.*" %>
<%@ page import="comment.*" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
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
	<script>
	
		window.onload = function() {
			if(<%= request.getParameter("successfulSave")%> == "true"){
				alert("User info successfully edited.");
			}
			else if(<%= request.getParameter("successfulSave")%> == "false"){
				alert("Info update error. User info not changed.");	
			}
		}
		
		function viewForms() {
			var div = document.getElementById("formsDiv")
			if (div.style.display == "none") {
				div.style.display = "inline";
			} else {
				div.style.display = "none";
			}
		}
		
		function expandMessages() {
			var mess = document.getElementById("messages");
			var butt = document.getElementById("messagesButton");
			if (mess.style.display == "none") {
				mess.style.display = "inline";
				butt.value = "Hide Messages";
			} else {
				mess.style.display = "none"
				butt.value = "Show New Messages";
			}
		}
		
		function confirmDeleteAll() {
			var ret = confirm("Are you sure you want to delete all the information in the database? This cannot be undone!");
			if (ret == true)
			{
				window.location = "/JSPPages/RemoveEverything.jsp";	
			}
		}

	</script>
			<%
		String table = "<table>";
			PriorityQueue<Message> p = new PriorityQueue<Message>();
			p.addAll(DatabaseUtil.getNewMessages(netID));

			if (p.isEmpty()) {
				table = "<br/><br/><b>You have no new messages.</b>";
			}
			int numNew = p.size();
			
			while (!p.isEmpty()) {
				Message m = p.poll();
				if (m != null) {
					
					table+= 
					"	<tr>"
					+"		<td>"
					+"			<b><a href='/JSPPages/ViewMessages.jsp?parentType=" + m.getParentType() + "&parentID=" + m.getParentID() + "'>" + (m.readBy(netID) ? "" : "(new) " ) + m.getTime().toString(12) + "</a></b> from <b>" + m.getSenderNetID() + ":</b>"
					+"		</td>"
					+"		<td >"
					+"			" + m.getContents()
					+"		</td>"
					+"	</tr>";
				}
			}
			table+="</table>";
		%>
	<body>
<!--*********************Page Trail*****************************-->
	
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Director_Page.jsp" title="Director Page">Director</a>
			
		You are logged in as the Director (<%= user.getFirstName() + " " + user.getLastName() %>)
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>		

<!--*********************info*****************************-->
		<br/><br/>
		<div>
		<table>
			<tr><td>NetID:</td> <td><%= user.getNetID() %></td></tr>
		</table>
		</div>
----------------------------------
<p>
		<!--********************* Buttons *****************************-->
		<input type ="submit" onClick="window.location = '/JSPPages/Director_attendanceTable.jsp';"  value = "View All Attendance">
		<br/>
		<input type ="submit" onClick="window.location = '/JSPPages/Director_Edit_Info.jsp';"  value = "Edit My Information">
		<br/>
		<input type ="submit" id="messagesButton" onClick="expandMessages();"  value = "New Messages(<%=numNew%>) ">	
		
		<br/>
		<input type ="submit" onClick="window.location = '/JSPPages/Director_View_AllForms.jsp';"  value = "View All Forms">
				
		<br/>
		<input type ="submit" onClick="window.location = '/JSPPages/Director_Show_Unanchored.jsp';"  value = "View Unanchored Absences and Tardies">
				
		<br/>
		<input type ="submit" onClick="confirmDeleteAll();"  value = "Delete Everything">	
		<div id="messages" style="display: none" style="border: 3px solid black; height: 200px; overflow: auto; background-color:#FFFDE0;">
			<%= table %>
		</div>		
	</body>
</html>