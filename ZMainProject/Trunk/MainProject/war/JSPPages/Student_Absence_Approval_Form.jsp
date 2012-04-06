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

	<a href="/" title="PageTrail_Home">Home</a> 
	>
	<a href="/JSPPages/Student_Page.jsp" title="PageTrail_Student">Student</a>
	>
	<a href="/JSPPages/Student_Absence_Approval_Form.jsp" title="PageTrail_AbsenceApprovalForm_Rehearsal_Performance">AbsenceApprovalForm(Rehearsal/Performance)</a>

	You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
	<a href="/JSPPages/logout.jsp">logout</a>		
	<!--HELP BUTTON-->	
	<a href="">Help</a>

	<!--*********************info*****************************-->
	
		<h1><%=user.getNetID() + ", " + user.getFirstName() + " " + user.getLastName() %></h1>
		
		<!--*********************calander div*****************************-->
		<div>
			<table>
				<tr><td>Date Requested:</td> </tr>
				
				<tr><td>

				</td></tr>							
			</table>
		</div>

		<!--*********************calander div*****************************-->
		</br></br>
		
		<div>
		<table>
			<tr><td> Will this be a Recurring Absence or Tardy?</td></tr>
		
		<!--drop down button-->
			<tr><td>
				<select>
					<option>Yes</option>
					<option>No</option>
				</select>
			</td></tr>
			
			<tr><td>How often?</td></tr>
			
		<!--drop down button-->
			<tr>
				<td>
					<select>
						<option>Daily</option>
						<option>Weekly</option>
						<option>Monthly</option>
					</select>
				</td>
			</tr>
		</table>
		</div>
	
	<!--*********************drop down bar div*****************************-->

		<table>
			<tr>					
				<td><label for="Reason">Reason:</label></td>
				<td><input type= "Reason" name="Reason" id="Reason"/></td>
			</tr>
			
			<tr>
				<td><label for="Additional_Option">Additional(Option):</label></td>
				<td><input type= "Additional" name="Additional" id="Additional"/></td>	
			</tr>
			
			<tr>
				<td><label for="UploadDocument">UploadDocument(Optional):</label></td>
				<td><input type= "UploadDocument" name="UploadDocument" id="UploadDocument"/></td>
				<td><button type="Browse">Browse</button></td>				
			</tr>

		</table>
	<!--*********************End Button*****************************-->

		<button type="Back">Back</button>
		<button type="Submit">Submit</button>
	</body>

</html>