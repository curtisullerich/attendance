<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ page import="people.*"%>
<%@ page import="serverLogic.DatabaseUtil"%>
<%@ page import="java.util.*"%>
<%@ page import="attendance.*"%>
<%@ page import="comparators.*"%>


<html>

<head>
<%
	String netID = (String) session.getAttribute("user");
	User user = null;

	if (netID == null || netID.equals("")) {
		response.sendRedirect("/JSPPages/logout.jsp");
		return;
	} else {
		user = DatabaseUtil.getUser(netID);
		if (!user.getType().equalsIgnoreCase("Director")) {
			response.sendRedirect("/JSPPages/logout.jsp");
		}
	}
%>

<%
	String type = request.getParameter("type");
	long id = Long.parseLong(request.getParameter("id"));
	int present = 0;
	int tardy = 0;
	int absent = 0;
	List<Event> events = DatabaseUtil.getAllEvents();
	List<User> users = DatabaseUtil.getAllUsers();
	String information = "";
	if (type.equals("Event")) {
		Event event = DatabaseUtil.getEventByID(id);
		tardy = event.getTardies().size();
		absent = event.getAbsences().size();
		present = users.size() - tardy - absent;
		
		information = "<p><b>Event Statistics</b><br/>";
		information += "Date: " +event.getStartTime().getDate().toString()+ "<br/>"; 
		information += "Start time: "+ event.getStartTime().get24Format() + "<br/>";
		information += "End time: " + event.getEndTime().get24Format() + "<br/>";
		information += "Type: " + (event.isPerformance() ? "Performance" : "Rehearshal" )+ "<br/></p>";
		
	} else if (type.equals("User")) {
		User person = DatabaseUtil.getUserByID(id);
		tardy = person.getTardies().size(); //TODO there may be tardies here that aren't connected to an event. What do?
		absent = person.getAbsences().size();
		present = users.size() - tardy - absent;
	} else if (type.equals("Season")) {
		for (Event e : events) {
			tardy += e.getTardies().size();
			absent += e.getAbsences().size();
		}
		present = users.size() * events.size() - tardy - absent;
	} else {
		System.err.println("statusDistributionGraph got an invalid type parameter");
	}
	
%>

<style>
#graph, #labels
{
	position:relative;
	width:660px;
	height:216px;
	margin:8px;
	padding:0;	
}

#graph ul
{
	position:absolute;
	top:0px;
	left:32px;
	display:inline;
	width:250px;
	height:200px;
	background-color:white;
	border-left:1px solid black;
	border-bottom:1px solid black;
	border-right:1px solid black;
	border-top:1px solid black;
	margin:0;
}

#graph li
{
	list-style:none;
	position:absolute;	
	width:40px;
	text-align:center;
	border:1px solid black;
	visibility: hidden;
	background-color:#7FFFD4;
	background-repeat:repeat-y;
}

#labels
{
	height: 15px;
	font-size:80%;
	background: white;
}

</style>
 



<script>

function makeGraph()
{
	var container = document.getElementById("graph");
	var labels = document.getElementById("labels");
	var dnl = container.getElementsByTagName("li");
	var largest = 0;
	var graphHeight = 200;
	
	for (var i = 0; i < dnl.length; i++) {
	    var val = parseInt(dnl.item(i).innerHTML.split(":")[0],10);
	    if ( val > largest) {
	        largest = val; 
	    }
	}
	
//	document.getElementById("ul").style.height = largest;
//	document.getElementById("graph").style.height = graphHeight;
	
	for(var i = 0; i < dnl.length; i++) {
		var item = dnl.item(i);
		var value = item.innerHTML;
		var content = value.split(":");
		var color = item.style.background;
		var status = "";
		displayvalue = content[0]*180/(largest == 0 ? 1 : largest);
		value=content[0];
		//creating bars
		item.style.top=(200 - 1 - displayvalue) + "px";
		item.style.left = (i * 50 + 20) + "px";
		item.style.height = (displayvalue) + "px";
		item.innerHTML = value;
		item.style.visibility="visible";
		if(content.length > 0) {
		    label = content[1]
		}

		left = new String(i * 50 + 58) + "px";
		
		labels.innerHTML = labels.innerHTML + "<span style='position:absolute;top:-16px;left:"+ left+";background:"+ color+"'>" + label + "</span>";
	}	
}

window.onload=makeGraph;

</script>

</head>


<body>

<div id = 'info'>
	<%= information %>
</div>

<div id="graph">
<ul id = "ul">
			<li><%= present+ ":Present" %></li>
			<li><%= tardy+ ":Tardy" %></li>
			<li><%= absent+ ":Absent" %></li>
</ul>
</div>
<p><div id="labels"></div>

</body>
</html>