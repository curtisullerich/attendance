<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
	<head>
		<title>@10Dance</title>
	</head>

	<body>
	
	
	<!--*********************Page Trail*****************************-->

	<!--TODO: need to connected to specific page-->
	<h3>
		<li>
			<a href="/" title="PageTrail_Home">Home</a> 
			>
			<a href="/JSPPages/Student_Page.jsp" title="PageTrail_Student">Student</a>
			>
			<a href="/JSPPages/Student_Edit_Info.jsp" title="PageTrail_Edit_Info">Edit Info</a>

	</h3>	

	<!--*********************info*****************************-->
	
		<h1>NetID</h1>
		<form action ="/login" method ="post" accept-charset="uft-8">
				<table>
					<tr>					
						<td><label for="FirstName">First Name:</label></td>
						<td><input type= "FirstName" name="FirstName" id="FirstName" value="<%=user.getFirstName() %>"/></td>
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
						<td><input type= "major" name="Major" id="Major" value="<%=user.getMajor() %>"/></td>	
					</tr>
					
					<!--Instrument-->
					<tr>
						<td><label for="Instrument">Instrument</label></td>
						
						<td>
						<select>
							<option><%= user.getInstrument() %></option>
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