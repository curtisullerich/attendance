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
		if (!user.getType().equalsIgnoreCase("TA")) {
			response.sendRedirect("/JSPPages/logout.jsp");
		}
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
 		 
		//validate current password
		 if(Sha1.hash(document.getElementById("Current Password").value).toUpperCase() != 
			 ("<%= DatabaseUtil.getUser(netID).getHashedPassword()%>")){
			 alert("Please enter the correct current password.");
			 return false;
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
			<a href="/JSPPages/TA_Page.jsp" title="PageTrail_TA">TA</a>
			>
			<a href="/JSPPages/TA_Edit_Info.jsp" title="PageTrail_Edit_Info">Edit TA Info</a>

	<!--*********************info*****************************-->
	
		<h1><%= user.getNetID() %></h1>
		<form action="/taEditInfo" method="post" onSubmit="return confirmAndHashData();" accept-charset="utf-8">
		
				<table>
				
					<tr>
						<td><label for="Current Password">Current Password</label></td>
						<td><input type= "password" name="Current Password" id="Current Password" /></td>	
					</tr>

					<tr>
						<td><label for="Password">Password</label></td>
						<td><input type= "password" name="Password" id="Password" /></td>	
					</tr>
					
					<tr>
						<td><label for="Password2">Re-Enter Password</label></td>
						<td><input type= "password" name="Password2" id="Password2"/></td>	

					</tr>
					
					<tr>
						<td><input type="password" name="Hashed Password"
								id="Hashed Password" hidden=true /></td>
					</tr>	


				</table>
				
				<input type="submit" value="Save Info" name="SaveInfo"/>
				<input type="button" value="Back" name="Back" onclick="window.location = '/JSPPages/TA_Page.jsp'"/>
		</form>		
	</body>

</html>