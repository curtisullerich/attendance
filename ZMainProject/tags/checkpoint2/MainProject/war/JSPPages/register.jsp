<%@ page contentType="text/html; charset=UTF-8" language="java"%>



<!DOCTYPE html>

<html>
<head>
<title>@10Dance</title>
</head>
<!-- <form> name=form1 action="MainProjectServlet" method="GET"</form> -->
<body>
	<h1>ISU Varsity Marching Band / Spring 2012</h1>
	<h2>Registration</h2>
	<form action="/register" method="post" accept-charset="uft-8">
		<table>
			<tr>
				<td><label for="FirstName">FirstName</label></td>
				<td><input type="text" name="FirstName" id="FirstName" /></td>
			</tr>

			<tr>
				<td><label for="LastName">LastName</label></td>
				<td><input type="text" name="LastName" id="LastName" /></td>
			</tr>
			
			<tr>

				<td><label for="NetID">NetID</label></td>
				<td><input type="text" name="NetID" id="NetID" /></td>
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



		</table>

		<input type="submit" value="Register" name="Register" />
	</form>
</body>




</html>