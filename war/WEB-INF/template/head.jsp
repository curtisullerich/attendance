<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<title>${pagetemplate.title}</title>
	<c:choose>
	<c:when test="${not pagetemplate.mobileSite}">
	<link href="/css/base.css" media="all" rel="stylesheet">
	<!--[if lt IE 9]>
		<style>
			ul.left > li, ul.right > li{ *display: inline; }
			#ribbon form > input[type="submit"]{ *vertical-align: baseline; }
		</style>
	<![endif]--> 
	<link rel="stylesheet" media="all" type="text/css" href="/css/custom.css">
	</c:when>
	<c:when test="${pagetemplate.mobileSite}">
	
	<link href="http://code.jquery.com/mobile/1.1.0/jquery.mobile-1.1.0.min.css" media="all" rel="stylesheet">
	<script type="text/javascript" src="http://code.jquery.com/mobile/1.1.0/jquery.mobile-1.1.0.min.js"></script>
	
	<link href="/css/base.mobile.css" media="all" rel="stylesheet">
	<!--[if lte IE 8]>
		<script>
			(function()
			{
				var els = 'header,footer,section,aside,nav,article,hgroup,time,figure,figcaption'.split(',');
				for (var i = 0; i < els.length; i++)
					document.createElement(els[i]);
			})();
		</script>
	<![endif]--> 
	<link rel="stylesheet" media="all" type="text/css" href="/css/custom.mobile.css">
	</c:when>
	</c:choose>
	<script type="text/javascript" src="/js/common.js"></script>
	<script type="text/javascript" src="/js/jquery.min.js"></script>