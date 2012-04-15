<%@ page contentType="text/html; charset=UTF-8" language="java" %>



<!DOCTYPE html>

<html>
	<head>
		<title>@10Dance</title>
		<script src="/JSPPages/sha.js"/></script>
		<script>
		
		/**
		 * Creates the appropriate header on page load
		 * @author Todd Wegter
		 */
		window.onload = function() {

			<%
			boolean fail = false;
			if (!(request.getParameter("fail") == null) && request.getParameter("fail").equals("true")) {
				fail = true;
			} 
			%>
			
			if (<%= fail %>) {
				alert("There is no director registered. You cannot register for an account yet.");
			}
			
			var d = new Date();
			var year = d.getFullYear();
			if(d.getMonth == 0)
				year -= 1;
			document.getElementsByTagName("h1")[0].innerHTML = "ISU Varsity Marching Band / Fall " + year;
			if(<%= request.getParameter("successfulAdd")%>){
				var user = "<%= request.getParameter("user")%>";
				alert(user + " successfully added to the system.");
			}
			if(<%= request.getParameter("validLogin")%> == 'false'){
				alert("Invalid username or password");
			}
		}
				
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
		<h1></h1>
		<form action ="/login" method ="post" onSubmit="hashPassword();" accept-charset="utf-8">
				<table>
					<tr>
					
						<td><label for="User Name">NetID:</label></td>
						<td><input type= "text" name="User Name" id="User Name"/></td>
					</tr>
					
					<tr>
						<td><label for="Password">Password:</label></td>
						<td><input type= "password" name="Password" id="Password"/></td>	
					</tr>
					
					<tr>
						<td><input type= "password" name="Hashed Password" id="Hashed Password" style="display: none"/></td>	
					</tr>
					
				</table>
				
				<input type="submit" value="Login" name ="Login"/>
				<input type="button" value="Register" name="Register" onClick='window.location="/JSPPages/register.jsp"'/>
				<input type="button" value="Reset Password" name="PassReset"/>
		</form>	
		<br/>
		<input type="button" id="bugReport" onClick="location.href='mailto:a10dance@iastate.edu?Subject=@10dance%20Bug%20Report'" value="Report a Bug"/>
	</body>
</html>