<%@ page import="com.example.testspring.entities.*" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Role" %>
<%--
Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 15/11/2024
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Assignation des séances</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Assignation des classes à des séances</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        Lesson lesson = (Lesson) request.getAttribute("lesson");
        List<Classes> availableClassesList = (List<Classes>) request.getAttribute("availableClassesList");
        List<Classes> participatingClass = (List<Classes>) request.getAttribute("participatingClass");
        Course course = (Course) request.getAttribute("lessonCourse");
        Users teacher = (Users) request.getAttribute("lessonTeacher");

        if (lesson == null) {
    %>
    <h2>La séance n'existe pas</h2>
    <%
    } else {
    %>
    <div id="OldInfos">
        <h3>Informations de la séance :</h3>
        <p>Cours :
            <%
                if(course == null) {
            %>
            Il n'y a pas de cours associé à la séance</p>
        <%
        } else {
        %>
            <%= course.getCourseName() %></p>
        <%
            }
        %>
        <p>Enseignant :
            <%
                if(teacher == null) {
            %>
            Il n'y a pas d'enseignant associé à la séance</p>
        <%
        } else {
        %>
            <%= " " + teacher.getUserLastName() + " " + teacher.getUserName() %></p>
        <%
            }
        %>
        <p>Date de début : <%= lesson.getLessonStartDate() %></p>
        <p>Date de fin : <%= lesson.getLessonEndDate() %></p>
    </div>
    <form action="/projetSB/LessonClassesAssignationController" method="post">
        <h3>Classe(s) disponible(s) : </h3>
        <%
            if(availableClassesList == null || availableClassesList.isEmpty()) {
        %>
        <p>Aucune classe n'est disponible pour la séance</p>
        <%
            } else {
        %>
        <table border="1">
            <tr>
                <th>Nom de la classe</th>
                <th>Selection</th>
            </tr>
            <%
                for(Classes classes : availableClassesList) {
            %>
            <tr>
                <td><%= classes.getClassName() %></td>
                <td><input type="radio" name="classId" value="<%= classes.getClassId() %>"></td>
            </tr>
            <%
                }
            %>
        </table>
            <input type="text" name="lessonId" value="<%= lesson.getLessonId() %>" style="display: none">
            </br>
            <button type="submit" onclick="confirmAction(event, 'assign')">Assigner</button>
        <%
            }
        %>
    </form>
    <form action="/projetSB/LessonClassesUnassignationController" method="post">
        <h3>Classe(s) participantes(s) : </h3>
        <%
            if(participatingClass == null || participatingClass.isEmpty()) {
        %>
        <p>Aucune classe ne participe à la séance</p>
        <%
            } else {
        %>
        <table border="1">
            <tr>
                <th>Nom de la classe</th>
                <th>Selection</th>
            </tr>
            <%
                for(Classes classes : participatingClass) {
            %>
            <tr>
                <td><%= classes.getClassName() %></td>
                <td><input type="radio" name="classId" value="<%= classes.getClassId() %>"></td>
            </tr>
            <%
                }
            %>
        </table>
        <input type="text" name="lessonId" value="<%= lesson.getLessonId() %>" style="display: none">

        <% String messageErreur = (String) request.getAttribute("erreur");
            if(messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p>
        <%
                }
        %>

        <button type="submit" onclick="confirmAction(event, 'unassign')">Désassigner</button>
        <%
            }
        %>
    </form>
    <%
        }
    %>
</div>
</body>
<script>
    function confirmAction(event, action) {
        let confirmationMessage = '';

        if (action === 'assign') {
            confirmationMessage = "Êtes-vous sûr de vouloir assigner cette classe de cette séance ?";
        } else if (action === 'unassign') {
            confirmationMessage = "Êtes-vous sûr de vouloir désassigner cette classe de cette séance ?";
        } else {
            confirmationMessage = "Êtes-vous sûr de vouloir effectuer cette action ?";
        }

        const confirmation = confirm(confirmationMessage);

        if (!confirmation) {
            event.preventDefault(); // Annule l'envoi du formulaire si l'utilisateur annule
        }
    }
</script>
</html>
