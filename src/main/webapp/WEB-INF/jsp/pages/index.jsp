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
        Users connectedUser = (Users) session.getAttribute("connectedUser");

        String greeting = "";
        if (connectedUser != null) {
            greeting = "Bonjour, " + connectedUser.getUserName();
        }
    %>
    <h1>Bienvenue sur CY-EASE</h1>
    <h2><%= greeting %></h2>
    <div class="video-container">
        <iframe
                width="560"
                height="315"
                src="https://www.youtube.com/embed/duW6Er0VZZQ"
                frameborder="0"
                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
                allowfullscreen>
        </iframe>
    </div>
    <h2>Cy-Ease, la plateforme innovante qui simplifie la gestion scolaire pour tous!</h2>
</div>
</body>
</html>