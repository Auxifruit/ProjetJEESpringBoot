<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Course" %>
<%@ page import="com.example.testspring.entities.Role" %>
<%@ page import="com.example.testspring.entities.Subjects" %>
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
    <title>Gestion des cours</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<script src="${pageContext.request.contextPath}/js/filterTable.js"></script>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Liste des cours</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        List<Course> courseList = (List<Course>) request.getAttribute("courses");
        Map<Integer, Subjects> mapCourseIdSubject = (Map<Integer, Subjects>) request.getAttribute("mapCourseIdSubject");

        if (courseList == null || courseList.isEmpty()) {
    %>
    <h2>Il n'y a pas encore de cours</h2>
    <%
    } else {
    %>
    <label for="searchInput">Rechercher :</label>
    <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Recherche">
    </br></br>
    <form method="get">
        <table border="1">
            <th>Nom du cours</th>
            <th>Nom de la matière du cours</th>
            <th>Selection</th>
            <%
                for (Course course : courseList) {
            %>
            <tr>
                <td><%= course.getCourseName() %></td>
                <td>
                    <%
                        Subjects subject = mapCourseIdSubject.get(course.getCourseId());
                        if(subject == null) {
                    %>
                    Pas de matière associé
                    <%
                    } else {
                    %>
                    <%= subject.getSubjectName() %>
                    <%
                        }
                    %>
                </td>
                <td><input type="radio" name="courseId" value="<%= course.getCourseId()%>" required></td>
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
        <button type="submit" formaction="/projetSB/CourseModificationController">Modifier</button>
        <button type="submit" formaction="/projetSB/CourseDeletionController" formmethod="post" onclick="confirmDelete(event)">Supprimer</button>
    </form>
    <%
        }
    %>
    <form action="/projetSB/CourseCreationController" method="get">
        <button type="submit">Créer</button>
    </form>
</div>
</body>
<script>
    function confirmDelete(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir supprimer le cours ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
