<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:choose>
<c:when test="${not pagetemplate.mobile}">

				</div>
			</div>
			<div class="grid-5 sidebar" id="right-sidebar">
				
			</div>
			<div class="clear"></div>
		</div>
	</div>

	<div class="fwrapper grids-24">
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
			</div>
		</div>
	</div>


</c:when>
</c:choose>