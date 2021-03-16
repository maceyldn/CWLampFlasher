<%@page import="uk.co.m0mux.CWLampFlasher.Morse.CWUtils"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@page import="uk.co.m0mux.CWLampFlasher.App"%>
<%@page import="uk.co.m0mux.CWLampFlasher.PinOutputThread"%>  
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Set"%>  
<%@page import="java.util.TreeMap"%> 
<html>
<head><style type="text/css">@charset "UTF-8";</style>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>CQLampFlasher</title>
	<meta name="description" content="">
	<link rel="stylesheet" href="styles/main.css">
	<script src="jquery-3.5.1.min.js"></script>
</head>

<body>
<script>
$(document).ready(function(){ 
$('#status').delay(3000).fadeOut(1000);
});
</script>
<div class="login">
  
<h1>CW Lamp Flasher</h1>

	<table width=='80%'>
		<thead>
			<th>Pin</th><th>Text</th><th>Speed</th><th></th>
		</thead>
<% 

for (Entry<Integer, PinOutputThread> a : App.getPtc().pinthreads.entrySet())
{
	out.write("<form action='updateData' method='post'>");

	String b = CWUtils.decode(a.getValue().getMorse());
	out.println("<tr><td>"+a.getKey()+"</td><td><input type='text' name='cw' value='"+b+"'/></td><td><select name='speed'>");

	out.println(a.getValue().getSpeed());
	
	if (a.getValue().getSpeed() == 240) { out.println("<option value='240' selected>5 wpm</option>"); } else { out.println("<option value='240'>5 wpm</option>"); } 
	if (a.getValue().getSpeed() == 360) { out.println("<option value='360' selected>3.7 wpm</option>"); } else { out.println("<option value='360'>3.7 wpm</option>"); }
	if (a.getValue().getSpeed() == 480) { out.println("<option value='480' selected>2.5 wpm</option>"); } else { out.println("<option value='480'>2.5 wpm</option>"); }
	
	out.println("</select></td><td><button type='submit' class='btn btn-primary btn-block btn-large'>Update</button>");
	out.println("<input type='hidden' name='pin' value='"+a.getKey()+"'");
	out.println("</td></tr></form>");
}

%>

</table>
    </form>
    <%
    
    if (request.getParameter("success") != null)
    {    	
    	if (request.getParameter("success").contains("true"))
    	{
    	    out.println("<h3 id='status'>Configured</h3>");
    	} else {
    		out.println("<h3 id='status'>Something went horribly wrong... Check the logs.</h3>");
    	}
    }
    %>
    
</div>

<div id='madeby'><a href='mailto:andy.mace@mediauk.net'>Andy Mace</a> - <a href='http://qrz.com/db/m0mux' target='_new'>M0MUX</a> - Jan 2021</div>

 </body>
</html>