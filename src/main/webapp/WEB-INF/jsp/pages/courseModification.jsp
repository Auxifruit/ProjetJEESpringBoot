<%@ page import="com.example.testspring.entities.Subjects" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.testspring.entities.Course" %>
<%@ page import="com.example.testspring.entities.Role" %>
<%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 12/11/2024
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modification de cours</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Modification d'un cours</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        Course course = (Course) request.getAttribute("course");
        Subjects courseSubject = (Subjects) request.getAttribute("courseSubject");
        List<Subjects> subjectList = (List<Subjects>) request.getAttribute("subjects");

        if (course == null) {
    %>
    <p>Le cours n'existe pas</p>
    <%
    } else {
    %>
    <div id="OldInfos">
        <h3>Anciennes informations</h3>
        <p>Ancien nom : <%= course.getCourseName() %></p>
        <p>Ancienne matière :
            <% if(courseSubject == null) {
            %>
            Pas de maitière associée
            <%
            } else {
            %>
            <%= courseSubject.getSubjectName() %>
            <%
                }
            %>
        </p>
    </div>
    <form action="/projetSB/CourseModificationController" method="post">
        <h3>Nouvelles informations</h3>
    <%
        if(subjectList == null || subjectList.isEmpty()) {
    %>
    <p>Il n'y a de matière disponible pour modifier le cours</p>
    <%
        }
        else {
    %>
        <label>Choix de la matière : </label>
        <select name="newCourseSubjectId">
            <option value="">Ne pas modifier la matière</option>
            <%
                for (Subjects subject : subjectList) {
            %>
            <option value="<%= subject.getSubjectId() %>"><%= subject.getSubjectName() %></option>
            <%
                }
            %>
        </select>
        <label>Choix du nouveau nom du cours : </label>
        <input type="text" name="newCourseName"/>

        <input name="courseId" value="<%= course.getCourseId() %>" style="display: none">

        <% String messageErreur = (String) request.getAttribute("erreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p>
        <%
            }
        %>
        <button type="submit" onclick="confirmModify(event)">Modifier</button>
    </form>
    <%
        }
        }
    %>
</div>
</body>
<script>
    function confirmModify(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir modifier le cours ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
