<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<jsp:useBean id="date" class="java.util.Date" />

<fmt:formatDate var="year" value="${date}" pattern="yyyy" />

<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>

	<body>
	
		<jsp:include page="/WEB-INF/template/header.jsp" />
	
		<h1>Class Conflict Request | Form B</h1>
		
		<p>First Year Members are only allowed one conflict.</p>
		
		
		<form action="./formb" method="post" accept-charset="utf-8">
		
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
			
				<dt><label for="StartMonth" class="required">Starting Date:</label></dt>
				<dd>
					<input id='startMonth' size='5' type='number' name='StartMonth' min='01' max='12' placeholder='MM' value='<c:out value="${StartMonth}" />' />
					/
					<input id='startDay' size='5' type='number' name='StartDay' min='01' max='31' step='1' placeholder='DD' value='<c:out value="${StartDay}" />' />
					/
					<input id='startYear' size='5' type='number' name='StartYear' min='${year}' max='${year+1}' step='1' placeholder='YYYY' value='<c:out value="${StartYear}" />' />
					<br/>
					Please enter the <b>first</b> day that the class meets.
				</dd>
				
				<dt><label for="EndMonth" class="required">Ending Date:</label></dt>
				<dd>
					<input size='5' type='number' name='EndMonth' min='01' max='12' placeholder='MM' value='<c:out value="${EndMonth}" />' />
					/
					<input size='5' type='number' name='EndDay' min='01' max='31' step='1' placeholder='DD' value='<c:out value="${EndDay}" />' />
					/
					<input size='5' type='number' name='EndYear' min='${year}' max='${year+1}' step='1' placeholder='YYYY' value='<c:out value="${EndYear}" />' />
					<br/>
					Please enter the <b>last</b> day that the class meets.
				</dd>
				
				<dt><label class="required">Type:</label></dt>
				<dd>
					<input type='radio' value='Until' name='Type' id='until'> Until<br/>
					<input type='radio' value='Starting At' name='Type' id='startingat'> Starting At<br/>
					<input type='radio' value='Completely' name='Type' id='completely'> Completely Miss<br/>
				</dd>
				
				<dt><label class="required">Time:</label></dt>
				<dd>
					<input size='5' type='number' name='StartHour' min='01' max='12' placeholder='HH' value='<c:out value="${StartHour}" />' />
					:
					<input size='5' type='number' name='StartMinute' min='00' max='59' step='1' placeholder='MM' value='<c:out value="${StartMinute}" />' />
					
					<select name="StartPeriod">
						<option>AM</option>
						<option>PM</option>
					</select>
				</dd>
				
				<dt><label for="Comments">Comments:</label></dt>
				<dd>
					<textarea rows="6" cols="50" name="Comments" wrap="physical"></textarea>
				</dd>
				
			</dl>
			
			<input type="submit" value="Save Info" name="SaveInfo"/>
			<input type="button" value="Back" name="Back" onclick="window.location = './'"/>
		</form>		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>