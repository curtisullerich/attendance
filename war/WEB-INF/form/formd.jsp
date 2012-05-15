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
	
		<h1>Time Worked | Form D</h1>
		
		<p>This form includes all performances through any post-season activity <br/>
		ending January 9, ${year+1}, and it must be submitted by 4:30 p.m. on Monday, August<br/>
		23, ${year}.  Documentation must be submitted to the director for all absences (doctor's note, obituary, wedding program, etc.).<br/></p>
		
		
		<form action="./formd" method="post" accept-charset="utf-8">
		
			<dl class="block-layout">
	
				<dt><label class="required">Who needs to verify?</label></dt>
				<dd>
					<select name="Email" autofocus>
						<option value="">(Select)</option>
						<c:forEach items="${verifiers}" var="verifier">
							<option><c:out value="${verifier}" /></option>
						</c:forEach>
					</select>
				</dd>
				
				<dt><label for="AmountWorked" class="required">Total amount of work</label></dt>
				<dd>
					<input size='5' type='number' name='AmountWorked' value='<c:out value="${AmountWorked}" />' style="width:64pt" />
					<label for="AmountWorked">Hours</label>
				</dd>
				
				<dt><label for="StartMonth" class="required">Date</label></dt>
				<dd>
					<input id='startMonth' size='5' type='number' name='StartMonth' min='01' max='12' placeholder='MM' value='<c:out value="${StartMonth}" />' />
					/
					<input id='startDay' size='5' type='number' name='StartDay' min='01' max='31' step='1' placeholder='DD' value='<c:out value="${StartDay}" />' />
					/
					<input id='startYear' size='5' type='number' name='StartYear' min='${year}' max='${year+1}' step='1' placeholder='YYYY' value='<c:out value="${StartYear}" />' />
				</dd>
				
				<dt><label for="Details">Work Details</label></dt>
				<dd>
					<textarea rows="6" cols="50" name="Details" wrap="physical"></textarea>
				</dd>
				
			</dl>
			
			<input type="submit" value="Save Info" name="SaveInfo"/>
			<input type="button" value="Back" name="Back" onclick="window.location = './'"/>
		</form>		
		
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>

</html>