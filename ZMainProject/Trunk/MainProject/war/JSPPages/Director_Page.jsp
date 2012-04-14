<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

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
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<script>
	
		window.onload = function(){
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
			
			
		}
		
	</script>
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

	<!--*********************User Info*****************************-->	
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
		<input type ="submit" onClick="expandMessages();"  value = "New Messages(<%="somenumber"/* TODO get the number of new messages*/ %>) ">

		<br/>
		<input type ="submit" onClick="window.location = '/JSPPages/Director_View_AllForms.jsp';"  value = "View All Forms">
		
		<div id="messages">
			<%
			
				DatabaseUtil.getNewMessages(user.getNetID());
				
			
			%>
		</div>		
		
		
	</body>

</html>