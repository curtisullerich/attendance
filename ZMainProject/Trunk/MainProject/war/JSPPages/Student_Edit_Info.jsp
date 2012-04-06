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
		function hashPassword() {
			var str = Sha1.hash(document.getElementById("Password").value);
			var str2 = Sha1.hash(document.getElementById("Password2").value);
    		alert("Passwords do not match. Please ensure passwords match.");

			if (str != str2) {
				return false;
			}
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
		<form action="/StudentEditInfo" method="post" onSubmit="return hashPassword();" accept-charset="utf-8">
		
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
						<td><label for="Password">Password:</label></td>
						<td><input type= "password" name="Password" id="Password" /></td>	
					</tr>
					
					<tr>
						<td><label for="Password2">Re-Enter Password:</label></td>
						<td><input type= "password2" name="Password2" id="Password2"/></td>	
					</tr>
					
					<!--major-->
					<tr>
						<td><label for="Major">Major</label></td>
						<td><input type= "major" name="Major" id="Major" value="<%=user.getMajor() == null ? "" : user.getMajor() %>"/></td>	
					</tr>
					
					<!--Instrument-->
					<tr>
						<td><label for="Instrument">Instrument</label></td>
						
						<td>
						<select>
							<option><%= user.getInstrument() == null ? "" : user.getInstrument() %></option>
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
				</table>
			
				<h2>
					<input type="submit" value="Save Info" name="SaveInfo"/>
				</h2>
		</form>		
	</body>

</html>