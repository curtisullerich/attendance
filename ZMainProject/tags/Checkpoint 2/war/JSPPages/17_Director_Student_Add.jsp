<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
	<head>
		<title>@10Dance</title>
	</head>

	<body>
	
	
	<!--*********************Page Trail*****************************-->

	<!--TODO: need to connected to specific page-->
	<h3>
		<li>
			<a href="http://www.iastate.edu" title="PageTrail_Home">Home</a> 
			>
			<a href="http://www.iastate.edu" title="PageTrail_Director">Director</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_ViewAndEditStudentList">View and Edit Student List</a>
			>
			<a href="http://www.iastate.edu" title="PageTrail_AboutStudent">About Student</a>

		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>
	</h3>	

	<!--*********************info*****************************-->
	
		<h1>17</h1>
		<form action ="/login" method ="post" accept-charset="uft-8">
				<table>
					<tr>			
						<td><label for="NetID">Net ID:</label></td>
						<td><input type= "NetID" name="NetID" id="NetID"/></td>
					</tr>
				
					<tr>			
						<td><label for="FirstName">First Name:</label></td>
						<td><input type= "FirstName" name="FirstName" id="FirstName"/></td>
					</tr>
					
					<tr>
						<td><label for="LastName">Last Name:</label></td>
						<td><input type= "LastName" name="LastName" id="LastName"/></td>	
					</tr>
					
					<tr>
						<td><label for="Password">Password:</label></td>
						<td><input type= "password" name="Password" id="Password"/></td>	
					</tr>
					
					<tr>
						<td><label for="Password2">Re-Enter Password:</label></td>
						<td><input type= "password2" name="Password2" id="Password2"/></td>	
					</tr>
					
					<!--major-->
					<tr>
						<td><label for="Major">Major</label></td>
						
						<td>
						<select>
						  <option>CPR E </option>
						  <option>M E</option>
						  <option>ENGL</option>
						</select>
						</td>
					</tr>
					
					<!--advisor-->
					<tr>
						<td><label for="Advisor">Advisor</label></td>
						
						<td>
						<select>
						  <option>Vicky Thorland-Oster</option>
						  <option>Deb Martin</option>
						  <option>.....</option>
						</select>
						</td>
					</tr>
					
					<!--Position-->
					<tr>
						<td><label for="Position">Position</label></td>
						
						<td>
						<select>
						  <option>Drums</option>
						  <option>Baritone</option>
						  <option>Sax</option>
						  <option>.....</option>
						</select>
						</td>
					</tr>
				</table>
			
				<h2>
					<input type="submit" value="Add Student" name="AddStudent"/>
				<h2>
		</form>		
	</body>
	



</html>