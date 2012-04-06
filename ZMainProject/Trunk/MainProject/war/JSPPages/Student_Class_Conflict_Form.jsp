<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	<%
	String netID = (String) session.getAttribute("user"); 
	
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
	}

	User user = DatabaseUtil.getUser(netID);
	%>

	<body>
	
	<!--*********************Page Trail*****************************-->

	<!--TODO: need to connected to specific page-->
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Student_Page.jsp" title="PageTrail_Student">Student</a>
			>
			<a href="/JSPPages/Student_Class_Conflict_Form.jsp" title="PageTrail_AbsenceApprovalForm_Class_Conflict">Class Conflict Form</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<a href="/JSPPages/logout.jsp">logout</a>		

		<!--HELP BUTTON-->	
		<a href="">Help</a>

	<!--*********************info*****************************-->
	
		<h1><%=user.getNetID() + ", " + user.getFirstName() + " " + user.getLastName() %></h1>
		
		<!--*********************Pick a day div*****************************-->
		<div>
			<table>
				<tr><td>Date Requested:</td> </tr>
				
				<tr><td>
number entry here
				</td></tr>							
			</table>
		</div>

		<!--*********************Class info div*****************************-->
		<br/><br/>
		<div>
		<table>
			<tr><td> What is the recurrence of the class?</td></tr>
		<!--Monday ~ Sunday -->
		<tr><td> 
			<input type="checkbox" name="Monday" value="Monday"> Monday
			<input type="checkbox" name="Tuesday" value="Tuesday"> Tuesday
			<input type="checkbox" name="Wednesday" value="Wednesday">Wednesday<br>
		</td></tr>
		
		<tr><td> 
			<input type="checkbox" name = "Thursday" value="Thursday">Thursday
			<input type="checkbox" name = "Friday" value="Friday">Friday<br>
		</td></tr>
		
	<!--start date -->		
		<tr>
			<td>Start Date: <input type= "text" name="StartDate" id="StartDate"/></td>
			
		<tr>
		
		<tr>
			<td>End Date:
			<input type ="text" name="EndDate" id="EndDate"/></td>
		</tr>
		
		<!--until/ start at/ completely miss-->
			<tr><td>
				<input type="radio" name="TimeMiss" value="Until"/>Util<br>
			</td></tr>
			<tr><td>
				<input type="radio" name="TimeMiss"  value="StartAt"/>Start At
				<input type= "text" name="StartDate" id="StartDate"/>
			</td></tr>
			
			<tr><td>
				<input type="radio" name="TimeMiss" value="CompletelyMiss"/>Completely Miss<br>
			</td></tr>	
		</table>
		</div>
		
	<!--*********************End Button*****************************-->
<br/>
		<button type="Back">Back</button>
		<button type="Submit">Submit</button>
	</body>

</html>