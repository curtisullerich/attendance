<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
	<head>
		<title>@10Dance</title>
	</head>

	<body>
	<!--TODO: need to connected to specific page-->
	    
	<h3>
		<li>
			<a href="http://www.iastate.edu" title="PageTrail_Home">Home</a> 
			>
			<a href="http://www.iastate.edu" title="PageTrail_Registration">Registration</a>
			
		<!--HELP BUTTON-->	
		<a class="addthis_button"><img
        src="http://icons.iconarchive.com/icons/deleket/button/24/Button-Help-icon.png"
        width="16" height="16" border="0" alt="Share" /></a>
		</li>

	</h3>		
		<h1>ISU Varsity Marching Band / Spring 2012</h1>
		<form action ="/login" method ="post" accept-charset="uft-8">
				<table>
					<tr>
					
						<td><label for="User Name">NetID:</label></td>
						<td><input type= "text" name="NetID" id="NetID"/></td>
					</tr>
					
					<tr>
						<td><label for="Password1">Password:</label></td>
						<td><input type= "password1" name="Password1" id="Password1"/></td>	
					</tr>
					
					<tr>
						<td><label for="Password2">Re-Enter Password:</label></td>
						<td><input type= "password2" name="Password2" id="Password2"/></td>	
					</tr>
				</table>
				
				<h1>
				<input type="submit" value="Register" name="Register"/>
				</h1>
				
				<h2>
					<input type="submit" value="Back" name="Back"/>
				<h2>
		</form>		
	</body>
	



</html>