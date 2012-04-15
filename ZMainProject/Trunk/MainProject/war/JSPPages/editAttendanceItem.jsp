<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>
<%@ page import="java.util.*" %>
<%@ page import="forms.*" %>
<%@ page import="attendance.*" %>

<html>

<script>
function confirmData()
{

	var monthDays = [0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
	var d = new Date();
	var year = d.getFullYear();
	var startMonth = parseInt(document.getElementById("tardyMonth").value);
	var startDay = parseInt(document.getElementById("tardyDay").value);
	var startYear = parseInt(document.getElementById("tardyYear").value);
	
	if (monthDays[startMonth] < startDay)
	{
		alert("Invalid day for the current starting month");
		return false;
	}

	if (monthDays[endMonth] < endDay)
	{
		alert("Invalid day for the current end month");
		return false;
	}
	
	if ((startYear > endYear) || (startYear == endYear && ((startMonth > endMonth) || (startMonth == endMonth && startDay > endDay)))) {
		alert("The start date must be before or the same as the end date");
		return false;
	}
	
	return true;
}
function showDivs() {
	if (document.getElementById("replaceWithTardy").checked) {
		document.getElementById("tardyForm").style.display="block";
		document.getElementById("absenceForm").style.display="none";
		document.getElementById("deleteForm").style.display="none";
	} else if (document.getElementById("replaceWithAbsence").checked) {
		document.getElementById("tardyForm").style.display="none";
		document.getElementById("absenceForm").style.display="block";
		document.getElementById("deleteForm").style.display="none";		
	} else if (document.getElementById("delete").checked) {
		document.getElementById("tardyForm").style.display="none";
		document.getElementById("absenceForm").style.display="none";
		document.getElementById("deleteForm").style.display="block";
	}
}
</script>
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
	boolean present = false;
	String itemType = request.getParameter("itemType");
	String itemIDstring = request.getParameter("itemID");
	long itemID = 0;
	if (itemType.equals("present")) {
		present = true;
	} else {	
		itemID = Long.parseLong(itemIDstring);
	}
	String oldNetID = "";
	String oldStatus = "";
	String oldType = "";
	String oldStartMonth = "";
	String oldStartDay = "";
	String oldStartYear = "";
	String oldStartHour = "";
	String oldStartMinute = "";
	String oldEndMonth = "";
	String oldEndYear = "";
	String oldEndHour = "";
	String oldEndMinute = "";
	boolean isStartAM = false;
	boolean isEndAM = false;
	String oldMessageIDs = "";
	int oldCurrentIndex = 0;
	if (itemType.equalsIgnoreCase("Tardy") && !present) {
		Tardy t = DatabaseUtil.getTardyByID(itemID);
		
		oldNetID = t.getNetID();
		oldStatus = t.getStatus();
		oldType = t.getType();
		oldCurrentIndex = t.getCurrentIndex();
		
		String[] tmp = t.getMessageIDs();
		for (int i = 0;i < tmp.length; i++) {
			oldMessageIDs += tmp[i] + " ";
		} 
		
		
		oldStartMonth = t.getTime().getDate().getMonth() +"";
		oldStartDay = t.getTime().getDate().getDay() +"";
		oldStartYear = t.getTime().getDate().getYear() +"";
		oldStartHour = t.getTime().getHour() +"";
		oldStartMinute = t.getTime().getMinute() +"";
		oldEndMonth = "";
		oldEndYear = "";
		oldEndHour = "";
		oldEndMinute = "";
		isStartAM = t.getTime().get12Format().substring(5).equals("AM");
	} else if (itemType.equalsIgnoreCase("Absence") && !present) {
		Absence a = DatabaseUtil.getAbsenceByID(itemID);
		
		oldNetID = a.getNetID();
		oldStatus = a.getStatus();
		oldType = a.getType();
		oldCurrentIndex = a.getCurrentIndex();
		
		
		String[] tmp = a.getMessageIDs();
		for (int i = 0;i < tmp.length; i++) {
			oldMessageIDs += tmp[i] + " ";
		} 
		
		oldStartMonth = a.getStartTime().getDate().getMonth() +"";
		oldStartDay = a.getStartTime().getDate().getDay() +"";
		oldStartYear = a.getStartTime().getDate().getYear() +"";
		oldStartHour = a.getStartTime().getHour() +"";
		oldStartMinute = a.getStartTime().getMinute() +"";
		oldEndMonth = a.getEndTime().getDate().getMonth() + "";
		oldEndYear = a.getEndTime().getDate().getYear() + "";
		oldEndHour = a.getEndTime().getHour() + "";
		oldEndMinute = a.getEndTime().getMinute() + "";		
		isStartAM = a.getStartTime().get12Format().substring(5).equals("AM");
		isEndAM = a.getEndTime().get12Format().substring(5).equals("AM");
	} else if (present) {
		//well, we won't do anything
	} else {
		System.err.println("We have an invalid item type in editAttendanceItem.jsp");
	}
	
	
	%>
	You are logged in as the Director (<%= user.getFirstName() + " " + user.getLastName()%>)
	<a href="/JSPPages/logout.jsp">Logout</a>		

	<!--HELP BUTTON-->	
	<a href="">Help</a>
	</head>

	<body>
<br/>
<br/>
	<input id='replaceWithTardy' name='action' type='radio' value='tardy' checked />Replace with Tardy
	<input id='replaceWithAbsence' name='action' type='radio' value='absence' />Replace with Absence
	<%= (present ? "" : "<input id='delete' type='radio' name='action' value='delete' />Delete the item")%>
	<input type="button" value="Go" name='action' onClick="showDivs();" />
<br/>
<br/>
	<div id='tardyForm'>
	<b>Submit a Tardy</b>
	<form action="/makeTardy" method="post" accept-charset="utf-8">
		<table>
			<tr>
				<td><input type="hidden" name="NetID" id="NetID" value="<%= oldNetID %>"/></td>
			</tr>
			<tr>
				<td><label for="Status">Status</label></td>
				<td><select name="Status" id="Status" value="<%= oldStatus%>">
						<option>excused</option>
						<option>pending</option>
						<option>unexcused</option>
				</select></td>
			</tr>
			<tr>
				<td><label for="Type">Type</label></td>
				<td><select name="Type" id="Type" value="<%= oldType %>">
						<option>rehearsal</option>
						<option>performance</option>
				</select></td>
			</tr>
			<tr>
				<td><input type="hidden" name="currentIndex" id="currentIndex" value="<%= oldCurrentIndex %>"/></td>
			</tr>
			<tr>
				<td><input type="hidden" name="messageIDs" id="messageIDs" value="<%=oldMessageIDs.trim() %>"/></td>
			</tr>
			<tr>
				<td>Date</td>
				<td>
					<div id='startDate'>
						<input id='tardyMonth' size='5' type='number' name='tardyMonth' min='01' max='31' value='<%= oldStartMonth %>'/>
 						<input id='tardyDay' size='5' type='number' name='tardyDay' min='1' max='31' step='1' value='<%= oldStartDay %>'/>
						<input id='tardyYear' size='5' type='number' name='tardyYear' min='2012' max='2013' step='1' value='<%= oldStartYear %>'/><!-- TODO make this work with current date instead of hard coding -->
					</div>
				</td>
			</tr>
			<tr>
				<td>Time</td>
				<td>
					<div id='time'>
						<input id='tardyHour' size='5' type='number' name='tardyHour' min='01' max='12' value='<%= oldStartHour %>'/>
						<input id='tardyMinute' size='5' type='number' name='tardyMinute' min='00' max='59' step='1' value='<%= oldStartMinute %>'/>
						<input id='tardyAM' type='radio' name='startrdio' value='AM' <%=isStartAM ? "checked" : "" %>/>AM
						<input id='tardyPM' type='radio' name='startrdio' value='PM' <%=isStartAM ? "" : "checked" %>/>PM
					</div>
				</td>
			</tr>
		</table>
		<input type="submit" value="Submit" name="Submit" />
	</form>
	</div>

	<div id='absenceForm'>
	<b>Submit an Absence</b>
	
	
	</div>
	<div id='deleteForm'>
	<b>Delete the Item</b>
	</div>
<script>window.onload = showDivs();
</script>
	</body>
	</html>