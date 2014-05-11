<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not pagetemplate.mobileSite}">
	<div class="skip"><a accesskey="2" href="#container">Skip Navigation</a></div>
	<div class="hwrapper" id="header">
		<div id="ribbon">
			<div class="grids-24">
				<div class="grid-16">
					<h1 class="nameplate">
						<a accesskey="1" href="http://www.iastate.edu/">
							<img alt="Iowa State University" src="/img/sprite.png"/>
						</a>
					</h1>
				</div>
				<div class="grid-8">
					<div class="userbox" id="userbox">
						<c:choose>
							<c:when test="${auth.user.type.student}">
								<a href="/student"><b><c:out value="${auth.user.name}" /></b></a>
								&nbsp;
								&middot;
								&nbsp;
								<a href="/auth/logout">
									<span style="text-size:small;color:lightgrey">Logout</span>
								</a>
							</c:when>
							<c:when test="${auth.user.type.ta}">
								<a href="/ta"><b><c:out value="${auth.user.name}" /></b></a>
								&nbsp;
								-
								&nbsp;
								<a href="/auth/logout">
									<span style="text-size:small;color:lightgrey">Logout</span>
								</a>
							</c:when>
							<c:when test="${auth.user.type.director}">
								<b>Director</b>
								<a href="/director"><b><c:out value="${auth.user.name}" /></b></a>
								&nbsp;
								-
								&nbsp;
								<a href="/auth/logout">
									<span style="text-size:small;color:lightgrey">Logout</span>
								</a>
							</c:when>
							<c:otherwise>
								<b>
								<c:choose>
									<c:when test="${auth.googleLogin}">
										<a href="/auth/logout" style="text-size:large;">Logout</a>
									</c:when>
									<c:otherwise>
										<a href="/auth/login" style="text-size:large;">Login</a>
									</c:otherwise>
								</c:choose>
								&nbsp;&middot;&nbsp;
								</b>
								<a href="/auth/register"><span style="text-size:small;">Register</span></a>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
			</div>
			<div class="grids-24">
				<div class="grid-12">
					<h2 class="site-title"><a href="/"><c:out value="${pagetemplate.siteTitle}" /></a></h2>
				</div>
				<div class="grid-12">
					<h2 class="site-tagline"></h2>
				</div>
			</div>
		</div>
	</div>
	<div id="container">
		<div id="container-inner" class="grids-24">
			<div class="grid-5 sidebar" id="left-sidebar">
<jsp:include page="/WEB-INF/template/menu.jsp" />
			</div>
			<div class="grid-19" id="content">
				<div class="gutter">
</c:if>
<c:if test="${pagetemplate.mobileSite}">
	<header class="hwrapper"><h1><c:out value="${pagetemplate.title}" /></h1></header>
</c:if>
<jsp:include page="/WEB-INF/common/displaymessages.jsp"/>