<%@ page contentType="text/html; charset=UTF-8" language="java"%>

<!DOCTYPE html>

<html>
<head>
<script src="sha.js"/></script>
<script>
	
	/**
 	 *
 	 * Hashes the password for sending to the server.
	 * @author Curtis
	 *
	 */
	function confirmAndHashData() {
 		 if(document.getElementById("Password").value != document.getElementById("Re-Enter Password").value){
 			 alert("Passwords do not match. Please ensure passwords match.");
 			 return false;
 		 }
 		 
 		 if(document.getElementById("UniversityID").value != document.getElementById("Re-Enter UniversityID").value){
 			 alert("University IDs do not match. Please ensure University IDs match.");
 			 return false;
 		 }
 		 
		 var str = Sha1.hash(document.getElementById("Password").value);
		 str = str.toUpperCase();
		 document.getElementById("Hashed Password").value = str;
		
		 return true;
	}
	
</script>

<title>@10Dance</title>
</head>
<!-- <form> name=form1 action="MainProjectServlet" method="GET"</form> -->
<body>
	<h1>ISU Varsity Marching Band / Spring 2012</h1>
	<h2>Registration</h2>
	<form action="/register" method="post" onSubmit="return confirmAndHashData();" accept-charset="utf-8">
		<table>
			<tr>
				<td><label for="FirstName">First Name</label></td>
				<td><input type="text" name="FirstName" id="FirstName" /></td>
			</tr>

			<tr>
				<td><label for="LastName">Last Name</label></td>
				<td><input type="text" name="LastName" id="LastName" /></td>
			</tr>
			
			<tr>
				<td><label for="NetID">NetID</label></td>
				<td><input type="text" name="NetID" id="NetID" /></td>
			</tr>
			
			<tr>
				<td><label for="UniversityID">UniversityID</label></td>
				<td><input type="text" name="UniversityID" id="UniversityID" /></td>
			</tr>
			
			<tr>
				<td><label for="Re-Enter UniversityID">Re-Enter UniversityID</label></td>
				<td><input type="text" name="Re-Enter UniversityID" id="Re-Enter UniversityID" /></td>
			</tr>

			<tr>
				<td><label for="Major">Major</label></td>
						<td><select name="Major">
							<option>Piccolo</option>
							<option>Clarinet</option>
							<option>Alto Sax</option>
							<option>Tenor Sax</option>
							<option>Trumpet</option>
							<option>Trombone</option>
							<option>Mellophone</option>
							<option>Baritone</option>
							<option>Sousaphone</option>
							<option>Guard</option>
							<option>Drum Major</option>
							<option>Staff</option>
							<option>Drumline: Cymbals</option>
							<option>Drumline: Tenors</option>
							<option>Drumline: Snare</option>
							<option>Drumline: Bass</option>
							<option>Twirler</option>						
						</select></td>
			</tr>
			
			<tr>
				<td><label for="Year">Years in band</label></td>
				<td><input type="text" name="Year" id="Year" /></td>
			</tr>

			<tr>
				<td><label for="Section">Section</label></td>
				<td><input type="text" name="Section" id="Section" /></td>
			</tr>

			<tr>
				<td><label for="Password">Password</label></td>
				<td><input type="password" name="Password" id="Password" /></td>
			</tr>

			<tr>
				<td><label for="Re-Enter Password">Re-Enter Password</label></td>
				<td><input type="password" name="Re-Enter Password" 
					id="Re-Enter Password" /></td>
			</tr>
			
			<tr>
				<td><input type="password" name="Hashed Password" id="Hashed Password" hidden=true/></td>
			</tr>
			
		</table>
		<input type="submit" value="Register" name="Register"/>
	</form>
	
</body>

</html>