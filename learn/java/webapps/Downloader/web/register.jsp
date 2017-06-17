<%-- 
    Document   : register
    Created on : 17.06.2017, 19:30:50
    Author     : somebody
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="includes/header.jsp" %>

<h1>Download registration</h1>

<p>To register for a downloads, enter your name and email
address below. Then click on the Submit buttom</p>

<c:if test="${message} != null" >
	<p>
		<i>${message}</i>
	</p>
</c:if>

	<form action="download" method="post">

		<input type="hidden" name="action" value="registerUser">

		<label for="email" class="pad_top">Email:</label>
		<input type="email" name="email" id="email" value="${user.email}">
		<br>

		<label for="firstName" class="pad_top">First name:</label>
		<input type="text" name="firstName" id="firstName" value="${user.firstName}">
		<br>

		<label for="lastName" class="pad_top">Last name:</label>
		<input type="text" name="lastName" id="lastName" value="${user.lastName}">
		<br>

		<label for="register">&n&nbsp;</label>
		<input type="submit" value="Register" id="register" class="margin_left">
	</form>

<%@include file="includes/footer.jsp" %>
