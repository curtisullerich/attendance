<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
<c:when test="${not pagetemplate.mobileSite}">

				</div>
			</div>
			<div class="grid-5 sidebar" id="right-sidebar">
				
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<script type="text/javascript">
	$(document).ready(function()
		{
		  //hide the all of the element with class msg_body
		  $(".msg_body").hide();
		  //toggle the componenet with class msg_body
		  $(".msg_head").click(function()
		  {
		    $(this).next(".msg_body").slideToggle(200);
		  });
		});
	</script>


	<div class="fwrapper grids-24">
		<div class="msg_list">
			<h2 class="msg_head"><strong>Submit a Bug Report</strong></h2>
			<div class="msg_body">
				<jsp:include page="/WEB-INF/common/bugreport.jsp"/>
			</div>
		</div>
		<div class="grids-24" id="footer">
			<div class="grid-3 first">
				<a class="nameplate" href="/"><img alt="Iowa State University" src="/img/sprite.png"></a>
			</div>
			<div class="grid-21 last">
				<p>
					ISUCF"V"MB Attendance System, <script>document.write('<a href="mailto:'+ ["mbattendance", "iastate.edu"].join('@') +'">'+ ["mbattendance", "iastate.edu"].join('@') +'</a>')</script><noscript>mbattendance (at) iastate (dot) edu</noscript>.
				</p>
				<p>
					Template Copyright &copy; 1995-2011, Iowa State University of Science and Technology.
				</p>
				<c:if test="${pagetemplate.mobileDevice}">
				<p>
					<b>Desktop</b> | <a href=".?mobile=true">Mobile</a> Site
				</p>
				</c:if>
			</div>
		</div>
	</div>

</c:when>
<c:when test="${pagetemplate.mobileSite}">
	
	<jsp:include page="/WEB-INF/template/menu.jsp" />
	

	<footer class="fwrapper">
		<div style="text-align:center">
			<p><strong>Mobile</strong> | <a href="?mobile=false">Full site</a></p>
			<p><a href="/feedback/">Feedback</a></p>
			<p class="light">Template &copy; 2012 <a href="http://m.iastate.edu/">Iowa State University</a></p>
		</div>
	</footer>

</c:when>
</c:choose>

	<div id="loading" style="display: none; ">Loading...</div>