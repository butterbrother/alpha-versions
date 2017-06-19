<%--
  Created by IntelliJ IDEA.
  User: somebody
  Date: 19.06.2017
  Time: 19:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
