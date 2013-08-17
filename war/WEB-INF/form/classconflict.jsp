<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<jsp:useBean id="date" class="java.util.Date" />

<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
        <link rel="stylesheet" media="all" type="text/css" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <link rel="stylesheet" media="all" type="text/css" href="/css/jquery-ui-timepicker-addon.css" />
	</head>

	<body>
	
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Class Conflict Form</h1>
		
		<p>First year members are only allowed one conflict.</p>
		<p>Note: If you have a class that meets multiple times per week (Tuesday and Thursday, for instance)
		you must submit one form for each day of the week.</p>
		<p>
		Helpful hints: All fields are required (except "comments"). If your class doesn't have a section, for instance, enter "none."
        This must be submitted by <fmt:formatDate value="${cutoff}" pattern="h:mm a 'on' E, MMMMM d, yyyy" />.
		</p>
		
		<form method="post" accept-charset="utf-8">
		
			<dl class="block-layout">

				<dt><label for="Department" class="required">Department:</label></dt>
				<dd>
					<input autofocus type="text" name="Department" value="<c:out value="${Department}"/>" />
				</dd>
				
				<dt><label for="Course" class="required">Course:</label></dt>
				<dd>
					<input type="text" name="Course" value="<c:out value="${Course}"/>" />
				</dd>
				
				<dt><label for="Section" class="required">Section:</label></dt>
				<dd>
					<input type="text" name="Section" value="<c:out value="${Section}"/>" />
				</dd>
				
				<dt><label for="Building" class="required">Building:</label></dt>
				<dd>
					<input type="text" name="Building" value="<c:out value="${Building}"/>" />
				</dd>
			
				<dt><label for="startdate" class="required">Starting Date:</label></dt>
                <dd>
                  <input type="text" name="startdate" id="startdate" value='<c:out value="${startdate}" />' />
                  <br/>
                  Please enter the <b>first</b> day that the class meets.
                </dd>
				
				<dt><label for="enddate" class="required">Ending Date:</label></dt>
                <dd>
                  <input type="text" name="enddate" id="enddate" value='<c:out value="${enddate}" />' />
                  <br/>
                  Please enter the <b>end</b> day that the class meets.
                </dd>
				
				<dt><label class="required">On:</label></dt>
				<dd>
					<select name="DayOfWeek">
						<c:forEach items="${daysOfWeek}" varStatus="status" var="day">
							<%//the purpose of the varStatus is to put an integer as the value to be sent
							//to the server so we can just to a parseInt instead of an iteration to get the day%>
							<option value="${day}" ${DayOfWeek eq day ? 'selected' : ''}>${day}</option>
						</c:forEach>
					</select>
					<br/>
					Please enter the day and time period that the class meets each week.
				</dd>
				
                <dt><label for="starttime" class="required">From:</label></dt>
                <dd>
                  <input type="text" name="starttime" id="starttime" value='<c:out value="${starttime}" />' />
                </dd>

                <dt><label for="endtime" class="required">To:</label></dt>
                <dd>
                  <input type="text" name="endtime" id="endtime" value='<c:out value="${endtime}" />' />
                </dd>

				<dt><label class="required">Minutes to travel between class and rehearsal</label></dt>
				<dd>
					<input size='5' type='number' name="MinutesToOrFrom" min='00' max='60' step='1' placeholder='MM' value='<c:out value="${MinutesToOrFrom }"/>'/>
					<br/>This is the time that it will take you to travel from class to band or from band to class. This is used to determine if you checked in or out too late or too soon, so make sure you leave enough time!
				</dd>

				<dt><label for="Type" class="required">Type</label></dt>
				<dd>
					<select name="Type">
						<c:forEach items="${types}" var="s" varStatus="loop">
							<option value="<c:out value="${s.value}" />"
								${absence.type==s.value ? 'selected="true"' : ''}
								><c:out value="${s.displayName}" /></option>
						</c:forEach>
					</select>
				</dd>
				
				<dt><label for="Comments">Comments:</label></dt>
				<dd>
					<textarea rows="6" cols="50" name="Comments" wrap="physical"><c:out value="${Comments}" /></textarea>
					<br/>
					Please be specific and be thorough.
				</dd>
				
			</dl>
			
			<input type="submit" value="Save Info" name="SaveInfo"/>
		</form>		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
        <script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
        <script type="text/javascript" src="/js/jquery-ui-timepicker-addon.js"></script>
        <script>
          $('#startdate').datepicker({
            });
          //because it starts out with a filled value (today's date) for some reason
          $('#startdate').val('');
          $('#enddate').val('');
          $('#enddate').datepicker({
            });
          $('#starttime').timepicker({
              timeFormat: "h:mm TT"
            });
          $('#endtime').timepicker({
              timeFormat: "h:mm TT"
            });
        </script>
	</body>

</html>