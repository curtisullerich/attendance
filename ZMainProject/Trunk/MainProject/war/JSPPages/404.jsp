<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<!DOCTYPE html>

<html>
	<head>
		<link rel="stylesheet" type="text/css" href="/JSPPages/404.css">
		<script src="/JSPPages/script.js"></script>
		<title>@10Dance 404!!</title>
	</head>
	<body>
		<div class="heading">
			<h1>You've been 404'd!!</h1>
		</div>
		</br>
		<div class="whitebox">
			<p>
				This is not the page you're looking for.</br>Unfortunately, we can't find it.</br>
				Try double-checking the URL you entered.</br>If you got this error by clicking on a link, contact Curtis at <a href="mailto:curtisu@iastate.edu?Subject=@10dance%20404%20Error">curtis@iastate.edu</a>.</br>
				</br>
				You can easily find your way to one of the main </br>@10dance pages using these fancy-shmancy buttons:
				<form class="centeralign">
					<input class="button" type="button" id="main" value="Main Home Page" onclick="parent.location='/'"/> 
					<input class="button" type="button" id="mobile" value="Mobile App" onclick="parent.location = '/MobileApp/FieldAppMain.html'"/>
				</form>
			</p>
		</div>
		<input class="back" type="button" id="back" value="Back" onclick="goBack()"/><br>

			
	</body>

</html>