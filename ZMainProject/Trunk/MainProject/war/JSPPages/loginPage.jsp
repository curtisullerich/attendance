<%@ page contentType="text/html; charset=UTF-8" language="java" %>



<!DOCTYPE html>

<html>
	<head>
		<title>@10Dance</title>
		<script src="/MobileApp/sha.js"/></script>
		<script>
		
		/**
		 *
		 * Hashes the password for sending to the server.
		 * @author Curtis
		 *
		 */
		function hashPassword() {
			var str = Sha1.hash(document.getElementById("Password").value);
			str = str.toUpperCase();
			document.getElementById("Hashed Password").value = str;
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
					
					<tr>
						<td><input type= "password" name="Hashed Password" id="Hashed Password" hidden=true/></td>	
					</tr>
					
				</table>
				
				<input type="submit" value="Login" name ="Login"/>
				<input type="button" value="Register" name="Register" onClick='window.location="/JSPPages/register.jsp"'/>
		</form>		
	</body>
</html>