<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
        <link rel="stylesheet" media="all" type="text/css" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <link rel="stylesheet" media="all" type="text/css" href="/css/jquery-ui-timepicker-addon.css" />
	</head>
	
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />
		
		<h1>Make a New Event</h1>
		
		<form method="post" accept-charset="utf-8">
			<dl class="block-layout">
			<dt><label></label></dt>


                <dt><label for="startdatetime" class="required">Start date and time</label></dt>
                <dd>
                  <input type="text" name="startdatetime" id="startdatetime" value='<c:out value="${startdatetime}" />' />
                </dd>
                <dt><label for="enddatetime" class="required">End date and time</label></dt>
                <dd>
                  <input type="text" name="enddatetime" id="enddatetime" value='<c:out value="${enddatetime}" />' />
                </dd>
			
				
				<dt><label for="Type" class="required">Type</label></dt>
				<dd>
					<select name="Type">
						<c:forEach items="${types}" var="s" varStatus="loop">
							<option value="<c:out value="${s.value}" />"> <c:out value="${s.displayName}" /> </option>
						</c:forEach>
					</select>
				</dd>
				
			</dl>
			
			<input type="submit" value="Create" name="Create" />
		</form>
	
	
		<jsp:include page="/WEB-INF/template/footer.jsp" />	
        <script type="text/javascript" src="http://code.jquery.com/ui/1.10.3/jquery-ui.min.js"></script>
        <script type="text/javascript" src="/js/jquery-ui-timepicker-addon.js"></script>
        <script>
        $('#startdatetime').datetimepicker({
            timeFormat: "h:mm TT"
          });
        $('#enddatetime').datetimepicker({
            timeFormat: "h:mm TT"
          });
        </script>
	</body>
</html>	