<%@ page import="com.example.testspring.entities.Subjects" %>
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
    <title>Modification de matière</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Modification d'une matière</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        Subjects subject = (Subjects) request.getAttribute("subject");

        if (subject == null) {
    %>
    <p>La matière n'existe pas</p>
    <%
    } else {
    %>
    <div id="OldInfos">
        <h3>Ancienne information</h3>
        <p>Ancien nom de la matière : <%= subject.getSubjectName() %></p>
    </div>
    <form action="/projetSB/SubjectModificationController" method="post">
        <label>Nouveau nom de la matière : </label>
        <input type="text" name="subjectNewName" required>
        <input name="subjectId" value="<%= subject.getSubjectId() %>" style="display: none">
        <%
            }
        %>
        <% String messageErreur = (String) request.getAttribute("erreur");
            if (messageErreur != null && !messageErreur.isEmpty()) {
        %>
        <p style='color: red'><%= messageErreur %></p>
        <%
            }
        %>

        <button type="submit" onclick="confirmModify(event)">Modifier</button>
    </form>
</div>
</body>
<script>
    function confirmModify(event) {
        const confirmation = confirm("Êtes-vous sûr de vouloir modifier la matière ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
