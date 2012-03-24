<%@ page contentType="text/html; charset=UTF-8" language="java" %>



<!DOCTYPE html>

<html>
	<head>
		<title>@10Dance</title>
		<script src="/MobileApp/sha.js"/></script>
		<script>
			
		function hashPassword() {
			var str = Sha1.hash(document.getElementById("Password").value);
			str = str.toUpperCase();
			document.getElementById("Password").value = str;
			return true;
		}

		</script>
	</head>
	<body>
		<h1>ISU Varsity Marching Band / Spring 2012</h1>
		<form action ="/login" method ="post" onSubmit="hashPassword();" accept-charset="utf-8">
				<table>
					<tr>
					
						<td><label for="User Name">Username</label></td>
						<td><input type= "text" name="User Name" id="User Name"/></td>
					</tr>
					
					<tr>
						<td><label for="Password">Password</label></td>
						<td><input type= "password" name="Password" id="Password"/></td>	
					</tr>
				</table>
				
				<input type="button" value="Register" name="Register" onClick='window.location="/JSPPages/register.jsp"'/>
				<input type="submit" value="Login" name ="Login"/>
		</form>		
	</body>
</html>