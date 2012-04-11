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
		if (!user.getType().equalsIgnoreCase("Student")) {
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<body>
<!--*********************Page Trail*****************************-->

	
	<!--TODO: need to connected to specific page-->
	<!--<h1>-->
		<li>
			<a href="http://www.iastate.edu" title="PageTrail_Home">Home</a> 
			>
			<a href="http://www.iastate.edu" title="PageTrail_Student">Student</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_PersonalMessages">Personal Messages</a>
		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>

	<!--</h1>-->
<!--*********************info*****************************-->
	</br>
	NetID, FirstN  LastN
	
		<!--*********************Director MailBox*****************************-->	
	<div>
		<table>
			<tr><td>(new) 9/12 Director <a target="reply" href="TODO" >reply</a></td></tr>
			<!--MAIL BOX INFO-->
			
				<td>You absence approval for 10/15 has been denied due to earthquakes</br></td>
				</br>
				<td>-The Director.</td>
						
			<td> </td>
				
			<tr><td> 8/31 Brandon Mxwell <a target="reply" href="TODO" >reply</a></td></tr>
			<tr><td>8/24 Draco Malfoy <a target="reply" href="TODO" >reply</a></td></tr>
			<tr><td> <a target="reply" href="TODO" >Send New Message to the Director</a></td></tr>		
		</table>
		</div>
		
		<!--********************* Button *****************************-->	

	<button type="Back"> Back</button>		
	</body>
	
</html>