<%@ page contentType="text/html;charset=UTF-8" language="java"
	isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
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

	<h1>Performance Absence Form</h1>

	<p>
		This form includes all performances through any post-season activity
		ending January 30th. It must be submitted by
		<fmt:formatDate value="${cutoff}" pattern="hh:mm a 'on' E, MMMMM d, yyyy" />. 
		Documentation must be submitted to the director for all absences
		(doctor's note, obituary, wedding program, etc.).
	</p>

	<form method="post" accept-charset="utf-8">

		<dl class="block-layout">

            <dt><label for="startdate" class="required">Date of the absence</label></dt>
            <dd>
              <input type="text" name="startdate" id="startdate" value='<c:out value="${startdate}" />' />
            </dd>

			<dt>
				<label class='required'>Reasons:</label>
			</dt>
			<dd>
				<textarea rows="6" cols="50" id="reason" name="Reason"
					wrap="physical">
					<c:out value="${Reason}" /></textarea>
				<br/> Please be specific and be thorough.
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
        </script>
</body>
</html>