<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	<%
	String netID = (String) session.getAttribute("user"); 
	
	
	Person user = DatabaseUtil.getPerson(netID);
	%>
	<script>
		function viewForms() {
			var div = document.getElementById("formsDiv")
			if (div.style.display == "none") {
				div.style.display = "block";
			} else {
				div.style.display = "none";
			}
		}
		function pullAttendance() {
			
		}
		function editInformation() {
			
		}
	</script>
	<body>
<!--*********************Page Trail*****************************-->
	
	<!--TODO: need to connected to specific page-->
	<!--<h1>-->
		<li>
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/7_Student_Page.jsp" title="PageTrail_Student">Student</a>
			
		<!--HELP BUTTON-->	
		<a href="">Help</a>
		</li>

	<!--</h1>-->
<!--*********************info*****************************-->

	<!--*********************Student Info*****************************-->	
		</br>
		<div>
		<table>
			<tr><td>Name:</td> <td><%= user.getFirstName() + " " + user.getLastName()%></td></tr>
			<tr><td>NetID:</td> <td><%= user.getNetID() %></td></tr>
			<tr><td>Rank:</td> <td><%= user.getRank().equals("|") ? "none" : user.getRank() %></td></tr>
		</table>
		</div>
	<h2>----------------------------------</h2>
		<!--*********************Student MailBox*****************************-->	
	<div>
		<table>
			<tr><td>(new) 9/12 Todd Wegter <a target="reply" href="TODO" >reply</a></td></tr>
			<!--MAIL BOX INFO-->
			
				<td>Can I have be excused for my birthday?</td>
				<td>	-9/12 -NO.</td>
						
			<tr><td> 8/31 Brandon Maxwell <a target="reply" href="TODO" >reply</a></td></tr>
			<tr><td>8/24 Draco Malfoy <a target="reply" href="TODO" >reply</a></td></tr>
			<tr><td> <a target="reply" href="TODO" >Post New Message</a></td></tr>		
		</table>
		</div>
<p>
		<!--********************* Button *****************************-->
		<input type ="submit" onClick="viewForms();" value = "Forms">
			<div id="formsDiv">
				<p><a href="/JSPPages/13_Student_Absence_Approval_Form.jsp">Absence Approval Form</a></p>
				<p><a href="/JSPPages/13_Student_Class_Conflict_Form.jsp">Class Conflict Form</a></p>
			</div>
		<input type ="submit" onClick="pullAttendance();" value = "View Attendance">
		<br/>
		<input type ="submit" onClick="editInformation();" value = "Edit my information">
			<div id ="infoDiv">
				<form>
				
				</form>
			</div>
		<p><a href="/JSPPages/logout.jsp">Logout</a></p>
</p>					
	</body>

</html>