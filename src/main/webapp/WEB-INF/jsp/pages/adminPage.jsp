<%@ page import="com.example.testspring.entities.Role" %><%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 22/11/2024
  Time: 18:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Page admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<%
    Users connectedUser = (Users) session.getAttribute("connectedUser");
    if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
        response.sendRedirect("index");
        return;
    }
%>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
<h1>Page admin</h1>
    <div id="OldInfos">
        <ul>
            <li><a href="/projetSB/UserToValidateManagerController">Gérer les inscriptions</a></li>
            <li><a href="/projetSB/UserManagerController?roleFilter=student">Gérer les utilisateurs</a></li>
            <li><a href="/projetSB/LessonManagerController">Gérer les séances</a></li>
            <li><a href="/projetSB/SubjectManagerController">Gérer les matières</a></li>
            <li><a href="/projetSB/CourseManagerController">Gérer les cours</a></li>
            <li><a href="/projetSB/MajorManagerController">Gérer les filières</a></li>
            <li><a href="/projetSB/GradeManagerController">Gérer les notes</a></li>
            <li><a href="/projetSB/ClassesManagerController">Gérer les classes</a></li>
        </ul>
    </div>
</div>
</body>
</html>
