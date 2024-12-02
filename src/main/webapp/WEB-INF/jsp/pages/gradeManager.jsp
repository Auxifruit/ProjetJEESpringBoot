<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="com.example.testspring.entities.*" %>
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
    <title>Liste des notes</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/filterTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Liste des notes</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        List<Grade> gradeList = (List<Grade>) request.getAttribute("grades");

        Map<Integer, Course> mapGradeIdCourse = (Map<Integer, Course>) request.getAttribute("mapGradeIdCourse");
        Map<Integer, Users> mapGradeIdStudent = (Map<Integer, Users>) request.getAttribute("mapGradeIdStudent");
        Map<Integer, Classes> mapStudentIdClass = (Map<Integer, Classes>) request.getAttribute("mapStudentIdClass");
        Map<Integer, Users> mapGradeIdTeacher = (Map<Integer, Users>) request.getAttribute("mapGradeIdTeacher");

        if (gradeList == null || gradeList.isEmpty()) {
    %>
    <h2>Il n'y a pas encore de notes</h2>
    <%
    } else {
    %>
    <label for="searchInput">Rechercher :</label>
    <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Recherche">
    </br></br>
    <form method="get">
        <table border="1">
            <th>Nom de la note</th>
            <th>Nom du cours</th>
            <th>Valeur de la note</th>
            <th>Coefficient de la note</th>
            <th>Nom et prénom de l'étudiant</th>
            <th>Classe de l'étudiant</th>
            <th>Nom et prénom de l'enseignant</th>
            <th>Selection</th>
            <%
                for (Grade grade : gradeList) {
            %>
            <tr>
                <td><%= grade.getGradeName() %></td>
                <td>
                    <%
                        Course course = mapGradeIdCourse.get(grade.getGradeId());
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
                <td><%= grade.getGradeValue() %></td>
                <td><%= grade.getGradeCoefficient() %></td>
                <td>
                    <%
                        Users student = mapGradeIdStudent.get(grade.getGradeId());
                    %>
                    <%= student.getUserLastName() + " " + student.getUserName() %></td>
                <td>
                    <%
                        Classes classe = mapStudentIdClass.get(student.getUserId());
                        if(classe == null) {
                    %>
                        Pas de cours associé
                    <%
                    } else {
                    %>
                        <%= classe.getClassName() %>
                    <%
                        }
                    %>
                <td>
                    <%
                        Users teacher = mapGradeIdTeacher.get(grade.getGradeId());
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
                <td><input type="radio" name="gradeId" value="<%= grade.getGradeId()%>" required></td>
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
        <button type="submit" formaction="/projetSB/GradeModificationController">Modifier</button>
        <button type="submit" formaction="/projetSB/GradeDeletionController" onclick="confirmDelete(event)">Supprimer</button>
    </form>
    <%
        }
    %>
    <form action="/projetSB/GradeCreationController" method="get">
        <button type="submit">Créer</button>
    </form>
</div>
</body>
<script>
    function confirmDelete(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir supprimer la note ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
