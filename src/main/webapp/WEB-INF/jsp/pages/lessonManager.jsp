<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Users" %>
<%@ page import="com.example.testspring.entities.Role" %>
<%@ page import="com.example.testspring.entities.Lesson" %>
<%@ page import="com.example.testspring.entities.Course" %>
<%@ page import="java.util.Map" %>
<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 15/11/2024
  Time: 19:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Gestion des séance</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/filterTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Liste des séance</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        List<Lesson> lessonList = (List<Lesson>) request.getAttribute("lessons");
        Map<Integer, Users> mapLessonIdTeacher = (Map<Integer, Users>) request.getAttribute("mapLessonIdTeacher");
        Map<Integer, Course> mapLessonIdCourse = (Map<Integer, Course>) request.getAttribute("mapLessonIdCourse");

        if (lessonList == null || lessonList.isEmpty()) {
    %>
    <h2>Il n'y a pas encore de séance</h2>
    <%
    } else {
    %>
    <label for="searchInput">Rechercher :</label>
    <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Recherche"></br></br>
    <form method="get">
        <table border="1">
            <tr>
                <th>Nom du cours</th>
                <th>Nom et prénom du professeur</th>
                <th>Date de début</th>
                <th>Date de fin</th>
                <th>Selection</th>
            </tr>
            <%
                for (Lesson lesson : lessonList) {
            %>
            <tr>
                <td>
                <%
                    Course course = mapLessonIdCourse.get(lesson.getLessonId());
                    if(course == null) {
                %>
                    Pas de cours associé
                <%
                    } else {
                %>
                    <%= course.getCourseName() %>
                <%
                    }
                %>
                </td>
                <td>
                <%
                    Users teacher = mapLessonIdTeacher.get(lesson.getLessonId());
                    if(teacher == null) {
                %>
                    Pas d'enseignant associé
                <%
                    } else {
                %>
                    <%= teacher.getUserLastName() + " " + teacher.getUserName() %>
                <%
                    }
                %>
                </td>
                <td><%= lesson.getLessonStartDate() %></td>
                <td><%= lesson.getLessonEndDate() %></td>
                <td><input type="radio" name="lessonId" value="<%= lesson.getLessonId()%>" required></td>
            </tr>
            <%
                }
            %>
        </table>
        <% String messageErreur = (String) request.getAttribute("erreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p>
        <%
            }
        %>
        <button type="submit" formaction="/projetSB/LessonModificationController">Modifier</button>
        <button type="submit" formaction="/projetSB/LessonDeletionController" onclick="confirmDelete(event)">Supprimer</button>
        <button type="submit" formaction="/projetSB/LessonClassesManagerController">Assigner une ou plusieurs classes à la séance</button>
    </form>
    <%
        }
    %>
    <form action="/projetSB/LessonCreationController" method="get">
        <button type="submit">Créer</button>
    </form>
</div>
</body>
<script>
    function confirmDelete(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir supprimer la séance ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
