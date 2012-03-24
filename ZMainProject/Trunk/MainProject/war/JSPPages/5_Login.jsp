<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
	<head>
		<title>@10Dance</title>
		<%//query database for Person object and set the attribute
		Person user = queryqueryquery;
		session.setAttribute("user", user.getNetID());
		System.out.println("Set user id " user.getNetID());		
				%>
	</head>

	<body>
	<!--TODO: need to connected to specific page-->
	    
	<h3>
		<li>
			<a href="http://www.iastate.edu" title="PageTrail_Home">Home</a> 
			>
			<a href="http://www.iastate.edu" title="PageTrail_Login">Login</a>
			
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
					
						<td><label for="User Name">UserName</label></td>
						<td><input type= "text" name="User Name" id="User Name"/></td>
					</tr>
					
					<tr>
						<td><label for="Password">Password</label></td>
						<td><input type= "password" name="Password" id="Password"/></td>	
					</tr>
				</table>
				
				<h1>
				<input type="submit" value="Register" name="Register"/>
				<input type="submit" value="Login" name ="Login"/>
				</h1>
				
				<h2>
					<input type="submit" value="Next" name ="Next"/>
				<h2>
		</form>		
	</body>
	
</html>