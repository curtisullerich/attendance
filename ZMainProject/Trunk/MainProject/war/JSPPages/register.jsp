<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="serverLogic.*" %>
<%@ page import="people.*" %>


<!DOCTYPE html>

<html>
<head>

<%
	if (!DatabaseUtil.directorExists()) {
		response.sendRedirect("/JSPPages/loginPage.jsp?fail=true");
	}
%>

<script src="sha.js" /></script>
<script>
	
	window.onload = function(){
		if(<%= request.getParameter("userExists")%>){
			var user = "<%= request.getParameter("user")%>";
			alert(user + " is already registered in the system. You can try resetting your password from the login page.");
		}
	}
	
	/**
 	 *
 	 * Hashes the password for sending to the server.
	 * @author Curtis Ullerich, Todd Wegter
	 *
	 */
	function confirmAndHashData() {
 		 
 		 var universityID = document.getElementById("UniversityID").value; 
		 //check that universityID is 9 digit number
 		 if((parseInt(universityID,10) < 0) || (parseInt(universityID,10) > 999999999)){
			 alert("This is not a valid University ID. Please re-enter your University ID.")
			 return false;
	 	 }
		 
 		 //check if years is 10...
 		 if(parseInt(document.getElementById("Year").value,10) == 10){
			 alert("Congratulations. You need to graduate.")
	 	 }
		 
		 var password = document.getElementById("Password").value
		 //check that password is appropriate length
		 if(password.length < 6 || password.length > 20){
			 alert("Password must be between 5 and 21 characters long.")
			 return false;
		 }
		 //check that password contains legal characters
		 var badChars = new Array();
		 for(var i = 0; i < password.length; i++){
	 		//between the brackets are all valid password characters
 		     if(!/[abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&*()_+=.,<>?-]/.test(password[i]))
		 	 	 badChars.push(password[i]);
		 }
		 if(badChars.length > 0){
		 	  var badCharsStr = "";
		      for(i = 0; i < badChars.length; i++)
		 	       badCharsStr += (badChars[i] + " ");
		      alert("Please do not use the following characters: " + badCharsStr);
		      return false;
		 }
		 //check that passwords match
		 if(password != document.getElementById("Re-Enter Password").value){
			 alert("Passwords do not match. Please ensure passwords match.");
			 return false;
		 }
		 
		 //hash password into hidden field
		 var str = Sha1.hash(password);
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
	<form action="/register" method="post"
		onSubmit="return confirmAndHashData();" accept-charset="utf-8">
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
				<td><label for="Section">Section</label></td>
				<td><select name="Section" id="Section">
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
				<td><select name="Year" id="Year">
						<option>1</option>
						<option>2</option>
						<option>3</option>
						<option>4</option>
						<option>5</option>
						<option>6</option>
						<option>7</option>
						<option>8</option>
						<option>9</option>
						<option>10</option>
				</select>years</td>
			</tr>

			<tr>
				<td><label for="Major">Major</label></td>
				<td><input type="text" name="Major" id="Major" /></td>
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
				<td><input type="password" name="Hashed Password"
					id="Hashed Password" style="display: none" /></td>
			</tr>

		</table>
		<input type="submit" value="Register" name="Register" />
		<input type="button" value="Back" name="Back" onclick="window.location='/'"/>
	</form>

</body>

</html>