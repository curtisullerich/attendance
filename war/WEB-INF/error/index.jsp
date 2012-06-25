<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setTimeZone value="${pagetemplate.timeZoneID}" />


<html>
  <head>
    <jsp:include page="/WEB-INF/template/head.jsp" />
	<script src="/js/common.js"></script>
  </head>
  <body>
  	<jsp:include page="/WEB-INF/template/header.jsp" />
   	<div>
<!--    		<div> -->
<!-- 			<h1>404!</h1> -->
<!-- 		</div> -->
<!-- 		<br/> -->
		<div>
			<h1>This is not the page you want.</h1>
			<br/>
			Unfortunately, we can't find it.
			<br/>
			Try double-checking the URL you entered.
			<br/>
			If you got this error by clicking on a link,
			<br/>
			contact the developers at <a href="mailto:mbattendance@iastate.edu?Subject=@10dance%20404%20Error">mbattendance@iastate.edu</a>.
			<br/> 
			In the meantime, feel free to play some 140-byte Tetris.
			<br/>

			<table>
				<tr>
					<td>
						<div id="output"></div>
					</td>
					<td>
						<img src="/img/404Cy.png" height="250" alt="404 Cy" />
					</td>
				</tr>
			</table>

			
			</div>
 		    </div>
  
  			<script>
				var t =
				function(a,b,c,d,e){return d+=c,e=a|b<<d,d<0|a&b<<d&&(a=e=parseInt((a|b<<c).toString(d=32).replace(/v/,""),d),b=new Date%2?1:3),[a,b,d,e]}
				
				// Controller and Display
				
				var out = document.getElementById("output");
				
				var board = 0,
				    block = 3,
				    position = 32,
				    display;
				
				function update(offset){
				  
				  var txt = "",
				    result = t(board,block,position, offset);
				    
				  board = result[0];
				  block = result[1]
				  position = result[2]
				  display = result[3];
				
				  display = ( 1<<30 | + display ).toString(2);
				  
				  for(var i=1; i<31;i++){
				    txt += display[i] == "1" ? "#" : ".";
				    if(i%5 == 0) txt+= "<br>"; 
				  }
				  
				  out.innerHTML = txt;
				}
				
				update(0);
				
				onkeydown = function(e){
				  
				  var offset = 0;
				  
				  switch (e.keyCode){
				    case 37: offset =  1; break; 
				    case 39: offset = -1; break; 
				    case 40: offset = -5; break; 
				  }
				  
				  update(offset);
				}
				
				var speed = 1000;
				
				function loop(){
				  update(-5);
				  setTimeout(loop, speed-=5);
				}
				  
				loop();
			</script>
			<style>
			    #output { margin: 2em; font-size: 20px; font-family: monospace;}
			</style>
  <br/>
    <jsp:include page="/WEB-INF/template/footer.jsp" />
  </body>
  
  
</html>