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
        <b>What should I do if I have a specific question about my attendance or grade?</b><br/>
        If it's a question about an error in the system, email <script>document.write('<a href="mailto:'+ ["mbattendance", "iastate.edu"].join('@') +'">'+ ["mbattendance", "iastate.edu"].join('@') +'</a>')</script><noscript>mbattendance (at) iastate (dot) edu</noscript>. If it's a question about something you've submitted or your grade, email <script>document.write('<a href="mailto:'+ ["mbattendance", "iastate.edu"].join('@') +'">'+ ["mbattendance", "iastate.edu"].join('@') +'</a>')</script><noscript>mbattendance (at) iastate (dot) edu</noscript>.
        <br/>
        <br/>
        <b>What exactly is the attendance policy as it relates to my grade?</b><br/>
        You get 160 minutes for free (that's equal to two rehearsals). You may miss up to this many minutes of rehearsal and still have an A in band. For every minute you miss due to an unapproved tardy, absence, or early checkout, a minute is deducted from your available total. For up to 160 minutes missed, you will still have an A. After missing 160, here's what your grade will be:<br/>
        <br/>
        missed &lt;= 15 minutes: B<br/>
        missed &lt;= 30 minutes: C<br/>
        missed &lt;= 45 minutes: D<br/>
        missed &gt; 45 minutes: F<br/>
        <br/>
        There will be no plus or minus grades.
        <br/>
        <b>I missed more than 160 minutes of band! How can I fix my grade?</b><br/>
        You can work off time and submit a time worked form. For each minute you miss, you must do two minutes of work to make up for it. After performing the work, submit a form stating the date of the absence and how many minutes you worked. If the form contains work toward more than one absence, just list one of them in the form and explain yourself in the comments box.
        <br/>
        <br/>
        <b>I missed the deadline for submitting a form! What can I do?</b><br/>
        If you submit a form after its deadline, then the system will allow the form to submit, but automatically mark it as late. Dr. Richards will see this and make the decision as to whether he will approve the form or not. Make sure you explain yourself thoroughly in these cases. It's probably best to talk to him in person.
        <br/>
        <br/>
        <b>Why is my form not approved?</b><br/>
        Dr. Richards must approve all forms. Email <script>document.write('<a href="mailto:'+ ["cmbattendance", "gmail.com"].join('@') +'">'+ ["cmbattendance", "gmail.com"].join('@') +'</a>')</script><noscript>cmbattendance (at) gmail (dot) com</noscript> to to ask why this has not happened if you have concerns.
        <br/>
        <br/>
        <b>Why is my tardy, absence, or early checkout not approved?</b><br/>
        Rehearsal absences will generally only be approved through a class conflict form. If the form is pending, then there is not a correct approved form in the system that can approve this absence. Forms must be approved by Dr. Richards before they will auto-approve any absence. If you did submit a form and the form has been approved, then please check that all the information the form is correct. If it is not, you must resubmit the form. If everything seems to be correct, email Curtis for help. For absences not approved by a class conflict form, you must submit a time worked form to earn back the minutes you missed.
        <br/>
        <br/>
        <b>Why is my tardy, absence, or early checkout not approved? I submitted a performance absence form!</b><br/>
        An approved performance absence form will approve any <i>performance</i> absence that occurred on the date you submitted with the form. Make sure that the form is both approved and has the correct date.
        <br/>
        <br/>
        <b>Why is my tardy, absence, or early checkout not approved? I submitted a class conflict form!</b><br/>
        An approved form B will approve any <i>rehearsal</i> absence for the weekday submitted between the start and end date submitted. On each form B you must specify the type of absence: Tardy, Absence, or EarlyCheckout. The form will only approve one type of absence.
        <br/><br/>Forms submitted for an Absence: If you have an absence on same weekday as submitted on an approved class conflict form between the submitted start and end dates, then the form will auto-approve it.
        <br/><br/>Forms submitted for a Tardy: On the correct weekday, a class conflict form will auto-approve any tardy for which you checked in to band on or before the submitted end time of the class + the submitted travel time. <i>If you check in after this time, the form will not auto-approve your tardy, and it will remain pending!</i> To gain back these minutes, you must submit a time worked form.
        <br/><br/>Forms submitted for an EarlyCheckout: On the correct weekday, a class conflict form will auto-approve any earlycheckout for which you checked out of band on or after the submitted start of the class minus the submitted travel time. <i>If you check out before this time, the form will not auto-approve your early checkout, and it will remain pending!</i> To gain back this minutes, submit a time worked form.
        <br/><br/>
        <b>Why can't I register for the system?</b><br/>
        When registering, some users will see that their email address has been detected as mynetid%iastate.edu@gtempaccount.com. To our knowledge, the only issues with registering for attendance are due to having an iastate email account that has been grandfathered into the iastate network. Known causes for this: Previously registering for a YouTube account with your iastate account (before the transition to CyMail); and never migrating to CyMail from WebMail.
        <br/>
        <br/>
        If, while attempting to register, you see this problem, just get your netID and University ID to a director, and he or she will be able to register you manually.<br/><br/>
        If you have any other issues registering for attendance, please ensure that you are logged into you iastate account when registering. If this is the case and you still can't register, then submit a bug report from the bottom of the registration page (preferred) or email <script>document.write('<a href="mailto:'+ ["mbattendance", "iastate.edu"].join('@') +'">'+ ["mbattendance", "iastate.edu"].join('@') +'</a>')</script><noscript>mbattendance (at) iastate (dot) edu</noscript>. If it's a question about something you've submitted or your grade, email <script>document.write('<a href="mailto:'+ ["mbattendance", "iastate.edu"].join('@') +'">'+ ["mbattendance", "iastate.edu"].join('@') +'</a>')</script><noscript>mbattendance (at) iastate (dot) edu</noscript>.
        </p>
		<jsp:include page="/WEB-INF/common/status.jsp"/>
		<jsp:include page="/WEB-INF/template/footer.jsp" />
	</body>
</html>