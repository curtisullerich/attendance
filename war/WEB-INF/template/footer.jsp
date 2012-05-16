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
					Unit name, address, (555) 555-5555, <script>document.write('<a href="mailto:'+ ["a10dance", "iastate.edu"].join('@') +'">'+ ["a10dance", "iastate.edu"].join('@') +'</a>')</script><noscript>a10dance (at) iastate (dot) edu</noscript>.
				</p>
				<p>
					Template Copyright &copy; 1995-2011, Iowa State University of Science and Technology.
				</p>
			</div>
		</div>
	</div>


</c:when>
</c:choose>