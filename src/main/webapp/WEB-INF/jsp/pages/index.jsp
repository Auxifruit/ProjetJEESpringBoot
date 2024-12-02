<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Accueil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div class="content">
    <%
//        Integer userId = (Integer) session.getAttribute("user");
//        String greeting = "";
//        if (userId != null) {
//            Users user = UserDAO.getUserById(userId); // Récupération de l'utilisateur
//            if (user != null) {
//                greeting = "Bonjour, " + user.getUserName(); // Ajouter le prénom de l'utilisateur
//            }
//        }
    %>
    <h1>Bienvenue sur CY-EASE</h1>
<%--    <h2><%= greeting %></h2>--%>
    <h2>Cy-Ease, la plateforme innovante qui simplifie la gestion scolaire pour tous!</h2>
</div>
</body>
</html>