<%@ page import="com.example.testspring.entities.Major" %><%--
  Created by IntelliJ IDEA.
  User: CYTech Student
  Date: 12/11/2024
  Time: 19:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modification de filière</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
<%@ include file="./sidebar.jsp" %>
<div>
    <h1>Modification d'une filière</h1>
    <%
        Users connectedUser = (Users) session.getAttribute("connectedUser");
        if(connectedUser == null || !Role.administrator.equals(connectedUser.getUserRole())) {
            response.sendRedirect("index");
            return;
        }

        Major major = (Major) request.getAttribute("major");

        if (major == null) {
    %>
    <p>La filière n'existe pas</p>
    <%
    } else {
    %>
    <div id="OldInfos">
        <h3>Ancienne information</h3>
        <p>Ancien nom de la filière : <%= major.getMajorName() %></p>
    </div>
    <form action="/projetSB/MajorModificationController" method="post">
        <h3>Nouvelle information</h3>
        <label>Nouveau nom de la filière : </label>
        <input type="text" name="majorNewName" required>
        <input name="majorId" value="<%= major.getMajorId() %>" style="display: none">

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
        const confirmation = confirm("Êtes-vous sûr de vouloir modifier la filière ?");

        if (!confirmation) {
            event.preventDefault();
        }
    }
</script>
</html>
