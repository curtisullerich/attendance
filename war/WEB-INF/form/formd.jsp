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
	
		<h1>Time Worked | Form D</h1>
				
		<form method="post" accept-charset="utf-8">
		
			<dl class="block-layout">
	
				<dt><label for="AmountWorked" class="required">Total amount of work</label></dt>
				<label>If you worked on more than one day, just select one of them here and make note of it below.</label>
				<dd>
					<input size='5' type='number' name='AmountWorked' value='<c:out value="${AmountWorked}" />' style="width:64pt" />
					<label for="AmountWorked">Minutes</label>
				</dd>
				
                <dt><label for="startdate" class="required">Date of the absence</label></dt>
                <dd>
                  <input type="text" name="startdate" id="startdate" value='<c:out value="${startdate}" />' />
                </dd>
        
				<dt><label class='required' for="Details">Work Details</label></dt>
				<dd>
					<textarea rows="6" cols="50" name="Details" wrap="physical"><c:out value="${Details}" /></textarea>
					<br/>
					Please be specific and be thorough.
				</dd>
				
			</dl>
			
			<input type="submit" value="Submit" name="Submit"/>
		</form>		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
        <script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
        <script type="text/javascript" src="/js/jquery-ui-timepicker-addon.js"></script>
        <script>
          $('#startdate').datepicker({
            });
          //because it starts out with a filled value (today's date) for some reason
          $('#startdate').val('');
        </script>
	</body>

</html>