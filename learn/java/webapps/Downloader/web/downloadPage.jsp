<%-- 
    Document   : downloadPage
    Created on : 17.06.2017, 20:28:36
    Author     : somebody
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@include file="includes/header.jsp" %>

<h1>Downloads</h1>

<h2>${product.name}</h2>

<table>
	<tr>
		<th>Sound title</th>
		<th>Format</th>
	</tr>
	<tr>
		<td>${product.title}</td>
		<td><a href="/musicStore/sound/${product.fileName}">${product.format}</a></td>
	</tr>
</table>

<%@include file="includes/footer.jsp" %>
