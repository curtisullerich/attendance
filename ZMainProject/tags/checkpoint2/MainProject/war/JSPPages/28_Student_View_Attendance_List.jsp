<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
	<head>
		<title>@10Dance</title>
	</head>

	<body>
<!--*********************Page Trail*****************************-->

	
	<!--TODO: need to connected to specific page-->
	<!--<h1>-->
		<li>
			<a href="http://www.iastate.edu" title="PageTrail_Home">Home</a> 
			>
			<a href="http://www.iastate.edu" title="PageTrail_Student">Student</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_ViewAttendance">View Attendance</a>
			
		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>

	<!--</h1>-->
<!--*********************info*****************************-->

	<!--*********************select..*****************************-->
	<a> Bar Graph <a/> </br>
	<a> Pie Chart <a/></br>
	<a> Line Graph <a/></br>
	<a> Absence List <a/></br>
	<a> Absence Forms<a/></br>
	
	</br>
	
	<!--*********************Student Table*****************************-->	
	NetID, Student name
	</br>
	<div>
		<table border="1">
			<tr><th>Date</th><th>Present</th><th>Absent</th><th>Tardy</th><th>Excused</th><th>Reason</th></tr>
			<tr><td>8/21</td><td><input type="checkbox" name="present"checked /></td><td><input type="checkbox" name="present" /></td><td><input type="checkbox" name="present" /></td><td><input type="checkbox" name="present" /></td><td>N/A</tr>
			<tr><td>8/22</td><td><input type="checkbox" name="present" /></td><td><input type="checkbox" name="present" checked /></td><td><input type="checkbox" name="present"  /></td><td><input type="checkbox" name="present"checked /></td><td>Crazy Teacher</tr>
			<tr><td>8/25</td><td><input type="checkbox" name="present" /></td><td><input type="checkbox" name="present" checked /></td><td><input type="checkbox" name="present"  /></td><td><input type="checkbox" name="present"checked /></td><td></tr>
		</table>
	</div>
	<button type="Save">Save Changes</button>
	
		<!--********************* Button *****************************-->	

	<h3>
		<!--button-->
		<button type="Back">Back</button>
	</h3>		
	</body>
	
</html>