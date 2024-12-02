<%@ page import="com.example.testspring.entities.Role" %>
<%@ page import="com.example.testspring.entities.Users" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
</head>
<style>
    body {
        margin-left: 350px;
    }

    .sidebar {
        position: fixed;
        width: 250px;
        height: 100%;
        background-color: #77A4C6;
        color: white;
        padding: 20px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        top: 0;
        left: 0;
        z-index: 1000;
        border-top-right-radius: 10px;
        border-bottom-right-radius: 10px;
    }

    .sidebar h2 {
        text-align: center;
        font-size: 24px;
        margin-bottom: 20px;
        border-bottom: 1px solid white;
        padding-bottom: 10px;
    }

    .sidebar ul {
        list-style: none;
        padding: 0;
    }

    .sidebar ul li {
        margin: 15px 0;
    }

    .sidebar ul li a {
        color: white;
        text-decoration: none;
        font-size: 16px;
        padding: 10px;
        display: block;
        border-radius: 8px;
        transition: background-color 0.3s ease;
    }

    .sidebar ul li a:hover {
        background-color: #A6BBD6;
    }
</style>
<body>
<div class="sidebar">
    <%
        Users user = (Users) session.getAttribute("connectedUser");
    %>
    <a href="index"><img src="${pageContext.request.contextPath}/css/logo.png" alt="logo" style="width:120px;height:120px;"></a>
    </br></br>
    <div style="background-color: #333333; height: 2px;" ></div>
    <ul>
        <li><a href="index">Accueil</a></li>
        <% if (user != null && Role.administrator.equals(user.getUserRole())) { %>
        <li><a href="adminPage">Page admin</a></li>
        <% } %>
        <% if (user == null) { %>
        <li><a href="login">Se connecter</a></li>
        <li><a href="/projetSB/RegisterController">Inscription</a></li>
        <% } else { %>
        <% if (!Role.administrator.equals(user.getUserRole())) { %>
        <li><a href="/projetSB/UserScheduleController">Emploi du temps</a></li>
        <% } %>
        <% if (Role.teacher.equals(user.getUserRole())) { %>
        <li><a href="${pageContext.request.contextPath}/projetSB/EntryNoteController">Saisie note</a></li>
        <li><a href="${pageContext.request.contextPath}/projetSB/TeacherGradeController">Voir les notes saisies</a></li>
        <% } %>
        <% if (Role.student.equals(user.getUserRole())) { %>
        <li><a href="${pageContext.request.contextPath}/projetSB/StudentGradeReportController?userId=<%= user.getUserId() %>">Ses notes</a></li>
        <% } %>
        <li><a href="/projetSB/PersonalInformationController">Informations personnelles</a></li>
        <li><a href="/projetSB/LogoutController">Déconnexion</a></li>
        <% } %>
    </ul>
</div>
</body>
</html>