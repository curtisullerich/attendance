<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	<%
	String netID = (String) session.getAttribute("user"); 
	
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
	}

	User user = DatabaseUtil.getUser(netID);
	%>
		<script src="/MobileApp/sha.js"/></script>
		<script>
		
		/**
		 *
		 * Hashes the password for sending to the server.
		 * @author Curtis
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
 		     if(!/[a-zA-Z0-9!@#$%&*()_+=.,<>?-]/.test(password[i]))
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
		 if(password != document.getElementById("Password2").value){
			 var va = 1+1;
			 alert("Passwords do not match. Please ensure passwords match.");
			 var va1 = 1+1;
			 return false;
		 }
		 
		 //hash password into hidden field
		 var str = Sha1.hash(document.getElementById("Password").value);
		 str = str.toUpperCase();
		 document.getElementById("Hashed Password").value = str;
		
		 return true;
	}

		</script>

	<body>
	
	
	<!--*********************Page Trail*****************************-->

	<!--TODO: need to connected to specific page-->
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Student_Page.jsp" title="PageTrail_Student">Student</a>
			>
			<a href="/JSPPages/Student_Edit_Info.jsp" title="PageTrail_Edit_Info">Edit Info</a>

	<!--*********************info*****************************-->
	
		<h1><%= user.getNetID() %></h1>
		<form action="/studentEditInfo" method="post" onSubmit="return confirmAndHashData();" accept-charset="utf-8">
		
				<table>
					<tr>					
						<td><label for="FirstName">First Name:</label></td>
						<td><input type="FirstName" name="FirstName" id="FirstName" value="<%=user.getFirstName() %>"/></td>
					</tr>
					
					<tr>
						<td><label for="LastName">Last Name:</label></td>
						<td><input type= "LastName" name="LastName" id="LastName" value="<%=user.getLastName()%>"/></td>	
					</tr>

					<tr>
						<td><label for="UniversityID">UniversityID</label></td>
						<td><input type="text" name="UniversityID" id="UniversityID" value="<%=user.getUnivID() %>"/></td>
					</tr>

							
					<!--Instrument-->
					<tr>
						<td><label for="Section">Section</label></td>
						
						<td>
						<select name="Section" id="Section">
							<option><%= user.getSection() == null ? "" : user.getSection() %></option>
							<option>Drums</option>
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
						</select>
						</td>
					</tr>

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
					
					<tr>
						<td><label for="Major">Major</label></td>
						<td><input type= "major" name="Major" id="Major" value="<%=user.getMajor() == null ? "" : user.getMajor() %>"/></td>	
					</tr>

					<tr>
						<td><label for="Password">Password:</label></td>
						<td><input type= "password" name="Password" id="Password" /></td>	
					</tr>
					
					<tr>
						<td><label for="Password2">Re-Enter Password:</label></td>
						<td><input type= "password" name="Password2" id="Password2"/></td>	

					</tr>
					
					<tr>
						<td><input type="password" name="Hashed Password"
								id="Hashed Password" hidden=true /></td>
					</tr>	


				</table>
				
				<input type="submit" value="Save Info" name="SaveInfo"/>
				<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Student_Page.jsp'"/>
		</form>		
	</body>

</html>