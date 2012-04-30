<%@ page contentType="text/html; charset=UTF-8" language="java"%>


<!DOCTYPE html>

<html>
<head>
	<title>@10Dance</title>
	<link rel="stylesheet" type="text/css" href="/JSPPages/MainCSS.css">
<script src="sha.js" /></script>
<script>
	
	window.onload = function(){
		var d = new Date();
		var year = d.getFullYear();
		if(d.getMonth == 0)
			year -= 1;
		document.getElementsByTagName("h1")[0].innerHTML = "ISU Varsity Marching Band / Fall " + year;

		if(<%= request.getParameter("error")%>)
			alert("There was an error with the staff creation. No accounts were added.");
		
		if(<%= request.getParameter("userExists")%>)
			alert("Both staff accounts are already registered in the system. You can try resetting your password from the login page.");
	}
	
	/**
 	 *
 	 * Hashes the password for sending to the server.
	 * @author Curtis Ullerich, Todd Wegter
	 *
	 */
	function confirmAndHashData() {
 		 
 		 //prepare Director data
		 var password = document.getElementById("Director Password").value
		 //check that password is appropriate length
		 if(password.length < 6 || password.length > 20){
			 alert("Director password must be between 5 and 21 characters long.")
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
		      alert("Please do not use the following characters in the director's password': " + badCharsStr);
		      return false;
		 }
		 //check that passwords match
		 if(password != document.getElementById("Re-Enter Director Password").value){
			 alert("Director passwords do not match. Please ensure passwords match.");
			 return false;
		 }
		 
		 //hash password into hidden field
		 var str = Sha1.hash(password);
		 str = str.toUpperCase();
		 document.getElementById("Hashed Director Password").value = str;
		
		 //prepare Student Staff data
		 password = document.getElementById("Student Staff Password").value
		 //check that password is appropriate length
		 if(password.length < 6 || password.length > 20){
			 alert("Student Staff password must be between 5 and 21 characters long.")
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
		      alert("Please do not use the following characters in the Student Staff's password': " + badCharsStr);
		      return false;
		 }
		 //check that passwords match
		 if(password != document.getElementById("Re-Enter Student Staff Password").value){
			 alert("Student Staff passwords do not match. Please ensure passwords match.");
			 return false;
		 }
		 
		 //hash password into hidden field
		 var str = Sha1.hash(password);
		 str = str.toUpperCase();
		 document.getElementById("Hashed Student Staff Password").value = str;
		
		 return true;
	}
	
</script>

<title>@10Dance</title>
</head>
<!-- <form> name=form1 action="MainProjectServlet" method="GET"</form> -->
<body>
	<h1>ISU Varsity Marching Band / Spring 2012</h1>
	<h2>Staff Account Creation</h2>
	<form action="/registerStaff" method="post"
		onSubmit="return confirmAndHashData();" accept-charset="utf-8">
		<table>
			<tr>
				<td><label for="Director NetID"> Director NetID</label></td>
				<td><input type="text" name="Director NetID" id="Director NetID" /></td>
			</tr>

			<tr>
				<td><label for="Director Password">Director Password</label></td>
				<td><input type="password" name="Director Password" id="Director Password" /></td>
			</tr>

			<tr>
				<td><label for="Re-Enter Director Password">Re-Enter Director Password</label></td>
				<td><input type="password" name="Re-Enter Director Password"
					id="Re-Enter Director Password" /></td>
			</tr>

			<tr>
				<td><input type="password" name="Hashed Director Password"
					id="Hashed Director Password" style="display: none" /></td>
			</tr>
			
			<tr>
				<td><label for="Student Staff NetID"> Student Staff NetID</label></td>
				<td><input type="text" name="Student Staff NetID" id="Student Staff NetID" /></td>
			</tr>

			<tr>
				<td><label for="Student Staff Password">Student Staff Password</label></td>
				<td><input type="password" name="Student Staff Password" id="Student Staff Password" /></td>
			</tr>

			<tr>
				<td><label for="Re-Enter Student Staff Password">Re-Enter Student Staff Password</label></td>
				<td><input type="password" name="Re-Enter Student Staff Password"
					id="Re-Enter Student Staff Password" /></td>
			</tr>

			<tr>
				<td><input type="password" name="Hashed Student Staff Password"
					id="Hashed Student Staff Password" style="display: none" /></td>
			</tr>

		</table>
		<input type="submit" value="Register" name="Register" />
		<input type="button" value="Back" name="Back" onclick="window.location='/'"/>
	</form>

</body>

</html>