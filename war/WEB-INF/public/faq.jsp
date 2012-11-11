<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<jsp:include page="/WEB-INF/template/head.jsp" />
	</head>
	<body>
		<jsp:include page="/WEB-INF/template/header.jsp" />

		<h1>Attendance FAQ</h1>
		<p>
			<h2>Answers to frequently asked questions about the system.</h2>
			<b>Please email any further questions to 
				<script>document.write('<a href="mailto:'+ ["curtisu", "iastate.edu"].join('@') +'">'+ ["curtisu", "iastate.edu"].join('@') +'</a>')</script><noscript>curtisu (at) iastate (dot) edu</noscript>.
			</b>
			<br>
			<br>
			<b>What is a form?</b><br>
			Think of a form as a contract between you and Dr. Richards that allows you to miss all or part of a rehearsal or performance. In this contract, you state the dates you will be gone (in the case of an absence) or the time you will check in or our (in the case of a tardy or early checkout). This means that if you submit a form C for 9/28/12 saying that you will check in by 4:45 PM, but you check in at 4:50 PM instead, you did not satisfy the contract, and the form will not auto-approve that tardy. The same goes for early checkouts. With a form B, you specify the start or end time of a class (or both) and the time it takes you to get to or from rehearsal. The form does the math to see the exact time by which you would check in, but if you show up after that time, the system has no mercy, and the tardy will not be auto-approved.	
			<br>
			<br>
			<b>I submitted a form, but I can't see it in the list of forms when I log in. Help!</b><br>
			There used to be a link on the members section of the website to the forms from the old, no longer used, and now deleted system. These forms sent a confirmatino email to the submitter, but sent the actual form data into the void (an invalid email address) so no director or staff member ever saw them. If this was the case, you must resubmit the form in the current system. Click "forms" on the left to get started.
			<br>
			<br>
			<b>I missed the deadline for submitting a form! What can I do?</b><br>
			Form As were due in August, and form Cs must be submitted within three weekdays of the applicable absence. If you submit a form after either of these times, then the system will allow the form to submit, but automatically mark it as denied, also adding a message to the form stating that this is the case. Dr. Richards will see a notification of this and make the decision as to whether he will approve the form or leave it as denied. Make sure you explain yourself thoroughly in these cases.
			<br>
			<br>
			<b>Why is my form not approved?</b><br>
			Dr. Richards must approve all forms. You can add a message to any form by clicking on the form and using the message box at the bottom of the page to add a message. Dr. Richards will get a notification of this message.
			<br>
			<br>
			<b>Why is my tardy, absence, or early checkout not approved?</b><br>
			Make sure that you've submitted a form for the day of the absence in question. If the form is pending, then there is not a correct approved form in the system that can approve this absence. Forms must be approved by Dr. Richards before they will auto-approve any absence. If you did submit a form and the form has been approved, then please check that all the information the form is correct. If it is not, you must resubmit the form.
			<br>
			<br>
			<b>Why is my tardy, absence, or early checkout not approved? I submitted a form A!</b><br>
			An approved form A will approve any <i>performance</i> absence that occurred on the date you submitted with the form A. Make sure that they form is both approved and has the correct date.
			<br>
			<br>
			<b>Why is my tardy, absence, or early checkout not approved? I submitted a form B!</b><br>
			An approved form B will approve any <i>rehearsal</i> absence for the weekday submitted between the start and end date submitted. On each form B you must specify the type of absence: Tardy, Absence, or EarlyCheckout. The form will only approve one type of absence.
			<br><br>Forms submitted for an Absence: If you have an absence on same weekday as submitted on an approved form B between the submitted start and end dates, then the form will auto-approve it.
			<br><br>Forms submitted for a Tardy: On the correct weekday, a form B will auto-approve any tardy for which you checked in to band on or before the submitted end time of the class + the submitted travel time. <i>If you check in after this time, the form will not auto-approve your tardy, and it will remain pending!</i> To approve the tardy, you must either add a message to the tardy explaining the circumstances to Dr. Richards, or submit a form C that covers the time of the late check in.
			<br><br>Forms submitted for an EarlyCheckout: On the correct weekday, a form B will auto-approve any earlycheckout for which you checked out of band on or after the submitted start of the class - the submitted travel time. <i>If you check out before this time, the form will not auto-approve your early checkout, and it will remain pending!</i> To approve the early checkout, you must either add a message to the early checkout explaining the circumstances to Dr. Richards, or submit a form C that covers the time of the early check out.
			<br><br>	
			<b>Why is my tardy, absence, or early checkout not approved? I submitted a form C!</b><br>
			An approved form C will approve any <i>rehearsal</i> absence for the date submitted. On each form C you must specify the type of absence: Tardy, Absence, or EarlyCheckout. The form will only approve one type of absence.
			<br><br>Forms submitted for an Absence: If you have an absence on a date submitted on an approved form C, then the form will auto-approve it.
			<br><br>Forms submitted for a Tardy: A form C will auto-approve any tardy for which you checked in to band on or before the submitted check in time on the form. <i>If you check in after this time, the form will not auto-approve your tardy, and it will remain pending!</i> To approve the tardy, you must either add a message to the tardy explaining the circumstances to Dr. Richards, or submit a <i>new</i> form C that covers the time of the late check in.
			<br><br>Forms submitted for an EarlyCheckout: A form C will auto-approve any earlycheckout for which you checked out of band on or after the submitted check out time on the form. <i>If you check out before this time, the form will not auto-approve your early checkout, and it will remain pending!</i> To approve the early checkout, you must either add a message to the early checkout explaining the circumstances to Dr. Richards, or submit a <i>new</i> form C that covers the time of the early check out.
		</p>
		<jsp:include page="/WEB-INF/common/status.jsp"/>
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>