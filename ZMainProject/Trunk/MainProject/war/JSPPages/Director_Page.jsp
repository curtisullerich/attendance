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
	
	if (netID == null || netID.equals("")) {
		response.sendRedirect("/JSPPages/logout.jsp");
		return;
	} else {
		user = DatabaseUtil.getUser(netID);
		if (!user.getType().equalsIgnoreCase("Director")) {
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
				div.style.display = "block";
			} else {
				div.style.display = "none";
			}
		}
		
		function expandMessages() {
			var mess = document.getElementById("messages");
			var butt = document.getElementById("messagesButton");
			if (mess.hidden == true) {
				mess.hidden = false;
				butt.value = "Hide Messages";
			} else {
				mess.hidden = true;
				butt.value = "Show New Messages";
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
					+"			<b><a href='/JSPPages/" + m.getParentType() +"Messages.jsp?parentID=" + m.getParentID() + "'>" + (m.readBy(netID) ? "" : "(new) " ) + m.getTime().toString(12) + "</a></b> from <b>" + m.getSenderNetID() + ":</b>"
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
	
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Director_Page.jsp" title="PageTrail_director">Director</a>
			
		You are logged in as the Director (<%= user.getFirstName() + " " + user.getLastName()%>)
		<a href="/JSPPages/logout.jsp">Logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

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
		
		<div id="messages" hidden="true" style="border: 3px solid black; height: 200px; overflow: auto; background-color:#FFFDE0;">
			<%= table %>
		</div>		
	</body>
</html>