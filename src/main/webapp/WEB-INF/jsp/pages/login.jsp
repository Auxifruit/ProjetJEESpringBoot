<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Connexion</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%
    Users connectedUser = (Users) session.getAttribute("connectedUser");
    if(connectedUser != null) {
        response.sendRedirect("index");
        return;
    }
%>
<%@ include file="./sidebar.jsp" %>
<form action="/projetSB/LoginController" method="post">
    <h1>Connexion</h1>
    <label for="email">Email :</label>
    <input type="email" name="email" id="email" placeholder="Saisissez votre Email" required><br>
    <label for="password">Mot de passe :</label>
    <input type="password" name="password" id="password" placeholder="Saisissez votre mot de passe" required><br>
    <% if (request.getAttribute("error") != null) { %>
    <p style="color: red;"><%= request.getAttribute("error") %></p><br>
    <% } %>
    <button type="submit">Se connecter</button>
    <p>Pas encore de compte ? <a href="${pageContext.request.contextPath}/register">Inscrivez-vous.</a></p>
</form>
</body>
</html>