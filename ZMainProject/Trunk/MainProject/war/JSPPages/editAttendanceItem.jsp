<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="forms.*" %>

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
	You are logged in as the Director (<%= user.getFirstName() + " " + user.getLastName()%>)
	<a href="/JSPPages/logout.jsp">Logout</a>		

	<!--HELP BUTTON-->	
	<a href="">Help</a>
	</head>

	<body>

	<div id='tardyForm'>
	
	</div>
	<div id='absenceForm'>
	
	</div>

	<form action="/makeTardy" method="post" accept-charset="utf-8">
		<table>
			<tr>
				<td><label for="NetID">NetID</label></td>
				<td><input type="text" name="NetID" id="NetID"/></td>
			</tr>
			<tr>
				<td><label for="Status">Status</label></td>
				<td><select name="Status" id="Status">
						<option>excused</option>
						<option>pending</option>
						<option>unexcused</option>
				</select></td>
			</tr>
			<tr>
				<td><label for="Type">Type</label></td>
				<td><select name="Type" id="Type">
						<option>rehearsal</option>
						<option>performance</option>
				</select></td>
			</tr>
			<tr>
				<td><label for="currentIndex">currentIndex</label></td>
				<td><input type="text" name="currentIndex" id="currentIndex"/></td>
			</tr>
			<tr>
				<td><label for="messageIDs">messageIDs</label></td>
				<td><input type="text" name="messageIDs" id="messageIDs"/></td>
			</tr>

			

		</table>
		<input type="submit" value="Register" name="Register" />
		<input type="button" value="Back" name="Back" onclick="window.location='/'"/>
	</form>
	</body>
	</html>