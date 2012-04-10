<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="serverLogic.DatabaseUtil"%>
<%@ page import="java.util.ArrayList" %>

<%ArrayList<String> peeps = DatabaseUtil.listAllUsers();%>
	<script src = "script.js"></script>
	<script src = "sha.js"></script>
	<link rel="stylesheet" type="text/css" href="/MobileApp/FieldAppCSS.css" />
	
	<script>
		var str = new Array();
		<% for (int i = 0; i < peeps.size(); i++) { %>
			str[<%= i %>] = "<%= peeps.get(i) %>";
		<% } %>
	

		for (var i = 0; i < str.length; i++) 
		{

			var splat = new Array();
			var mystring = str[i];
			splat = mystring.split(" ");

			if(splat[0] == "TA")
			{
				//create TA entry
				var netID = splat[1];
				var firstname = splat[2];
				var lastname = splat[3];
				var rank = splat[5];
				var hashedPassword = splat[4];
				storeEntry("storedLogin", firstname, lastname, netID, dateToday(), "|","|", rank, hashedPassword);
			}
			else
			{
				//create student entries
		        var netID = splat[0];
		        var firstname = splat[1];
		        var lastname = splat[2];
				var rank = splat[7];
				var hashedUnivID = Sha1.hash(splat[3],true).toUpperCase();
		        storeEntry("studentRecord", firstname, lastname, netID, dateToday(),"|","|",rank,hashedUnivID);
			}
		}
		
		//Need to do this so it passes the localhost
		localStorage[""];
		window.location = "/MobileApp/FieldAppMain.html";
	</script>
