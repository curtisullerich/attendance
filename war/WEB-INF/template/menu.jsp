<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
				<ul class="navigation group drilldown panel">
					<c:if test="${auth.user.type.student or auth.user.type.ta}">
					<li class="selected">
						<a href="/student">Student</a>
						<ul>
							<li class="first ${pagetemplate.jspath eq 'student/attendance'?'selected':''}"><a href="/student/attendance">Attendance</a></li>
							<c:if test="${fn:startsWith(pagetemplate.jspath, 'form/')}">
							<li class="${pagetemplate.jspath eq 'form/index'?'selected':''}">
								<a href="/student/forms">Forms</a>
								<ul>
									<li class="first ${pagetemplate.jspath eq 'form/forma'?'selected':''}"><a href="/student/forms/forma">Form A</a></li>
									<li class="first ${pagetemplate.jspath eq 'form/formb'?'selected':''}"><a href="/student/forms/formb">Form B</a></li>
									<li class="first ${pagetemplate.jspath eq 'form/formc'?'selected':''}"><a href="/student/forms/formc">Form C</a></li>
									<li class="first ${pagetemplate.jspath eq 'form/formd'?'selected':''}"><a href="/student/forms/formd">Form D</a></li>
								</ul>
							</li>
							</c:if>
							<c:if test="${!fn:startsWith(pagetemplate.jspath, 'form/')}">
							<li><a href="/student/forms">Forms</a></li>
							</c:if>
							<li class="${pagetemplate.jspath eq 'student/messages'?'selected':''}"><a href="/student/messages">Messages</a></li>
							<li class="${pagetemplate.jspath eq 'student/info'?'selected':''}"><a href="/student/info">Edit Info</a></li>
						</ul>
					</c:if>
					
					<c:if test="${auth.user.type.ta}">
					<li class="selected">
						<a href="/ta">Student Staff</a>
						<ul>
							<li><a href="/ta/ranks">Set Ranks</a></li>
							<li><a href="/MobileApp/FieldAppMain.html">Mobile Field App</a></li>
						</ul>
					</li>
					</c:if>
					
					<c:if test="${auth.user.type.director}">
					<li class="selected">
						<a href="/director/">Director</a>
						<ul>
							<li class="${pagetemplate.jspath eq 'director/attendance'?'selected':''}"><a href="/director/attendance">Attendance</a></li>
							<li class="${pagetemplate.jspath eq 'director/forms'?'selected':''}"><a href="/director/forms">Forms</a></li>
							<li class="${pagetemplate.jspath eq 'director/stats'?'selected':''}"><a href="/director/stats">Statistics</a></li>
							<li class="${pagetemplate.jspath eq 'director/messages'?'selected':''}"><a href="/director/messages">Messages</a></li>
							<li class="${pagetemplate.jspath eq 'director/unanchored'?'selected':''}"><a href="/director/unanchored">Un-Anchored</a></li>
							<li class="${pagetemplate.jspath eq 'director/users'?'selected':''}"><a href="/director/users">Users</a></li>
							<li class="${pagetemplate.jspath eq 'director/info'?'selected':''}"><a href="/director/info">My Info</a></li>
							<li class="${pagetemplate.jspath eq 'director/appinfo'?'selected':''}"><a href="/director/appinfo">App Settings</a></li>
							<li class="${pagetemplate.jspath eq 'director/export'?'selected':''}"><a href="/director/export">Export Grades</a></li>
						</ul>
					</li>
					</c:if>
					
					<c:if test="${auth.admin}">
					<li class="selected">
						<a href="/admin/">Admin</a>
						<ul>
							<li><a href="/admin/users">Users</a></li>
						</ul>
					</li>
					</c:if>
				</ul>