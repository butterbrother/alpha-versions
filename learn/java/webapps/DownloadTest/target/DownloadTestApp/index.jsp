<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@include file="includes/header.jsp"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${cookie}" var="c">
    <c:if test="${c.value.name == 'emailCookie'}">
        <p>You registered as: ${c.value.value}</p>
    </c:if>
</c:forEach>
<h1>List of albums:</h1>
<p>
    <a href="download?action=checkUser&amp;productCode=8601">Music 1</a>
    <br/>

    <a href="download?action=checkUser&amp;productCode=pf01">Music 2</a>
    <br/>

    <a href="download?action=checkUser&amp;productCode=pf02">Music 3</a>
    <br/>

    <a href="download?action=checkUser&amp;productCode=jr01">Music 4</a>
</p>

<%@include file="includes/footer.jsp" %>