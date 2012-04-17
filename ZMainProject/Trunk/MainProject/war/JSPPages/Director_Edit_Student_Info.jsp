<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="people.*" %>
<%@ page import="serverLogic.DatabaseUtil" %>

<html>
	<head>
		<title>@10Dance</title>
	</head>
	<%
	String netID = (String) session.getAttribute("user");
	User user = null;
	
	if (netID == null || netID.equals("")) 
	{
		response.sendRedirect("/JSPPages/logout.jsp");
		return;
	}
	else
	{
		user = DatabaseUtil.getUser(netID);
		if (!user.getType().equalsIgnoreCase("Director")) {
			if(user.getType().equalsIgnoreCase("TA"))
				response.sendRedirect("/JSPPages/TA_Page.jsp");
			else if(user.getType().equalsIgnoreCase("Student"))
				response.sendRedirect("/JSPPages/Student_Page.jsp");
			else
				response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
	%>
	<%
	String studentNetID = request.getParameter("student");//TODO send this parameter
	if (studentNetID == null || studentNetID.equals("")) {
		System.err.println("There was a null or empty student param sent to the director view student page.");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
	}
	User student = DatabaseUtil.getUser(studentNetID);
	if (student == null) {
		System.err.println("The director tried to view a null student");
		response.sendRedirect("/JSPPages/Director_Page.jsp");
	}
	
	%>
	<script src="sha.js"/></script>
	<script>
	
	/**
	 *
	 * Hashes the password for sending to the server.
	 * @author Curtis
	 *
	 */
	function confirmAndHashData() {
		 
		 var firstName = document.getElementById("FirstName").value;
		 if(firstName == null || firstName == ""){
			 alert("Please enter a first name.");
			 return false;
		 }
		 
		 var LastName = document.getElementById("LastName").value;
		 if(LastName == null || LastName == ""){
			 alert("Please enter a last name.");
			 return false;
		 }
		 
		 var major = document.getElementById("Major").value;
		 if(major == null || major == ""){
			 alert("Please enter a major.");
			 return false;
		 }
		 
		 var section = document.getElementById("Section").value;
		 if(section == null || section == ""){
			 alert("Please enter a section.");
			 return false;
		 }
		 
		 var rank = document.getElementById("Rank").value;
		 if(rank == null || rank == ""){
			 alert("Please enter a rank.");
			 return false;
		 }
		 if(rank == "None" || rank == "none")
		 	document.getElementById("Hidden Rank").value = "|";
		 else
			 document.getElementById("Hidden Rank").value = rank;
		 
		 
		 var years = document.getElementById("Year").value;
 		 if(isNaN(parseInt(years,10)) || (parseInt(years,10) < 0) || years == "" || years == null){
			 alert("Please enter the number of years in the band.");
			 return false;
		 }
 		 
 		 var universityID = document.getElementById("UniversityID").value; 
		 //check that universityID is 9 digit number
 		 if(isNaN(parseInt(universityID,10)) || universityID.length != 9 || (parseInt(universityID,10) < 0) || (parseInt(universityID,10) > 999999999) || universityID == "" || universityID == null){
			 alert("This is not a valid University ID. Please re-enter your University ID.");
			 return false;
	 	 }
 		 
 		 //validate director password
 		 if(Sha1.hash(document.getElementById("Current Password").value).toUpperCase() != 
 			 ("<%=user.getHashedPassword()%>")){
 			 alert("Please enter the correct Director password.");
 			 return false;
 		 }
 		
 		 //new passwords are optional
		 var password = document.getElementById("Password").value;
		 if(password.length != "" && password.length != null)
			 {
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
				 alert("Passwords do not match. Please ensure passwords match.");
				 return false;
			 }
			 
			 //hash password into hidden field
			 var str = Sha1.hash(document.getElementById("Password").value);
			 str = str.toUpperCase();
			 document.getElementById("Hashed Password").value = str;
		 }
		
		 return true;
	}
		
	function help(){
		alert("Helpful information about director page.")
	}

	</script>
	<body>
	
	
	<!--*********************Page Trail*****************************-->
	
		<a href="/JSPPages/logout.jsp" title="Logout and Return to Login Screen">Home</a> 
		>
		<a href="/JSPPages/Director_Page.jsp" title="Director Page">Director</a>
		>
		<a href="/JSPPages/Director_attendanceTable.jsp" title="View Class Attendance">View Class Attendance</a>
		>
		<a href="/JSPPages/Director_Student_View.jsp?student=<%= studentNetID %>" title="View Individual Student">View Individual Student</a>
		>
		<a href="/JSPPages/Director_Edit_Student_Info?student=<%= studentNetID %>" title="View Student's Submitted Forms">View Submitted Forms</a>

		You are logged in as <%= user.getFirstName() + " " + user.getLastName() %>
		<!--LOGOUT BUTTON-->
		<input type="button" onclick="window.location = '/JSPPages/logout.jsp'" id="Logout" value="Logout"/>		

		<!--HELP BUTTON-->	
		<input type="button" onclick="javascript: help();" id="Help" value="Help"/>
		
		<br/>
		
	<!--*********************info*****************************-->
	
		<h1><%= student.getNetID() %></h1>
		<form action="/directorEditStudentInfo" method="post" onSubmit="return confirmAndHashData();" accept-charset="utf-8">
		
				<table>
					<tr>					
						<td><label for="FirstName">First Name</label></td>
						<td><input type="text" name="FirstName" id="FirstName" value="<%=student.getFirstName() %>"/></td>
					</tr>
					
					<tr>
						<td><label for="LastName">Last Name</label></td>
						<td><input type= "text" name="LastName" id="LastName" value="<%=student.getLastName()%>"/></td>	
					</tr>

					<tr>
						<td><label for="UniversityID">UniversityID</label></td>
						<td><input type="text" name="UniversityID" id="UniversityID" value="<%=student.getUnivID() %>"/></td>
					</tr>

							
					<!--Instrument-->
					<tr>
						<td><label for="Section">Section</label></td>
						
						<td>
						<select name="Section" id="Section">
							<option><%= student.getSection() == null ? "" : student.getSection() %></option>
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
							<option><%= student.getYear()%></option>
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
					</select></td>
					
					<tr>
						<td><label for="Rank">Rank</label></td>
						<td><input type= "text" name="Rank" id="Rank" value="<%=student.getRank() == null ? "None" : (student.getRank().equals("|") ? "None" : student.getRank())%>"/></td>	
					</tr>
					
					<tr>
						<td><input type="text" name="Hidden Rank"
								id="Hidden Rank" style="display: none" /></td>
					</tr>	
					
					<tr>
						<td><label for="Major">Major</label></td>
						<td><input type= "text" name="Major" id="Major" value="<%=student.getMajor() == null ? "" : student.getMajor() %>"/></td>	
					</tr>
					
					<tr>
						<td><label for="Current Password">Director Password</label></td>
						<td><input type= "password" name="Current Password" id="Current Password" /></td>	
					</tr>

					<tr>
						<td><label for="Password">New Student Password</label></td>
						<td><input type= "password" name="Password" id="Password" /></td>	
					</tr>
					
					<tr>
						<td><label for="Password2">Re-Enter New <br/> Student Password</label></td>
						<td><input type= "password" name="Password2" id="Password2"/></td>	

					</tr>
					
					<tr>
						<td><input type="password" name="Hashed Password"
								id="Hashed Password" style="display: none" /></td>
					</tr>	

					
					<tr>
						<td><input type="text" name="Student NetID"
								id="Student NetID" style="display: none" value="<%= student.getNetID()%>"/></td>
					</tr>	

				</table>
				<p>Student Passwords are Optional</p>
				<input type="submit" value="Save Info" name="SaveInfo"/>
				<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/Director_Student_View.jsp?student=<%= studentNetID %>'"/>
		</form>		
	</body>

</html>