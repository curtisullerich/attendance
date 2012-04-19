<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
		<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
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
	<%
	String studentNetID = request.getParameter("student");//TODO send this parameter
	if (studentNetID == null || studentNetID.equals("")) {
		System.out.println("There was a null or empty student param sent to the director view student page.");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
	}
	User student = DatabaseUtil.getUser(studentNetID);
	if (student == null) {
		System.out.println("The director tried to view a null student");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
	}
	
	%>
	
	<script>
		window.onload = function(){

			var Row = document.getElementById("rankRow");
			var Cells = Row.getElementsByTagName("td");
			if("<%= student.getRank() %>" == "|"){
				Cells[1].innerText = "None";
			}
			else
				Cells[1].innerText = "<%= student.getRank() %>";
				
			
			if(<%= request.getParameter("successfulSave")%> == "true"){
				alert("Student info successfully edited.");
			}
			else if(<%= request.getParameter("successfulSave")%> == "false"){
				alert("Info update error. Student info not changed.");	
			}
			
		}
	
		function listForms() {
			var div = document.getElementById("formsDiv")
			if (div.style.display == "none") {
				div.style.display = "inline";
				document.getElementById("listForms").value = "Select a Form Below";
			} else {
				div.style.display = "none";
				document.getElementById("listForms").value = "Submit a Form";
			}
		}
		
		function help(){
			alert("Helpful information about student page.")
		}
		
		function confirmDeleteOne() {
			var ret = confirm("Are you sure you want to delete this student?");
			if (ret == true)
			{
				var loc = "/JSPPages/removeStudent.jsp?student=<%=studentNetID%>";
				window.location = "" + loc;	
			}
		}
	</script>
	<body>
<!--*********************Page Trail*****************************-->
	
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Director_Page.jsp" title="Director Page">Director</a>
		>
		<a href="/JSPPages/Director_attendanceTable.jsp" title="View Class Attendance">View Class Attendance</a>
		>
		<a href="/JSPPages/Director_Student_View.jsp?student=<%= studentNetID %>" title="View Individual Student">View Individual Student</a>
			
		You are logged in as the Director (<%= user.getFirstName() + " " + user.getLastName() %>)
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>	
	


<!--*********************info*****************************-->

	<!--*********************Student Info*****************************-->	
		<br/>
		<br/>
		<table>
			<tr><td>NetID: </td> <td><%= student.getNetID() %></td></tr>
			<tr><td>First Name: </td> <td><%= student.getFirstName() %> </td></tr>
			<tr><td>Last Name: </td> <td><%= student.getLastName() %> </td></tr>
			<tr><td>University ID: </td> <td><%= student.getUnivID() %> </td></tr>
			<tr><td>Major: </td> <td><%= student.getMajor() %> </td></tr>
			<tr><td><p/></td><td><p/></td></tr>
			<tr><td>Section: </td> <td><%= student.getSection() %> </td></tr>
			<tr><td>Year in Band: </td> <td><%= student.getYear() %> </td></tr>
			<tr id="rankRow"><td>Rank: </td> <td></td></tr>
		</table>
				
----------------------------------
		<p>
		<!--********************* Button *****************************-->
		<input type="submit" onClick="window.location='/JSPPages/Director_View_Student_Submitted_Forms.jsp?student=<%= student.getNetID()%>';"  value="View Submitted Forms"/>
		<br/>
		<input type="submit" onClick="window.location='/JSPPages/Director_View_Student_Attendance.jsp?student=<%= student.getNetID()%>';"  value="View Attendance">
		<br/>
		<input type="submit" onClick="window.location='/JSPPages/Director_Edit_Student_Info.jsp?student=<%= student.getNetID()%>';"  value="Edit Student's Information">
		<br/>
		<input type="submit" onClick="confirmDeleteOne();"  value="Remove This Student">
		<br/>
		<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Director_attendanceTable.jsp'"/>
	</body>

</html>