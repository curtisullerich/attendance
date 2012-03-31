<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="serverLogic.Parser"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.lang.String" %>

<!-- This should be changed to reflect successful processing when we can -->
<!-- Parser.splat(info) should return true if successful -->
<script src = "script.js"></script>
<script src = "sha.js"></script>
<link rel="stylesheet" type="text/css" href="/MobileApp/FieldAppCSS.css" />
<script>
	function uploadData(){
		<%String info = request.getParameter("tempForUpload");
			//out.println("value="+info);
			//System.out.println("We got here");
		%>
		<%if (info != null && !info.equalsIgnoreCase(""))
		{%>
			<%Parser.splat(info);%>
			localStorage.setItem("success", true);
		<%}%>
		
		<%if (info == null)
		{%>
			localStorage.setItem("success", false);
		<%}%>
		
		<%if (info.equalsIgnoreCase(""))
		{%>
			localStorage.setItem("success", "emptyString");	
		<%}%>
		window.location = "/MobileApp/FieldAppMain.html";
	}
	
	function loginAndUpload(){
		if(confirmTAForUpload())
			uploadData();
		return false;
	}
</script>
<html>
	<body>
		<div class="box" id="login">
			<h1>Data Upload Login</h1>
			</br>
			<form class="centeralign" id="TACredentials" onsubmit="return loginAndUpload();">
				<input type="text" id="TA" placeholder="TA netID"/>
				</br>
				<input type="password" id="password" placeholder="password"/>
				</br>
				</br>
				<input type="submit" id="submit" value="Submit"/>
				<input type="button" id="cancel" value="Cancel" onclick="window.location='/MobileApp/FieldAppMain.html'"/>
			</form>
		</div>
    </body>
</html>
