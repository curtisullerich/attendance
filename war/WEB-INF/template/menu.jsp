<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
		<c:choose>
			<c:when test="${not pagetemplate.mobileSite}">
				<ul class="navigation group drilldown panel">
				<c:forEach var="item" items="${pagetemplate.navigation.links}">
					<li class="selected">
						<a href="${item.href}">${item.text}</a>
						<ul>
							<c:forEach var="subitem" items="${item.links}">
							<li class="${fn:startsWith(pagetemplate.path, subitem.href)?'selected':''}">
								<a href="${subitem.href}">${subitem.text}</a>
							</li>
							</c:forEach>
						</ul>
					</li>
				</c:forEach>
				</ul>
			</c:when>
			<c:when test="${pagetemplate.mobileSite}">
				<ul data-role="listview" class="navigation">
				<c:forEach var="item" items="${pagetemplate.navigation.links}">
					<li data-role="list-divider">
						<a href="${item.href}">${item.text}</a>
					</li>
					<c:forEach var="subitem" items="${item.links}">
					<li class="${fn:startsWith(pagetemplate.path, subitem.href)?'selected':''}">
						<a href="${subitem.href}">${subitem.text}</a>
					</li>
					</c:forEach>
				</c:forEach>
				</ul>
			</c:when>
		</c:choose>